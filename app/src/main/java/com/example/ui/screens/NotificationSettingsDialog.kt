package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.NotificationPreferences
import com.example.ui.viewmodel.SchoolViewModel

@Composable
fun NotificationSettingsDialog(
    viewModel: SchoolViewModel,
    onDismiss: () -> Unit
) {
    val currentPrefs by viewModel.notificationPreferences.collectAsState()

    var assignmentsEnabled by remember { mutableStateOf(currentPrefs.assignmentsEnabled) }
    var deadlinesEnabled by remember { mutableStateOf(currentPrefs.deadlinesEnabled) }
    var gradesEnabled by remember { mutableStateOf(currentPrefs.gradesEnabled) }
    var announcementsEnabled by remember { mutableStateOf(currentPrefs.announcementsEnabled) }
    var systemTrayAlerts by remember { mutableStateOf(currentPrefs.systemTrayAlerts) }
    var soundAndVibrate by remember { mutableStateOf(currentPrefs.soundAndVibrate) }
    var deadlineLeadHours by remember { mutableStateOf(currentPrefs.deadlineLeadHours) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .testTag("notification_settings_dialog"),
            color = Color.White,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
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
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                                tint = Color(0xFF0F5132),
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Notification Settings",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color(0xFF6B7280)
                        )
                    }
                }

                Text(
                    text = "Configure real-time alerts for your classes, deadlines, and grade updates.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )

                // Category toggles
                Text(
                    text = "ACADEMIC ALERT CHANNELS",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F5132),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                SettingToggleRow(
                    icon = Icons.Default.HourglassTop,
                    iconTint = Color(0xFFDC2626),
                    title = "Upcoming Deadlines",
                    description = "Alerts before assignments and practicals are due",
                    checked = deadlinesEnabled,
                    onCheckedChange = { deadlinesEnabled = it }
                )

                if (deadlinesEnabled) {
                    Text(
                        text = "Alert me before deadline:",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF4B5563),
                        modifier = Modifier.padding(start = 42.dp, top = 4.dp, bottom = 4.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 42.dp, bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(6 to "6h", 12 to "12h", 24 to "24h", 48 to "48h").forEach { (hours, label) ->
                            val selected = deadlineLeadHours == hours
                            FilterChip(
                                selected = selected,
                                onClick = { deadlineLeadHours = hours },
                                label = { Text(label, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFFD97706),
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                SettingToggleRow(
                    icon = Icons.Default.Grade,
                    iconTint = Color(0xFFB45309),
                    title = "Grade Releases & Marks",
                    description = "Instant alerts when exam controller enters test scores",
                    checked = gradesEnabled,
                    onCheckedChange = { gradesEnabled = it }
                )

                SettingToggleRow(
                    icon = Icons.Default.Assignment,
                    iconTint = Color(0xFF0F5132),
                    title = "New Homework & Assignments",
                    description = "Notifications when teachers assign new coursework",
                    checked = assignmentsEnabled,
                    onCheckedChange = { assignmentsEnabled = it }
                )

                SettingToggleRow(
                    icon = Icons.Default.Campaign,
                    iconTint = Color(0xFF1E3A8A),
                    title = "College Announcements",
                    description = "Official circulars, exam dates, and weather advisories",
                    checked = announcementsEnabled,
                    onCheckedChange = { announcementsEnabled = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Delivery toggles
                Text(
                    text = "DELIVERY & SYSTEM PREFERENCES",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F5132),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                SettingToggleRow(
                    icon = Icons.Default.Notifications,
                    iconTint = Color(0xFF4F46E5),
                    title = "System Status Bar Notifications",
                    description = "Show heads-up banners on your Android device tray",
                    checked = systemTrayAlerts,
                    onCheckedChange = { systemTrayAlerts = it }
                )

                SettingToggleRow(
                    icon = Icons.Default.VolumeUp,
                    iconTint = Color(0xFF059669),
                    title = "Sound & Vibration Alerts",
                    description = "Audible chime and haptics for urgent academic events",
                    checked = soundAndVibrate,
                    onCheckedChange = { soundAndVibrate = it }
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Save button
                Button(
                    onClick = {
                        val newPreferences = NotificationPreferences(
                            assignmentsEnabled = assignmentsEnabled,
                            deadlinesEnabled = deadlinesEnabled,
                            gradesEnabled = gradesEnabled,
                            announcementsEnabled = announcementsEnabled,
                            systemTrayAlerts = systemTrayAlerts,
                            soundAndVibrate = soundAndVibrate,
                            deadlineLeadHours = deadlineLeadHours
                        )
                        viewModel.updatePreferences(newPreferences)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("save_notification_prefs_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0F5132)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save Notification Preferences", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun SettingToggleRow(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = iconTint.copy(alpha = 0.12f),
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier
                    .padding(6.dp)
                    .size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF111827)
            )
            Text(
                text = description,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF6B7280)
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF0F5132)
            ),
            modifier = Modifier.testTag("switch_${title.replace(" ", "_").lowercase()}")
        )
    }
}
