package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Room entity representing a student assignment with due dates and completion status.
 */
@Entity(tableName = "student_assignments")
data class StudentAssignment(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val subject: String,
    val courseCode: String,
    val classLevel: String = "12th", // "5th" to "12th"
    val teacherName: String,
    val description: String,
    val dueDateTimestamp: Long,
    val dueDateFormatted: String,
    val totalMarks: Int = 100,
    val isCompleted: Boolean = false,
    val completedAtTimestamp: Long? = null,
    val studentName: String = "Muhammad Khan",
    val rollNumber: String = "BHSS-2026-104",
    val submissionNotes: String? = null,
    val priority: String = "NORMAL", // "URGENT", "NORMAL", "LOW"
    val isPublished: Boolean = true
) {
    val isOverdue: Boolean
        get() = !isCompleted && System.currentTimeMillis() > dueDateTimestamp
}

/**
 * Room entity representing an academic test or exam result entered by an administrator.
 */
@Entity(tableName = "student_results")
data class StudentResult(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val studentName: String,
    val rollNumber: String,
    val classLevel: String = "12th", // "5th", "6th", "7th", "8th", "9th", "10th", "11th", "12th"
    val examTerm: String, // "Annual Board Exam", "Mid Term", "Monthly Test", "Final Exam"
    val subjectName: String,
    val subjectCode: String,
    val totalMarks: Int = 100,
    val obtainedMarks: Int,
    val remarks: String = "",
    val enteredByAdmin: String = "Admin / Exam Controller",
    val dateRecorded: Long = System.currentTimeMillis(),
    val isPublished: Boolean = true
) {
    val percentage: Double
        get() = if (totalMarks > 0) (obtainedMarks.toDouble() / totalMarks.toDouble()) * 100.0 else 0.0

    val grade: String
        get() = calculateGrade(obtainedMarks, totalMarks)

    val status: String
        get() = if (obtainedMarks >= (totalMarks * 0.40)) "PASS" else "FAIL"

    companion object {
        fun calculateGrade(obtained: Int, total: Int): String {
            if (total <= 0) return "F"
            val pct = (obtained.toDouble() / total.toDouble()) * 100.0
            return when {
                pct >= 85.0 -> "A+"
                pct >= 75.0 -> "A"
                pct >= 65.0 -> "B"
                pct >= 55.0 -> "C"
                pct >= 40.0 -> "D"
                else -> "F"
            }
        }
    }
}

/**
 * Room entity representing attendance standing for a course subject.
 */
@Entity(tableName = "attendance_records")
data class AttendanceRecord(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val courseCode: String,
    val subjectName: String,
    val classLevel: String = "12th",
    val teacherName: String,
    val attendedCount: Int,
    val totalCount: Int,
    val roomNumber: String = "Room 101"
) {
    val percentage: Float
        get() = if (totalCount == 0) 100f else (attendedCount.toFloat() / totalCount.toFloat()) * 100f
}

/**
 * Types of academic notifications supported by the real-time alerting engine.
 */
enum class NotificationType(val label: String) {
    NEW_ASSIGNMENT("New Assignment"),
    UPCOMING_DEADLINE("Upcoming Deadline"),
    GRADE_UPDATE("Grade Update"),
    COLLEGE_ANNOUNCEMENT("College Announcement")
}

/**
 * Priority level for sorting and styling notifications.
 */
enum class NotificationPriority {
    URGENT,
    HIGH,
    NORMAL,
    INFO
}

/**
 * Room entity representing an academic notification received by the student.
 */
@Entity(tableName = "school_notifications")
data class SchoolNotification(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val message: String,
    val type: NotificationType,
    val priority: NotificationPriority = NotificationPriority.NORMAL,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val targetClass: String = "ALL", // "ALL", "12th", "11th", etc.
    val relatedId: String? = null,
    val relatedSubject: String? = null,
    val actionPayload: String? = null
)

/**
 * Room entity representing an official college broadcast or administrative announcement.
 */
@Entity(tableName = "college_announcements")
data class CollegeAnnouncement(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val category: String, // "Academic", "Examinations", "Campus Events", "Administrative", "Weather Alert"
    val targetAudience: String = "All Students",
    val author: String = "Principal Office",
    val timestamp: Long = System.currentTimeMillis(),
    val isPinned: Boolean = false,
    val isUrgent: Boolean = false,
    val isPublished: Boolean = true
)

/**
 * User notification preferences for alerts and channel routing.
 */
data class NotificationPreferences(
    val assignmentsEnabled: Boolean = true,
    val deadlinesEnabled: Boolean = true,
    val gradesEnabled: Boolean = true,
    val announcementsEnabled: Boolean = true,
    val systemTrayAlerts: Boolean = true,
    val soundAndVibrate: Boolean = true,
    val deadlineLeadHours: Int = 24
)

// ==========================================
// EXPANDED SCHOOL MANAGEMENT SYSTEM ENTITIES
// ==========================================

/**
 * Enrolled student master profile record (Classes 5th to 12th).
 */
@Entity(tableName = "student_profiles")
data class StudentProfileRecord(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val fullName: String,
    val fatherName: String,
    val rollNumber: String, // Unique Roll Number e.g. "BHSS-2026-104"
    val classLevel: String, // "5th", "6th", "7th", "8th", "9th", "10th", "11th", "12th"
    val section: String = "A",
    val dateOfBirth: String = "2008-05-12",
    val admissionDate: String = "2024-03-01",
    val contactPhone: String = "0345-9876543",
    val address: String = "Main Bazar, Tolti, Kharmang",
    val bloodGroup: String = "O+",
    val status: String = "ACTIVE", // "ACTIVE", "ALUMNI", "TRANSFERRED"
    val notes: String = "Excelling in academics and physical athletics."
)

/**
 * Teacher faculty master profile record.
 */
@Entity(tableName = "teacher_profiles")
data class TeacherProfileRecord(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val designation: String, // e.g. "Principal / SSS", "Subject Specialist Physics", "Lecturer Math"
    val department: String, // "Computer Science", "Physics", "Mathematics", "Science", "Humanities"
    val qualification: String, // "M.Sc, B.Ed, M.Phil"
    val assignedSubjects: String, // "C++ Programming, Database Systems"
    val assignedClasses: String, // "11th, 12th"
    val introduction: String,
    val phone: String = "0355-1234567",
    val email: String = "faculty.tolti@edu.pk",
    val joiningYear: String = "2018"
)

/**
 * Fee structure and payment records for students.
 */
@Entity(tableName = "fee_records")
data class FeeRecord(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val studentRollNumber: String,
    val studentName: String,
    val classLevel: String,
    val feeType: String, // "Monthly Tuition", "Board Examination Fee", "Computer Lab Fund", "Sports Fund"
    val amount: Double,
    val dueDate: String,
    val status: String = "PAID", // "PAID", "PENDING", "OVERDUE"
    val paymentDate: String? = "2026-10-15",
    val receiptNumber: String? = "REC-2026-104",
    val paymentMode: String = "Cash at Office"
)

/**
 * Daily student attendance records (Present, Absent, Leave).
 */
@Entity(tableName = "daily_attendance")
data class DailyAttendance(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val studentRollNumber: String,
    val studentName: String,
    val classLevel: String,
    val date: String, // YYYY-MM-DD
    val status: String, // "PRESENT", "ABSENT", "LEAVE"
    val recordedBy: String = "Teacher / Attendance Incharge"
)

/**
 * Weekly class timetable periods (Monday to Saturday, Classes 5th to 12th).
 */
@Entity(tableName = "timetable_periods")
data class TimetablePeriod(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val classLevel: String, // "5th" to "12th"
    val dayOfWeek: String, // "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    val periodNumber: Int, // 1 to 7
    val timeRange: String, // e.g. "08:30 AM - 09:15 AM"
    val subjectName: String,
    val teacherName: String,
    val roomName: String = "Room 101"
)

/**
 * School photo gallery record.
 */
@Entity(tableName = "gallery_photos")
data class GalleryPhoto(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val category: String, // "School Building", "Classrooms", "Teachers", "School Events", "Sports Activities", "Students' Activities", "Achievements"
    val drawableResName: String, // drawable name e.g. "reel_cricket", "reel_chai", "reel_fields", "school_logo"
    val dateUploaded: Long = System.currentTimeMillis(),
    val isPublished: Boolean = true
)

/**
 * Configurable school information and settings.
 */
@Entity(tableName = "school_settings")
data class SchoolSettingsRecord(
    @PrimaryKey
    val id: Int = 1,
    val schoolName: String = "BOYS HIGHER SECONDARY SCHOOL TOLTI",
    val tagline: String = "LEARN • GROW • SUCCEED",
    val address: String = "Main Bazar, Tolti, District Kharmang, Gilgit-Baltistan",
    val phone: String = "+92 5816 920101",
    val email: String = "info.bhsstolti@edu.pk",
    val principalName: String = "Sir Ghulam Nabi (M.Sc, M.Ed)",
    val showStampPlaceholder: Boolean = true,
    val showSignaturePlaceholder: Boolean = true,
    val aboutHistory: String = "Established as the premier secondary and higher secondary institution in the historic Kharmang valley, Boys Higher Secondary School Tolti delivers rigorous science and humanities education from Class 5th through Class 12th (Matric & Intermediate ICS / Pre-Engineering). The school prides itself on discipline, academic merit, character building, and Karakoram student talent development.",
    val language: String = "EN"
)

/**
 * Secure administrator user with salted SHA-256 password authentication.
 */
@Entity(tableName = "admin_users")
data class AdminUser(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val username: String = "admin",
    val passwordHash: String,
    val salt: String,
    val fullName: String = "Headmaster / System Administrator",
    val role: String = "SUPER_ADMIN",
    val securityQuestion: String = "What valley is the school located in?",
    val securityAnswerHash: String
)

/**
 * Audit log of administrative operations.
 */
@Entity(tableName = "admin_activity_logs")
data class AdminActivityLog(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val adminUsername: String = "admin",
    val action: String,
    val details: String
)

/**
 * Timed administrator session with auto-expiry.
 */
data class AdminSession(
    val token: String = UUID.randomUUID().toString(),
    val username: String = "admin",
    val role: String = "SUPER_ADMIN",
    val loginTimestamp: Long = System.currentTimeMillis(),
    val expiryTimestamp: Long = System.currentTimeMillis() + (25 * 60 * 1000L)
)
