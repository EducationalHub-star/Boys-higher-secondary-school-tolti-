package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.TeacherProfileRecord
import com.example.ui.theme.LocalAppDesignConfig
import com.example.ui.theme.designButtonShape
import com.example.ui.theme.designCardBorder
import com.example.ui.theme.designCardColors
import com.example.ui.theme.designCardShape
import com.example.ui.viewmodel.DeleteTarget
import com.example.ui.viewmodel.SchoolViewModel
import com.example.util.MediaStorageHelper
import com.example.util.UrduLocalization
import java.util.Locale

@Composable
fun PublicTeachersScreen(
    viewModel: SchoolViewModel,
    contentPadding: PaddingValues
) {
    val language by viewModel.currentLanguage.collectAsState()
    val isUrdu = language == UrduLocalization.Language.URDU
    val teachers by viewModel.allTeachers.collectAsState()
    val allRatings by viewModel.allTeacherRatings.collectAsState()
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

    val ratingsByTeacher = remember(allRatings) {
        allRatings.filter { it.isApproved }.groupBy { it.teacherId }
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
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = if (isUrdu) "بوائز ہائر سیکنڈری اسکول تولتی کے قابل ترین اساتذہ" else "Dedicated teachers and subject specialists of BHSS Tolti — tap Rate & Reviews to give feedback",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }
            }

            // Faculty Cards List
            items(filtered, key = { it.id }) { teacher ->
                val tRatings = ratingsByTeacher[teacher.id].orEmpty()
                val avg = if (tRatings.isNotEmpty()) tRatings.map { it.rating }.average() else 0.0

                TeacherCardItem(
                    teacher = teacher,
                    averageRating = avg,
                    reviewCount = tRatings.size,
                    isAdmin = isAdmin,
                    onRateClick = {
                        viewModel.teacherForRating.value = teacher
                        viewModel.showRateTeacherDialog.value = true
                    },
                    onEdit = {
                        viewModel.editingTeacher.value = teacher
                        viewModel.showAddTeacherDialog.value = true
                    },
                    onDelete = { viewModel.requestDelete(DeleteTarget.Teacher(teacher)) }
                )
            }
        }

        if (isAdmin) {
            FloatingActionButton(
                onClick = {
                    viewModel.editingTeacher.value = null
                    viewModel.showAddTeacherDialog.value = true
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(contentPadding)
                    .padding(16.dp)
                    .testTag("admin_add_teacher_fab"),
                containerColor = MaterialTheme.colorScheme.primary,
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
    averageRating: Double,
    reviewCount: Int,
    isAdmin: Boolean,
    onRateClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val designConfig = LocalAppDesignConfig.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("teacher_card_${teacher.id}"),
        shape = designCardShape(designConfig),
        colors = designCardColors(designConfig),
        border = designCardBorder(designConfig),
        elevation = CardDefaults.cardElevation(defaultElevation = if (designConfig.cardStyle == "OUTLINED") 0.dp else 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val resolvedPhoto = remember(teacher.photoUri) {
                    MediaStorageHelper.resolvePhotoModel(teacher.photoUri)
                }
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.12f),
                    modifier = Modifier.size(52.dp)
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
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

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
                        Text(
                            text = "${teacher.qualification} • Joined ${teacher.joiningYear}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 10.sp
                        )
                    }
                }

                // Average Star Rating Badge
                Surface(
                    color = Color(0xFFFEF3C7),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = if (reviewCount > 0) "${String.format(Locale.US, "%.1f", averageRating)} ($reviewCount)" else "New",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row {
                        Text("Subjects: ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Text(teacher.assignedSubjects, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Row(modifier = Modifier.padding(top = 2.dp)) {
                        Text("Classes: ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Text(teacher.assignedClasses, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = teacher.introduction,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(teacher.email, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(teacher.phone, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onRateClick,
                    shape = designButtonShape(designConfig),
                    modifier = Modifier.height(48.dp).testTag("rate_teacher_btn_${teacher.id}")
                ) {
                    Icon(Icons.Default.RateReview, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Rate & Reviews ($reviewCount)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                if (isAdmin) {
                    Row {
                        IconButton(
                            onClick = onEdit,
                            modifier = Modifier.size(48.dp).testTag("edit_teacher_btn_${teacher.id}")
                        ) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Teacher", tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(18.dp))
                        }
                        IconButton(
                            onClick = onDelete,
                            modifier = Modifier.size(48.dp).testTag("delete_teacher_btn_${teacher.id}")
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Teacher", tint = Color(0xFFDC2626), modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

