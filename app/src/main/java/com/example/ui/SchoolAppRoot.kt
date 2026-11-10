package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.FeeRecord
import com.example.data.model.NotificationType
import com.example.ui.components.InAppNotificationBanner
import com.example.ui.components.SchoolEmblem
import com.example.ui.screens.AddAssignmentDialog
import com.example.ui.screens.AddStudentDialog
import com.example.ui.screens.AddTeacherDialog
import com.example.ui.screens.AddTimetableDialog
import com.example.ui.screens.AdminChangePasswordDialog
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AdminLoginDialog
import com.example.ui.screens.AdminPasswordRecoveryDialog
import com.example.ui.screens.AdminResultInputDialog
import com.example.ui.screens.AnnouncementDetailDialog
import com.example.ui.screens.AssignmentsScreen
import com.example.ui.screens.BroadcastAnnouncementDialog
import com.example.ui.screens.EditSchoolSettingsDialog
import com.example.ui.screens.FeeReceiptDialog
import com.example.ui.screens.NotificationCenterDialog
import com.example.ui.screens.NotificationSettingsDialog
import com.example.ui.screens.NotificationSimulatorDialog
import com.example.ui.screens.OfficialResultCardDialog
import com.example.ui.screens.PublicAboutContactScreen
import com.example.ui.screens.PublicAnnouncementsScreen
import com.example.ui.screens.PublicAttendanceScreen
import com.example.ui.screens.PublicGalleryScreen
import com.example.ui.screens.PublicHomeScreen
import com.example.ui.screens.PublicResultsScreen
import com.example.ui.screens.PublicTeachersScreen
import com.example.ui.screens.PublicTimetableScreen
import com.example.ui.screens.RecordFeeDialog
import com.example.ui.screens.UploadGalleryPhotoDialog
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.ElegantGold
import com.example.ui.theme.RoyalBlue
import com.example.ui.viewmodel.SchoolAppSection
import com.example.ui.viewmodel.SchoolViewModel
import com.example.util.UrduLocalization

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolAppRoot(viewModel: SchoolViewModel) {
    val currentSection by viewModel.currentSection.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val isUrdu = currentLanguage == UrduLocalization.Language.URDU

    val isAdminLoggedIn by viewModel.isAdminLoggedIn.collectAsState()
    val allAssignments by viewModel.allAssignments.collectAsState()
    val allAnnouncements by viewModel.allAnnouncements.collectAsState()
    val unreadNotifCount by viewModel.unreadNotificationCount.collectAsState()
    val realtimeAlert by viewModel.realtimeAlert.collectAsState()
    val snackbarMsg by viewModel.snackbarMessage.collectAsState()

    // Dialog states from ViewModel
    val showAddAssignment by viewModel.showAddAssignmentDialog.collectAsState()
    val showAdminResult by viewModel.showAdminResultDialog.collectAsState()
    val showAddStudent by viewModel.showAddStudentDialog.collectAsState()
    val showAddTeacher by viewModel.showAddTeacherDialog.collectAsState()
    val showRecordFee by viewModel.showRecordFeeDialog.collectAsState()
    val showAddTimetable by viewModel.showAddTimetableDialog.collectAsState()
    val showUploadPhoto by viewModel.showUploadPhotoDialog.collectAsState()
    val showEditSettings by viewModel.showEditSettingsDialog.collectAsState()
    val showAdminLogin by viewModel.showAdminLoginDialog.collectAsState()
    val showChangePassword by viewModel.showChangePasswordDialog.collectAsState()
    val showPasswordRecovery by viewModel.showPasswordRecoveryDialog.collectAsState()
    val showNotificationCenter by viewModel.showNotificationCenter.collectAsState()
    val showNotificationSettings by viewModel.showNotificationSettings.collectAsState()
    val showBroadcastAnnouncement by viewModel.showBroadcastAnnouncementDialog.collectAsState()
    val showNotificationSimulator by viewModel.showNotificationSimulator.collectAsState()
    val selectedAnnouncement by viewModel.selectedAnnouncementForDetails.collectAsState()
    val selectedResultCard by viewModel.selectedResultCard.collectAsState()

    // Local dialog state for fee receipt
    var feeForReceipt by remember { mutableStateOf<FeeRecord?>(null) }
    var showWelcomeOpeningDialog by remember { mutableStateOf(true) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMsg) {
        snackbarMsg?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    // Directional provider for bilingual Urdu RTL and English LTR
    CompositionLocalProvider(
        LocalLayoutDirection provides (if (isUrdu) LayoutDirection.Rtl else LayoutDirection.Ltr)
    ) {
        // Handle Android hardware/gesture back press to return to Home screen
        BackHandler(enabled = currentSection != SchoolAppSection.HOME) {
            viewModel.currentSection.value = SchoolAppSection.HOME
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                Surface(
                    color = Color.White,
                    shadowElevation = 1.dp,
                    border = BorderStroke(0.5.dp, Color(0xFFE2E8F0))
                ) {
                    TopAppBar(
                        title = {
                            if (currentSection == SchoolAppSection.HOME) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable { viewModel.currentSection.value = SchoolAppSection.HOME }
                                ) {
                                    SchoolEmblem(size = 30.dp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = if (isUrdu) "گورنمنٹ بوائز ہائر سیکنڈری اسکول تولتی" else "BHSS TOLTI",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = DeepNavy,
                                            maxLines = 1
                                        )
                                        Text(
                                            text = if (isUrdu) "سیکھو • آگے بڑھو • کامیاب ہو" else "LEARN • GROW • SUCCEED",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = ElegantGold,
                                            fontSize = 9.5.sp
                                        )
                                    }
                                }
                            } else {
                                Column {
                                    Text(
                                        text = if (isUrdu) currentSection.urduTitle else currentSection.englishTitle,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DeepNavy,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = if (isUrdu) "بوائز ہائر سیکنڈری اسکول تولتی" else "Govt. BHSS Tolti • Kharmang",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFF64748B),
                                        fontSize = 9.5.sp
                                    )
                                }
                            }
                        },
                        navigationIcon = {
                            if (currentSection != SchoolAppSection.HOME) {
                                IconButton(
                                    onClick = { viewModel.currentSection.value = SchoolAppSection.HOME },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .testTag("appbar_back_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back to Home",
                                        tint = DeepNavy,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        },
                        actions = {
                            // 1. Bilingual Language Switcher (EN ↔ URDU)
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = RoyalBlue.copy(alpha = 0.08f),
                                modifier = Modifier
                                    .clickable { viewModel.toggleLanguage() }
                                    .testTag("appbar_language_toggle_btn")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Language, contentDescription = "Language", tint = RoyalBlue, modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = if (isUrdu) "EN" else "اردو",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = RoyalBlue
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(2.dp))

                            // 2. Notification Center Bell with live badge
                            IconButton(
                                onClick = { viewModel.showNotificationCenter.value = true },
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("appbar_notification_bell_button")
                            ) {
                                BadgedBox(
                                    badge = {
                                        if (unreadNotifCount > 0) {
                                            Badge(
                                                containerColor = Color(0xFFDC2626),
                                                contentColor = Color.White
                                            ) {
                                                Text(
                                                    text = if (unreadNotifCount > 99) "99+" else unreadNotifCount.toString(),
                                                    fontSize = 8.5.sp
                                                )
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = "Academic Notifications",
                                        tint = if (unreadNotifCount > 0) Color(0xFFDC2626) else DeepNavy,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            // 3. Admin Login / Portal button
                            IconButton(
                                onClick = {
                                    if (isAdminLoggedIn) {
                                        viewModel.currentSection.value = SchoolAppSection.ADMIN_PANEL
                                    } else {
                                        viewModel.showAdminLoginDialog.value = true
                                    }
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("appbar_admin_portal_btn")
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (isAdminLoggedIn) Color(0xFF16A34A).copy(alpha = 0.15f) else DeepNavy.copy(alpha = 0.08f),
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = if (isAdminLoggedIn) Icons.Default.AdminPanelSettings else Icons.Default.Lock,
                                            contentDescription = if (isAdminLoggedIn) "Admin Portal" else "Admin Login",
                                            tint = if (isAdminLoggedIn) Color(0xFF16A34A) else DeepNavy,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                    )
                }
            },
            bottomBar = {
                Surface(
                    color = DeepNavy,
                    border = BorderStroke(0.5.dp, Color(0xFF1E293B)),
                    shadowElevation = 6.dp
                ) {
                    NavigationBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("school_bottom_nav_bar"),
                        containerColor = DeepNavy,
                        tonalElevation = 0.dp
                    ) {
                        // 1. Persistent Home Tab
                        val isHomeSelected = currentSection == SchoolAppSection.HOME
                        NavigationBarItem(
                            selected = isHomeSelected,
                            onClick = { viewModel.currentSection.value = SchoolAppSection.HOME },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "Home",
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = if (isUrdu) "ہوم" else "Home",
                                    fontWeight = if (isHomeSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 10.5.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = DeepNavy,
                                selectedTextColor = ElegantGold,
                                indicatorColor = ElegantGold,
                                unselectedIconColor = Color.White.copy(alpha = 0.7f),
                                unselectedTextColor = Color.White.copy(alpha = 0.7f)
                            ),
                            modifier = Modifier.testTag("bottom_nav_home")
                        )

                        // 2. Persistent Results Tab (First option after Home, styled with bold gold badge)
                        val isResultsSelected = currentSection == SchoolAppSection.RESULTS
                        NavigationBarItem(
                            selected = isResultsSelected,
                            onClick = { viewModel.currentSection.value = SchoolAppSection.RESULTS },
                            icon = {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isResultsSelected) ElegantGold else Color(0xFFD97706),
                                    border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                                    shadowElevation = if (isResultsSelected) 3.dp else 1.dp
                                ) {
                                    Box(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Grade,
                                            contentDescription = "Results",
                                            tint = if (isResultsSelected) DeepNavy else Color.White,
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }
                                }
                            },
                            label = {
                                Text(
                                    text = if (isUrdu) "امتحانی نتائج" else "RESULTS",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 11.sp,
                                    color = if (isResultsSelected) ElegantGold else Color(0xFFFDE68A),
                                    letterSpacing = 0.4.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = DeepNavy,
                                selectedTextColor = ElegantGold,
                                indicatorColor = Color.Transparent,
                                unselectedIconColor = Color(0xFFF59E0B),
                                unselectedTextColor = Color(0xFFFDE68A)
                            ),
                            modifier = Modifier
                                .testTag("bottom_nav_results")
                                .testTag("bottom_bar_results_btn")
                        )

                        // 3. Persistent Announcements Tab (Notices)
                        val isNoticesSelected = currentSection == SchoolAppSection.ANNOUNCEMENTS
                        NavigationBarItem(
                            selected = isNoticesSelected,
                            onClick = { viewModel.currentSection.value = SchoolAppSection.ANNOUNCEMENTS },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Campaign,
                                    contentDescription = "Notices",
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = if (isUrdu) "اعلانات" else "Notices",
                                    fontWeight = if (isNoticesSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 10.5.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = DeepNavy,
                                selectedTextColor = ElegantGold,
                                indicatorColor = ElegantGold,
                                unselectedIconColor = Color.White.copy(alpha = 0.7f),
                                unselectedTextColor = Color.White.copy(alpha = 0.7f)
                            ),
                            modifier = Modifier.testTag("bottom_nav_announcements")
                        )

                        // 4. Persistent Attendance Tab
                        val isAttendanceSelected = currentSection == SchoolAppSection.ATTENDANCE
                        NavigationBarItem(
                            selected = isAttendanceSelected,
                            onClick = { viewModel.currentSection.value = SchoolAppSection.ATTENDANCE },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.EventAvailable,
                                    contentDescription = "Attendance",
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = if (isUrdu) "حاضری" else "Attendance",
                                    fontWeight = if (isAttendanceSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 10.5.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = DeepNavy,
                                selectedTextColor = ElegantGold,
                                indicatorColor = ElegantGold,
                                unselectedIconColor = Color.White.copy(alpha = 0.7f),
                                unselectedTextColor = Color.White.copy(alpha = 0.7f)
                            ),
                            modifier = Modifier.testTag("bottom_nav_attendance")
                        )

                        // 5. Admin Portal Tab
                        val isAdminSelected = currentSection == SchoolAppSection.ADMIN_PANEL
                        NavigationBarItem(
                            selected = isAdminSelected,
                            onClick = {
                                if (isAdminLoggedIn) {
                                    viewModel.currentSection.value = SchoolAppSection.ADMIN_PANEL
                                } else {
                                    viewModel.showAdminLoginDialog.value = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (isAdminLoggedIn) Icons.Default.AdminPanelSettings else Icons.Default.Lock,
                                    contentDescription = "Admin",
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = if (isUrdu) "ایڈمن" else "Admin",
                                    fontWeight = if (isAdminSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 10.5.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = DeepNavy,
                                selectedTextColor = ElegantGold,
                                indicatorColor = ElegantGold,
                                unselectedIconColor = Color.White.copy(alpha = 0.7f),
                                unselectedTextColor = Color.White.copy(alpha = 0.7f)
                            ),
                            modifier = Modifier.testTag("bottom_nav_admin")
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize()) {
                // Section Router
                when (currentSection) {
                    SchoolAppSection.HOME -> {
                        PublicHomeScreen(viewModel = viewModel, contentPadding = paddingValues)
                    }
                    SchoolAppSection.RESULTS -> {
                        PublicResultsScreen(viewModel = viewModel, contentPadding = paddingValues)
                    }
                    SchoolAppSection.ATTENDANCE -> {
                        PublicAttendanceScreen(viewModel = viewModel, contentPadding = paddingValues)
                    }
                    SchoolAppSection.ANNOUNCEMENTS -> {
                        PublicAnnouncementsScreen(viewModel = viewModel, contentPadding = paddingValues)
                    }
                    SchoolAppSection.TEACHERS -> {
                        PublicTeachersScreen(viewModel = viewModel, contentPadding = paddingValues)
                    }
                    SchoolAppSection.TIMETABLE -> {
                        PublicTimetableScreen(viewModel = viewModel, contentPadding = paddingValues)
                    }
                    SchoolAppSection.HOMEWORK -> {
                        AssignmentsScreen(viewModel = viewModel, contentPadding = paddingValues)
                    }
                    SchoolAppSection.GALLERY -> {
                        PublicGalleryScreen(viewModel = viewModel, contentPadding = paddingValues)
                    }
                    SchoolAppSection.ABOUT_CONTACT -> {
                        PublicAboutContactScreen(viewModel = viewModel, contentPadding = paddingValues)
                    }
                    SchoolAppSection.ADMIN_PANEL -> {
                        AdminDashboardScreen(
                            viewModel = viewModel,
                            contentPadding = paddingValues,
                            onViewFeeReceipt = { fee -> feeForReceipt = fee }
                        )
                    }
                }

                // Real-Time Floating In-App Heads-Up Banner
                InAppNotificationBanner(
                    notification = realtimeAlert,
                    onDismiss = { viewModel.dismissRealtimeAlert() },
                    onActionClick = { notif ->
                        viewModel.markNotificationAsRead(notif.id)
                        viewModel.dismissRealtimeAlert()
                        when (notif.type) {
                            NotificationType.UPCOMING_DEADLINE,
                            NotificationType.NEW_ASSIGNMENT -> {
                                viewModel.currentSection.value = SchoolAppSection.HOMEWORK
                                val matchedAsg = allAssignments.find { it.id == notif.relatedId }
                                if (matchedAsg != null) {
                                    viewModel.selectedAssignmentForDetails.value = matchedAsg
                                }
                            }
                            NotificationType.GRADE_UPDATE -> {
                                viewModel.currentSection.value = SchoolAppSection.RESULTS
                            }
                            NotificationType.COLLEGE_ANNOUNCEMENT -> {
                                val matchedAnn = allAnnouncements.find { it.id == notif.relatedId }
                                if (matchedAnn != null) {
                                    viewModel.selectedAnnouncementForDetails.value = matchedAnn
                                } else {
                                    viewModel.currentSection.value = SchoolAppSection.ANNOUNCEMENTS
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = paddingValues.calculateTopPadding() + 4.dp)
                )
            }

            // --- Dialog Overlays ---

            if (showAdminLogin) {
                AdminLoginDialog(
                    viewModel = viewModel,
                    onDismiss = { viewModel.showAdminLoginDialog.value = false }
                )
            }

            if (showChangePassword) {
                AdminChangePasswordDialog(
                    viewModel = viewModel,
                    onDismiss = { viewModel.showChangePasswordDialog.value = false }
                )
            }

            if (showPasswordRecovery) {
                AdminPasswordRecoveryDialog(
                    viewModel = viewModel,
                    onDismiss = { viewModel.showPasswordRecoveryDialog.value = false }
                )
            }

            if (showAddStudent) {
                AddStudentDialog(
                    viewModel = viewModel,
                    onDismiss = { viewModel.showAddStudentDialog.value = false }
                )
            }

            if (showAddTeacher) {
                AddTeacherDialog(
                    viewModel = viewModel,
                    onDismiss = { viewModel.showAddTeacherDialog.value = false }
                )
            }

            if (showRecordFee) {
                RecordFeeDialog(
                    viewModel = viewModel,
                    onDismiss = { viewModel.showRecordFeeDialog.value = false }
                )
            }

            if (showAddTimetable) {
                AddTimetableDialog(
                    viewModel = viewModel,
                    onDismiss = { viewModel.showAddTimetableDialog.value = false }
                )
            }

            if (showUploadPhoto) {
                UploadGalleryPhotoDialog(
                    viewModel = viewModel,
                    onDismiss = { viewModel.showUploadPhotoDialog.value = false }
                )
            }

            if (showEditSettings) {
                EditSchoolSettingsDialog(
                    viewModel = viewModel,
                    onDismiss = { viewModel.showEditSettingsDialog.value = false }
                )
            }

            if (showAddAssignment) {
                AddAssignmentDialog(
                    viewModel = viewModel,
                    onDismiss = { viewModel.showAddAssignmentDialog.value = false }
                )
            }

            if (showAdminResult) {
                AdminResultInputDialog(
                    viewModel = viewModel,
                    onDismiss = { viewModel.showAdminResultDialog.value = false }
                )
            }

            if (showNotificationCenter) {
                NotificationCenterDialog(
                    viewModel = viewModel,
                    onDismiss = { viewModel.showNotificationCenter.value = false }
                )
            }

            if (showNotificationSettings) {
                NotificationSettingsDialog(
                    viewModel = viewModel,
                    onDismiss = { viewModel.showNotificationSettings.value = false }
                )
            }

            if (showBroadcastAnnouncement) {
                BroadcastAnnouncementDialog(
                    viewModel = viewModel,
                    onDismiss = { viewModel.showBroadcastAnnouncementDialog.value = false }
                )
            }

            if (showNotificationSimulator) {
                NotificationSimulatorDialog(
                    viewModel = viewModel,
                    onDismiss = { viewModel.showNotificationSimulator.value = false }
                )
            }

            selectedAnnouncement?.let { announcement ->
                AnnouncementDetailDialog(
                    announcement = announcement,
                    onDismiss = { viewModel.selectedAnnouncementForDetails.value = null }
                )
            }

            selectedResultCard?.let { res ->
                OfficialResultCardDialog(
                    result = res,
                    onDismiss = { viewModel.selectedResultCard.value = null },
                    onPrintOrDownload = {
                        viewModel.showSnackbar("Printed official result card for ${res.studentName} (${res.rollNumber}).")
                    }
                )
            }

            feeForReceipt?.let { fee ->
                FeeReceiptDialog(
                    fee = fee,
                    onDismiss = { feeForReceipt = null },
                    onPrintOrDownload = {
                        viewModel.showSnackbar("Printed official fee receipt for ${fee.studentName} (${fee.receiptNumber}).")
                    }
                )
            }

            if (showWelcomeOpeningDialog) {
                Dialog(onDismissRequest = { showWelcomeOpeningDialog = false }) {
                    Card(
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .testTag("opening_welcome_dialog")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(22.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            SchoolEmblem(size = 96.dp, showBorder = true)
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = if (isUrdu) "گورنمنٹ بوائز ہائر سیکنڈری اسکول تولتی" else "GOVT. HIGHER SECONDARY SCHOOL TOLTI",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepNavy,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = if (isUrdu) "ضلع کھرمنگ، گلگت بلتستان" else "KHARMANG • GILGIT-BALTISTAN",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF64748B)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                color = ElegantGold.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = if (isUrdu) "علم حاصل کرو • آگے بڑھو • کامیاب بنو" else "LEARN • GROW • SUCCEED",
                                    color = Color(0xFFB45309),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            // Fast action buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        viewModel.currentSection.value = SchoolAppSection.RESULTS
                                        showWelcomeOpeningDialog = false
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(if (isUrdu) "امتحانی نتائج" else "Results", fontSize = 12.sp)
                                }
                                Button(
                                    onClick = {
                                        viewModel.currentSection.value = SchoolAppSection.ATTENDANCE
                                        showWelcomeOpeningDialog = false
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F5132)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(if (isUrdu) "حاضری ریکارڈ" else "Attendance", fontSize = 12.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedButton(
                                onClick = { showWelcomeOpeningDialog = false },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("enter_campus_portal_btn"),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(if (isUrdu) "اسکول پورٹل میں داخل ہوں" else "Enter Campus Portal", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
