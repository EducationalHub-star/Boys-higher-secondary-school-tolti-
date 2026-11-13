package com.example

import android.content.Context
import android.net.Uri
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.SchoolDatabase
import com.example.data.model.TeacherProfileRecord
import com.example.data.repository.SchoolRepository
import com.example.util.MediaStorageHelper
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class TeacherPhotoPersistenceTest {

    private lateinit var context: Context
    private lateinit var tempSourceDir: File
    private val dbName = "test_teacher_photo_persistence.db"

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        tempSourceDir = File(context.cacheDir, "test_gallery_picks").apply { mkdirs() }
        File(context.filesDir, "school_media").deleteRecursively()
        context.deleteDatabase(dbName)
    }

    @After
    fun tearDown() {
        tempSourceDir.deleteRecursively()
        File(context.filesDir, "school_media").deleteRecursively()
        context.deleteDatabase(dbName)
    }

    @Test
    fun `saveTeacherPhoto stores verified file under teacherId and resolvePhotoModel loads it`() {
        val fakeJpeg = File(tempSourceDir, "gallery_pick_1.jpg").apply {
            writeBytes(byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte(), 0xE0.toByte(), 1, 2, 3, 4))
        }
        val sourceUri = Uri.fromFile(fakeJpeg)

        val savedUri = MediaStorageHelper.saveTeacherPhoto(context, sourceUri, "tch-001")
        assertNotNull(savedUri)
        assertTrue(savedUri!!.contains("teacher_photos/teacher_tch-001_"))
        assertTrue(MediaStorageHelper.hasValidPhoto(savedUri))

        val resolved = MediaStorageHelper.resolvePhotoModel(savedUri)
        assertTrue(resolved is File)
        val savedFile = resolved as File
        assertTrue(savedFile.exists())
        assertEquals(8L, savedFile.length())
    }

    @Test
    fun `saveTeacherPhoto returns null for missing or empty files without creating garbage files`() {
        val missingUri = Uri.fromFile(File(tempSourceDir, "does_not_exist.jpg"))
        assertNull(MediaStorageHelper.saveTeacherPhoto(context, missingUri, "tch-missing"))

        val emptyFile = File(tempSourceDir, "empty.jpg").apply { writeBytes(ByteArray(0)) }
        assertNull(MediaStorageHelper.saveTeacherPhoto(context, Uri.fromFile(emptyFile), "tch-empty"))

        val teacherDir = File(File(context.filesDir, "school_media"), "teacher_photos")
        val remaining = teacherDir.listFiles()?.toList().orEmpty()
        assertTrue("No zero-byte files should remain", remaining.isEmpty())
        assertFalse(MediaStorageHelper.hasValidPhoto("file://${emptyFile.absolutePath}"))
    }

    @Test
    fun `teacher photo persists across database close and reopen, replaces cleanly, and deletes cleanly`() = runBlocking {
        val photo1Source = File(tempSourceDir, "portrait_v1.jpg").apply {
            writeBytes(ByteArray(64) { 0x42 })
        }
        val photo2Source = File(tempSourceDir, "portrait_v2.jpg").apply {
            writeBytes(ByteArray(128) { 0x24 })
        }

        // 1. Open DB, insert teacher with photo #1, and close DB (simulating app close)
        var db = Room.databaseBuilder(context, SchoolDatabase::class.java, dbName)
            .allowMainThreadQueries()
            .build()
        var repo = SchoolRepository(db)

        val savedUri1 = MediaStorageHelper.saveTeacherPhoto(context, Uri.fromFile(photo1Source), "tch-custom-101")
        assertNotNull(savedUri1)

        val teacher = TeacherProfileRecord(
            id = "tch-custom-101",
            name = "Ustad Ghulam Nabi",
            designation = "Subject Specialist Physics",
            department = "Physics & Applied Sciences",
            qualification = "M.Sc Physics",
            assignedSubjects = "Physics",
            assignedClasses = "11th, 12th",
            introduction = "Senior lecturer at BHSS Tolti.",
            photoUri = savedUri1
        )
        repo.insertTeacher(teacher)
        db.close()

        // 2. Reopen DB (simulating app restart) and verify photo is still attached to teacher ID
        db = Room.databaseBuilder(context, SchoolDatabase::class.java, dbName)
            .allowMainThreadQueries()
            .build()
        repo = SchoolRepository(db)

        val reloadedTeacher = repo.getTeacherById("tch-custom-101")
        assertNotNull(reloadedTeacher)
        assertEquals(savedUri1, reloadedTeacher!!.photoUri)
        assertTrue(MediaStorageHelper.hasValidPhoto(reloadedTeacher.photoUri))

        // 3. Replace photo with photo #2 and verify old file is cleaned up and new photo persists
        val savedUri2 = MediaStorageHelper.saveTeacherPhoto(context, Uri.fromFile(photo2Source), "tch-custom-101")
        assertNotNull(savedUri2)
        repo.updateTeacherPhoto("tch-custom-101", savedUri2)

        val file1 = File(Uri.parse(savedUri1).path!!)
        val file2 = File(Uri.parse(savedUri2).path!!)
        assertFalse("Old photo file should be removed upon replacement", file1.exists())
        assertTrue("New photo file should exist", file2.exists())

        db.close()
        db = Room.databaseBuilder(context, SchoolDatabase::class.java, dbName)
            .allowMainThreadQueries()
            .build()
        repo = SchoolRepository(db)

        val afterReplace = repo.getTeacherById("tch-custom-101")
        assertEquals(savedUri2, afterReplace?.photoUri)
        assertTrue(MediaStorageHelper.hasValidPhoto(afterReplace?.photoUri))

        // 4. Delete teacher photo and verify DB column becomes null and file on disk is deleted
        repo.updateTeacherPhoto("tch-custom-101", null)
        assertFalse("Deleted photo file should be removed from storage", file2.exists())

        val afterDelete = repo.getTeacherById("tch-custom-101")
        assertNotNull(afterDelete)
        assertNull(afterDelete!!.photoUri)
        db.close()
    }
}
