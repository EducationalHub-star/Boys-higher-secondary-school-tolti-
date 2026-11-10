package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import com.example.data.model.CollegeAnnouncement
import com.example.data.model.NotificationPriority
import com.example.data.model.NotificationType
import com.example.data.model.SchoolNotification
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AttendanceRecord
import com.example.data.model.StudentAssignment
import com.example.data.model.StudentResult
import com.example.ui.viewmodel.SchoolNavTab
import com.example.ui.viewmodel.SchoolViewModel

@Composable
fun StudentDashboardScreen(
    viewModel: SchoolViewModel,
    contentPadding: PaddingValues
) {
    val student by viewModel.studentProfile.collectAsState()
    val assignments by viewModel.allAssignments.collectAsState()
    val results by viewModel.allResults.collectAsState()
    val attendance by viewModel.attendanceRecords.collectAsState()
    val selectedClass by viewModel.selectedClassLevel.collectAsState()
    val notifications by viewModel.allNotifications.collectAsState()
    val unreadNotifCount by viewModel.unreadNotificationCount.collectAsState()
    val announcements by viewModel.allAnnouncements.collectAsState()

    val pendingAssignments = assignments.filter { !it.isCompleted }
    val studentResults = results.filter { it.studentName.contains("Muhammad Khan", ignoreCase = true) }
    val overallPercentage = if (studentResults.isNotEmpty()) studentResults.map { it.percentage }.average() else 90.0

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("dashboard_screen"),
        contentPadding = PaddingValues(
            top = contentPadding.calculateTopPadding() + 8.dp,
            bottom = contentPadding.calculateBottomPadding() + 80.dp,
            start = 16.dp,
            end = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. School Crest Header with official logo
        item {
            SchoolEmblemCard()
        }

        // 2. Class Level Switcher (8th to 12th)
        item {
            Column {
                Text(
                    text = "Select Class Section:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(viewModel.classOptions) { classOpt ->
                        val isSelected = selectedClass == classOpt
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.selectedClassLevel.value = classOpt },
                            label = {
                                Text(
                                    text = if (classOpt == "ALL") "All Classes" else "Class $classOpt",
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 12.sp
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF0F5132),
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }
            }
        }

        // 3. Student Identity Card (Muhammad Khan)
        item {
            StudentProfileCard(
                name = student.name,
                rollNo = student.rollNumber,
                stream = student.stream,
                currentClass = student.currentClass
            )
        }

        // 4. Quick Actions
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DashboardActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Track Homework",
                    subtitle = "${pendingAssignments.size} Pending Due",
                    icon = Icons.Default.Assignment,
                    color = Color(0xFFD97706),
                    onClick = { viewModel.selectedTab.value = SchoolNavTab.ASSIGNMENTS }
                )
                DashboardActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Admin Marks",
                    subtitle = "${results.size} Stored Results",
                    icon = Icons.Default.AdminPanelSettings,
                    color = Color(0xFF0F5132),
                    onClick = { viewModel.selectedTab.value = SchoolNavTab.ADMIN_RESULTS }
                )
            }
        }

        // 4b. Real-Time Academic Alerts Banner Card
        item {
            DashboardAcademicAlertsCard(
                unreadCount = unreadNotifCount,
                allNotifications = notifications,
                pendingAssignments = pendingAssignments,
                onOpenAlertsHub = { viewModel.showNotificationCenter.value = true },
                onScanDeadlines = { viewModel.checkUpcomingDeadlines(leadHours = 24, manualTrigger = true) }
            )
        }

        // 4c. Official College Announcements & Circulars
        item {
            DashboardCollegeAnnouncementsSection(
                announcements = announcements,
                onAnnouncementClick = { ann -> viewModel.selectedAnnouncementForDetails.value = ann },
                onBroadcastClick = { viewModel.showBroadcastAnnouncementDialog.value = true }
            )
        }

        // 5. Academic Performance Marksheet Snapshot
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Muhammad Khan's Marksheet",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Avg: ${String.format("%.1f%%", overallPercentage)}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0F5132)
                )
            }
        }

        if (studentResults.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
                        Text("No results recorded yet. Use 'Admin Marks' tab to input grades.")
                    }
                }
            }
        } else {
            items(studentResults.take(3)) { res ->
                DashboardResultRow(result = res)
            }
        }

        // 6. Active Assignments Preview
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Upcoming Homework Deadlines",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "View All",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF1E3A8A),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { viewModel.selectedTab.value = SchoolNavTab.ASSIGNMENTS }
                )
            }
        }

        items(pendingAssignments.take(3)) { asg ->
            DashboardAssignmentRow(
                assignment = asg,
                onToggle = { viewModel.toggleAssignmentCompletion(asg) }
            )
        }

        // 7. Course Attendance Health
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Course Attendance Standing",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Class 12th",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(attendance) { att ->
            DashboardAttendanceRow(
                record = att,
                onMark = { isPresent -> viewModel.markAttendance(att, isPresent) }
            )
        }
    }
}

@Composable
private fun SchoolEmblemCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF0F5132),
                            Color(0xFF198754)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    modifier = Modifier.size(60.dp),
                    shadowElevation = 4.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.school_logo),
                            contentDescription = "Govt. Higher Secondary School Tolti Logo",
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Govt. HSS Tolti",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "Kharmang • Education for all",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFFEF08A)
                    )
                    Text(
                        text = "Official Academic & Examination Portal",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }
    }
}

@Composable
private fun StudentProfileCard(
    name: String,
    rollNo: String,
    stream: String,
    currentClass: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.student_avatar),
                contentDescription = "Muhammad Khan Photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color(0xFF1E3A8A), CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFF10B981).copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "Enrolled",
                            color = Color(0xFF10B981),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = "Roll No: $rollNo • Class $currentClass",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = stream,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1E3A8A)
                )
            }
        }
    }
}

@Composable
private fun DashboardActionCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = color, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun DashboardResultRow(result: StudentResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "${result.subjectCode} • ${result.subjectName}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${result.examTerm} • ${result.obtainedMarks}/${result.totalMarks} Marks",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (result.status == "PASS") Color(0xFF0F5132) else Color(0xFFEF4444)
            ) {
                Text(
                    text = "${result.grade} (${String.format("%.0f%%", result.percentage)})",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun DashboardAssignmentRow(
    assignment: StudentAssignment,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onToggle, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = if (assignment.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = if (assignment.isCompleted) Color(0xFF10B981) else Color.Gray,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = assignment.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${assignment.courseCode} • Due ${assignment.dueDateFormatted}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFFD97706).copy(alpha = 0.15f)
            ) {
                Text(
                    text = "Due",
                    color = Color(0xFFD97706),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun DashboardAttendanceRow(
    record: AttendanceRecord,
    onMark: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = record.subjectName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Text(text = "${record.attendedCount}/${record.totalCount} classes attended", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text(
                    text = String.format("%.0f%%", record.percentage),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (record.percentage >= 75f) Color(0xFF10B981) else Color(0xFFEF4444)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { (record.percentage / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (record.percentage >= 75f) Color(0xFF10B981) else Color(0xFFEF4444),
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = { onMark(false) },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text("Absent", fontSize = 11.sp, color = Color(0xFFEF4444))
                }
                Spacer(modifier = Modifier.width(6.dp))
                Button(
                    onClick = { onMark(true) },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F5132))
                ) {
                    Text("Present", fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
private fun DashboardAcademicAlertsCard(
    unreadCount: Int,
    allNotifications: List<SchoolNotification>,
    pendingAssignments: List<StudentAssignment>,
    onOpenAlertsHub: () -> Unit,
    onScanDeadlines: () -> Unit
) {
    val urgentCount = allNotifications.count { it.priority == NotificationPriority.URGENT && !it.isRead }
    val latestNotif = allNotifications.firstOrNull()
    val dueTodayCount = pendingAssignments.count {
        val diff = it.dueDateTimestamp - System.currentTimeMillis()
        diff in 0..(24L * 3600 * 1000)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("dashboard_academic_alerts_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF0F5132).copy(alpha = 0.12f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = Color(0xFF0F5132),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Real-Time Academic Alerts",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827)
                        )
                        Text(
                            text = if (dueTodayCount > 0) "$dueTodayCount assignment(s) due within 24h" else "All course deadlines up to date",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (dueTodayCount > 0) Color(0xFFDC2626) else Color(0xFF6B7280),
                            fontWeight = if (dueTodayCount > 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }

                if (unreadCount > 0) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (urgentCount > 0) Color(0xFFDC2626) else Color(0xFF0F5132)
                    ) {
                        Text(
                            text = "$unreadCount New",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            if (latestNotif != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF9FAFB),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenAlertsHub() }
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (!latestNotif.isRead) Color(0xFFDC2626) else Color(0xFF9CA3AF))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = latestNotif.title,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1F2937),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = latestNotif.message,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF6B7280),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onOpenAlertsHub,
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .testTag("dashboard_view_all_alerts_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F5132)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text("View Notification Center", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onScanDeadlines,
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .testTag("dashboard_check_deadlines_btn"),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.HourglassTop,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Color(0xFFD97706)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Scan Deadlines", fontSize = 11.sp, color = Color(0xFFD97706))
                }
            }
        }
    }
}

@Composable
private fun DashboardCollegeAnnouncementsSection(
    announcements: List<CollegeAnnouncement>,
    onAnnouncementClick: (CollegeAnnouncement) -> Unit,
    onBroadcastClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Campaign,
                    contentDescription = null,
                    tint = Color(0xFF1E3A8A),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Official College Circulars",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
            }

            OutlinedButton(
                onClick = onBroadcastClick,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .height(32.dp)
                    .testTag("dashboard_broadcast_notice_btn")
            ) {
                Text("+ Broadcast Notice", fontSize = 11.sp, color = Color(0xFF1E3A8A), fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (announcements.isEmpty()) {
            Text(
                text = "No circulars currently published.",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF6B7280)
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                announcements.take(3).forEach { ann ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAnnouncementClick(ann) }
                            .testTag("announcement_item_${ann.id}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        border = if (ann.isUrgent) {
                            androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDC2626).copy(alpha = 0.5f))
                        } else null
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = Color(0xFF1E3A8A).copy(alpha = 0.12f)
                                    ) {
                                        Text(
                                            text = ann.category.uppercase(),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF1E3A8A),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }

                                    if (ann.isUrgent) {
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = Color(0xFFDC2626)
                                        ) {
                                            Text(
                                                text = "URGENT",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = Color.White,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }

                                if (ann.isPinned) {
                                    Icon(
                                        imageVector = Icons.Default.PushPin,
                                        contentDescription = "Pinned",
                                        tint = Color(0xFFD97706),
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = ann.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111827),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = ann.content,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF4B5563),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(top = 2.dp)
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Issued by ${ann.author}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF9CA3AF),
                                    fontSize = 10.sp
                                )
                                Text(
                                    text = "Read Official Notice →",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF1E3A8A)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

