package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.AdminActivityLog
import com.example.data.model.AdminUser
import com.example.data.model.AppDesignConfig
import com.example.data.model.AttendanceRecord
import com.example.data.model.ClassSubjectRecord
import com.example.data.model.CollegeAnnouncement
import com.example.data.model.DailyAttendance
import com.example.data.model.FeeRecord
import com.example.data.model.GalleryPhoto
import com.example.data.model.SchoolNotification
import com.example.data.model.SchoolSettingsRecord
import com.example.data.model.StudentAssignment
import com.example.data.model.StudentProfileRecord
import com.example.data.model.StudentResult
import com.example.data.model.StudentResults
import com.example.data.model.TeacherProfileRecord
import com.example.data.model.TeacherRating
import com.example.data.model.TimetablePeriod
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        StudentAssignment::class,
        StudentResult::class,
        StudentResults::class,
        AttendanceRecord::class,
        SchoolNotification::class,
        CollegeAnnouncement::class,
        StudentProfileRecord::class,
        TeacherProfileRecord::class,
        TeacherRating::class,
        FeeRecord::class,
        DailyAttendance::class,
        TimetablePeriod::class,
        GalleryPhoto::class,
        SchoolSettingsRecord::class,
        AdminUser::class,
        AdminActivityLog::class,
        AppDesignConfig::class,
        ClassSubjectRecord::class
    ],
    version = 6,
    exportSchema = false
)
abstract class SchoolDatabase : RoomDatabase() {

    abstract fun assignmentDao(): AssignmentDao
    abstract fun resultDao(): ResultDao
    abstract fun studentResultsDao(): StudentResultsDao
    abstract fun attendanceDao(): AttendanceDao
    abstract fun notificationDao(): NotificationDao
    abstract fun announcementDao(): AnnouncementDao
    abstract fun studentProfileDao(): StudentProfileDao
    abstract fun teacherProfileDao(): TeacherProfileDao
    abstract fun teacherRatingDao(): TeacherRatingDao
    abstract fun feeDao(): FeeDao
    abstract fun dailyAttendanceDao(): DailyAttendanceDao
    abstract fun timetableDao(): TimetableDao
    abstract fun galleryDao(): GalleryDao
    abstract fun schoolSettingsDao(): SchoolSettingsDao
    abstract fun adminUserDao(): AdminUserDao
    abstract fun adminActivityLogDao(): AdminActivityLogDao
    abstract fun appDesignConfigDao(): AppDesignConfigDao
    abstract fun classSubjectDao(): ClassSubjectDao

    companion object {
        @Volatile
        private var INSTANCE: SchoolDatabase? = null

        fun getInstance(context: Context): SchoolDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SchoolDatabase::class.java,
                    "bhss_tolti_academic_db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(SchoolDatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class SchoolDatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            populateInitialData(seedCollections = true)
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            populateInitialData(seedCollections = false)
        }

        private fun populateInitialData(seedCollections: Boolean) {
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    val isFirstBootstrap = seedCollections || database.adminUserDao().getAdminByUsername("admin") == null
                    if (isFirstBootstrap) {
                        database.assignmentDao().insertAllAssignments(InitialSchoolData.getInitialAssignments())
                        database.resultDao().insertAllResults(InitialSchoolData.getInitialResults())
                        database.studentResultsDao().insertAllStudentResults(InitialSchoolData.getInitialStudentSubjectResults())
                        database.attendanceDao().insertAllAttendance(InitialSchoolData.getInitialAttendance())
                        database.notificationDao().insertAllNotifications(InitialSchoolData.getInitialNotifications())
                        database.announcementDao().insertAllAnnouncements(InitialSchoolData.getInitialAnnouncements())

                        database.studentProfileDao().insertAllStudents(InitialSchoolData.getInitialStudents())
                        database.teacherProfileDao().insertAllTeachers(InitialSchoolData.getInitialTeachers())
                        database.feeDao().insertAllFees(InitialSchoolData.getInitialFees())
                        database.dailyAttendanceDao().insertAllDailyAttendance(InitialSchoolData.getInitialDailyAttendance())
                        database.timetableDao().insertAllPeriods(InitialSchoolData.getInitialTimetable())
                        database.galleryDao().insertAllPhotos(InitialSchoolData.getInitialGallery())
                        database.teacherRatingDao().insertAllRatings(InitialSchoolData.getInitialTeacherRatings())
                        database.classSubjectDao().insertAllClassSubjects(InitialSchoolData.getInitialClassSubjects())

                        database.adminActivityLogDao().insertLog(
                            AdminActivityLog(
                                adminUsername = "system",
                                action = "BOOTSTRAP_COMPLETE",
                                details = "School database initialized with students, faculty, classes, results, timetable & gallery."
                            )
                        )
                    }

                    if (database.schoolSettingsDao().getSettings() == null) {
                        database.schoolSettingsDao().insertSettings(InitialSchoolData.getInitialSettings())
                    }
                    if (database.appDesignConfigDao().getConfig(AppDesignConfig.ID_PUBLISHED) == null) {
                        database.appDesignConfigDao().upsertConfig(AppDesignConfig(id = AppDesignConfig.ID_PUBLISHED))
                    }
                    if (database.adminUserDao().getAdminByUsername("admin") == null) {
                        database.adminUserDao().insertAdmin(InitialSchoolData.getInitialAdminUser())
                    }
                }
            }
        }
    }
}
