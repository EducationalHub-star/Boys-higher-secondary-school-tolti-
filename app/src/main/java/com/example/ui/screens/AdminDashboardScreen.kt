package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AdminActivityLog
import com.example.data.model.FeeRecord
import com.example.data.model.GalleryPhoto
import com.example.data.model.StudentProfileRecord
import com.example.data.model.TeacherProfileRecord
import com.example.data.model.TimetablePeriod
import com.example.ui.components.SchoolEmblem
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.ElegantGold
import com.example.ui.theme.RoyalBlue
import com.example.ui.viewmodel.SchoolViewModel
import com.example.util.UrduLocalization
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class AdminSubModule(val title: String, val icon: ImageVector) {
    OVERVIEW("Overview", Icons.Default.FactCheck),
    STUDENTS("Students", Icons.Default.People),
    TEACHERS("Teachers", Icons.Default.School),
    RESULTS("Results", Icons.Default.Grade),
    FEES("Fees", Icons.Default.Payments),
    ATTENDANCE("Attendance", Icons.Default.EventAvailable),
    TIMETABLE("Timetable", Icons.Default.Schedule),
    GALLERY("Gallery", Icons.Default.Image),
    LOGS("Audit Logs", Icons.Default.History),
    SETTINGS("Settings", Icons.Default.Settings)
}

@Composable
fun AdminDashboardScreen(
    viewModel: SchoolViewModel,
    contentPadding: PaddingValues = PaddingValues(),
    onViewFeeReceipt: (FeeRecord) -> Unit = {}
) {
    val isLoggedIn by viewModel.isAdminLoggedIn.collectAsState()
    val adminName by viewModel.adminUsername.collectAsState()
    val lang by viewModel.currentLanguage.collectAsState()
    val isUrdu = lang == UrduLocalization.Language.URDU

    if (!isLoggedIn) {
        // Locked Portal Screen if not signed in
        AdminLockedPortal(
            onOpenLogin = { viewModel.showAdminLoginDialog.value = true },
            onOpenRecovery = { viewModel.showPasswordRecoveryDialog.value = true },
            contentPadding = contentPadding
        )
        return
    }

    var selectedModule by remember { mutableStateOf(AdminSubModule.OVERVIEW) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .background(Color(0xFFF8FAFC))
            .testTag("admin_dashboard_screen")
    ) {
        // Admin Top Banner
        Surface(
            color = DeepNavy,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.15f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = ElegantGold, modifier = Modifier.size(20.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Central Administration Portal",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = "Logged in as @${adminName ?: "admin"} • Secure Session",
                            color = Color.White.copy(alpha = 0.75f),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.showChangePasswordDialog.value = true },
                        modifier = Modifier.testTag("admin_change_pwd_icon")
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = "Change Password", tint = Color.White)
                    }
                    IconButton(
                        onClick = { viewModel.logoutAdmin() },
                        modifier = Modifier.testTag("admin_logout_btn")
                    ) {
                        Icon(Icons.Default.Logout, contentDescription = "Log Out", tint = Color(0xFFEF4444))
                    }
                }
            }
        }

        // Submodules Navigation Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedModule.ordinal,
            containerColor = Color.White,
            contentColor = RoyalBlue,
            edgePadding = 8.dp
        ) {
            AdminSubModule.values().forEach { module ->
                Tab(
                    selected = selectedModule == module,
                    onClick = { selectedModule = module },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(module.icon, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(module.title, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                )
            }
        }

        // Module Body
        Box(modifier = Modifier.fillMaxSize()) {
            when (selectedModule) {
                AdminSubModule.OVERVIEW -> AdminOverviewTab(viewModel = viewModel, onNavigateTo = { selectedModule = it })
                AdminSubModule.STUDENTS -> AdminStudentsTab(viewModel = viewModel)
                AdminSubModule.TEACHERS -> AdminTeachersTab(viewModel = viewModel)
                AdminSubModule.RESULTS -> AdminResultsTab(viewModel = viewModel)
                AdminSubModule.FEES -> AdminFeesTab(viewModel = viewModel, onViewReceipt = onViewFeeReceipt)
                AdminSubModule.ATTENDANCE -> AdminAttendanceTab(viewModel = viewModel)
                AdminSubModule.TIMETABLE -> AdminTimetableTab(viewModel = viewModel)
                AdminSubModule.GALLERY -> AdminGalleryTab(viewModel = viewModel)
                AdminSubModule.LOGS -> AdminLogsTab(viewModel = viewModel)
                AdminSubModule.SETTINGS -> AdminSettingsTab(viewModel = viewModel)
            }
        }
    }
}

// ==========================================
// LOCKED PORTAL VIEW
// ==========================================

@Composable
private fun AdminLockedPortal(
    onOpenLogin: () -> Unit,
    onOpenRecovery: () -> Unit,
    contentPadding: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .background(Color(0xFF0F172A))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SchoolEmblem(size = 90.dp)
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "BOYS HIGHER SECONDARY SCHOOL TOLTI",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        Text(
            text = "Central Administration & School Records System",
            style = MaterialTheme.typography.bodySmall,
            color = ElegantGold
        )

        Spacer(modifier = Modifier.height(28.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth().border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = CircleShape,
                    color = RoyalBlue.copy(alpha = 0.1f),
                    modifier = Modifier.size(54.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = RoyalBlue, modifier = Modifier.size(28.dp))
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Restricted Administrator Access",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = DeepNavy
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Student admission records, examination marksheets, teacher profiles, daily attendance, fee collections, and system settings require verified administrator authorization.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF64748B),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onOpenLogin,
                    modifier = Modifier.fillMaxWidth().testTag("admin_unlock_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.LockOpen, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sign In to Admin Portal")
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = onOpenRecovery,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, modifier = Modifier.size(18.dp), tint = RoyalBlue)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Forgot Password? (Security Recovery)", color = RoyalBlue)
                }
            }
        }
    }
}

// ==========================================
// TAB 1: OVERVIEW & STATS
// ==========================================

@Composable
private fun AdminOverviewTab(
    viewModel: SchoolViewModel,
    onNavigateTo: (AdminSubModule) -> Unit
) {
    val students by viewModel.allStudents.collectAsState()
    val teachers by viewModel.allTeachers.collectAsState()
    val results by viewModel.allResults.collectAsState()
    val announcements by viewModel.allAnnouncements.collectAsState()
    val fees by viewModel.allFees.collectAsState()
    val attendance by viewModel.allDailyAttendance.collectAsState()

    val publishedResultsCount = results.count { it.isPublished }
    val paidFeesTotal = fees.filter { it.status == "PAID" }.sumOf { it.amount }
    val pendingFeesTotal = fees.filter { it.status == "PENDING" }.sumOf { it.amount }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Academic & Administrative Summary",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = DeepNavy
            )
            Text(
                text = "Boys Higher Secondary School Tolti • Real-time database metrics",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF64748B)
            )
        }

        // Summary Metric Grid
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AdminStatCard(
                    title = "Total Students",
                    value = students.size.toString(),
                    subtitle = "Classes 5th - 12th",
                    icon = Icons.Default.People,
                    color = RoyalBlue,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateTo(AdminSubModule.STUDENTS) }
                )
                AdminStatCard(
                    title = "Faculty Staff",
                    value = teachers.size.toString(),
                    subtitle = "All Departments",
                    icon = Icons.Default.School,
                    color = Color(0xFF0F5132),
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateTo(AdminSubModule.TEACHERS) }
                )
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AdminStatCard(
                    title = "Exam Results",
                    value = "$publishedResultsCount / ${results.size}",
                    subtitle = "Published to public",
                    icon = Icons.Default.Grade,
                    color = ElegantGold,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateTo(AdminSubModule.RESULTS) }
                )
                AdminStatCard(
                    title = "Announcements",
                    value = announcements.size.toString(),
                    subtitle = "Active circulars",
                    icon = Icons.Default.Campaign,
                    color = Color(0xFF7C3AED),
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.showBroadcastAnnouncementDialog.value = true }
                )
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AdminStatCard(
                    title = "Fee Collection",
                    value = "Rs. ${paidFeesTotal.toInt()}",
                    subtitle = "Pending: Rs. ${pendingFeesTotal.toInt()}",
                    icon = Icons.Default.Payments,
                    color = Color(0xFF16A34A),
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateTo(AdminSubModule.FEES) }
                )
                AdminStatCard(
                    title = "Attendance Logs",
                    value = attendance.size.toString(),
                    subtitle = "Daily entries",
                    icon = Icons.Default.EventAvailable,
                    color = Color(0xFF0284C7),
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateTo(AdminSubModule.ATTENDANCE) }
                )
            }
        }

        // Quick Actions Hub
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Quick Administrative Actions",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { viewModel.showAddStudentDialog.value = true },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Enroll Student", fontSize = 12.sp)
                        }
                        Button(
                            onClick = { viewModel.showAdminResultDialog.value = true },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F5132)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Grade, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Enter Result", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { viewModel.showRecordFeeDialog.value = true },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Payments, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Record Fee", fontSize = 12.sp)
                        }
                        Button(
                            onClick = { viewModel.showBroadcastAnnouncementDialog.value = true },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Campaign, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Broadcast Notice", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun AdminStatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, style = MaterialTheme.typography.labelMedium, color = Color(0xFF64748B))
                Surface(
                    shape = CircleShape,
                    color = color.copy(alpha = 0.12f),
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = DeepNavy)
            Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = Color(0xFF94A3B8))
        }
    }
}

// ==========================================
// TAB 2: STUDENTS MANAGEMENT
// ==========================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdminStudentsTab(viewModel: SchoolViewModel) {
    val students by viewModel.filteredStudents.collectAsState()
    val currentClassFilter by viewModel.studentClassFilter.collectAsState()
    val searchQuery by viewModel.studentSearchQuery.collectAsState()

    var studentToEdit by remember { mutableStateOf<StudentProfileRecord?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Student Enrollment Records", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = DeepNavy)
                Text(text = "${students.size} students listed", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
            }
            Button(
                onClick = { viewModel.showAddStudentDialog.value = true },
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("admin_enroll_student_btn")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Enroll Student")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.studentSearchQuery.value = it },
            placeholder = { Text("Search by student name, roll number, or address...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth().testTag("student_search_bar"),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Class Chips
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(viewModel.classOptions) { cls ->
                FilterChip(
                    selected = currentClassFilter == cls,
                    onClick = { viewModel.studentClassFilter.value = cls },
                    label = { Text(if (cls == "ALL") "All Classes" else "Class $cls") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = RoyalBlue,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (students.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No student records match the search criteria.", color = Color(0xFF64748B))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(students, key = { it.id }) { std ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = RoyalBlue.copy(alpha = 0.1f),
                                modifier = Modifier.size(42.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = std.fullName.take(1).uppercase(),
                                        fontWeight = FontWeight.Bold,
                                        color = RoyalBlue
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = std.fullName, fontWeight = FontWeight.Bold, color = DeepNavy)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = Color(0xFFEFF6FF)
                                    ) {
                                        Text(
                                            text = "Class ${std.classLevel}-${std.section}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = RoyalBlue,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Father: ${std.fatherName} • Roll No: ${std.rollNumber}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF64748B)
                                )
                                Text(
                                    text = "Phone: ${std.contactPhone} • ${std.address}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF94A3B8)
                                )
                            }

                            IconButton(
                                onClick = {
                                    viewModel.deleteStudent(std)
                                }
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444), modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// TAB 3: TEACHERS MANAGEMENT
// ==========================================

@Composable
private fun AdminTeachersTab(viewModel: SchoolViewModel) {
    val teachers by viewModel.allTeachers.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Faculty & Staff Directory", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = DeepNavy)
                Text(text = "${teachers.size} teachers appointed", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
            }
            Button(
                onClick = { viewModel.showAddTeacherDialog.value = true },
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("admin_add_teacher_btn")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Faculty")
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(teachers, key = { it.id }) { teacher ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = teacher.name, fontWeight = FontWeight.Bold, color = DeepNavy)
                                Text(text = "${teacher.designation} • ${teacher.department}", style = MaterialTheme.typography.labelMedium, color = RoyalBlue)
                            }
                            IconButton(onClick = { viewModel.deleteTeacher(teacher) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444), modifier = Modifier.size(20.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Qualifications: ${teacher.qualification}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF475569))
                        Text(text = "Assigned: ${teacher.assignedSubjects} (Classes: ${teacher.assignedClasses})", style = MaterialTheme.typography.bodySmall, color = Color(0xFF475569))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Contact: ${teacher.phone} • ${teacher.email}", style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B))
                    }
                }
            }
        }
    }
}

// ==========================================
// TAB 4: RESULTS MANAGEMENT
// ==========================================

@Composable
private fun AdminResultsTab(viewModel: SchoolViewModel) {
    val results by viewModel.filteredResults.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Examination Marksheets & Results", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = DeepNavy)
                Text(text = "Toggle publish status to control public visibility", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
            }
            Button(
                onClick = { viewModel.showAdminResultDialog.value = true },
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("admin_add_result_btn")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Enter Result")
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(results, key = { it.id }) { res ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = res.studentName, fontWeight = FontWeight.Bold, color = DeepNavy)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "(${res.rollNumber})", color = Color(0xFF64748B), fontSize = 12.sp)
                            }
                            Text(
                                text = "${res.examTerm} • Class ${res.classLevel} • ${res.subjectName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = RoyalBlue
                            )
                            Text(
                                text = "Marks: ${res.obtainedMarks} / ${res.totalMarks} (${res.percentage.toInt()}%) • Grade ${res.grade} • ${res.status}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = if (res.status == "PASS") Color(0xFF16A34A) else Color(0xFFDC2626)
                            )
                        }

                        // Publish toggle button
                        IconButton(
                            onClick = { viewModel.toggleResultPublish(res) }
                        ) {
                            Icon(
                                imageVector = if (res.isPublished) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (res.isPublished) "Published" else "Unpublished",
                                tint = if (res.isPublished) Color(0xFF16A34A) else Color(0xFF94A3B8)
                            )
                        }

                        // Generate card
                        IconButton(
                            onClick = { viewModel.selectedResultCard.value = res }
                        ) {
                            Icon(Icons.Default.Receipt, contentDescription = "View Card", tint = RoyalBlue)
                        }

                        // Delete
                        IconButton(
                            onClick = { viewModel.deleteResult(res) }
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444))
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// TAB 5: FEES MANAGEMENT
// ==========================================

@Composable
private fun AdminFeesTab(
    viewModel: SchoolViewModel,
    onViewReceipt: (FeeRecord) -> Unit
) {
    val fees by viewModel.allFees.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Student Fees & Collections", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = DeepNavy)
                Text(text = "Record fees, view receipts, and monitor arrears", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
            }
            Button(
                onClick = { viewModel.showRecordFeeDialog.value = true },
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("admin_record_fee_btn")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Record Fee")
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(fees, key = { it.id }) { fee ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = fee.studentName, fontWeight = FontWeight.Bold, color = DeepNavy)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "(${fee.studentRollNumber} • Class ${fee.classLevel})", color = Color(0xFF64748B), fontSize = 12.sp)
                            }
                            Text(text = "${fee.feeType} • Rs. ${fee.amount.toInt()}", style = MaterialTheme.typography.bodySmall, color = RoyalBlue)
                            Text(
                                text = "Status: ${fee.status} • Due: ${fee.dueDate} ${fee.receiptNumber?.let { "• $it" } ?: ""}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (fee.status == "PAID") Color(0xFF16A34A) else Color(0xFFDC2626)
                            )
                        }

                        if (fee.status == "PENDING") {
                            Button(
                                onClick = { viewModel.markFeePaid(fee) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text("Mark Paid", fontSize = 11.sp)
                            }
                        } else {
                            IconButton(onClick = { onViewReceipt(fee) }) {
                                Icon(Icons.Default.Receipt, contentDescription = "Receipt", tint = RoyalBlue)
                            }
                        }

                        IconButton(onClick = { viewModel.deleteFee(fee) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444), modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// TAB 6: ATTENDANCE MANAGEMENT
// ==========================================

@Composable
private fun AdminAttendanceTab(viewModel: SchoolViewModel) {
    val students by viewModel.allStudents.collectAsState()
    val dailyAttendance by viewModel.allDailyAttendance.collectAsState()

    var selectedClass by remember { mutableStateOf("10th") }
    val todayDate = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) }

    val classStudents = students.filter { it.classLevel == selectedClass }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Daily Student Attendance", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = DeepNavy)
                Text(text = "Date: $todayDate • Mark Present, Absent, or Leave", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Class selector row
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(viewModel.gradeLevelOptions) { cls ->
                FilterChip(
                    selected = selectedClass == cls,
                    onClick = { selectedClass = cls },
                    label = { Text("Class $cls") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = RoyalBlue,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (classStudents.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No students enrolled in Class $selectedClass yet.", color = Color(0xFF64748B))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(classStudents, key = { it.id }) { std ->
                    val record = dailyAttendance.find { it.studentRollNumber == std.rollNumber && it.date == todayDate }
                    val currentStatus = record?.status ?: "PENDING"

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = std.fullName, fontWeight = FontWeight.Bold, color = DeepNavy)
                                Text(text = "Roll: ${std.rollNumber} • Section ${std.section}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
                                Text(
                                    text = "Status: $currentStatus",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = when (currentStatus) {
                                        "PRESENT" -> Color(0xFF16A34A)
                                        "ABSENT" -> Color(0xFFDC2626)
                                        "LEAVE" -> Color(0xFFD97706)
                                        else -> Color(0xFF64748B)
                                    }
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Button(
                                    onClick = { viewModel.markStudentDailyAttendance(std.rollNumber, std.fullName, std.classLevel, todayDate, "PRESENT") },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (currentStatus == "PRESENT") Color(0xFF16A34A) else Color(0xFFE2E8F0),
                                        contentColor = if (currentStatus == "PRESENT") Color.White else Color(0xFF334155)
                                    ),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text("P", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                                Button(
                                    onClick = { viewModel.markStudentDailyAttendance(std.rollNumber, std.fullName, std.classLevel, todayDate, "ABSENT") },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (currentStatus == "ABSENT") Color(0xFFDC2626) else Color(0xFFE2E8F0),
                                        contentColor = if (currentStatus == "ABSENT") Color.White else Color(0xFF334155)
                                    ),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text("A", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                                Button(
                                    onClick = { viewModel.markStudentDailyAttendance(std.rollNumber, std.fullName, std.classLevel, todayDate, "LEAVE") },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (currentStatus == "LEAVE") Color(0xFFD97706) else Color(0xFFE2E8F0),
                                        contentColor = if (currentStatus == "LEAVE") Color.White else Color(0xFF334155)
                                    ),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text("L", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// TAB 7: TIMETABLE MANAGEMENT
// ==========================================

@Composable
private fun AdminTimetableTab(viewModel: SchoolViewModel) {
    val timetable by viewModel.allTimetablePeriods.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "School Timetable Schedules", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = DeepNavy)
                Text(text = "${timetable.size} class periods configured", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
            }
            Button(
                onClick = { viewModel.showAddTimetableDialog.value = true },
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("admin_add_period_btn")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Period")
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(timetable, key = { it.id }) { period ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = RoyalBlue.copy(alpha = 0.1f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = "P${period.periodNumber}", fontWeight = FontWeight.Bold, color = RoyalBlue)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "${period.subjectName} (Class ${period.classLevel} • ${period.dayOfWeek})", fontWeight = FontWeight.Bold, color = DeepNavy)
                            Text(text = "${period.timeRange} • ${period.teacherName} • ${period.roomName}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
                        }
                        IconButton(onClick = { viewModel.deleteTimetablePeriod(period) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444), modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// TAB 8: GALLERY MANAGEMENT
// ==========================================

@Composable
private fun AdminGalleryTab(viewModel: SchoolViewModel) {
    val photos by viewModel.allGalleryPhotos.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "School Photo Gallery", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = DeepNavy)
                Text(text = "${photos.size} photos cataloged", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
            }
            Button(
                onClick = { viewModel.showUploadPhotoDialog.value = true },
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("admin_upload_photo_btn")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Upload Photo")
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(photos, key = { it.id }) { photo ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = photo.title, fontWeight = FontWeight.Bold, color = DeepNavy)
                            Text(text = "Category: ${photo.category}", style = MaterialTheme.typography.bodySmall, color = RoyalBlue)
                            Text(text = photo.description, style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B), maxLines = 2)
                        }
                        IconButton(onClick = { viewModel.deleteGalleryPhoto(photo) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444), modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// TAB 9: AUDIT LOGS
// ==========================================

@Composable
private fun AdminLogsTab(viewModel: SchoolViewModel) {
    val logs by viewModel.adminActivityLogs.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Security & Administrative Audit Logs", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = DeepNavy)
        Text(text = "Every administrative action is tracked for accountability", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))

        Spacer(modifier = Modifier.height(14.dp))

        if (logs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No activity logged yet.", color = Color(0xFF64748B))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(logs, key = { it.id }) { log ->
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = log.action, fontWeight = FontWeight.Bold, color = RoyalBlue, fontSize = 13.sp)
                                Text(
                                    text = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(log.timestamp)),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = log.details, style = MaterialTheme.typography.bodySmall, color = Color(0xFF334155))
                            Text(text = "Admin: @${log.adminUsername}", style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B))
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// TAB 10: SETTINGS & BACKUP
// ==========================================

@Composable
private fun AdminSettingsTab(viewModel: SchoolViewModel) {
    val settings by viewModel.schoolSettings.collectAsState()
    var exportedJson by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(text = "School Configuration & System Backup", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = DeepNavy)
            Text(text = "Official credentials, contact details, and database snapshot", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
        }

        // Settings Summary Card
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "School Information", fontWeight = FontWeight.Bold, color = DeepNavy)
                        Button(
                            onClick = { viewModel.showEditSettingsDialog.value = true },
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit Info")
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Principal: ${settings?.principalName ?: "Prof. Muhammad Hussain"}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF475569))
                    Text(text = "Phone: ${settings?.phone ?: "+92 5816 920011"}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF475569))
                    Text(text = "Email: ${settings?.email ?: "contact@bhsstolti.edu.pk"}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF475569))
                    Text(text = "Address: ${settings?.address ?: "Main Bazar Road, Tolti, District Kharmang"}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF475569))
                }
            }
        }

        // Security Actions
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Security & Password Management", fontWeight = FontWeight.Bold, color = DeepNavy)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { viewModel.showChangePasswordDialog.value = true },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Change Password")
                        }
                    }
                }
            }
        }

        // Database Backup & Restore
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Database Backup & Export", fontWeight = FontWeight.Bold, color = DeepNavy)
                    Text(text = "Export a full JSON snapshot of all student records, faculty profiles, marks, fees, and timetables.", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            viewModel.exportBackup { json ->
                                exportedJson = json
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("admin_backup_export_btn")
                    ) {
                        Icon(Icons.Default.Backup, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Generate & Export Backup Snapshot")
                    }

                    if (exportedJson != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF1F5F9),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = "Backup Snapshot JSON:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DeepNavy)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = (exportedJson ?: "").take(250) + "...\n[Full database encrypted payload exported]",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    color = Color(0xFF334155)
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
