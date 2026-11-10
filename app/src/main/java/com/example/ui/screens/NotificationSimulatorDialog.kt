package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.viewmodel.SchoolViewModel

@Composable
fun NotificationSimulatorDialog(
    viewModel: SchoolViewModel,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .testTag("notification_simulator_dialog"),
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
                            color = Color(0xFF4F46E5).copy(alpha = 0.12f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = Color(0xFF4F46E5),
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Real-Time Alert Simulator",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Test instant heads-up banners & system tray alerts",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF6B7280)
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color(0xFF6B7280)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Tap any preset below to trigger an immediate real-time alert and observe the floating in-app banner and Android notification sound/vibration:",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF4B5563),
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Presets
                SimulatorPresetItem(
                    icon = Icons.Default.HourglassTop,
                    iconColor = Color(0xFFDC2626),
                    title = "Urgent Approaching Deadline",
                    subtitle = "C++ OOP Lab Project due tonight at 11:59 PM (High Urgency)",
                    onClick = {
                        viewModel.simulateAlert("DEADLINE_URGENT")
                        onDismiss()
                    },
                    testTag = "sim_deadline_btn"
                )

                Spacer(modifier = Modifier.height(10.dp))

                SimulatorPresetItem(
                    icon = Icons.Default.Grade,
                    iconColor = Color(0xFFB45309),
                    title = "New Grade Publication",
                    subtitle = "Physics Practical exam result: 48/50 (Grade A+) recorded",
                    onClick = {
                        viewModel.simulateAlert("GRADE_EXAM")
                        onDismiss()
                    },
                    testTag = "sim_grade_btn"
                )

                Spacer(modifier = Modifier.height(10.dp))

                SimulatorPresetItem(
                    icon = Icons.Default.Assignment,
                    iconColor = Color(0xFF0F5132),
                    title = "New Assignment Added",
                    subtitle = "Sir Zakir Hussain posted 'Karakoram Alpine Flora Survey'",
                    onClick = {
                        viewModel.simulateAlert("NEW_HOMEWORK")
                        onDismiss()
                    },
                    testTag = "sim_assignment_btn"
                )

                Spacer(modifier = Modifier.height(10.dp))

                SimulatorPresetItem(
                    icon = Icons.Default.Campaign,
                    iconColor = Color(0xFF1E3A8A),
                    title = "General College Announcement",
                    subtitle = "Principal Office: Winter Snowfall & Lab Timing Adjustment",
                    onClick = {
                        viewModel.simulateAlert("CAMPUS_NOTICE")
                        onDismiss()
                    },
                    testTag = "sim_announcement_btn"
                )

                Spacer(modifier = Modifier.height(10.dp))

                SimulatorPresetItem(
                    icon = Icons.Default.Refresh,
                    iconColor = Color(0xFF0D9488),
                    title = "Scan All Deadlines Now",
                    subtitle = "Checks all assignments in database due within 24 hours",
                    onClick = {
                        viewModel.checkUpcomingDeadlines(leadHours = 24, manualTrigger = true)
                        onDismiss()
                    },
                    testTag = "sim_scan_deadlines_btn"
                )
            }
        }
    }
}

@Composable
private fun SimulatorPresetItem(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag(testTag),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF9FAFB)
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = iconColor.copy(alpha = 0.12f),
                modifier = Modifier.size(38.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B7280),
                    fontSize = 12.sp
                )
            }

            Text(
                text = "Trigger",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = iconColor,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}
