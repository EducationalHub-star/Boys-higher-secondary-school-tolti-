package com.example.util

import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

/**
 * Copies user-picked Photo Picker URIs into app-private storage (filesDir/school_media)
 * so images persist reliably across app restarts without requiring broad storage permissions.
 *
 * Note on Cloud Storage: Firebase Storage (com.google.firebase:firebase-storage) is not
 * provisioned in this project's Firebase configuration yet (only Firestore and Auth are active).
 * Photos are persisted locally in app-private storage and referenced via file:// URIs.
 */
object MediaStorageHelper {

    private const val TAG = "MediaStorageHelper"
    private const val MEDIA_DIR = "school_media"
    private const val TEACHER_PHOTOS_DIR = "teacher_photos"

    fun copyPickerUriToInternalStorage(context: Context, sourceUri: Uri, prefix: String = "img"): String? {
        return try {
            val mediaDir = File(context.filesDir, MEDIA_DIR)
            if (!mediaDir.exists()) {
                mediaDir.mkdirs()
            }
            val fileName = "${prefix}_${UUID.randomUUID().toString().take(8)}_${System.currentTimeMillis()}.jpg"
            val destFile = File(mediaDir, fileName)

            val bytesCopied = context.contentResolver.openInputStream(sourceUri)?.use { input ->
                FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            } ?: 0L

            if (bytesCopied <= 0L || !destFile.exists() || destFile.length() <= 0L) {
                if (destFile.exists()) destFile.delete()
                return null
            }

            Uri.fromFile(destFile).toString()
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

            val bytesCopied = context.contentResolver.openInputStream(sourceUri)?.use { input ->
                FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            } ?: 0L

            if (bytesCopied <= 0L || !destFile.exists() || destFile.length() <= 0L) {
                if (destFile.exists()) destFile.delete()
                return null
            }

            Uri.fromFile(destFile).toString()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to persist teacher photo for teacherId=$teacherId from $sourceUri", e)
            null
        }
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
