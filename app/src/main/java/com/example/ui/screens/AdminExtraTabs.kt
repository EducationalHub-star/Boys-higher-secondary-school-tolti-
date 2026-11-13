package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.PublicOff
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.designButtonShape
import com.example.ui.theme.designCardBorder
import com.example.ui.theme.designCardColors
import com.example.ui.theme.designCardShape
import com.example.ui.theme.LocalAppDesignConfig
import com.example.ui.viewmodel.DeleteTarget
import com.example.ui.viewmodel.SchoolViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminClassesSubjectsTab(viewModel: SchoolViewModel) {
    val subjects by viewModel.allClassSubjects.collectAsState()
    val designConfig = LocalAppDesignConfig.current
    var searchQuery by remember { mutableStateOf("") }
    var classFilter by remember { mutableStateOf("ALL") }

    val filtered = remember(subjects, searchQuery, classFilter) {
        subjects.filter { item ->
            val matchCls = classFilter == "ALL" || item.classLevel == classFilter
            val matchQuery = searchQuery.isBlank() ||
                item.subjectName.contains(searchQuery, true) ||
                item.subjectCode.contains(searchQuery, true) ||
                item.teacherName.contains(searchQuery, true)
            matchCls && matchQuery
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Classes & Subjects Curriculum", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = DeepNavy)
                Text("${filtered.size} subject(s) configured (5th–12th)", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
            }
            Button(
                onClick = {
                    viewModel.editingClassSubject.value = null
                    viewModel.showAddClassSubjectDialog.value = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                shape = designButtonShape(designConfig),
                modifier = Modifier.testTag("admin_add_subject_btn")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Subject", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search subject, code or teacher...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(viewModel.classOptions) { cls ->
                FilterChip(
                    selected = classFilter == cls,
                    onClick = { classFilter = cls },
                    label = { Text(if (cls == "ALL") "All" else "Class $cls", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = RoyalBlue, selectedLabelColor = Color.White)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (filtered.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No subjects found for this filter.", color = Color(0xFF64748B))
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filtered, key = { it.id }) { item ->
                    Card(
                        shape = designCardShape(designConfig),
                        colors = designCardColors(designConfig),
                        border = designCardBorder(designConfig),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(item.subjectName, fontWeight = FontWeight.Bold, color = DeepNavy)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(color = Color(0xFFEFF6FF), shape = RoundedCornerShape(4.dp)) {
                                        Text(
                                            text = "Class ${item.classLevel}",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = RoyalBlue,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "${item.subjectCode} • Teacher: ${item.teacherName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF475569)
                                )
                                Text(
                                    text = "Max: ${item.maxMarks} | Pass: ${item.passMarks} • ${if (item.isCompulsory) "Compulsory" else "Elective"}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF64748B)
                                )
                            }
                            IconButton(
                                onClick = {
                                    viewModel.editingClassSubject.value = item
                                    viewModel.showAddClassSubjectDialog.value = true
                                }
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit Subject", tint = RoyalBlue, modifier = Modifier.size(18.dp))
                            }
                            IconButton(
                                onClick = { viewModel.requestDelete(DeleteTarget.ClassSubject(item)) }
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete Subject", tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminNoticesTab(viewModel: SchoolViewModel) {
    val notices by viewModel.allAnnouncements.collectAsState()
    val designConfig = LocalAppDesignConfig.current
    var searchQuery by remember { mutableStateOf("") }

    val filtered = remember(notices, searchQuery) {
        notices.filter { ann ->
            searchQuery.isBlank() ||
                ann.title.contains(searchQuery, true) ||
                ann.content.contains(searchQuery, true) ||
                ann.category.contains(searchQuery, true)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Notices & Circulars", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = DeepNavy)
                Text("${filtered.size} notice(s) in archive", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
            }
            Button(
                onClick = {
                    viewModel.editingAnnouncement.value = null
                    viewModel.showBroadcastAnnouncementDialog.value = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                shape = designButtonShape(designConfig),
                modifier = Modifier.testTag("admin_tab_add_notice_btn")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("New Notice", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search notices...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filtered, key = { it.id }) { ann ->
                Card(
                    shape = designCardShape(designConfig),
                    colors = designCardColors(designConfig),
                    border = designCardBorder(designConfig),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(ann.title, fontWeight = FontWeight.Bold, color = DeepNavy)
                            Text("${ann.category} • ${ann.author}", style = MaterialTheme.typography.labelSmall, color = RoyalBlue)
                            Text(ann.content, style = MaterialTheme.typography.bodySmall, color = Color(0xFF475569), maxLines = 2)
                            Text(
                                text = if (ann.isPublished) "Published" else "Hidden / Draft",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (ann.isPublished) Color(0xFF16A34A) else Color(0xFFDC2626)
                            )
                        }
                        IconButton(
                            onClick = {
                                viewModel.editingAnnouncement.value = ann
                                viewModel.showBroadcastAnnouncementDialog.value = true
                            }
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Notice", tint = RoyalBlue, modifier = Modifier.size(18.dp))
                        }
                        IconButton(onClick = { viewModel.toggleAnnouncementPublish(ann) }) {
                            Icon(
                                imageVector = if (ann.isPublished) Icons.Default.Public else Icons.Default.PublicOff,
                                contentDescription = "Toggle Publish",
                                tint = if (ann.isPublished) Color(0xFF16A34A) else Color(0xFF94A3B8),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        IconButton(onClick = { viewModel.requestDelete(DeleteTarget.Announcement(ann)) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Notice", tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminHomeworkTab(viewModel: SchoolViewModel) {
    val assignments by viewModel.allAssignments.collectAsState()
    val designConfig = LocalAppDesignConfig.current
    var searchQuery by remember { mutableStateOf("") }
    var classFilter by remember { mutableStateOf("ALL") }

    val filtered = remember(assignments, searchQuery, classFilter) {
        assignments.filter { asg ->
            val matchCls = classFilter == "ALL" || asg.classLevel == classFilter
            val matchQuery = searchQuery.isBlank() ||
                asg.title.contains(searchQuery, true) ||
                asg.subject.contains(searchQuery, true) ||
                asg.teacherName.contains(searchQuery, true)
            matchCls && matchQuery
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Homework & Assignments", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = DeepNavy)
                Text("${filtered.size} assignment(s)", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
            }
            Button(
                onClick = {
                    viewModel.editingAssignment.value = null
                    viewModel.showAddAssignmentDialog.value = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                shape = designButtonShape(designConfig),
                modifier = Modifier.testTag("admin_tab_add_homework_btn")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Assign", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search homework by title, subject, teacher...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(viewModel.classOptions) { cls ->
                FilterChip(
                    selected = classFilter == cls,
                    onClick = { classFilter = cls },
                    label = { Text(if (cls == "ALL") "All" else "Class $cls", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = RoyalBlue, selectedLabelColor = Color.White)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filtered, key = { it.id }) { asg ->
                Card(
                    shape = designCardShape(designConfig),
                    colors = designCardColors(designConfig),
                    border = designCardBorder(designConfig),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(asg.title, fontWeight = FontWeight.Bold, color = DeepNavy)
                            Text("Class ${asg.classLevel} • ${asg.subject} (${asg.courseCode})", style = MaterialTheme.typography.labelSmall, color = RoyalBlue)
                            Text("Due: ${asg.dueDateFormatted} • ${asg.teacherName}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
                        }
                        IconButton(
                            onClick = {
                                viewModel.editingAssignment.value = asg
                                viewModel.showAddAssignmentDialog.value = true
                            }
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Homework", tint = RoyalBlue, modifier = Modifier.size(18.dp))
                        }
                        IconButton(onClick = { viewModel.toggleAssignmentPublish(asg) }) {
                            Icon(
                                imageVector = if (asg.isPublished) Icons.Default.Public else Icons.Default.PublicOff,
                                contentDescription = "Toggle Publish",
                                tint = if (asg.isPublished) Color(0xFF16A34A) else Color(0xFF94A3B8),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        IconButton(onClick = { viewModel.requestDelete(DeleteTarget.Assignment(asg)) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Homework", tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminRatingsModerationTab(viewModel: SchoolViewModel) {
    val ratings by viewModel.allTeacherRatings.collectAsState()
    val designConfig = LocalAppDesignConfig.current
    var filterMode by remember { mutableStateOf("ALL") } // ALL, APPROVED, FLAGGED

    val filtered = remember(ratings, filterMode) {
        when (filterMode) {
            "APPROVED" -> ratings.filter { it.isApproved }
            "FLAGGED" -> ratings.filter { !it.isApproved }
            else -> ratings
        }
    }

    val avgScore = remember(ratings) {
        val approved = ratings.filter { it.isApproved }
        if (approved.isEmpty()) 0.0 else approved.map { it.rating }.average()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Teacher Ratings & Feedback Moderation", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = DeepNavy)
        Text(
            text = "Separate from academic marks • Avg: ${String.format(Locale.US, "%.1f", avgScore)}★ across ${ratings.size} review(s)",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF64748B)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("ALL" to "All (${ratings.size})", "APPROVED" to "Approved", "FLAGGED" to "Flagged/Hidden").forEach { (key, label) ->
                FilterChip(
                    selected = filterMode == key,
                    onClick = { filterMode = key },
                    label = { Text(label, fontSize = 11.5.sp) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = RoyalBlue, selectedLabelColor = Color.White)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (filtered.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No teacher feedback matching this filter.", color = Color(0xFF64748B))
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filtered, key = { it.id }) { rating ->
                    val dateStr = remember(rating.timestamp) {
                        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(rating.timestamp))
                    }
                    Card(
                        shape = designCardShape(designConfig),
                        colors = designCardColors(designConfig),
                        border = designCardBorder(designConfig),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(rating.teacherName, fontWeight = FontWeight.Bold, color = DeepNavy)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(14.dp))
                                    Text("${rating.rating}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFFB45309))
                                }
                                Text(
                                    text = "By ${rating.studentName} (${rating.studentRollNumber}) • $dateStr",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = RoyalBlue
                                )
                                if (rating.feedback.isNotBlank()) {
                                    Text(rating.feedback, style = MaterialTheme.typography.bodySmall, color = Color(0xFF475569))
                                }
                                Text(
                                    text = if (rating.isApproved) "APPROVED" else "FLAGGED / HIDDEN",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (rating.isApproved) Color(0xFF16A34A) else Color(0xFFDC2626)
                                )
                            }
                            IconButton(onClick = { viewModel.moderateTeacherRating(rating, !rating.isApproved) }) {
                                Icon(
                                    imageVector = if (rating.isApproved) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (rating.isApproved) "Hide Review" else "Approve Review",
                                    tint = RoyalBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            IconButton(onClick = { viewModel.requestDelete(DeleteTarget.Rating(rating)) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete Review", tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
