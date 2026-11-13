package com.example.data.remote

import android.content.Context
import android.util.Log
import com.example.R
import com.example.data.model.AppDesignConfig
import com.example.data.model.CollegeAnnouncement
import com.example.data.model.GalleryPhoto
import com.example.data.model.StudentResult
import com.example.data.model.TeacherRating
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import org.json.JSONArray
import org.json.JSONObject

enum class OperationType(val value: String) {
    CREATE("create"),
    UPDATE("update"),
    DELETE("delete"),
    LIST("list"),
    GET("get"),
    WRITE("write"),
}

/**
 * Optional non-blocking cloud synchronization helper using the configured named Firestore database.
 * Local Room storage remains primary so offline/local workflows always work uninterrupted.
 */
object FirestoreSyncHelper {

    private fun getDb(context: Context): FirebaseFirestore? {
        return try {
            val dbId = context.applicationContext.getString(R.string.firestore_database_id)
            FirebaseFirestore.getInstance(dbId)
        } catch (e: Exception) {
            Log.w("FirestoreSync", "Named Firestore DB unavailable: ${e.message}")
            null
        }
    }

    fun handleFirestoreError(exception: Throwable, operationType: OperationType, path: String?): String {
        val causes = generateSequence(exception) { it.cause }
        val rootError = causes.firstOrNull { it is FirebaseFirestoreException }
            ?: causes.firstOrNull { it !is CancellationException }
            ?: exception

        val currentUser = Firebase.auth.currentUser
        val providerInfoList = currentUser?.providerData?.map { provider ->
            JSONObject().apply {
                put("providerId", provider.providerId)
                put("email", provider.email)
            }
        } ?: emptyList()

        val authInfoJson = JSONObject().apply {
            put("userId", currentUser?.uid)
            put("email", currentUser?.email)
            put("emailVerified", currentUser?.isEmailVerified)
            put("tenantId", currentUser?.tenantId)
            put("providerInfo", JSONArray(providerInfoList))
        }

        val errorInfoJson = JSONObject().apply {
            put("error", rootError.message ?: rootError.toString())
            put("operationType", operationType.value)
            put("path", path)
            put("authInfo", authInfoJson)
        }

        val jsonString = errorInfoJson.toString()
        Log.e("FirestoreError", "Firestore Error: $jsonString")
        return jsonString
    }

    suspend fun syncPublishedDesignConfig(context: Context?, config: AppDesignConfig) {
        if (context == null) return
        val uid = Firebase.auth.currentUser?.uid ?: return
        val db = getDb(context) ?: return
        val path = "design_config/published"
        val payload = mapOf(
            "themeMode" to config.themeMode,
            "primaryColorHex" to config.primaryColorHex,
            "secondaryColorHex" to config.secondaryColorHex,
            "accentColorHex" to config.accentColorHex,
            "backgroundColorHex" to config.backgroundColorHex,
            "buttonStyle" to config.buttonStyle,
            "cardStyle" to config.cardStyle,
            "fontSizeScale" to config.fontSizeScale,
            "iconNavStyle" to config.iconNavStyle,
            "homeSectionOrder" to config.homeSectionOrder,
            "hiddenHomeSections" to config.hiddenHomeSections,
            "announcementsLayout" to config.announcementsLayout,
            "resultsLayout" to config.resultsLayout,
            "adminDashboardLayout" to config.adminDashboardLayout,
            "updatedByUid" to uid,
            "updatedAt" to FieldValue.serverTimestamp()
        ).filterValues { it != null }
        try {
            db.document(path).set(payload, SetOptions.merge()).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
        }
    }

    suspend fun syncTeacherRating(context: Context?, rating: TeacherRating) {
        if (context == null) return
        val uid = Firebase.auth.currentUser?.uid ?: return
        val db = getDb(context) ?: return
        val path = "teacher_ratings/${rating.id}"
        val payload = mapOf(
            "authorUid" to uid,
            "teacherId" to rating.teacherId,
            "teacherName" to rating.teacherName,
            "studentName" to rating.studentName,
            "studentRollNumber" to rating.studentRollNumber,
            "rating" to rating.rating,
            "feedback" to rating.feedback,
            "subjectName" to rating.subjectName,
            "isApproved" to rating.isApproved,
            "updatedAt" to FieldValue.serverTimestamp()
        )
        try {
            db.document(path).set(payload, SetOptions.merge()).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
        }
    }

    suspend fun syncAnnouncement(context: Context?, announcement: CollegeAnnouncement) {
        if (context == null) return
        Firebase.auth.currentUser?.uid ?: return
        val db = getDb(context) ?: return
        val path = "announcements/${announcement.id}"
        val payload = mapOf(
            "title" to announcement.title,
            "content" to announcement.content,
            "category" to announcement.category,
            "targetAudience" to announcement.targetAudience,
            "author" to announcement.author,
            "isUrgent" to announcement.isUrgent,
            "isPinned" to announcement.isPinned,
            "isPublished" to announcement.isPublished,
            "updatedAt" to FieldValue.serverTimestamp()
        )
        try {
            db.document(path).set(payload, SetOptions.merge()).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
        }
    }

    suspend fun syncStudentResult(context: Context?, result: StudentResult) {
        if (context == null) return
        Firebase.auth.currentUser?.uid ?: return
        val db = getDb(context) ?: return
        val path = "student_results/${result.id}"
        val payload = mapOf(
            "studentName" to result.studentName,
            "rollNumber" to result.rollNumber,
            "classLevel" to result.classLevel,
            "examTerm" to result.examTerm,
            "subjectName" to result.subjectName,
            "subjectCode" to result.subjectCode,
            "totalMarks" to result.totalMarks,
            "obtainedMarks" to result.obtainedMarks,
            "grade" to result.grade,
            "status" to result.status,
            "isPublished" to result.isPublished,
            "updatedAt" to FieldValue.serverTimestamp()
        )
        try {
            db.document(path).set(payload, SetOptions.merge()).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
        }
    }

    suspend fun syncGalleryPhoto(context: Context?, photo: GalleryPhoto) {
        if (context == null) return
        Firebase.auth.currentUser?.uid ?: return
        val db = getDb(context) ?: return
        val path = "gallery_photos/${photo.id}"
        val payload = mapOf(
            "title" to photo.title,
            "description" to photo.description,
            "category" to photo.category,
            "drawableResName" to photo.drawableResName,
            "isPublished" to photo.isPublished,
            "updatedAt" to FieldValue.serverTimestamp()
        )
        try {
            db.document(path).set(payload, SetOptions.merge()).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
        }
    }
}
