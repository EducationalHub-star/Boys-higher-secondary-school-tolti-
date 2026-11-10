package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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
import com.example.data.model.TeacherProfileRecord
import com.example.data.model.TimetablePeriod
import kotlinx.coroutines.flow.Flow

@Dao
interface AssignmentDao {

    @Query("SELECT * FROM student_assignments ORDER BY isCompleted ASC, dueDateTimestamp ASC")
    fun getAllAssignments(): Flow<List<StudentAssignment>>

    @Query("SELECT * FROM student_assignments WHERE isPublished = 1 ORDER BY isCompleted ASC, dueDateTimestamp ASC")
    fun getPublishedAssignments(): Flow<List<StudentAssignment>>

    @Query("SELECT * FROM student_assignments WHERE classLevel = :classLevel ORDER BY isCompleted ASC, dueDateTimestamp ASC")
    fun getAssignmentsByClass(classLevel: String): Flow<List<StudentAssignment>>

    @Query("SELECT * FROM student_assignments WHERE id = :id LIMIT 1")
    suspend fun getAssignmentById(id: String): StudentAssignment?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignment(assignment: StudentAssignment)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAssignments(assignments: List<StudentAssignment>)

    @Update
    suspend fun updateAssignment(assignment: StudentAssignment)

    @Query("UPDATE student_assignments SET isCompleted = :isCompleted, completedAtTimestamp = :completedAt WHERE id = :id")
    suspend fun updateCompletionStatus(id: String, isCompleted: Boolean, completedAt: Long?)

    @Query("UPDATE student_assignments SET isPublished = :isPublished WHERE id = :id")
    suspend fun updatePublishStatus(id: String, isPublished: Boolean)

    @Delete
    suspend fun deleteAssignment(assignment: StudentAssignment)

    @Query("DELETE FROM student_assignments WHERE id = :id")
    suspend fun deleteAssignmentById(id: String)
}

@Dao
interface ResultDao {

    @Query("SELECT * FROM student_results ORDER BY dateRecorded DESC")
    fun getAllResults(): Flow<List<StudentResult>>

    @Query("SELECT * FROM student_results WHERE isPublished = 1 ORDER BY dateRecorded DESC")
    fun getPublishedResults(): Flow<List<StudentResult>>

    @Query("SELECT * FROM student_results WHERE classLevel = :classLevel ORDER BY dateRecorded DESC")
    fun getResultsByClass(classLevel: String): Flow<List<StudentResult>>

    @Query("SELECT * FROM student_results WHERE rollNumber = :rollNumber ORDER BY dateRecorded DESC")
    fun getResultsByRollNumber(rollNumber: String): Flow<List<StudentResult>>

    @Query("SELECT * FROM student_results WHERE isPublished = 1 AND rollNumber = :rollNumber ORDER BY dateRecorded DESC")
    fun getPublishedResultsByRollNumber(rollNumber: String): Flow<List<StudentResult>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: StudentResult)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllResults(results: List<StudentResult>)

    @Update
    suspend fun updateResult(result: StudentResult)

    @Query("UPDATE student_results SET isPublished = :isPublished WHERE id = :id")
    suspend fun updatePublishStatus(id: String, isPublished: Boolean)

    @Delete
    suspend fun deleteResult(result: StudentResult)

    @Query("DELETE FROM student_results WHERE id = :id")
    suspend fun deleteResultById(id: String)
}

@Dao
interface AttendanceDao {

    @Query("SELECT * FROM attendance_records ORDER BY courseCode ASC")
    fun getAllAttendanceRecords(): Flow<List<AttendanceRecord>>

    @Query("SELECT * FROM attendance_records WHERE classLevel = :classLevel ORDER BY courseCode ASC")
    fun getAttendanceByClass(classLevel: String): Flow<List<AttendanceRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendanceRecord(record: AttendanceRecord)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAttendance(records: List<AttendanceRecord>)

    @Update
    suspend fun updateAttendanceRecord(record: AttendanceRecord)
}

@Dao
interface NotificationDao {

    @Query("SELECT * FROM school_notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<SchoolNotification>>

    @Query("SELECT * FROM school_notifications WHERE isRead = 0 ORDER BY timestamp DESC")
    fun getUnreadNotifications(): Flow<List<SchoolNotification>>

    @Query("SELECT COUNT(*) FROM school_notifications WHERE isRead = 0")
    fun getUnreadCount(): Flow<Int>

    @Query("SELECT * FROM school_notifications WHERE type = :type ORDER BY timestamp DESC")
    fun getNotificationsByType(type: NotificationType): Flow<List<SchoolNotification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: SchoolNotification)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllNotifications(notifications: List<SchoolNotification>)

    @Query("UPDATE school_notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: String)

    @Query("UPDATE school_notifications SET isRead = 1")
    suspend fun markAllAsRead()

    @Delete
    suspend fun deleteNotification(notification: SchoolNotification)

    @Query("DELETE FROM school_notifications WHERE id = :id")
    suspend fun deleteNotificationById(id: String)

    @Query("DELETE FROM school_notifications WHERE isRead = 1")
    suspend fun clearReadNotifications()

    @Query("DELETE FROM school_notifications")
    suspend fun clearAllNotifications()
}

@Dao
interface AnnouncementDao {

    @Query("SELECT * FROM college_announcements ORDER BY isPinned DESC, timestamp DESC")
    fun getAllAnnouncements(): Flow<List<CollegeAnnouncement>>

    @Query("SELECT * FROM college_announcements WHERE isPublished = 1 ORDER BY isPinned DESC, timestamp DESC")
    fun getPublishedAnnouncements(): Flow<List<CollegeAnnouncement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: CollegeAnnouncement)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAnnouncements(announcements: List<CollegeAnnouncement>)

    @Query("UPDATE college_announcements SET isPublished = :isPublished WHERE id = :id")
    suspend fun updatePublishStatus(id: String, isPublished: Boolean)

    @Delete
    suspend fun deleteAnnouncement(announcement: CollegeAnnouncement)

    @Query("DELETE FROM college_announcements WHERE id = :id")
    suspend fun deleteAnnouncementById(id: String)
}

@Dao
interface StudentProfileDao {

    @Query("SELECT * FROM student_profiles ORDER BY classLevel ASC, rollNumber ASC")
    fun getAllStudents(): Flow<List<StudentProfileRecord>>

    @Query("SELECT * FROM student_profiles WHERE classLevel = :classLevel ORDER BY rollNumber ASC")
    fun getStudentsByClass(classLevel: String): Flow<List<StudentProfileRecord>>

    @Query("SELECT * FROM student_profiles WHERE rollNumber = :rollNumber LIMIT 1")
    suspend fun getStudentByRollNumber(rollNumber: String): StudentProfileRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: StudentProfileRecord)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllStudents(students: List<StudentProfileRecord>)

    @Update
    suspend fun updateStudent(student: StudentProfileRecord)

    @Delete
    suspend fun deleteStudent(student: StudentProfileRecord)

    @Query("DELETE FROM student_profiles WHERE id = :id")
    suspend fun deleteStudentById(id: String)
}

@Dao
interface TeacherProfileDao {

    @Query("SELECT * FROM teacher_profiles ORDER BY name ASC")
    fun getAllTeachers(): Flow<List<TeacherProfileRecord>>

    @Query("SELECT * FROM teacher_profiles WHERE department = :dept ORDER BY name ASC")
    fun getTeachersByDepartment(dept: String): Flow<List<TeacherProfileRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacher(teacher: TeacherProfileRecord)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTeachers(teachers: List<TeacherProfileRecord>)

    @Update
    suspend fun updateTeacher(teacher: TeacherProfileRecord)

    @Delete
    suspend fun deleteTeacher(teacher: TeacherProfileRecord)

    @Query("DELETE FROM teacher_profiles WHERE id = :id")
    suspend fun deleteTeacherById(id: String)
}

@Dao
interface FeeDao {

    @Query("SELECT * FROM fee_records ORDER BY dueDate DESC")
    fun getAllFees(): Flow<List<FeeRecord>>

    @Query("SELECT * FROM fee_records WHERE studentRollNumber = :rollNo ORDER BY dueDate DESC")
    fun getFeesByRollNumber(rollNo: String): Flow<List<FeeRecord>>

    @Query("SELECT * FROM fee_records WHERE status = :status ORDER BY dueDate DESC")
    fun getFeesByStatus(status: String): Flow<List<FeeRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFee(fee: FeeRecord)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFees(fees: List<FeeRecord>)

    @Update
    suspend fun updateFee(fee: FeeRecord)

    @Delete
    suspend fun deleteFee(fee: FeeRecord)
}

@Dao
interface DailyAttendanceDao {

    @Query("SELECT * FROM daily_attendance ORDER BY date DESC, studentRollNumber ASC")
    fun getAllDailyAttendance(): Flow<List<DailyAttendance>>

    @Query("SELECT * FROM daily_attendance WHERE classLevel = :classLevel AND date = :date ORDER BY studentRollNumber ASC")
    fun getDailyAttendanceByClassAndDate(classLevel: String, date: String): Flow<List<DailyAttendance>>

    @Query("SELECT * FROM daily_attendance WHERE studentRollNumber = :rollNo ORDER BY date DESC")
    fun getDailyAttendanceByRollNumber(rollNo: String): Flow<List<DailyAttendance>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyAttendance(attendance: DailyAttendance)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllDailyAttendance(records: List<DailyAttendance>)

    @Update
    suspend fun updateDailyAttendance(attendance: DailyAttendance)
}

@Dao
interface TimetableDao {

    @Query("SELECT * FROM timetable_periods ORDER BY classLevel ASC, dayOfWeek ASC, periodNumber ASC")
    fun getAllTimetablePeriods(): Flow<List<TimetablePeriod>>

    @Query("SELECT * FROM timetable_periods WHERE classLevel = :classLevel ORDER BY periodNumber ASC")
    fun getTimetableByClass(classLevel: String): Flow<List<TimetablePeriod>>

    @Query("SELECT * FROM timetable_periods WHERE classLevel = :classLevel AND dayOfWeek = :dayOfWeek ORDER BY periodNumber ASC")
    fun getTimetableByClassAndDay(classLevel: String, dayOfWeek: String): Flow<List<TimetablePeriod>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPeriod(period: TimetablePeriod)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPeriods(periods: List<TimetablePeriod>)

    @Update
    suspend fun updatePeriod(period: TimetablePeriod)

    @Delete
    suspend fun deletePeriod(period: TimetablePeriod)
}

@Dao
interface GalleryDao {

    @Query("SELECT * FROM gallery_photos ORDER BY dateUploaded DESC")
    fun getAllPhotos(): Flow<List<GalleryPhoto>>

    @Query("SELECT * FROM gallery_photos WHERE isPublished = 1 ORDER BY dateUploaded DESC")
    fun getPublishedPhotos(): Flow<List<GalleryPhoto>>

    @Query("SELECT * FROM gallery_photos WHERE isPublished = 1 AND category = :category ORDER BY dateUploaded DESC")
    fun getPublishedPhotosByCategory(category: String): Flow<List<GalleryPhoto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: GalleryPhoto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPhotos(photos: List<GalleryPhoto>)

    @Update
    suspend fun updatePhoto(photo: GalleryPhoto)

    @Delete
    suspend fun deletePhoto(photo: GalleryPhoto)

    @Query("DELETE FROM gallery_photos WHERE id = :id")
    suspend fun deletePhotoById(id: String)
}

@Dao
interface SchoolSettingsDao {

    @Query("SELECT * FROM school_settings WHERE id = 1 LIMIT 1")
    fun getSettingsFlow(): Flow<SchoolSettingsRecord?>

    @Query("SELECT * FROM school_settings WHERE id = 1 LIMIT 1")
    suspend fun getSettings(): SchoolSettingsRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: SchoolSettingsRecord)

    @Update
    suspend fun updateSettings(settings: SchoolSettingsRecord)
}

@Dao
interface AdminUserDao {

    @Query("SELECT * FROM admin_users WHERE username = :username LIMIT 1")
    suspend fun getAdminByUsername(username: String): AdminUser?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdmin(admin: AdminUser)

    @Update
    suspend fun updateAdmin(admin: AdminUser)
}

@Dao
interface AdminActivityLogDao {

    @Query("SELECT * FROM admin_activity_logs ORDER BY timestamp DESC LIMIT 100")
    fun getRecentLogs(): Flow<List<AdminActivityLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: AdminActivityLog)

    @Query("DELETE FROM admin_activity_logs")
    suspend fun clearLogs()
}
