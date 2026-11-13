package com.example.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

/**
 * Copies user-picked Photo Picker URIs into app-private storage (filesDir/school_media)
 * so images persist reliably across app restarts without requiring broad storage permissions.
 * Downsamples oversized camera photos cleanly for low-RAM and older Android phones.
 *
 * Note on Cloud Storage: Firebase Storage (com.google.firebase:firebase-storage) is not
 * provisioned in this project's Firebase configuration yet (only Firestore and Auth are active).
 * Photos are persisted locally in app-private storage and referenced via file:// URIs.
 */
object MediaStorageHelper {

    private const val TAG = "MediaStorageHelper"
    private const val MEDIA_DIR = "school_media"
    private const val TEACHER_PHOTOS_DIR = "teacher_photos"
    private const val MAX_PORTRAIT_DIM_PX = 800
    private const val MAX_MEDIA_DIM_PX = 1600

    fun launchImagePickerSafely(
        launcher: ActivityResultLauncher<PickVisualMediaRequest>,
        request: PickVisualMediaRequest,
        onUnavailable: () -> Unit = {}
    ) {
        try {
            launcher.launch(request)
        } catch (e: ActivityNotFoundException) {
            Log.w(TAG, "No photo or document picker available on this device", e)
            onUnavailable()
        } catch (e: SecurityException) {
            Log.w(TAG, "Photo picker blocked by device policy", e)
            onUnavailable()
        }
    }

    fun copyPickerUriToInternalStorage(context: Context, sourceUri: Uri, prefix: String = "img"): String? {
        return try {
            val mediaDir = File(context.filesDir, MEDIA_DIR)
            if (!mediaDir.exists()) {
                mediaDir.mkdirs()
            }
            val fileName = "${prefix}_${UUID.randomUUID().toString().take(8)}_${System.currentTimeMillis()}.jpg"
            val destFile = File(mediaDir, fileName)
            val maxDim = if (prefix == "student" || prefix == "teacher") MAX_PORTRAIT_DIM_PX else MAX_MEDIA_DIM_PX
            persistNormalizedImage(context, sourceUri, destFile, maxDim)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to persist picked media URI: $sourceUri", e)
            null
        }
    }

    /**
     * Persists a teacher's selected photograph explicitly under the given teacherId
     * inside app-private internal storage so it remains available across app restarts.
     */
    fun saveTeacherPhoto(context: Context, sourceUri: Uri, teacherId: String): String? {
        return try {
            val safeId = teacherId.trim().replace(Regex("[^a-zA-Z0-9_-]"), "_").ifBlank { "unknown" }
            val teacherDir = File(File(context.filesDir, MEDIA_DIR), TEACHER_PHOTOS_DIR)
            if (!teacherDir.exists()) {
                teacherDir.mkdirs()
            }
            val fileName = "teacher_${safeId}_${System.currentTimeMillis()}.jpg"
            val destFile = File(teacherDir, fileName)
            persistNormalizedImage(context, sourceUri, destFile, MAX_PORTRAIT_DIM_PX)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to persist teacher photo for teacherId=$teacherId from $sourceUri", e)
            null
        }
    }

    private fun persistNormalizedImage(
        context: Context,
        sourceUri: Uri,
        destFile: File,
        maxDimensionPx: Int
    ): String? {
        val tempFile = File(destFile.parentFile, "${destFile.nameWithoutExtension}.tmp")
        return try {
            val bytesCopied = context.contentResolver.openInputStream(sourceUri)?.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            } ?: 0L

            if (bytesCopied <= 0L || !tempFile.exists() || tempFile.length() <= 0L) {
                if (tempFile.exists()) tempFile.delete()
                return null
            }

            val downsampled = try {
                val boundsOpts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                BitmapFactory.decodeFile(tempFile.absolutePath, boundsOpts)
                val srcW = boundsOpts.outWidth
                val srcH = boundsOpts.outHeight

                if (srcW > maxDimensionPx || srcH > maxDimensionPx) {
                    var sampleSize = 1
                    while ((srcW / sampleSize) > maxDimensionPx * 2 || (srcH / sampleSize) > maxDimensionPx * 2) {
                        sampleSize *= 2
                    }
                    val decodeOpts = BitmapFactory.Options().apply {
                        inSampleSize = sampleSize
                        inPreferredConfig = Bitmap.Config.RGB_565
                    }
                    val decoded = BitmapFactory.decodeFile(tempFile.absolutePath, decodeOpts)
                    if (decoded != null) {
                        val rotated = applyExifRotationIfNeeded(tempFile.absolutePath, decoded)
                        val finalBmp = scaleDownIfNeeded(rotated, maxDimensionPx)
                        FileOutputStream(destFile).use { out ->
                            finalBmp.compress(Bitmap.CompressFormat.JPEG, 85, out)
                        }
                        if (finalBmp !== rotated) finalBmp.recycle()
                        if (rotated !== decoded) rotated.recycle()
                        decoded.recycle()
                        tempFile.delete()
                        true
                    } else {
                        false
                    }
                } else {
                    false
                }
            } catch (e: Throwable) {
                Log.w(TAG, "Bitmap normalization skipped, preserving original stream for $sourceUri", e)
                false
            }

            if (!downsampled && tempFile.exists()) {
                moveOrCopy(tempFile, destFile)
            }

            if (!destFile.exists() || destFile.length() <= 0L) {
                if (destFile.exists()) destFile.delete()
                null
            } else {
                Uri.fromFile(destFile).toString()
            }
        } catch (e: Exception) {
            if (tempFile.exists()) tempFile.delete()
            if (destFile.exists()) destFile.delete()
            Log.e(TAG, "Error normalizing image $sourceUri", e)
            null
        }
    }

    private fun moveOrCopy(src: File, dst: File) {
        if (!src.renameTo(dst)) {
            src.inputStream().use { input ->
                FileOutputStream(dst).use { output ->
                    input.copyTo(output)
                }
            }
            src.delete()
        }
    }

    private fun applyExifRotationIfNeeded(filePath: String, bitmap: Bitmap): Bitmap {
        return try {
            val exif = ExifInterface(filePath)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            val degrees = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                else -> 0f
            }
            if (degrees == 0f) {
                bitmap
            } else {
                val matrix = Matrix().apply { postRotate(degrees) }
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            }
        } catch (_: Exception) {
            bitmap
        }
    }

    private fun scaleDownIfNeeded(bitmap: Bitmap, maxDimensionPx: Int): Bitmap {
        val maxSide = maxOf(bitmap.width, bitmap.height)
        if (maxSide <= maxDimensionPx) return bitmap
        val ratio = maxDimensionPx.toFloat() / maxSide.toFloat()
        val targetW = (bitmap.width * ratio).toInt().coerceAtLeast(1)
        val targetH = (bitmap.height * ratio).toInt().coerceAtLeast(1)
        return Bitmap.createScaledBitmap(bitmap, targetW, targetH, true)
    }

    fun copyMultiplePickerUris(context: Context, uris: List<Uri>, prefix: String = "gallery"): List<String> {
        return uris.mapNotNull { copyPickerUriToInternalStorage(context, it, prefix) }
    }

    /**
     * Resolves a stored URI string into a loadable Coil image model (`File` for persisted local files,
     * `String` for remote/content URIs), returning `null` if a local file no longer exists on disk.
     */
    fun resolvePhotoModel(uriString: String?): Any? {
        if (uriString.isNullOrBlank()) return null
        return try {
            val parsed = Uri.parse(uriString)
            if (parsed.scheme == "file" || parsed.scheme == null) {
                val path = parsed.path ?: return null
                val file = File(path)
                if (file.exists() && file.length() > 0L) file else null
            } else {
                uriString
            }
        } catch (e: Exception) {
            Log.w(TAG, "Invalid photo URI: $uriString", e)
            null
        }
    }

    fun hasValidPhoto(uriString: String?): Boolean = resolvePhotoModel(uriString) != null

    fun deleteLocalMediaIfManaged(uriString: String?) {
        if (uriString.isNullOrBlank()) return
        try {
            val parsed = Uri.parse(uriString)
            if (parsed.scheme == "file" && parsed.path?.contains(MEDIA_DIR) == true) {
                val file = File(parsed.path!!)
                if (file.exists()) {
                    file.delete()
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Unable to delete local media file: $uriString", e)
        }
    }
}
