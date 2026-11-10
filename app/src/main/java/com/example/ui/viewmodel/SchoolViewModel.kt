package com.example.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.InitialSchoolData
import com.example.data.model.AdminActivityLog
import com.example.data.model.AttendanceRecord
import com.example.data.model.CollegeAnnouncement
import com.example.data.model.DailyAttendance
import com.example.data.model.FeeRecord
import com.example.data.model.GalleryPhoto
import com.example.data.model.NotificationPreferences
import com.example.data.model.NotificationPriority
import com.example.data.model.NotificationType
import com.example.data.model.SchoolNotification
import com.example.data.model.SchoolSettingsRecord
import com.example.data.model.StudentAssignment
import com.example.data.model.StudentProfileRecord
import com.example.data.model.StudentResult
import com.example.data.model.StudentResults
import com.example.data.model.TeacherProfileRecord
import com.example.data.model.TimetablePeriod
import com.example.data.repository.SchoolRepository
import com.example.service.RealtimeNotificationManager
import com.example.util.UrduLocalization
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

enum class SchoolNavTab {
    ASSIGNMENTS,
    ADMIN_RESULTS,
    DASHBOARD
}

enum class SchoolAppSection(val englishTitle: String, val urduTitle: String) {
    HOME("Home", "ہوم پیج"),
    RESULTS("Student Results", "امتحانی نتائج"),
    ATTENDANCE("Attendance", "حاضری ریکارڈ"),
    ANNOUNCEMENTS("Announcements", "اعلانات"),
    TEACHERS("Our Teachers", "اساتذہ کرام"),
    TIMETABLE("Timetable", "کلاس شیڈول"),
    HOMEWORK("Homework", "ہوم ورک"),
    GALLERY("School Gallery", "گیلری"),
    ABOUT_CONTACT("About & Contact", "ہمارے بارے میں"),
    ADMIN_PANEL("Admin Portal", "ایڈمن پینل")
}

data class StudentProfile(
    val name: String = "Muhammad Khan",
    val rollNumber: String = "BHSS-2026-104",
    val currentClass: String = "12th",
    val stream: String = "ICS (Computer Science, Math & Physics)",
    val school: String = "Boys Higher Secondary School Tolti, Kharmang",
    val email: String = "shabirhasan291@gmail.com"
)

class SchoolViewModel(
    private val repository: SchoolRepository,
    private val context: Context? = null
) : ViewModel() {

    // --- NAVIGATION & LANGUAGE ---
    val currentSection = MutableStateFlow(SchoolAppSection.HOME)
    val currentLanguage = MutableStateFlow(UrduLocalization.Language.ENGLISH)

    fun toggleLanguage() {
        currentLanguage.value = if (currentLanguage.value == UrduLocalization.Language.ENGLISH) {
            UrduLocalization.Language.URDU
        } else {
            UrduLocalization.Language.ENGLISH
        }
    }

    // --- ADMIN AUTHENTICATION STATE ---
    val isAdminLoggedIn = MutableStateFlow(false)
    val adminUsername = MutableStateFlow<String?>("admin")
    val adminLoginError = MutableStateFlow<String?>(null)

    val showAdminLoginDialog = MutableStateFlow(false)
    val showChangePasswordDialog = MutableStateFlow(false)
    val showPasswordRecoveryDialog = MutableStateFlow(false)

    // Legacy tab compatibility
    val selectedTab = MutableStateFlow(SchoolNavTab.DASHBOARD)
    val studentProfile = MutableStateFlow(StudentProfile())

    // --- FILTER & SEARCH STATES ---
    val selectedClassLevel = MutableStateFlow("ALL") // "ALL", "5th", "6th", "7th", "8th", "9th", "10th", "11th", "12th"
    val resultSearchQuery = MutableStateFlow("")
    val resultExamTermFilter = MutableStateFlow("ALL")
    val resultClassFilter = MutableStateFlow("ALL")

    // Subject-wise results for Classes 8th to 12th
    val subjectResultsClassFilter = MutableStateFlow("ALL") // "ALL", "8th", "9th", "10th", "11th", "12th"
    val subjectResultsSearchQuery = MutableStateFlow("")
    val classes8To12Options = listOf("ALL", "8th", "9th", "10th", "11th", "12th")

    val studentSearchQuery = MutableStateFlow("")
    val studentClassFilter = MutableStateFlow("ALL")

    val teacherSearchQuery = MutableStateFlow("")
    val teacherDeptFilter = MutableStateFlow("ALL")

    val timetableClassFilter = MutableStateFlow("12th")
    val timetableDayFilter = MutableStateFlow("Monday")

    val galleryCategoryFilter = MutableStateFlow("ALL")

    val assignmentStatusFilter = MutableStateFlow("ALL")
    val assignmentSearchQuery = MutableStateFlow("")

    val notificationFilterType = MutableStateFlow("ALL")
    val notificationSearchQuery = MutableStateFlow("")

    // --- DATABASE FLOWS ---
    val allAssignments: StateFlow<List<StudentAssignment>> = repository.allAssignmentsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allResults: StateFlow<List<StudentResult>> = repository.allResultsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val publishedResults: StateFlow<List<StudentResult>> = repository.publishedResultsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allStudentSubjectResults: StateFlow<List<StudentResults>> = repository.allStudentSubjectResultsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val classes8To12Results: StateFlow<List<StudentResults>> = repository.classes8To12ResultsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allStudents: StateFlow<List<StudentProfileRecord>> = repository.allStudentsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTeachers: StateFlow<List<TeacherProfileRecord>> = repository.allTeachersFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allFees: StateFlow<List<FeeRecord>> = repository.allFeesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allDailyAttendance: StateFlow<List<DailyAttendance>> = repository.allDailyAttendanceFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTimetablePeriods: StateFlow<List<TimetablePeriod>> = repository.allTimetableFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allGalleryPhotos: StateFlow<List<GalleryPhoto>> = repository.allGalleryPhotosFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val publishedGalleryPhotos: StateFlow<List<GalleryPhoto>> = repository.publishedGalleryPhotosFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val schoolSettings: StateFlow<SchoolSettingsRecord?> = repository.schoolSettingsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val adminActivityLogs: StateFlow<List<AdminActivityLog>> = repository.activityLogsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val attendanceRecords: StateFlow<List<AttendanceRecord>> = repository.allAttendanceFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAnnouncements: StateFlow<List<CollegeAnnouncement>> = repository.allAnnouncementsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allNotifications: StateFlow<List<SchoolNotification>> = repository.allNotificationsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unreadNotificationCount: StateFlow<Int> = repository.unreadCountFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val notificationPreferences = MutableStateFlow(NotificationPreferences())

    // --- REALTIME ALERT BANNER ---
    private val _realtimeAlert = MutableStateFlow<SchoolNotification?>(null)
    val realtimeAlert: StateFlow<SchoolNotification?> = _realtimeAlert.asStateFlow()
    private var alertDismissJob: Job? = null

    // Dialog & Detail states
    val showAddAssignmentDialog = MutableStateFlow(false)
    val showAdminResultDialog = MutableStateFlow(false)
    val showAddStudentDialog = MutableStateFlow(false)
    val showAddTeacherDialog = MutableStateFlow(false)
    val showRecordFeeDialog = MutableStateFlow(false)
    val showAddTimetableDialog = MutableStateFlow(false)
    val showUploadPhotoDialog = MutableStateFlow(false)
    val showEditSettingsDialog = MutableStateFlow(false)

    val showNotificationCenter = MutableStateFlow(false)
    val showNotificationSettings = MutableStateFlow(false)
    val showBroadcastAnnouncementDialog = MutableStateFlow(false)
    val showNotificationSimulator = MutableStateFlow(false)

    val selectedAssignmentForDetails = MutableStateFlow<StudentAssignment?>(null)
    val selectedAnnouncementForDetails = MutableStateFlow<CollegeAnnouncement?>(null)
    val selectedStudentForDetails = MutableStateFlow<StudentProfileRecord?>(null)
    val selectedTeacherForDetails = MutableStateFlow<TeacherProfileRecord?>(null)
    val selectedResultCard = MutableStateFlow<StudentResult?>(null)
    val selectedSubjectResultCard = MutableStateFlow<StudentResults?>(null)
    val showAddSubjectResultDialog = MutableStateFlow(false)
    val selectedGalleryPhoto = MutableStateFlow<GalleryPhoto?>(null)

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    fun showSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    val classOptions = listOf("ALL", "5th", "6th", "7th", "8th", "9th", "10th", "11th", "12th")
    val gradeLevelOptions = listOf("5th", "6th", "7th", "8th", "9th", "10th", "11th", "12th")

    // --- FILTERED STREAMS ---

    val filteredAssignments: StateFlow<List<StudentAssignment>> = combine(
        allAssignments,
        selectedClassLevel,
        assignmentStatusFilter,
        assignmentSearchQuery
    ) { list, classLevel, status, query ->
        list.filter { asg ->
            val matchesClass = if (classLevel == "ALL") true else asg.classLevel == classLevel
            val matchesStatus = when (status) {
                "PENDING" -> !asg.isCompleted && !asg.isOverdue
                "COMPLETED" -> asg.isCompleted
                "OVERDUE" -> asg.isOverdue
                else -> true
            }
            val matchesQuery = query.isBlank() ||
                    asg.title.contains(query, ignoreCase = true) ||
                    asg.subject.contains(query, ignoreCase = true) ||
                    asg.teacherName.contains(query, ignoreCase = true)

            matchesClass && matchesStatus && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredResults: StateFlow<List<StudentResult>> = combine(
        allResults,
        resultClassFilter,
        resultExamTermFilter,
        resultSearchQuery,
        isAdminLoggedIn
    ) { list, classLevel, examTerm, query, isAdmin ->
        list.filter { res ->
            val matchesPublish = isAdmin || res.isPublished
            val matchesClass = if (classLevel == "ALL") true else res.classLevel == classLevel
            val matchesTerm = if (examTerm == "ALL") true else res.examTerm == examTerm
            val matchesQuery = query.isBlank() ||
                    res.studentName.contains(query, ignoreCase = true) ||
                    res.rollNumber.contains(query, ignoreCase = true) ||
                    res.subjectName.contains(query, ignoreCase = true)

            matchesPublish && matchesClass && matchesTerm && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredSubjectResults: StateFlow<List<StudentResults>> = combine(
        allStudentSubjectResults,
        subjectResultsClassFilter,
        subjectResultsSearchQuery,
        isAdminLoggedIn
    ) { list, classLevel, query, isAdmin ->
        list.filter { res ->
            val matchesPublish = isAdmin || res.isPublished
            val matchesClass = if (classLevel == "ALL") true else res.classLevel == classLevel
            val matchesQuery = query.isBlank() ||
                    res.studentName.contains(query, ignoreCase = true) ||
                    res.fatherName.contains(query, ignoreCase = true) ||
                    res.rollNumber.contains(query, ignoreCase = true) ||
                    res.examName.contains(query, ignoreCase = true) ||
                    res.boardName.contains(query, ignoreCase = true)

            matchesPublish && matchesClass && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredStudents: StateFlow<List<StudentProfileRecord>> = combine(
        allStudents,
        studentClassFilter,
        studentSearchQuery
    ) { list, classLevel, query ->
        list.filter { std ->
            val matchesClass = if (classLevel == "ALL") true else std.classLevel == classLevel
            val matchesQuery = query.isBlank() ||
                    std.fullName.contains(query, ignoreCase = true) ||
                    std.fatherName.contains(query, ignoreCase = true) ||
                    std.rollNumber.contains(query, ignoreCase = true) ||
                    std.address.contains(query, ignoreCase = true)

            matchesClass && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredNotifications: StateFlow<List<SchoolNotification>> = combine(
        allNotifications,
        notificationFilterType,
        notificationSearchQuery
    ) { list, filterType, query ->
        list.filter { notif ->
            val matchesFilter = when (filterType) {
                "UNREAD" -> !notif.isRead
                "DEADLINES" -> notif.type == NotificationType.UPCOMING_DEADLINE
                "GRADES" -> notif.type == NotificationType.GRADE_UPDATE
                "ASSIGNMENTS" -> notif.type == NotificationType.NEW_ASSIGNMENT
                "ANNOUNCEMENTS" -> notif.type == NotificationType.COLLEGE_ANNOUNCEMENT
                else -> true
            }
            val matchesQuery = query.isBlank() ||
                    notif.title.contains(query, ignoreCase = true) ||
                    notif.message.contains(query, ignoreCase = true) ||
                    (notif.relatedSubject?.contains(query, ignoreCase = true) == true)

            matchesFilter && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            delay(1000)
            checkUpcomingDeadlines(manualTrigger = false)
        }
    }

    // --- ADMIN AUTHENTICATION ---

    fun loginAdmin(username: String, plainPass: String) {
        viewModelScope.launch {
            adminLoginError.value = null
            if (username.isBlank() || plainPass.isBlank()) {
                adminLoginError.value = "Username and password cannot be empty."
                return@launch
            }
            val success = repository.authenticateAdmin(username, plainPass)
            if (success) {
                isAdminLoggedIn.value = true
                adminUsername.value = username.trim()
                showAdminLoginDialog.value = false
                currentSection.value = SchoolAppSection.ADMIN_PANEL
                _snackbarMessage.value = "Welcome, Administrator. Secure session active."
            } else {
                adminLoginError.value = "Invalid administrator credentials. Please check password."
            }
        }
    }

    fun logoutAdmin() {
        isAdminLoggedIn.value = false
        currentSection.value = SchoolAppSection.HOME
        _snackbarMessage.value = "Administrator signed out safely."
    }

    fun changeAdminPassword(oldPass: String, newPass: String, onDone: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val user = adminUsername.value ?: "admin"
            if (newPass.length < 6) {
                onDone(false, "New password must be at least 6 characters.")
                return@launch
            }
            val success = repository.changeAdminPassword(user, oldPass, newPass)
            if (success) {
                showChangePasswordDialog.value = false
                onDone(true, "Admin password updated successfully.")
                _snackbarMessage.value = "Admin password updated successfully."
            } else {
                onDone(false, "Current password is incorrect.")
            }
        }
    }

    fun recoverAdminPassword(answer: String, newPass: String, onDone: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val user = adminUsername.value ?: "admin"
            val success = repository.recoverAdminPassword(user, answer, newPass)
            if (success) {
                showPasswordRecoveryDialog.value = false
                onDone(true, "Password reset successful! You can now log in.")
                _snackbarMessage.value = "Password reset successful! Please log in."
            } else {
                onDone(false, "Incorrect answer to the security challenge.")
            }
        }
    }

    // --- STUDENT MANAGEMENT ACTIONS ---

    fun addStudent(
        fullName: String,
        fatherName: String,
        rollNumber: String,
        classLevel: String,
        section: String,
        dateOfBirth: String,
        admissionDate: String,
        contactPhone: String,
        address: String,
        bloodGroup: String,
        notes: String
    ) {
        viewModelScope.launch {
            val student = StudentProfileRecord(
                fullName = fullName,
                fatherName = fatherName,
                rollNumber = rollNumber,
                classLevel = classLevel,
                section = section,
                dateOfBirth = dateOfBirth,
                admissionDate = admissionDate,
                contactPhone = contactPhone,
                address = address,
                bloodGroup = bloodGroup,
                notes = notes
            )
            repository.insertStudent(student)
            showAddStudentDialog.value = false
            _snackbarMessage.value = "Enrolled student '${fullName}' ($rollNumber) in Class $classLevel."
        }
    }

    fun updateStudent(student: StudentProfileRecord) {
        viewModelScope.launch {
            repository.updateStudent(student)
            _snackbarMessage.value = "Updated student profile for ${student.fullName}."
        }
    }

    fun deleteStudent(student: StudentProfileRecord) {
        viewModelScope.launch {
            repository.deleteStudent(student)
            _snackbarMessage.value = "Removed student '${student.fullName}' (${student.rollNumber})."
        }
    }

    // --- TEACHER MANAGEMENT ACTIONS ---

    fun addTeacher(
        name: String,
        designation: String,
        department: String,
        qualification: String,
        assignedSubjects: String,
        assignedClasses: String,
        introduction: String,
        phone: String,
        email: String
    ) {
        viewModelScope.launch {
            val teacher = TeacherProfileRecord(
                name = name,
                designation = designation,
                department = department,
                qualification = qualification,
                assignedSubjects = assignedSubjects,
                assignedClasses = assignedClasses,
                introduction = introduction,
                phone = phone,
                email = email
            )
            repository.insertTeacher(teacher)
            showAddTeacherDialog.value = false
            _snackbarMessage.value = "Added faculty member '${name}' to $department."
        }
    }

    fun updateTeacher(teacher: TeacherProfileRecord) {
        viewModelScope.launch {
            repository.updateTeacher(teacher)
            _snackbarMessage.value = "Updated faculty profile for ${teacher.name}."
        }
    }

    fun deleteTeacher(teacher: TeacherProfileRecord) {
        viewModelScope.launch {
            repository.deleteTeacher(teacher)
            _snackbarMessage.value = "Removed faculty profile '${teacher.name}'."
        }
    }

    // --- RESULTS & MARKSHEET ACTIONS ---

    fun saveStudentResult(
        studentName: String,
        rollNumber: String,
        classLevel: String,
        examTerm: String,
        subjectName: String,
        subjectCode: String,
        totalMarks: Int,
        obtainedMarks: Int,
        remarks: String,
        enteredBy: String,
        isPublished: Boolean = true
    ) {
        viewModelScope.launch {
            val result = StudentResult(
                studentName = studentName,
                rollNumber = rollNumber,
                classLevel = classLevel,
                examTerm = examTerm,
                subjectName = subjectName,
                subjectCode = subjectCode,
                totalMarks = totalMarks,
                obtainedMarks = obtainedMarks,
                remarks = remarks,
                enteredByAdmin = if (enteredBy.isBlank()) "Examination Admin" else enteredBy,
                dateRecorded = System.currentTimeMillis(),
                isPublished = isPublished
            )
            repository.insertResult(result)
            showAdminResultDialog.value = false

            // Real-time alert
            val pct = result.percentage.toInt()
            val gradeStr = result.grade
            postNotification(
                title = "New Grade Released: $subjectName ($obtainedMarks/$totalMarks)",
                message = "$examTerm marks entered for $studentName. Grade: $gradeStr ($pct%). ${if (remarks.isNotBlank()) "Remarks: $remarks" else ""}",
                type = NotificationType.GRADE_UPDATE,
                priority = if (result.status == "PASS") NotificationPriority.HIGH else NotificationPriority.URGENT,
                targetClass = classLevel,
                relatedId = result.id,
                relatedSubject = subjectName
            )

            _snackbarMessage.value = "Result for ${studentName} (${subjectName}: ${obtainedMarks}/${totalMarks} Grade ${result.grade}) recorded!"
        }
    }

    fun toggleResultPublish(result: StudentResult) {
        viewModelScope.launch {
            val newStatus = !result.isPublished
            repository.toggleResultPublish(result.id, newStatus)
            _snackbarMessage.value = if (newStatus) {
                "Published result for ${result.studentName} (${result.subjectName})"
            } else {
                "Unpublished result for ${result.studentName} (hidden from public view)"
            }
        }
    }

    fun deleteResult(result: StudentResult) {
        viewModelScope.launch {
            repository.deleteResult(result)
            _snackbarMessage.value = "Removed result entry for ${result.subjectName} (${result.studentName})."
        }
    }

    // --- SUBJECT-WISE RESULTS (CLASSES 8th-12th) ACTIONS ---

    fun insertStudentSubjectResults(results: StudentResults) {
        viewModelScope.launch {
            repository.insertStudentResult(results)
            showAddSubjectResultDialog.value = false
            _snackbarMessage.value = "Marksheet for ${results.studentName} (Class ${results.classLevel}, Roll ${results.rollNumber}) recorded!"
        }
    }

    fun updateStudentSubjectResults(results: StudentResults) {
        viewModelScope.launch {
            repository.updateStudentResult(results)
            _snackbarMessage.value = "Updated marksheet for ${results.studentName}."
        }
    }

    fun deleteStudentSubjectResults(results: StudentResults) {
        viewModelScope.launch {
            repository.deleteStudentResult(results)
            _snackbarMessage.value = "Deleted marksheet for ${results.studentName} (${results.rollNumber})."
        }
    }

    fun toggleSubjectResultPublish(results: StudentResults) {
        viewModelScope.launch {
            val updated = results.copy(isPublished = !results.isPublished)
            repository.updateStudentResult(updated)
            _snackbarMessage.value = if (updated.isPublished) {
                "Published marksheet for ${results.studentName} (${results.classLevel})"
            } else {
                "Unpublished marksheet for ${results.studentName} (hidden from public view)"
            }
        }
    }

    // --- TIMETABLE ACTIONS ---

    fun addTimetablePeriod(
        classLevel: String,
        dayOfWeek: String,
        periodNumber: Int,
        timeRange: String,
        subjectName: String,
        teacherName: String,
        roomName: String
    ) {
        viewModelScope.launch {
            val period = TimetablePeriod(
                classLevel = classLevel,
                dayOfWeek = dayOfWeek,
                periodNumber = periodNumber,
                timeRange = timeRange,
                subjectName = subjectName,
                teacherName = teacherName,
                roomName = roomName
            )
            repository.insertTimetablePeriod(period)
            showAddTimetableDialog.value = false
            _snackbarMessage.value = "Scheduled Period $periodNumber ($subjectName) for Class $classLevel on $dayOfWeek."
        }
    }

    fun deleteTimetablePeriod(period: TimetablePeriod) {
        viewModelScope.launch {
            repository.deleteTimetablePeriod(period)
            _snackbarMessage.value = "Removed Period ${period.periodNumber} (${period.subjectName})."
        }
    }

    // --- FEES ACTIONS ---

    fun recordFee(
        rollNo: String,
        name: String,
        classLevel: String,
        feeType: String,
        amount: Double,
        dueDate: String,
        status: String,
        paymentMode: String
    ) {
        viewModelScope.launch {
            val receiptNo = if (status == "PAID") "REC-2026-${(100..999).random()}" else null
            val payDate = if (status == "PAID") "2026-11-03" else null
            val fee = FeeRecord(
                studentRollNumber = rollNo,
                studentName = name,
                classLevel = classLevel,
                feeType = feeType,
                amount = amount,
                dueDate = dueDate,
                status = status,
                paymentDate = payDate,
                receiptNumber = receiptNo,
                paymentMode = paymentMode
            )
            repository.insertFee(fee)
            showRecordFeeDialog.value = false
            _snackbarMessage.value = "Fee recorded for $name (Rs. $amount, $status)."
        }
    }

    fun markFeePaid(fee: FeeRecord) {
        viewModelScope.launch {
            val updated = fee.copy(
                status = "PAID",
                paymentDate = "2026-11-03",
                receiptNumber = "REC-2026-${(100..999).random()}"
            )
            repository.updateFee(updated)
            _snackbarMessage.value = "Fee marked as PAID for ${fee.studentName}. Receipt: ${updated.receiptNumber}"
        }
    }

    fun deleteFee(fee: FeeRecord) {
        viewModelScope.launch {
            repository.deleteFee(fee)
            _snackbarMessage.value = "Removed fee record for ${fee.studentName}."
        }
    }

    // --- DAILY ATTENDANCE ACTIONS ---

    fun markStudentDailyAttendance(rollNo: String, name: String, classLevel: String, date: String, status: String) {
        viewModelScope.launch {
            val record = DailyAttendance(
                studentRollNumber = rollNo,
                studentName = name,
                classLevel = classLevel,
                date = date,
                status = status,
                recordedBy = adminUsername.value ?: "Admin"
            )
            repository.markDailyAttendance(record)
            _snackbarMessage.value = "Marked $name as $status for $date."
        }
    }

    // --- GALLERY ACTIONS ---

    fun uploadGalleryPhoto(
        title: String,
        description: String,
        category: String,
        drawableResName: String
    ) {
        viewModelScope.launch {
            val photo = GalleryPhoto(
                title = title,
                description = description,
                category = category,
                drawableResName = if (drawableResName.isBlank()) "school_logo" else drawableResName,
                isPublished = true
            )
            repository.insertGalleryPhoto(photo)
            showUploadPhotoDialog.value = false
            _snackbarMessage.value = "Added photo '${title}' to school gallery."
        }
    }

    fun deleteGalleryPhoto(photo: GalleryPhoto) {
        viewModelScope.launch {
            repository.deleteGalleryPhoto(photo)
            _snackbarMessage.value = "Deleted photo '${photo.title}' from gallery."
        }
    }

    // --- SCHOOL SETTINGS ACTIONS ---

    fun updateSchoolSettings(
        address: String,
        phone: String,
        email: String,
        principalName: String,
        aboutHistory: String,
        showStamp: Boolean,
        showSignature: Boolean
    ) {
        viewModelScope.launch {
            val current = schoolSettings.value ?: InitialSchoolData.getInitialSettings()
            val updated = current.copy(
                address = address,
                phone = phone,
                email = email,
                principalName = principalName,
                aboutHistory = aboutHistory,
                showStampPlaceholder = showStamp,
                showSignaturePlaceholder = showSignature
            )
            repository.updateSettings(updated)
            showEditSettingsDialog.value = false
            _snackbarMessage.value = "Official school settings and credentials updated."
        }
    }

    // --- BACKUP ACTIONS ---

    fun exportBackup(onResult: (String) -> Unit) {
        viewModelScope.launch {
            val json = repository.exportDatabaseBackup()
            onResult(json)
            _snackbarMessage.value = "School database state exported successfully."
        }
    }

    // --- REAL-TIME NOTIFICATION DISPATCH ENGINE ---

    fun postNotification(
        title: String,
        message: String,
        type: NotificationType,
        priority: NotificationPriority = NotificationPriority.NORMAL,
        targetClass: String = "ALL",
        relatedId: String? = null,
        relatedSubject: String? = null,
        actionPayload: String? = null
    ) {
        val prefs = notificationPreferences.value

        val isAllowed = when (type) {
            NotificationType.NEW_ASSIGNMENT -> prefs.assignmentsEnabled
            NotificationType.UPCOMING_DEADLINE -> prefs.deadlinesEnabled
            NotificationType.GRADE_UPDATE -> prefs.gradesEnabled
            NotificationType.COLLEGE_ANNOUNCEMENT -> prefs.announcementsEnabled
        }

        val notification = SchoolNotification(
            id = UUID.randomUUID().toString(),
            title = title,
            message = message,
            type = type,
            priority = priority,
            timestamp = System.currentTimeMillis(),
            isRead = false,
            targetClass = targetClass,
            relatedId = relatedId,
            relatedSubject = relatedSubject,
            actionPayload = actionPayload
        )

        viewModelScope.launch {
            repository.insertNotification(notification)

            if (isAllowed) {
                triggerRealtimeBanner(notification)
                if (prefs.systemTrayAlerts && context != null) {
                    RealtimeNotificationManager.dispatchSystemNotification(
                        context = context,
                        notification = notification,
                        enableSoundAndVibrate = prefs.soundAndVibrate
                    )
                }
            }
        }
    }

    private fun triggerRealtimeBanner(notification: SchoolNotification) {
        alertDismissJob?.cancel()
        _realtimeAlert.value = notification
        alertDismissJob = viewModelScope.launch {
            delay(6500)
            if (_realtimeAlert.value?.id == notification.id) {
                _realtimeAlert.value = null
            }
        }
    }

    fun dismissRealtimeAlert() {
        alertDismissJob?.cancel()
        _realtimeAlert.value = null
    }

    fun checkUpcomingDeadlines(leadHours: Int = 24, manualTrigger: Boolean = false) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val leadMillis = leadHours.toLong() * 3600 * 1000
            val pendingList = allAssignments.value.filter { !it.isCompleted }
            var newAlertsCount = 0

            pendingList.forEach { asg ->
                val timeDiff = asg.dueDateTimestamp - now
                if (timeDiff in 0..leadMillis) {
                    val hoursLeft = (timeDiff / (3600 * 1000)).coerceAtLeast(0)
                    val minutesLeft = ((timeDiff % (3600 * 1000)) / (60 * 1000)).coerceAtLeast(0)
                    val timeStr = if (hoursLeft > 0) "${hoursLeft}h ${minutesLeft}m" else "${minutesLeft}m"

                    val alreadyNotified = allNotifications.value.any {
                        it.type == NotificationType.UPCOMING_DEADLINE &&
                                it.relatedId == asg.id &&
                                (now - it.timestamp) < (6L * 3600 * 1000)
                    }

                    if (!alreadyNotified) {
                        newAlertsCount++
                        postNotification(
                            title = "Deadline in $timeStr: ${asg.title}",
                            message = "Assignment in ${asg.subject} (${asg.courseCode}) is due at ${asg.dueDateFormatted}. Don't miss the deadline!",
                            type = NotificationType.UPCOMING_DEADLINE,
                            priority = if (hoursLeft <= 2) NotificationPriority.URGENT else NotificationPriority.HIGH,
                            targetClass = asg.classLevel,
                            relatedId = asg.id,
                            relatedSubject = asg.subject
                        )
                    }
                }
            }

            if (manualTrigger) {
                _snackbarMessage.value = if (newAlertsCount > 0) {
                    "Found $newAlertsCount approaching deadline(s)! Alerts dispatched."
                } else {
                    "All pending deadlines are up-to-date and monitored."
                }
            }
        }
    }

    fun broadcastAnnouncement(
        title: String,
        content: String,
        category: String,
        targetAudience: String,
        author: String,
        isUrgent: Boolean,
        isPinned: Boolean
    ) {
        viewModelScope.launch {
            val announcement = CollegeAnnouncement(
                id = UUID.randomUUID().toString(),
                title = title,
                content = content,
                category = category,
                targetAudience = targetAudience,
                author = if (author.isBlank()) "Principal Office" else author,
                timestamp = System.currentTimeMillis(),
                isPinned = isPinned,
                isUrgent = isUrgent
            )
            repository.insertAnnouncement(announcement)
            showBroadcastAnnouncementDialog.value = false

            postNotification(
                title = "College Notice: $title",
                message = content,
                type = NotificationType.COLLEGE_ANNOUNCEMENT,
                priority = if (isUrgent) NotificationPriority.URGENT else NotificationPriority.NORMAL,
                targetClass = targetAudience,
                relatedId = announcement.id
            )

            _snackbarMessage.value = "Announcement broadcasted to students in real-time!"
        }
    }

    fun toggleAnnouncementPublish(announcement: CollegeAnnouncement) {
        viewModelScope.launch {
            val newStatus = !announcement.isPublished
            repository.toggleAnnouncementPublish(announcement.id, newStatus)
            _snackbarMessage.value = if (newStatus) "Published notice '${announcement.title}'" else "Unpublished notice"
        }
    }

    fun deleteAnnouncement(announcement: CollegeAnnouncement) {
        viewModelScope.launch {
            repository.deleteAnnouncement(announcement)
            _snackbarMessage.value = "Deleted notice '${announcement.title}'"
        }
    }

    fun simulateAlert(preset: String) {
        when (preset) {
            "DEADLINE_URGENT" -> {
                postNotification(
                    title = "Due Today: C++ OOP & Matrix Classes Lab Project",
                    message = "Urgent reminder: Submit your operator overloading and matrix solutions by 11:59 PM tonight.",
                    type = NotificationType.UPCOMING_DEADLINE,
                    priority = NotificationPriority.URGENT,
                    targetClass = "12th",
                    relatedId = "asg-12-001",
                    relatedSubject = "Computer Science"
                )
            }
            "GRADE_EXAM" -> {
                postNotification(
                    title = "New Grade Released: Physics Practical (48/50)",
                    message = "Sir Muhammad Ali entered Annual Practical exam marks for Muhammad Khan. Result: Grade A+ (96.0%). Exemplary circuit wiring.",
                    type = NotificationType.GRADE_UPDATE,
                    priority = NotificationPriority.HIGH,
                    targetClass = "12th",
                    relatedId = "res-12-003",
                    relatedSubject = "Physics"
                )
            }
            "NEW_HOMEWORK" -> {
                postNotification(
                    title = "New Assignment: Karakoram Alpine Flora Survey",
                    message = "Sir Zakir Hussain assigned a biology report on alpine vegetation and altitude zones. Due in 4 days.",
                    type = NotificationType.NEW_ASSIGNMENT,
                    priority = NotificationPriority.NORMAL,
                    targetClass = "12th",
                    relatedId = "asg-sim-01",
                    relatedSubject = "Biology"
                )
            }
            "CAMPUS_NOTICE" -> {
                postNotification(
                    title = "College Notice: Snowfall Weather Advisory",
                    message = "Due to expected snowfall in upper Kharmang, morning assembly will be held indoors and lab hours are adjusted.",
                    type = NotificationType.COLLEGE_ANNOUNCEMENT,
                    priority = NotificationPriority.HIGH,
                    targetClass = "ALL",
                    relatedId = "ann-sim-01"
                )
            }
        }
        _snackbarMessage.value = "Simulated real-time alert dispatched!"
    }

    fun markNotificationAsRead(id: String) {
        viewModelScope.launch {
            repository.markNotificationAsRead(id)
        }
    }

    fun markAllNotificationsAsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsAsRead()
            _snackbarMessage.value = "All notifications marked as read."
        }
    }

    fun deleteNotification(notification: SchoolNotification) {
        viewModelScope.launch {
            repository.deleteNotification(notification)
        }
    }

    fun clearReadNotifications() {
        viewModelScope.launch {
            repository.clearReadNotifications()
            _snackbarMessage.value = "Cleared all read notifications."
        }
    }

    fun clearAllNotifications() {
        viewModelScope.launch {
            repository.clearAllNotifications()
            _snackbarMessage.value = "All notifications cleared."
        }
    }

    fun updatePreferences(newPrefs: NotificationPreferences) {
        notificationPreferences.value = newPrefs
        _snackbarMessage.value = "Notification preferences updated."
    }

    // --- ASSIGNMENT ACTIONS ---

    fun toggleAssignmentCompletion(assignment: StudentAssignment) {
        viewModelScope.launch {
            val newStatus = !assignment.isCompleted
            repository.toggleAssignmentCompletion(assignment.id, newStatus)
            _snackbarMessage.value = if (newStatus) {
                "Marked '${assignment.title}' as Completed!"
            } else {
                "Reopened '${assignment.title}' as Pending"
            }
        }
    }

    fun toggleAssignmentPublish(assignment: StudentAssignment) {
        viewModelScope.launch {
            val newStatus = !assignment.isPublished
            repository.toggleAssignmentPublish(assignment.id, newStatus)
            _snackbarMessage.value = if (newStatus) "Published homework for ${assignment.classLevel}" else "Unpublished homework"
        }
    }

    fun addAssignment(
        title: String,
        subject: String,
        courseCode: String,
        classLevel: String,
        teacherName: String,
        description: String,
        dueDateTimestamp: Long,
        dueDateFormatted: String,
        totalMarks: Int,
        priority: String
    ) {
        viewModelScope.launch {
            val newAssignment = StudentAssignment(
                title = title,
                subject = subject,
                courseCode = courseCode,
                classLevel = classLevel,
                teacherName = teacherName,
                description = description,
                dueDateTimestamp = dueDateTimestamp,
                dueDateFormatted = dueDateFormatted,
                totalMarks = totalMarks,
                isCompleted = false,
                priority = priority,
                isPublished = true
            )
            repository.insertAssignment(newAssignment)
            showAddAssignmentDialog.value = false

            postNotification(
                title = "New Assignment: $title",
                message = "Sir/Madam $teacherName posted a new $subject assignment due on $dueDateFormatted ($priority Priority).",
                type = NotificationType.NEW_ASSIGNMENT,
                priority = if (priority == "URGENT") NotificationPriority.HIGH else NotificationPriority.NORMAL,
                targetClass = classLevel,
                relatedId = newAssignment.id,
                relatedSubject = subject
            )

            _snackbarMessage.value = "New assignment '${title}' saved & notification dispatched!"
        }
    }

    fun deleteAssignment(assignment: StudentAssignment) {
        viewModelScope.launch {
            repository.deleteAssignment(assignment)
            _snackbarMessage.value = "Assignment '${assignment.title}' deleted."
        }
    }

    fun markAttendance(record: AttendanceRecord, isPresent: Boolean) {
        viewModelScope.launch {
            val updated = record.copy(
                attendedCount = if (isPresent) record.attendedCount + 1 else record.attendedCount,
                totalCount = record.totalCount + 1
            )
            repository.updateAttendance(updated)
            _snackbarMessage.value = "Attendance updated for ${record.subjectName}: ${if (isPresent) "Present" else "Absent"}."
        }
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    class Factory(
        private val repository: SchoolRepository,
        private val context: Context? = null
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SchoolViewModel::class.java)) {
                return SchoolViewModel(repository, context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
