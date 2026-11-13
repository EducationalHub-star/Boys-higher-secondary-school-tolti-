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

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
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
}
