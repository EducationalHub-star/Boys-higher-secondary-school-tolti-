package com.example

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.NotificationPriority
import com.example.data.model.NotificationType
import com.example.data.model.SchoolNotification
import com.example.service.RealtimeNotificationManager
import com.example.util.MediaStorageHelper
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File
import java.io.FileOutputStream
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.MessageDigest
import java.util.jar.JarFile
import java.util.zip.Adler32
import java.util.zip.ZipFile

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class CompatibilityConfigTest {

    private lateinit var context: Context
    private lateinit var tempDir: File

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        tempDir = File(context.cacheDir, "compat_test_media").apply { mkdirs() }
        File(context.filesDir, "school_media").deleteRecursively()
    }

    @After
    fun tearDown() {
        tempDir.deleteRecursively()
        File(context.filesDir, "school_media").deleteRecursively()
    }

    @Test
    fun `app configures minSdk 23 and targetSdk 36 for widest Android compatibility`() {
        val appInfo = context.applicationInfo
        assertEquals(23, appInfo.minSdkVersion)
        assertEquals(36, appInfo.targetSdkVersion)
    }

    @Test
    fun `manifest keeps required permissions and drops unused Camera and RecordAudio`() {
        val packageInfo = context.packageManager.getPackageInfo(
            context.packageName,
            PackageManager.GET_PERMISSIONS
        )
        val requested = packageInfo.requestedPermissions?.toSet().orEmpty()
        assertTrue(requested.contains(Manifest.permission.INTERNET))
        assertTrue(requested.contains(Manifest.permission.VIBRATE))
        assertTrue(requested.contains(Manifest.permission.POST_NOTIFICATIONS))
        assertFalse("Unused CAMERA permission should not block camera-less devices", requested.contains(Manifest.permission.CAMERA))
        assertFalse("Unused RECORD_AUDIO permission should not be requested", requested.contains(Manifest.permission.RECORD_AUDIO))
    }

    @Test
    fun `oversized camera portrait is downsampled cleanly for low-RAM devices`() {
        val largeBitmap = Bitmap.createBitmap(2000, 1600, Bitmap.Config.ARGB_8888)
        val sourceFile = File(tempDir, "large_camera_photo.jpg")
        FileOutputStream(sourceFile).use { out ->
            largeBitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
        }
        largeBitmap.recycle()

        val savedUri = MediaStorageHelper.saveTeacherPhoto(context, Uri.fromFile(sourceFile), "tch-compat")
        assertNotNull(savedUri)

        val savedFile = File(Uri.parse(savedUri).path!!)
        assertTrue(savedFile.exists() && savedFile.length() > 0L)

        val opts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(savedFile.absolutePath, opts)
        assertTrue("Portrait width (${opts.outWidth}) should be bounded <= 800px", opts.outWidth in 1..800)
        assertTrue("Portrait height (${opts.outHeight}) should be bounded <= 800px", opts.outHeight in 1..800)
    }

    @Test
    fun `notifications setup and dispatch operate safely`() {
        RealtimeNotificationManager.createNotificationChannels(context)
        val sample = SchoolNotification(
            id = "compat-notif-1",
            title = "Exam Schedule",
            message = "Class 10th practical timetable updated.",
            type = NotificationType.COLLEGE_ANNOUNCEMENT,
            priority = NotificationPriority.NORMAL
        )
        RealtimeNotificationManager.dispatchSystemNotification(context, sample)
    }

    @Test
    fun `built debug APK has valid ZIP, DEX checksums, binary manifest, ABIs, and v1-v2 signatures`() {
        val candidates = listOf(
            File("build/outputs/apk/debug/app-debug.apk"),
            File("app/build/outputs/apk/debug/app-debug.apk")
        )
        val apkFile = candidates.firstOrNull { it.exists() }
            ?: return // Only runs after assembleDebug output is present

        verifyApkFile(apkFile)

        // Also check persisted .build-outputs/app-debug.apk if present
        val persistedCandidates = listOf(
            File("../.build-outputs/app-debug.apk"),
            File(".build-outputs/app-debug.apk")
        )
        persistedCandidates.firstOrNull { it.exists() }?.let { verifyApkFile(it) }
    }

    private fun verifyApkFile(apkFile: File) {
        assertTrue("APK file must be non-empty: ${apkFile.path}", apkFile.length() > 1024L * 1024L)

        // 1. Verify ZIP structure, CRC32 of every entry, binary manifest, and every DEX header/SHA-1/Adler32
        ZipFile(apkFile).use { zip ->
            val manifestEntry = zip.getEntry("AndroidManifest.xml")
            assertNotNull("APK must contain AndroidManifest.xml", manifestEntry)
            val manifestBytes = zip.getInputStream(manifestEntry).use { it.readBytes() }
            assertTrue("AndroidManifest.xml must be compiled binary AXML", manifestBytes.size >= 8)
            val axmlType = ByteBuffer.wrap(manifestBytes, 0, 2).order(ByteOrder.LITTLE_ENDIAN).short.toInt()
            assertEquals("Expected AXML type 0x0003", 0x0003, axmlType)

            assertNotNull("APK must contain resources.arsc", zip.getEntry("resources.arsc"))

            val entries = zip.entries().toList()
            val dexEntries = entries.filter { it.name.matches(Regex("^classes\\d*\\.dex$")) }
            assertTrue("APK must contain at least one DEX file", dexEntries.isNotEmpty())
            dexEntries.forEach { dex ->
                val dexBytes = zip.getInputStream(dex).use { it.readBytes() }
                assertTrue("${dex.name} too small (${dexBytes.size} bytes)", dexBytes.size >= 112)
                assertEquals(
                    "Invalid DEX magic in ${dex.name}",
                    "dex\n",
                    String(dexBytes, 0, 4, Charsets.US_ASCII)
                )
                val header = ByteBuffer.wrap(dexBytes).order(ByteOrder.LITTLE_ENDIAN)
                val declaredAdler = header.getInt(8).toLong() and 0xFFFFFFFFL
                val declaredSha1 = dexBytes.copyOfRange(12, 32)
                val declaredFileSize = header.getInt(32)
                assertEquals(
                    "${dex.name} file_size mismatch",
                    dexBytes.size,
                    declaredFileSize
                )
                val actualSha1 = MessageDigest.getInstance("SHA-1").apply {
                    update(dexBytes, 32, dexBytes.size - 32)
                }.digest()
                assertTrue(
                    "${dex.name} internal SHA-1 mismatch",
                    declaredSha1.contentEquals(actualSha1)
                )
                val actualAdler = Adler32().apply {
                    update(dexBytes, 12, dexBytes.size - 12)
                }.value
                assertEquals(
                    "${dex.name} Adler32 checksum mismatch",
                    declaredAdler,
                    actualAdler
                )
            }

            // Read all remaining ZIP entries to verify CRC32 integrity across the entire APK archive
            val scratch = ByteArray(16384)
            entries.filterNot { it.isDirectory }.forEach { entry ->
                zip.getInputStream(entry).use { input ->
                    while (input.read(scratch) != -1) {
                        // Forces ZipFile CRC32 verification on every entry
                    }
                }
            }

            val entryNames = entries.map { it.name }
            listOf("arm64-v8a", "armeabi-v7a", "x86", "x86_64").forEach { abi ->
                assertTrue(
                    "APK should contain native libraries for $abi",
                    entryNames.any { it.startsWith("lib/$abi/") && it.endsWith(".so") }
                )
            }
        }

        // 2. Verify v1 JAR signature and SHA-1 fingerprint matching google-services.json
        val certSha1s = mutableSetOf<String>()
        JarFile(apkFile, true).use { jar ->
            val buffer = ByteArray(8192)
            jar.entries().toList().forEach { entry ->
                if (!entry.isDirectory) {
                    jar.getInputStream(entry).use { input ->
                        while (input.read(buffer) != -1) {
                            // Reading all bytes forces JarFile to verify entry digest against manifest/SF
                        }
                    }
                    entry.certificates?.forEach { cert ->
                        val sha1 = MessageDigest.getInstance("SHA-1")
                            .digest(cert.encoded)
                            .joinToString("") { "%02x".format(it) }
                        certSha1s.add(sha1)
                    }
                }
            }
        }
        assertTrue(
            "APK v1 signature must match google-services.json SHA-1 (30ee71212a205297e5a0e1ab403d0ea2535fbffd), got $certSha1s",
            certSha1s.contains("30ee71212a205297e5a0e1ab403d0ea2535fbffd")
        )

        // 3. Verify APK Signing Block (v2/v3 signature magic "APK Sig Block 42")
        val magic = "APK Sig Block 42".toByteArray(Charsets.US_ASCII)
        val hasApkSigBlock = RandomAccessFile(apkFile, "r").use { raf ->
            val len = raf.length()
            val scanBytes = ByteArray(minOf(len, 1024L * 512L).toInt())
            raf.seek(len - scanBytes.size)
            raf.readFully(scanBytes)
            scanBytes.indices.any { i ->
                i + magic.size <= scanBytes.size &&
                    magic.indices.all { j -> scanBytes[i + j] == magic[j] }
            }
        }
        assertTrue("APK must include v2/v3 APK Signing Block", hasApkSigBlock)

        // 4. Verify Android PackageArchiveInfo parsing
        val archiveInfo = context.packageManager.getPackageArchiveInfo(
            apkFile.absolutePath,
            PackageManager.GET_PERMISSIONS
        )
        assertNotNull("PackageManager must parse APK archive info", archiveInfo)
        assertEquals("com.aistudio.kasurumaltok.vzrkx", archiveInfo!!.packageName)
        assertEquals(23, archiveInfo.applicationInfo?.minSdkVersion)
        assertEquals(36, archiveInfo.applicationInfo?.targetSdkVersion)
    }
}
