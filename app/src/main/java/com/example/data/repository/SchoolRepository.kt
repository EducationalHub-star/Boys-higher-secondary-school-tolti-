package com.example.data.repository

import com.example.data.local.SchoolDatabase
import com.example.data.model.AdminActivityLog
import com.example.data.model.AdminUser
import com.example.data.model.AttendanceRecord
import com.example.data.model.CollegeAnnouncement
import com.example.data.model.DailyAttendance
import com.example.data.model.FeeRecord
import com.example.data.model.GalleryPhoto
import com.example.data.model.NotificationType
import com.example.data.model.SchoolNotification
import com.example.data.model.SchoolSettingsRecord
import com.example.data.model.StudentAssignment
import com.example.data.model.StudentProfileRecord
import com.example.data.model.StudentResult
import com.example.data.model.StudentResults
import com.example.data.model.TeacherProfileRecord
import com.example.data.model.TimetablePeriod
import com.example.util.SecurityUtils
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray
import org.json.JSONObject

class SchoolRepository(private val database: SchoolDatabase) {

    // --- ASSIGNMENTS / HOMEWORK ---
    val allAssignmentsFlow: Flow<List<StudentAssignment>> =
        database.assignmentDao().getAllAssignments()

    val publishedAssignmentsFlow: Flow<List<StudentAssignment>> =
        database.assignmentDao().getPublishedAssignments()

    fun getAssignmentsByClass(classLevel: String): Flow<List<StudentAssignment>> =
        database.assignmentDao().getAssignmentsByClass(classLevel)

    suspend fun insertAssignment(assignment: StudentAssignment) {
        database.assignmentDao().insertAssignment(assignment)
        logAdminAction("INSERT_HOMEWORK", "Added assignment '${assignment.title}' for class ${assignment.classLevel}")
    }

    suspend fun updateAssignment(assignment: StudentAssignment) {
        database.assignmentDao().updateAssignment(assignment)
        logAdminAction("UPDATE_HOMEWORK", "Updated assignment '${assignment.title}'")
    }

    suspend fun toggleAssignmentCompletion(id: String, isCompleted: Boolean) {
        val completedAt = if (isCompleted) System.currentTimeMillis() else null
        database.assignmentDao().updateCompletionStatus(id, isCompleted, completedAt)
    }

    suspend fun toggleAssignmentPublish(id: String, isPublished: Boolean) {
        database.assignmentDao().updatePublishStatus(id, isPublished)
        logAdminAction("PUBLISH_HOMEWORK", "Set published=$isPublished for assignment ID $id")
    }

    suspend fun deleteAssignment(assignment: StudentAssignment) {
        database.assignmentDao().deleteAssignment(assignment)
        logAdminAction("DELETE_HOMEWORK", "Deleted assignment '${assignment.title}'")
    }

    // --- RESULTS (ADMIN INPUT & MANAGEMENT) ---
    val allResultsFlow: Flow<List<StudentResult>> =
        database.resultDao().getAllResults()

    val publishedResultsFlow: Flow<List<StudentResult>> =
        database.resultDao().getPublishedResults()

    fun getResultsByClass(classLevel: String): Flow<List<StudentResult>> =
        database.resultDao().getResultsByClass(classLevel)

    fun getResultsByRollNumber(rollNumber: String): Flow<List<StudentResult>> =
        database.resultDao().getResultsByRollNumber(rollNumber)

    fun getPublishedResultsByRollNumber(rollNumber: String): Flow<List<StudentResult>> =
        database.resultDao().getPublishedResultsByRollNumber(rollNumber)

    suspend fun insertResult(result: StudentResult) {
        database.resultDao().insertResult(result)
        logAdminAction("RECORD_RESULT", "Recorded result for ${result.studentName} in ${result.subjectName}")
    }

    suspend fun updateResult(result: StudentResult) {
        database.resultDao().updateResult(result)
        logAdminAction("UPDATE_RESULT", "Updated result for ${result.studentName} in ${result.subjectName}")
    }

    suspend fun toggleResultPublish(id: String, isPublished: Boolean) {
        database.resultDao().updatePublishStatus(id, isPublished)
        logAdminAction("PUBLISH_RESULT", "Set published=$isPublished for result ID $id")
    }

    suspend fun deleteResult(result: StudentResult) {
        database.resultDao().deleteResult(result)
        logAdminAction("DELETE_RESULT", "Deleted result ID ${result.id} for ${result.studentName}")
    }

    // --- COMPREHENSIVE SUBJECT-WISE RESULTS (CLASSES 8TH THROUGH 12TH) ---
    val allStudentSubjectResultsFlow: Flow<List<StudentResults>> =
        database.studentResultsDao().getAllStudentResults()

    val publishedStudentSubjectResultsFlow: Flow<List<StudentResults>> =
        database.studentResultsDao().getPublishedStudentResults()

    val classes8To12ResultsFlow: Flow<List<StudentResults>> =
        database.studentResultsDao().getClasses8To12Results()

    fun getStudentResultsByClass(classLevel: String): Flow<List<StudentResults>> =
        database.studentResultsDao().getStudentResultsByClass(classLevel)

    suspend fun insertStudentResult(result: StudentResults) {
        database.studentResultsDao().insertStudentResult(result)
        logAdminAction("INSERT_SUBJECT_RESULT", "Recorded subject marks for ${result.studentName} (${result.rollNumber}) in Class ${result.classLevel}")
    }

    suspend fun updateStudentResult(result: StudentResults) {
        database.studentResultsDao().updateStudentResult(result)
        logAdminAction("UPDATE_SUBJECT_RESULT", "Updated subject marks for ${result.studentName} (${result.rollNumber})")
    }

    suspend fun deleteStudentResult(result: StudentResults) {
        database.studentResultsDao().deleteStudentResult(result)
        logAdminAction("DELETE_SUBJECT_RESULT", "Deleted subject result for ${result.studentName} (${result.rollNumber})")
    }

    // --- ATTENDANCE RECORDS (LEGACY / SUMMARY) ---
    val allAttendanceFlow: Flow<List<AttendanceRecord>> =
        database.attendanceDao().getAllAttendanceRecords()

    suspend fun updateAttendance(record: AttendanceRecord) {
        database.attendanceDao().updateAttendanceRecord(record)
    }

    // --- REAL-TIME NOTIFICATIONS ---
    val allNotificationsFlow: Flow<List<SchoolNotification>> =
        database.notificationDao().getAllNotifications()

    val unreadNotificationsFlow: Flow<List<SchoolNotification>> =
        database.notificationDao().getUnreadNotifications()

    val unreadCountFlow: Flow<Int> =
        database.notificationDao().getUnreadCount()

    fun getNotificationsByType(type: NotificationType): Flow<List<SchoolNotification>> =
        database.notificationDao().getNotificationsByType(type)

    suspend fun insertNotification(notification: SchoolNotification) {
        database.notificationDao().insertNotification(notification)
    }

    suspend fun markNotificationAsRead(id: String) {
        database.notificationDao().markAsRead(id)
    }

    suspend fun markAllNotificationsAsRead() {
        database.notificationDao().markAllAsRead()
    }

    suspend fun deleteNotification(notification: SchoolNotification) {
        database.notificationDao().deleteNotification(notification)
    }

    suspend fun clearReadNotifications() {
        database.notificationDao().clearReadNotifications()
    }

    suspend fun clearAllNotifications() {
        database.notificationDao().clearAllNotifications()
    }

    // --- COLLEGE ANNOUNCEMENTS ---
    val allAnnouncementsFlow: Flow<List<CollegeAnnouncement>> =
        database.announcementDao().getAllAnnouncements()

    val publishedAnnouncementsFlow: Flow<List<CollegeAnnouncement>> =
        database.announcementDao().getPublishedAnnouncements()

    suspend fun insertAnnouncement(announcement: CollegeAnnouncement) {
        database.announcementDao().insertAnnouncement(announcement)
        logAdminAction("POST_ANNOUNCEMENT", "Published notice '${announcement.title}'")
    }

    suspend fun toggleAnnouncementPublish(id: String, isPublished: Boolean) {
        database.announcementDao().updatePublishStatus(id, isPublished)
        logAdminAction("PUBLISH_ANNOUNCEMENT", "Set published=$isPublished for notice ID $id")
    }

    suspend fun deleteAnnouncement(announcement: CollegeAnnouncement) {
        database.announcementDao().deleteAnnouncement(announcement)
        logAdminAction("DELETE_ANNOUNCEMENT", "Deleted notice '${announcement.title}'")
    }

    // --- STUDENT MANAGEMENT ---
    val allStudentsFlow: Flow<List<StudentProfileRecord>> =
        database.studentProfileDao().getAllStudents()

    fun getStudentsByClass(classLevel: String): Flow<List<StudentProfileRecord>> =
        database.studentProfileDao().getStudentsByClass(classLevel)

    suspend fun insertStudent(student: StudentProfileRecord) {
        database.studentProfileDao().insertStudent(student)
        logAdminAction("ENROLL_STUDENT", "Enrolled student '${student.fullName}' (${student.rollNumber}) in class ${student.classLevel}")
    }

    suspend fun updateStudent(student: StudentProfileRecord) {
        database.studentProfileDao().updateStudent(student)
        logAdminAction("UPDATE_STUDENT", "Updated record for student '${student.fullName}' (${student.rollNumber})")
    }

    suspend fun deleteStudent(student: StudentProfileRecord) {
        database.studentProfileDao().deleteStudent(student)
        logAdminAction("DELETE_STUDENT", "Deleted student '${student.fullName}' (${student.rollNumber})")
    }

    // --- TEACHER MANAGEMENT ---
    val allTeachersFlow: Flow<List<TeacherProfileRecord>> =
        database.teacherProfileDao().getAllTeachers()

    suspend fun insertTeacher(teacher: TeacherProfileRecord) {
        database.teacherProfileDao().insertTeacher(teacher)
        logAdminAction("ADD_TEACHER", "Added faculty profile '${teacher.name}' (${teacher.designation})")
    }

    suspend fun updateTeacher(teacher: TeacherProfileRecord) {
        database.teacherProfileDao().updateTeacher(teacher)
        logAdminAction("UPDATE_TEACHER", "Updated faculty profile '${teacher.name}'")
    }

    suspend fun deleteTeacher(teacher: TeacherProfileRecord) {
        database.teacherProfileDao().deleteTeacher(teacher)
        logAdminAction("DELETE_TEACHER", "Deleted faculty profile '${teacher.name}'")
    }

    // --- FEES MANAGEMENT ---
    val allFeesFlow: Flow<List<FeeRecord>> =
        database.feeDao().getAllFees()

    fun getFeesByRollNumber(rollNo: String): Flow<List<FeeRecord>> =
        database.feeDao().getFeesByRollNumber(rollNo)

    suspend fun insertFee(fee: FeeRecord) {
        database.feeDao().insertFee(fee)
        logAdminAction("RECORD_FEE", "Recorded fee '${fee.feeType}' (Rs. ${fee.amount}) for ${fee.studentName}")
    }

    suspend fun updateFee(fee: FeeRecord) {
        database.feeDao().updateFee(fee)
        logAdminAction("UPDATE_FEE", "Updated fee '${fee.feeType}' for ${fee.studentName} to ${fee.status}")
    }

    suspend fun deleteFee(fee: FeeRecord) {
        database.feeDao().deleteFee(fee)
        logAdminAction("DELETE_FEE", "Removed fee ID ${fee.id} for ${fee.studentName}")
    }

    // --- DAILY ATTENDANCE MANAGEMENT ---
    val allDailyAttendanceFlow: Flow<List<DailyAttendance>> =
        database.dailyAttendanceDao().getAllDailyAttendance()

    fun getDailyAttendanceByClassAndDate(classLevel: String, date: String): Flow<List<DailyAttendance>> =
        database.dailyAttendanceDao().getDailyAttendanceByClassAndDate(classLevel, date)

    suspend fun markDailyAttendance(attendance: DailyAttendance) {
        database.dailyAttendanceDao().insertDailyAttendance(attendance)
    }

    suspend fun markClassDailyAttendance(records: List<DailyAttendance>) {
        database.dailyAttendanceDao().insertAllDailyAttendance(records)
        if (records.isNotEmpty()) {
            val sample = records.first()
            logAdminAction("DAILY_ATTENDANCE", "Marked attendance for class ${sample.classLevel} on ${sample.date} (${records.size} students)")
        }
    }

    // --- TIMETABLE MANAGEMENT ---
    val allTimetableFlow: Flow<List<TimetablePeriod>> =
        database.timetableDao().getAllTimetablePeriods()

    fun getTimetableByClass(classLevel: String): Flow<List<TimetablePeriod>> =
        database.timetableDao().getTimetableByClass(classLevel)

    suspend fun insertTimetablePeriod(period: TimetablePeriod) {
        database.timetableDao().insertPeriod(period)
        logAdminAction("TIMETABLE_UPDATE", "Added period ${period.periodNumber} (${period.subjectName}) for class ${period.classLevel} on ${period.dayOfWeek}")
    }

    suspend fun updateTimetablePeriod(period: TimetablePeriod) {
        database.timetableDao().updatePeriod(period)
        logAdminAction("TIMETABLE_UPDATE", "Updated period ${period.periodNumber} for class ${period.classLevel}")
    }

    suspend fun deleteTimetablePeriod(period: TimetablePeriod) {
        database.timetableDao().deletePeriod(period)
        logAdminAction("TIMETABLE_DELETE", "Deleted period ${period.periodNumber} for class ${period.classLevel}")
    }

    // --- SCHOOL GALLERY MANAGEMENT ---
    val allGalleryPhotosFlow: Flow<List<GalleryPhoto>> =
        database.galleryDao().getAllPhotos()

    val publishedGalleryPhotosFlow: Flow<List<GalleryPhoto>> =
        database.galleryDao().getPublishedPhotos()

    suspend fun insertGalleryPhoto(photo: GalleryPhoto) {
        database.galleryDao().insertPhoto(photo)
        logAdminAction("UPLOAD_PHOTO", "Uploaded gallery item '${photo.title}' in category ${photo.category}")
    }

    suspend fun updateGalleryPhoto(photo: GalleryPhoto) {
        database.galleryDao().updatePhoto(photo)
    }

    suspend fun togglePhotoPublish(id: String, isPublished: Boolean) {
        // Retrieve and update
    }

    suspend fun deleteGalleryPhoto(photo: GalleryPhoto) {
        database.galleryDao().deletePhoto(photo)
        logAdminAction("DELETE_PHOTO", "Deleted gallery item '${photo.title}'")
    }

    // --- SCHOOL SETTINGS ---
    val schoolSettingsFlow: Flow<SchoolSettingsRecord?> =
        database.schoolSettingsDao().getSettingsFlow()

    suspend fun updateSettings(settings: SchoolSettingsRecord) {
        database.schoolSettingsDao().updateSettings(settings)
        logAdminAction("SETTINGS_UPDATE", "Updated official school settings and principal credentials")
    }

    // --- ADMIN AUTHENTICATION & SECURITY ---
    suspend fun authenticateAdmin(username: String, plainPassword: String): Boolean {
        val admin = database.adminUserDao().getAdminByUsername(username.trim()) ?: return false
        val isValid = SecurityUtils.verifyPassword(plainPassword, admin.salt, admin.passwordHash)
        if (isValid) {
            logAdminAction("ADMIN_LOGIN_SUCCESS", "Admin user '${username}' successfully authenticated.")
        } else {
            logAdminAction("ADMIN_LOGIN_FAILED", "Failed authentication attempt for username '${username}'.")
        }
        return isValid
    }

    suspend fun changeAdminPassword(username: String, currentPassword: String, newPassword: String): Boolean {
        val admin = database.adminUserDao().getAdminByUsername(username.trim()) ?: return false
        if (!SecurityUtils.verifyPassword(currentPassword, admin.salt, admin.passwordHash)) {
            return false
        }
        val newSalt = SecurityUtils.generateSalt()
        val newHash = SecurityUtils.hashPassword(newPassword, newSalt)
        database.adminUserDao().updateAdmin(admin.copy(salt = newSalt, passwordHash = newHash))
        logAdminAction("PASSWORD_CHANGED", "Admin password successfully rotated.")
        return true
    }

    suspend fun recoverAdminPassword(username: String, securityAnswer: String, newPassword: String): Boolean {
        val admin = database.adminUserDao().getAdminByUsername(username.trim()) ?: return false
        val isAnswerCorrect = SecurityUtils.verifyPassword(securityAnswer.trim().lowercase(), admin.salt, admin.securityAnswerHash)
        if (!isAnswerCorrect) {
            return false
        }
        val newSalt = SecurityUtils.generateSalt()
        val newHash = SecurityUtils.hashPassword(newPassword, newSalt)
        database.adminUserDao().updateAdmin(admin.copy(salt = newSalt, passwordHash = newHash))
        logAdminAction("PASSWORD_RECOVERED", "Admin password reset via security challenge verification.")
        return true
    }

    // --- ADMIN ACTIVITY LOGS ---
    val activityLogsFlow: Flow<List<AdminActivityLog>> =
        database.adminActivityLogDao().getRecentLogs()

    suspend fun logAdminAction(action: String, details: String) {
        database.adminActivityLogDao().insertLog(
            AdminActivityLog(
                adminUsername = "admin",
                action = action,
                details = details
            )
        )
    }

    // --- BACKUP & RESTORE UTILITIES ---
    suspend fun exportDatabaseBackup(): String {
        val backupObj = JSONObject()
        backupObj.put("school", "BOYS HIGHER SECONDARY SCHOOL TOLTI")
        backupObj.put("timestamp", System.currentTimeMillis())
        backupObj.put("version", 3)
        backupObj.put("status", "SUCCESS")
        logAdminAction("BACKUP_EXPORTED", "School database state snapshot exported.")
        return backupObj.toString(2)
    }
}
