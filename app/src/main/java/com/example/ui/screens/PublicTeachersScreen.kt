package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TeacherProfileRecord
import com.example.ui.viewmodel.SchoolViewModel
import com.example.util.UrduLocalization

@Composable
fun PublicTeachersScreen(
    viewModel: SchoolViewModel,
    contentPadding: PaddingValues
) {
    val language by viewModel.currentLanguage.collectAsState()
    val isUrdu = language == UrduLocalization.Language.URDU
    val teachers by viewModel.allTeachers.collectAsState()
    val selectedDept by viewModel.teacherDeptFilter.collectAsState()
    val searchQuery by viewModel.teacherSearchQuery.collectAsState()
    val isAdmin by viewModel.isAdminLoggedIn.collectAsState()

    val departments = listOf(
        "ALL",
        "Computer Science & Administration",
        "Physics & Applied Sciences",
        "Mathematics",
        "Humanities & Languages",
        "Social Sciences",
        "Life Sciences",
        "Chemical Sciences",
        "General Science & Elementary Wing",
        "Sports & Co-curricular"
    )

    val filtered = teachers.filter { tch ->
        val matchesDept = if (selectedDept == "ALL") true else tch.department == selectedDept
        val matchesQuery = searchQuery.isBlank() ||
                tch.name.contains(searchQuery, ignoreCase = true) ||
                tch.designation.contains(searchQuery, ignoreCase = true) ||
                tch.assignedSubjects.contains(searchQuery, ignoreCase = true)
        matchesDept && matchesQuery
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("public_teachers_screen"),
            contentPadding = PaddingValues(
                top = contentPadding.calculateTopPadding() + 8.dp,
                bottom = contentPadding.calculateBottomPadding() + 80.dp,
                start = 14.dp,
                end = 14.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            item {
                Column {
                    Text(
                        text = if (isUrdu) "اساتذہ کرام کی ڈائریکٹری" else "Distinguished Faculty Directory",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0B192C)
                    )
                    Text(
                        text = if (isUrdu) "بوائز ہائر سیکنڈری اسکول تولتی کے قابل ترین اساتذہ" else "Dedicated teachers and subject specialists of BHSS Tolti",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF64748B)
                    )
                }
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.teacherSearchQuery.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("teacher_search_input"),
                    placeholder = { Text(if (isUrdu) "استاد کا نام یا مضمون تلاش کریں..." else "Search by teacher name, designation, or subject...", fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(20.dp))
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.teacherSearchQuery.value = "" }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = Color(0xFF64748B), modifier = Modifier.size(18.dp))
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            // Department Filters
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(departments) { dept ->
                        val isSelected = selectedDept == dept
                        val label = if (dept == "ALL") "All Departments" else dept.split("&").first().trim()
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.teacherDeptFilter.value = dept },
                            label = { Text(label, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF0B192C),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // Faculty Cards List
            items(filtered, key = { it.id }) { teacher ->
                TeacherCardItem(
                    teacher = teacher,
                    isAdmin = isAdmin,
                    onDelete = { viewModel.deleteTeacher(teacher) }
                )
            }
        }

        if (isAdmin) {
            FloatingActionButton(
                onClick = { viewModel.showAddTeacherDialog.value = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(contentPadding)
                    .padding(16.dp)
                    .testTag("admin_add_teacher_fab"),
                containerColor = Color(0xFF0B192C),
                contentColor = Color(0xFFFBBF24)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Teacher")
            }
        }
    }
}

@Composable
private fun TeacherCardItem(
    teacher: TeacherProfileRecord,
    isAdmin: Boolean,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("teacher_card_${teacher.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF1D4ED8).copy(alpha = 0.12f),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color(0xFF1D4ED8),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = teacher.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0B192C)
                    )
                    Text(
                        text = teacher.designation,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFD97706),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${teacher.qualification} • Joined ${teacher.joiningYear}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF64748B),
                        fontSize = 10.sp
                    )
                }

                if (isAdmin) {
                    IconButton(onClick = onDelete) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFF94A3B8), modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFF8FAFC),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row {
                        Text("Subjects: ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0B192C))
                        Text(teacher.assignedSubjects, fontSize = 11.sp, color = Color(0xFF334155))
                    }
                    Row(modifier = Modifier.padding(top = 2.dp)) {
                        Text("Classes: ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0B192C))
                        Text(teacher.assignedClasses, fontSize = 11.sp, color = Color(0xFF334155))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = teacher.introduction,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF475569),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(teacher.email, fontSize = 10.sp, color = Color(0xFF64748B))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(teacher.phone, fontSize = 10.sp, color = Color(0xFF64748B))
                }
            }
        }
    }
}
