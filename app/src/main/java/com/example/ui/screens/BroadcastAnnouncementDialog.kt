package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.CollegeAnnouncement
import com.example.ui.viewmodel.SchoolViewModel

@Composable
fun BroadcastAnnouncementDialog(
    viewModel: SchoolViewModel,
    existingAnnouncement: CollegeAnnouncement? = null,
    onDismiss: () -> Unit
) {
    val isEditing = existingAnnouncement != null
    var title by remember(existingAnnouncement?.id) { mutableStateOf(existingAnnouncement?.title ?: "") }
    var content by remember(existingAnnouncement?.id) { mutableStateOf(existingAnnouncement?.content ?: "") }
    var category by remember(existingAnnouncement?.id) { mutableStateOf(existingAnnouncement?.category ?: "Academic") }
    var targetAudience by remember(existingAnnouncement?.id) { mutableStateOf(existingAnnouncement?.targetAudience ?: "All Students") }
    var author by remember(existingAnnouncement?.id) { mutableStateOf(existingAnnouncement?.author ?: "Principal Office & Administration") }
    var isUrgent by remember(existingAnnouncement?.id) { mutableStateOf(existingAnnouncement?.isUrgent ?: false) }
    var isPinned by remember(existingAnnouncement?.id) { mutableStateOf(existingAnnouncement?.isPinned ?: true) }
    var isPublished by remember(existingAnnouncement?.id) { mutableStateOf(existingAnnouncement?.isPublished ?: true) }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val categories = listOf("Academic", "Examinations", "Campus Events", "Weather / Winter Advisory", "Sports")
    val audiences = listOf("All Students", "12th Class", "11th Class", "10th Class", "9th Class", "8th Class")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .testTag("broadcast_announcement_dialog"),
            color = MaterialTheme.colorScheme.surface,
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
                            color = Color(0xFF1E3A8A).copy(alpha = 0.12f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Campaign,
                                contentDescription = null,
                                tint = Color(0xFF1E3A8A),
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isEditing) "Edit Official Notice" else "Broadcast Announcement",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isEditing) "Update existing circular without duplicating" else "Dispatches instant alert to student devices",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Title
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it; errorMessage = null },
                    label = { Text("Announcement Title *") },
                    placeholder = { Text("e.g. Winter Pre-Board Exam Timetable") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("announcement_title_input"),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Category chips
                Text(
                    text = "CATEGORY",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.take(3).forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF1E3A8A),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.drop(3).forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF1E3A8A),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                // Target Audience
                Text(
                    text = "TARGET AUDIENCE",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    audiences.take(3).forEach { aud ->
                        FilterChip(
                            selected = targetAudience == aud,
                            onClick = { targetAudience = aud },
                            label = { Text(aud, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF0F5132),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    audiences.drop(3).forEach { aud ->
                        FilterChip(
                            selected = targetAudience == aud,
                            onClick = { targetAudience = aud },
                            label = { Text(aud, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF0F5132),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                // Author
                OutlinedTextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("Issuing Authority") },
                    placeholder = { Text("e.g. Principal Office, Examination Controller") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("announcement_author_input"),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Content
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it; errorMessage = null },
                    label = { Text("Notice / Circular Content *") },
                    placeholder = { Text("Write official circular details, dates, and instructions...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .testTag("announcement_content_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Switches
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Mark as Urgent Alert", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Text("Triggers priority sound & vibration", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Switch(
                        checked = isUrgent,
                        onCheckedChange = { isUrgent = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFFDC2626)
                        )
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Pin to Top of Dashboard", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Text("Featured banner for students", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Switch(
                        checked = isPinned,
                        onCheckedChange = { isPinned = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF0F5132)
                        )
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Published to Students", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Text("Visible in student notices feed", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Switch(checked = isPublished, onCheckedChange = { isPublished = it })
                }

                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        color = Color(0xFFDC2626),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            if (title.isBlank()) {
                                errorMessage = "Please enter an announcement title."
                                return@Button
                            }
                            if (content.isBlank()) {
                                errorMessage = "Please enter notice content."
                                return@Button
                            }

                            if (existingAnnouncement == null) {
                                viewModel.broadcastAnnouncement(
                                    title = title.trim(),
                                    content = content.trim(),
                                    category = category,
                                    targetAudience = targetAudience,
                                    author = author.trim(),
                                    isUrgent = isUrgent,
                                    isPinned = isPinned
                                )
                            } else {
                                viewModel.updateAnnouncement(
                                    existingAnnouncement.copy(
                                        title = title.trim(),
                                        content = content.trim(),
                                        category = category,
                                        targetAudience = targetAudience,
                                        author = author.trim().ifBlank { existingAnnouncement.author },
                                        isUrgent = isUrgent,
                                        isPinned = isPinned,
                                        isPublished = isPublished
                                    )
                                )
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("broadcast_now_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isUrgent) Color(0xFFDC2626) else Color(0xFF1E3A8A)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (isEditing) "Update Notice" else "Broadcast Alert Now", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

