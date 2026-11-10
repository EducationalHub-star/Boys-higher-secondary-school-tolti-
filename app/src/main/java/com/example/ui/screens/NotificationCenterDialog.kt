package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.NotificationPriority
import com.example.data.model.NotificationType
import com.example.data.model.SchoolNotification
import com.example.ui.viewmodel.SchoolNavTab
import com.example.ui.viewmodel.SchoolViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationCenterDialog(
    viewModel: SchoolViewModel,
    onDismiss: () -> Unit
) {
    val notifications by viewModel.filteredNotifications.collectAsState()
    val allNotifications by viewModel.allNotifications.collectAsState()
    val unreadCount by viewModel.unreadNotificationCount.collectAsState()
    val filterType by viewModel.notificationFilterType.collectAsState()
    val searchQuery by viewModel.notificationSearchQuery.collectAsState()
    val allAssignments by viewModel.allAssignments.collectAsState()
    val allAnnouncements by viewModel.allAnnouncements.collectAsState()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 28.dp, bottom = 12.dp, start = 10.dp, end = 10.dp)
                .clip(RoundedCornerShape(24.dp))
                .testTag("notification_center_dialog"),
            color = MaterialTheme.colorScheme.background,
            tonalElevation = 6.dp,
            shadowElevation = 12.dp
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header Bar
                Surface(
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFF0F5132).copy(alpha = 0.12f),
                                    modifier = Modifier.size(38.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.NotificationsActive,
                                            contentDescription = null,
                                            tint = Color(0xFF0F5132),
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Academic Alerts",
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF111827)
                                        )
                                        if (unreadCount > 0) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                color = Color(0xFFDC2626),
                                                shape = RoundedCornerShape(12.dp)
                                            ) {
                                                Text(
                                                    text = "$unreadCount New",
                                                    color = Color.White,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = "Real-time updates for assignments, exams & college events",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFF6B7280)
                                    )
                                }
                            }

                            // Close button
                            IconButton(
                                onClick = onDismiss,
                                modifier = Modifier.testTag("close_notification_center_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = Color(0xFF6B7280)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Action Tools Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Mark all as read
                            OutlinedButton(
                                onClick = { viewModel.markAllNotificationsAsRead() },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(36.dp)
                                    .testTag("mark_all_read_btn"),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DoneAll,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = Color(0xFF0F5132)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Mark All Read", fontSize = 11.sp, color = Color(0xFF0F5132))
                            }

                            // Scan Deadlines button
                            OutlinedButton(
                                onClick = { viewModel.checkUpcomingDeadlines(leadHours = 24, manualTrigger = true) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(36.dp)
                                    .testTag("scan_deadlines_btn"),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.HourglassTop,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = Color(0xFFD97706)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Scan Deadlines", fontSize = 11.sp, color = Color(0xFFD97706))
                            }

                            // Simulator button
                            IconButton(
                                onClick = { viewModel.showNotificationSimulator.value = true },
                                modifier = Modifier
                                    .size(36.dp)
                                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(8.dp))
                                    .testTag("open_simulator_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = "Simulate Real-time Alert",
                                    tint = Color(0xFF4F46E5),
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            // Settings button
                            IconButton(
                                onClick = { viewModel.showNotificationSettings.value = true },
                                modifier = Modifier
                                    .size(36.dp)
                                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(8.dp))
                                    .testTag("open_settings_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Notification Settings",
                                    tint = Color(0xFF4B5563),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                // Search Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.notificationSearchQuery.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("notification_search_input"),
                        placeholder = { Text("Search alerts, subjects, teachers...", fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = Color(0xFF9CA3AF),
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.notificationSearchQuery.value = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = Color(0xFF9CA3AF),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }

                // Filter Categories Row
                val filterOptions = listOf(
                    "ALL" to "All (${allNotifications.size})",
                    "UNREAD" to "Unread ($unreadCount)",
                    "DEADLINES" to "Deadlines ⏰",
                    "GRADES" to "Grades 🏆",
                    "ASSIGNMENTS" to "Assignments 📝",
                    "ANNOUNCEMENTS" to "Announcements 📢"
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filterOptions) { (key, label) ->
                        val isSelected = filterType == key
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.notificationFilterType.value = key },
                            label = {
                                Text(
                                    text = label,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF0F5132),
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Notification Cards List
                if (notifications.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFFF3F4F6),
                                modifier = Modifier.size(72.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.NotificationsOff,
                                        contentDescription = null,
                                        tint = Color(0xFF9CA3AF),
                                        modifier = Modifier.size(38.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "No Notifications Found",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF374151)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (searchQuery.isNotEmpty()) {
                                    "No alerts match '$searchQuery'. Try adjusting your search term."
                                } else {
                                    "You are completely caught up! New assignments and grades will appear here in real time."
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF6B7280),
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .testTag("notification_list"),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(notifications, key = { it.id }) { notif ->
                            NotificationCardItem(
                                notification = notif,
                                onCardClick = {
                                    viewModel.markNotificationAsRead(notif.id)
                                    handleNotificationNavigation(
                                        notif = notif,
                                        viewModel = viewModel,
                                        allAssignments = allAssignments,
                                        allAnnouncements = allAnnouncements,
                                        onDismiss = onDismiss
                                    )
                                },
                                onMarkRead = { viewModel.markNotificationAsRead(notif.id) },
                                onDelete = { viewModel.deleteNotification(notif) },
                                onPrimaryAction = {
                                    viewModel.markNotificationAsRead(notif.id)
                                    handleNotificationNavigation(
                                        notif = notif,
                                        viewModel = viewModel,
                                        allAssignments = allAssignments,
                                        allAnnouncements = allAnnouncements,
                                        onDismiss = onDismiss
                                    )
                                }
                            )
                        }
                    }
                }

                // Bottom summary bar
                Surface(
                    color = Color.White,
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${allNotifications.size} total alerts (${unreadCount} unread)",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF6B7280)
                        )

                        TextButton(
                            onClick = { viewModel.clearReadNotifications() },
                            modifier = Modifier.testTag("clear_read_btn")
                        ) {
                            Text(
                                text = "Clear Read",
                                color = Color(0xFFDC2626),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationCardItem(
    notification: SchoolNotification,
    onCardClick: () -> Unit,
    onMarkRead: () -> Unit,
    onDelete: () -> Unit,
    onPrimaryAction: () -> Unit
) {
    val (icon, tintColor, actionLabel) = when (notification.type) {
        NotificationType.UPCOMING_DEADLINE -> {
            val color = if (notification.priority == NotificationPriority.URGENT) Color(0xFFDC2626) else Color(0xFFD97706)
            Triple(Icons.Default.HourglassTop, color, "View Assignment")
        }
        NotificationType.GRADE_UPDATE -> {
            Triple(Icons.Default.Grade, Color(0xFFB45309), "View Marksheet")
        }
        NotificationType.NEW_ASSIGNMENT -> {
            Triple(Icons.Default.Assignment, Color(0xFF0F5132), "View Assignment")
        }
        NotificationType.COLLEGE_ANNOUNCEMENT -> {
            Triple(Icons.Default.Campaign, Color(0xFF1E40AF), "Read Notice")
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onCardClick() }
            .testTag("notification_card_${notification.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (!notification.isRead) Color.White else Color(0xFFF9FAFB)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (!notification.isRead) 3.dp else 1.dp),
        border = if (!notification.isRead) {
            androidx.compose.foundation.BorderStroke(1.dp, tintColor.copy(alpha = 0.35f))
        } else {
            androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB))
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Colored Icon
                Surface(
                    shape = CircleShape,
                    color = tintColor.copy(alpha = 0.12f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = tintColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Category Chip
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = tintColor.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = notification.type.label,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = tintColor,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }

                            if (notification.priority == NotificationPriority.URGENT) {
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

                        // Timestamp & unread dot
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = formatTimeAgo(notification.timestamp),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF9CA3AF),
                                fontSize = 11.sp
                            )
                            if (!notification.isRead) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(tintColor)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = notification.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = if (!notification.isRead) FontWeight.Bold else FontWeight.SemiBold,
                        color = Color(0xFF111827)
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = notification.message,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF4B5563),
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action footer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 50.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Class or Subject pill
                if (!notification.relatedSubject.isNullOrBlank() || notification.targetClass != "ALL") {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFF3F4F6)
                    ) {
                        Text(
                            text = notification.relatedSubject ?: "Class ${notification.targetClass}",
                            fontSize = 10.sp,
                            color = Color(0xFF6B7280),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Quick Action button
                    Button(
                        onClick = onPrimaryAction,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = tintColor
                        ),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Text(
                            text = actionLabel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // Delete button
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun handleNotificationNavigation(
    notif: SchoolNotification,
    viewModel: SchoolViewModel,
    allAssignments: List<com.example.data.model.StudentAssignment>,
    allAnnouncements: List<com.example.data.model.CollegeAnnouncement>,
    onDismiss: () -> Unit
) {
    when (notif.type) {
        NotificationType.UPCOMING_DEADLINE,
        NotificationType.NEW_ASSIGNMENT -> {
            viewModel.selectedTab.value = SchoolNavTab.ASSIGNMENTS
            val matchedAsg = allAssignments.find { it.id == notif.relatedId }
            if (matchedAsg != null) {
                viewModel.selectedAssignmentForDetails.value = matchedAsg
            }
            onDismiss()
        }
        NotificationType.GRADE_UPDATE -> {
            viewModel.selectedTab.value = SchoolNavTab.ADMIN_RESULTS
            onDismiss()
        }
        NotificationType.COLLEGE_ANNOUNCEMENT -> {
            val matchedAnn = allAnnouncements.find { it.id == notif.relatedId }
            if (matchedAnn != null) {
                viewModel.selectedAnnouncementForDetails.value = matchedAnn
            } else {
                // Create a temporary announcement view for the notice
                viewModel.selectedAnnouncementForDetails.value = com.example.data.model.CollegeAnnouncement(
                    id = notif.relatedId ?: notif.id,
                    title = notif.title,
                    content = notif.message,
                    category = "College Notice",
                    targetAudience = notif.targetClass,
                    author = "Principal Office & Administration",
                    timestamp = notif.timestamp,
                    isPinned = false,
                    isUrgent = notif.priority == NotificationPriority.URGENT
                )
            }
            onDismiss()
        }
    }
}

private fun formatTimeAgo(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        minutes < 1 -> "Just now"
        minutes < 60 -> "${minutes}m ago"
        hours < 24 -> "${hours}h ago"
        days < 7 -> "${days}d ago"
        else -> "Earlier"
    }
}
