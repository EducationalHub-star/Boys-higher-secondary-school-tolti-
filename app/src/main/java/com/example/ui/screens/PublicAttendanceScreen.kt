package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyAttendance
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.ElegantGold
import com.example.ui.theme.RoyalBlue
import com.example.ui.viewmodel.SchoolViewModel
import com.example.util.UrduLocalization
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicAttendanceScreen(
    viewModel: SchoolViewModel,
    contentPadding: PaddingValues = PaddingValues()
) {
    val attendanceRecords by viewModel.attendanceRecords.collectAsState()
    val dailyAttendance by viewModel.allDailyAttendance.collectAsState()
    val allStudents by viewModel.allStudents.collectAsState()
    val isAdminLoggedIn by viewModel.isAdminLoggedIn.collectAsState()
    val lang by viewModel.currentLanguage.collectAsState()
    val isUrdu = lang == UrduLocalization.Language.URDU

    var selectedClass by remember { mutableStateOf("12th") }
    var searchQuery by remember { mutableStateOf("") }
    var statusFilter by remember { mutableStateOf("ALL") } // ALL, PRESENT, ABSENT, LATE

    val todayFormatted = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) }

    // Collect available unique dates recorded
    val availableDates = remember(dailyAttendance) {
        val dates = dailyAttendance.map { it.date }.distinct().toMutableList()
        if (!dates.contains(todayFormatted)) dates.add(0, todayFormatted)
        dates.sortedDescending()
    }
    var selectedDate by remember(availableDates) {
        mutableStateOf(if (availableDates.contains(todayFormatted)) todayFormatted else (availableDates.firstOrNull() ?: todayFormatted))
    }

    // Filter students by selected class
    val classStudents = allStudents.filter { it.classLevel == selectedClass }

    // Class daily logs for chosen date
    val classDailyLogs = dailyAttendance.filter { it.classLevel == selectedClass && it.date == selectedDate }

    // Counts for stats summary
    val presentCount = classDailyLogs.count { it.status.equals("PRESENT", ignoreCase = true) }
    val absentCount = classDailyLogs.count { it.status.equals("ABSENT", ignoreCase = true) }
    val lateCount = classDailyLogs.count { it.status.equals("LATE", ignoreCase = true) }
    val leaveCount = classDailyLogs.count { it.status.equals("LEAVE", ignoreCase = true) }

    val totalLogged = presentCount + absentCount + lateCount + leaveCount
    val presentRatio = if (totalLogged > 0) (presentCount.toFloat() / totalLogged.toFloat()) * 100f else 0f

    // Filter students according to search and status
    val displayedStudents = classStudents.filter { std ->
        val record = classDailyLogs.find { it.studentRollNumber == std.rollNumber }
        val status = record?.status ?: "PENDING"

        val matchesSearch = searchQuery.isBlank() ||
                std.fullName.contains(searchQuery, ignoreCase = true) ||
                std.rollNumber.contains(searchQuery, ignoreCase = true)

        val matchesStatus = when (statusFilter) {
            "PRESENT" -> status.equals("PRESENT", ignoreCase = true)
            "ABSENT" -> status.equals("ABSENT", ignoreCase = true)
            "LATE" -> status.equals("LATE", ignoreCase = true)
            else -> true
        }

        matchesSearch && matchesStatus
    }

    val classSubjectRecords = attendanceRecords.filter { it.classLevel == selectedClass }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .background(Color(0xFFF8FAFC))
            .testTag("public_attendance_screen")
    ) {
        // Top Header & Controls Card
        Surface(
            color = Color.White,
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Title and Date Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isUrdu) "طالب علموں کا حاضری ریکارڈ" else "Student Attendance Tracking",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = DeepNavy
                        )
                        Text(
                            text = if (isUrdu) "روزانہ حاضری و تعلیمی رپورٹس" else "Live Presence Logs & Academic Reports",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF64748B)
                        )
                    }

                    // Date Indicator / Selector
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = RoyalBlue.copy(alpha = 0.08f),
                        border = BorderStroke(1.dp, RoyalBlue.copy(alpha = 0.2f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Date",
                                tint = RoyalBlue,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = selectedDate,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = RoyalBlue
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Search by Name or Roll Number
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = if (isUrdu) "رول نمبر یا نام سے تلاش کریں..." else "Search by student name or roll number...",
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = RoyalBlue, modifier = Modifier.size(18.dp))
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", modifier = Modifier.size(16.dp))
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("attendance_search_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Class Selection Row (5th to 12th)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(viewModel.gradeLevelOptions) { cls ->
                        val isSelected = selectedClass == cls
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) DeepNavy else Color(0xFFF1F5F9),
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) DeepNavy else Color(0xFFE2E8F0)
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { selectedClass = cls }
                        ) {
                            Text(
                                text = "Class $cls",
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Color(0xFF334155),
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            contentPadding = PaddingValues(top = 14.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Modern Color-Coded Stats Overview Cards
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Present Card (Emerald Green)
                        AttendanceStatCard(
                            label = if (isUrdu) "حاضر" else "Present",
                            count = presentCount,
                            color = Color(0xFF15803D),
                            bgColor = Color(0xFFDCFCE7),
                            borderColor = Color(0xFF86EFAC),
                            icon = Icons.Default.CheckCircle,
                            isSelected = statusFilter == "PRESENT",
                            onClick = { statusFilter = if (statusFilter == "PRESENT") "ALL" else "PRESENT" },
                            modifier = Modifier.weight(1f)
                        )

                        // Absent Card (Coral Red)
                        AttendanceStatCard(
                            label = if (isUrdu) "غیر حاضر" else "Absent",
                            count = absentCount,
                            color = Color(0xFFB91C1C),
                            bgColor = Color(0xFFFEE2E2),
                            borderColor = Color(0xFFFCA5A5),
                            icon = Icons.Default.Cancel,
                            isSelected = statusFilter == "ABSENT",
                            onClick = { statusFilter = if (statusFilter == "ABSENT") "ALL" else "ABSENT" },
                            modifier = Modifier.weight(1f)
                        )

                        // Late Card (Amber Orange)
                        AttendanceStatCard(
                            label = if (isUrdu) "تاخیر" else "Late",
                            count = lateCount,
                            color = Color(0xFFB45309),
                            bgColor = Color(0xFFFEF3C7),
                            borderColor = Color(0xFFFCD34D),
                            icon = Icons.Default.Schedule,
                            isSelected = statusFilter == "LATE",
                            onClick = { statusFilter = if (statusFilter == "LATE") "ALL" else "LATE" },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Attendance Ratio Banner
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.TrendingUp,
                                        contentDescription = null,
                                        tint = RoyalBlue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isUrdu) "کلاس $selectedClass کی یومیہ حاضری شرح" else "Class $selectedClass Attendance Rate",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepNavy
                                    )
                                }
                                Text(
                                    text = "${presentRatio.toInt()}%",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 15.sp,
                                    color = if (presentRatio >= 75f) Color(0xFF15803D) else Color(0xFFB91C1C)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { if (totalLogged > 0) presentRatio / 100f else 0f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(7.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = if (presentRatio >= 75f) Color(0xFF16A34A) else Color(0xFFDC2626),
                                trackColor = Color(0xFFE2E8F0)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (isUrdu) "کل طالب علم: ${classStudents.size}" else "Enrolled: ${classStudents.size} Students",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF64748B)
                                )
                                Text(
                                    text = if (isUrdu) "ریکارڈ شدہ: $totalLogged" else "Logged: $totalLogged on $selectedDate",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                    }
                }
            }

            // Student Roster Section Header with Active Filter Indicator
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isUrdu) "کلاس $selectedClass کا یومیہ حاضری رجسٹر" else "Class $selectedClass Student Attendance Roster",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )

                    if (statusFilter != "ALL") {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFE2E8F0),
                            modifier = Modifier.clickable { statusFilter = "ALL" }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Filtered: $statusFilter",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.Clear, contentDescription = "Clear", modifier = Modifier.size(12.dp))
                            }
                        }
                    }
                }
            }

            // Student Roster Rows with Color-Coded Badges
            if (displayedStudents.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.EventAvailable,
                                    contentDescription = null,
                                    tint = Color(0xFF94A3B8),
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = if (isUrdu) "اس معیار سے مطابقت رکھنے والا کوئی ریکارڈ نہیں ملا۔" else "No student records found matching this filter.",
                                    color = Color(0xFF64748B),
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            } else {
                items(displayedStudents, key = { it.id }) { std ->
                    val dailyRecord = classDailyLogs.find { it.studentRollNumber == std.rollNumber }
                    val currentStatus = dailyRecord?.status ?: "PENDING"

                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("student_attendance_row_${std.rollNumber}")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Student Initials Avatar
                                Surface(
                                    shape = CircleShape,
                                    color = RoyalBlue.copy(alpha = 0.12f),
                                    modifier = Modifier.size(42.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = std.fullName.take(1).uppercase(),
                                            fontWeight = FontWeight.ExtraBold,
                                            color = RoyalBlue,
                                            fontSize = 16.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                // Student Details
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = std.fullName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = DeepNavy
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = Color(0xFFF1F5F9)
                                        ) {
                                            Text(
                                                text = std.rollNumber,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF475569),
                                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Sec ${std.section} • Class ${std.classLevel}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color(0xFF64748B)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                // Prominent Color-Coded Status Badge for Present, Absent, and Late
                                AttendanceStatusBadge(
                                    status = currentStatus,
                                    isUrdu = isUrdu
                                )
                            }

                            // If Admin is logged in, provide quick 1-tap marking toggles
                            if (isAdminLoggedIn) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (isUrdu) "حاضری درج کریں:" else "Mark Status:",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFF64748B),
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    QuickStatusButton(
                                        label = "P",
                                        activeColor = Color(0xFF15803D),
                                        isActive = currentStatus.equals("PRESENT", ignoreCase = true),
                                        onClick = {
                                            viewModel.markStudentDailyAttendance(
                                                std.rollNumber,
                                                std.fullName,
                                                std.classLevel,
                                                selectedDate,
                                                "PRESENT"
                                            )
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    QuickStatusButton(
                                        label = "A",
                                        activeColor = Color(0xFFB91C1C),
                                        isActive = currentStatus.equals("ABSENT", ignoreCase = true),
                                        onClick = {
                                            viewModel.markStudentDailyAttendance(
                                                std.rollNumber,
                                                std.fullName,
                                                std.classLevel,
                                                selectedDate,
                                                "ABSENT"
                                            )
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    QuickStatusButton(
                                        label = "L",
                                        activeColor = Color(0xFFB45309),
                                        isActive = currentStatus.equals("LATE", ignoreCase = true),
                                        onClick = {
                                            viewModel.markStudentDailyAttendance(
                                                std.rollNumber,
                                                std.fullName,
                                                std.classLevel,
                                                selectedDate,
                                                "LATE"
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Subject-Wise Overall Attendance Standings
            if (classSubjectRecords.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isUrdu) "کلاس $selectedClass کے مضامین کا اوسط حاضری تناسب" else "Subject-Wise Academic Attendance Ratio (Class $selectedClass)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                }

                items(classSubjectRecords, key = { it.id }) { record ->
                    val percentage = record.percentage
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = record.subjectName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = DeepNavy
                                    )
                                    Text(
                                        text = "Instructor: ${record.teacherName} • Room ${record.roomNumber}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFF64748B)
                                    )
                                }
                                Text(
                                    text = "${percentage.toInt()}%",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 15.sp,
                                    color = if (percentage >= 75f) Color(0xFF15803D) else Color(0xFFB91C1C)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { percentage / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = if (percentage >= 75f) Color(0xFF15803D) else Color(0xFFB91C1C),
                                trackColor = Color(0xFFE2E8F0)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Attended: ${record.attendedCount} / ${record.totalCount} lectures",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF64748B)
                                )
                                Text(
                                    text = if (percentage >= 75f) "Eligible for Final Board Exams" else "Attendance Warning",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (percentage >= 75f) Color(0xFF15803D) else Color(0xFFB91C1C)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Dedicated Color-Coded Status Badge for 'Present', 'Absent', and 'Late'.
 */
@Composable
fun AttendanceStatusBadge(
    status: String,
    isUrdu: Boolean = false,
    modifier: Modifier = Modifier
) {
    val normStatus = status.uppercase()

    val (bgColor, borderColor, contentColor, icon, text) = when (normStatus) {
        "PRESENT" -> StatusBadgeConfig(
            bgColor = Color(0xFFDCFCE7),
            borderColor = Color(0xFF86EFAC),
            contentColor = Color(0xFF15803D),
            icon = Icons.Default.CheckCircle,
            text = if (isUrdu) "حاضر" else "PRESENT"
        )
        "ABSENT" -> StatusBadgeConfig(
            bgColor = Color(0xFFFEE2E2),
            borderColor = Color(0xFFFCA5A5),
            contentColor = Color(0xFFB91C1C),
            icon = Icons.Default.Cancel,
            text = if (isUrdu) "غیر حاضر" else "ABSENT"
        )
        "LATE" -> StatusBadgeConfig(
            bgColor = Color(0xFFFEF3C7),
            borderColor = Color(0xFFFCD34D),
            contentColor = Color(0xFFB45309),
            icon = Icons.Default.Schedule,
            text = if (isUrdu) "تاخیر" else "LATE"
        )
        "LEAVE" -> StatusBadgeConfig(
            bgColor = Color(0xFFF3E8FF),
            borderColor = Color(0xFFD8B4FE),
            contentColor = Color(0xFF7E22CE),
            icon = Icons.Default.HourglassEmpty,
            text = if (isUrdu) "رخصت" else "LEAVE"
        )
        else -> StatusBadgeConfig(
            bgColor = Color(0xFFF1F5F9),
            borderColor = Color(0xFFCBD5E1),
            contentColor = Color(0xFF64748B),
            icon = Icons.Default.HourglassEmpty,
            text = if (isUrdu) "درج ہو رہا ہے" else "PENDING"
        )
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = bgColor,
        border = BorderStroke(1.2.dp, borderColor),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = contentColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = text,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = contentColor,
                letterSpacing = 0.4.sp
            )
        }
    }
}

private data class StatusBadgeConfig(
    val bgColor: Color,
    val borderColor: Color,
    val contentColor: Color,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val text: String
)

@Composable
private fun AttendanceStatCard(
    label: String,
    count: Int,
    color: Color,
    bgColor: Color,
    borderColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) bgColor else Color.White,
        border = BorderStroke(if (isSelected) 1.8.dp else 1.dp, if (isSelected) color else borderColor),
        shadowElevation = if (isSelected) 3.dp else 1.dp,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = count.toString(),
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                color = color
            )
        }
    }
}

@Composable
private fun QuickStatusButton(
    label: String,
    activeColor: Color,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = if (isActive) activeColor else Color(0xFFF1F5F9),
        border = BorderStroke(1.dp, if (isActive) activeColor else Color(0xFFCBD5E1)),
        modifier = Modifier
            .size(30.dp)
            .clip(RoundedCornerShape(6.dp))
            .clickable(onClick = onClick)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (isActive) Color.White else Color(0xFF475569)
            )
        }
    }
}
