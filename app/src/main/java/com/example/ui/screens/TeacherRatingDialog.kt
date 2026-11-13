package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.data.model.TeacherProfileRecord
import com.example.data.model.TeacherRating
import com.example.ui.viewmodel.DeleteTarget
import com.example.ui.viewmodel.SchoolViewModel
import com.example.util.MediaStorageHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TeacherRatingDialog(
    teacher: TeacherProfileRecord,
    viewModel: SchoolViewModel,
    onDismiss: () -> Unit
) {
    val allRatings by viewModel.allTeacherRatings.collectAsState()
    val isAdmin by viewModel.isAdminLoggedIn.collectAsState()

    val teacherRatings = remember(allRatings, teacher.id, isAdmin) {
        allRatings.filter { it.teacherId == teacher.id && (isAdmin || it.isApproved) }
    }
    val approvedRatings = remember(allRatings, teacher.id) {
        allRatings.filter { it.teacherId == teacher.id && it.isApproved }
    }
    val averageRating = if (approvedRatings.isNotEmpty()) {
        approvedRatings.map { it.rating }.average()
    } else 0.0

    var selectedStars by remember(teacher.id) { mutableIntStateOf(5) }
    var studentName by remember(teacher.id) { mutableStateOf("") }
    var studentRoll by remember(teacher.id) { mutableStateOf("") }
    var subjectName by remember(teacher.id) { mutableStateOf(teacher.assignedSubjects.split(",").firstOrNull()?.trim() ?: "") }
    var feedback by remember(teacher.id) { mutableStateOf("") }
    var statusMessage by remember(teacher.id) { mutableStateOf<Pair<Boolean, String>?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 640.dp),
            shape = RoundedCornerShape(22.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val resolvedPhoto = remember(teacher.photoUri) {
                        MediaStorageHelper.resolvePhotoModel(teacher.photoUri)
                    }
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.14f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        if (resolvedPhoto != null) {
                            AsyncImage(
                                model = resolvedPhoto,
                                contentDescription = teacher.name,
                                modifier = Modifier.fillMaxSize().clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = teacher.name,
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = teacher.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = teacher.designation,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.tertiary,
                            fontWeight = FontWeight.Bold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = if (approvedRatings.isNotEmpty()) {
                                    "${String.format(Locale.US, "%.1f", averageRating)} (${approvedRatings.size} review${if (approvedRatings.size == 1) "" else "s"})"
                                } else {
                                    "No reviews yet"
                                },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(48.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    color = Color(0xFFF8FAFC),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Text(
                        text = "Student Feedback Portal: Teacher ratings reflect classroom teaching feedback only and remain strictly separate from official examination results.",
                        fontSize = 10.sp,
                        color = Color(0xFF475569),
                        lineHeight = 14.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Submit Rating Form
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.RateReview, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Rate This Teacher (1–5 Stars)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            // Interactive Stars Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                (1..5).forEach { star ->
                                    IconButton(
                                        onClick = { selectedStars = star },
                                        modifier = Modifier.size(48.dp).testTag("rate_star_$star")
                                    ) {
                                        Icon(
                                            imageVector = if (star <= selectedStars) Icons.Default.Star else Icons.Outlined.StarOutline,
                                            contentDescription = "$star star",
                                            tint = if (star <= selectedStars) Color(0xFFF59E0B) else Color(0xFF94A3B8),
                                            modifier = Modifier.size(30.dp)
                                        )
                                    }
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = studentName,
                                    onValueChange = { studentName = it },
                                    label = { Text("Your Name") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f).testTag("rating_student_name")
                                )
                                OutlinedTextField(
                                    value = studentRoll,
                                    onValueChange = { studentRoll = it },
                                    label = { Text("Roll No. *") },
                                    placeholder = { Text("BHSS-...") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f).testTag("rating_roll_no")
                                )
                            }

                            OutlinedTextField(
                                value = subjectName,
                                onValueChange = { subjectName = it },
                                label = { Text("Subject Taught") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = feedback,
                                onValueChange = { feedback = it },
                                label = { Text("Written Feedback (Optional)") },
                                placeholder = { Text("Share constructive feedback on teaching clarity and support...") },
                                maxLines = 3,
                                modifier = Modifier.fillMaxWidth().testTag("rating_feedback_input")
                            )

                            statusMessage?.let { (ok, msg) ->
                                Surface(
                                    color = if (ok) Color(0xFFDCFCE7) else Color(0xFFFEE2E2),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = if (ok) Color(0xFF15803D) else Color(0xFFB91C1C),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = msg,
                                            fontSize = 11.sp,
                                            color = if (ok) Color(0xFF166534) else Color(0xFF991B1B)
                                        )
                                    }
                                }
                            }

                            Button(
                                onClick = {
                                    viewModel.submitTeacherRating(
                                        teacher = teacher,
                                        studentName = studentName,
                                        studentRollNumber = studentRoll,
                                        rating = selectedStars,
                                        feedback = feedback,
                                        subjectName = subjectName
                                    ) { success, message ->
                                        statusMessage = success to message
                                        if (success) {
                                            feedback = ""
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("submit_teacher_rating_btn")
                            ) {
                                Text("Submit Rating & Feedback", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    HorizontalDivider()

                    // Reviews List
                    Text(
                        text = if (isAdmin) "All Reviews & Moderation (${teacherRatings.size})" else "Student Reviews (${teacherRatings.size})",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    if (teacherRatings.isEmpty()) {
                        Text(
                            text = "No student reviews posted for ${teacher.name} yet. Be the first to rate!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        teacherRatings.forEach { review ->
                            TeacherReviewCard(
                                review = review,
                                isAdmin = isAdmin,
                                onToggleApprove = { viewModel.moderateTeacherRating(review, !review.isApproved) },
                                onDelete = { viewModel.requestDelete(DeleteTarget.Rating(review)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TeacherReviewCard(
    review: TeacherRating,
    isAdmin: Boolean,
    onToggleApprove: () -> Unit,
    onDelete: () -> Unit
) {
    val dateStr = remember(review.timestamp) {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(review.timestamp))
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (review.isApproved) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else Color(0xFFFEF2F2),
        border = BorderStroke(1.dp, if (review.isApproved) Color(0xFFE2E8F0) else Color(0xFFFCA5A5))
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = review.studentName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${review.subjectName.ifBlank { "General" }} • $dateStr",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    (1..5).forEach { s ->
                        Icon(
                            imageVector = if (s <= review.rating) Icons.Default.Star else Icons.Outlined.StarOutline,
                            contentDescription = null,
                            tint = if (s <= review.rating) Color(0xFFF59E0B) else Color(0xFFCBD5E1),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            if (review.feedback.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = review.feedback,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 17.sp
                )
            }

            if (isAdmin) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = if (review.isApproved) Color(0xFFDCFCE7) else Color(0xFFFEE2E2),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = if (review.isApproved) "APPROVED (${review.studentRollNumber})" else "FLAGGED / HIDDEN (${review.studentRollNumber})",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (review.isApproved) Color(0xFF15803D) else Color(0xFFB91C1C),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Row {
                        IconButton(onClick = onToggleApprove, modifier = Modifier.size(48.dp)) {
                            Icon(
                                imageVector = if (review.isApproved) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (review.isApproved) "Hide Review" else "Approve Review",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        IconButton(onClick = onDelete, modifier = Modifier.size(48.dp)) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Review",
                                tint = Color(0xFFDC2626),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
