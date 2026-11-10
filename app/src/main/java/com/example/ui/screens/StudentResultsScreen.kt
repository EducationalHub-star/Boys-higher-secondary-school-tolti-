package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.PublicOff
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.data.model.StudentResults
import com.example.data.model.SubjectScoreItem
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.ElegantGold
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.StatusFailRed
import com.example.ui.theme.StatusPassGreen
import com.example.ui.viewmodel.SchoolViewModel
import com.example.util.UrduLocalization

/**
 * Screen displaying Subject-Wise Results and Marksheets for Classes 8th through 12th.
 */
@Composable
fun StudentResultsScreen(
    viewModel: SchoolViewModel,
    contentPadding: PaddingValues
) {
    val language by viewModel.currentLanguage.collectAsState()
    val isUrdu = language == UrduLocalization.Language.URDU
    val subjectResults by viewModel.filteredSubjectResults.collectAsState()
    val selectedClass by viewModel.subjectResultsClassFilter.collectAsState()
    val searchQuery by viewModel.subjectResultsSearchQuery.collectAsState()
    val isAdmin by viewModel.isAdminLoggedIn.collectAsState()
    val selectedCard by viewModel.selectedSubjectResultCard.collectAsState()
    val showAddDialog by viewModel.showAddSubjectResultDialog.collectAsState()

    // Aggregate stats calculation
    val totalCount = subjectResults.size
    val passingCount = subjectResults.count { it.status == "PASS" }
    val passRate = if (totalCount > 0) (passingCount.toDouble() / totalCount * 100).toInt() else 100
    val avgPercentage = if (totalCount > 0) {
        val totalPct = subjectResults.sumOf { it.percentage }
        (totalPct / totalCount)
    } else 0.0
    val topScore = subjectResults.maxOfOrNull { it.percentage } ?: 0.0

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("student_results_screen"),
            contentPadding = PaddingValues(
                top = contentPadding.calculateTopPadding() + 8.dp,
                bottom = contentPadding.calculateBottomPadding() + 84.dp,
                start = 14.dp,
                end = 14.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Header Banner
            item {
                AcademicBannerCard(isUrdu = isUrdu, isAdmin = isAdmin)
            }

            // 2. Performance Summary Metric Cards
            item {
                PerformanceSummaryRow(
                    totalStudents = totalCount,
                    passRate = passRate,
                    avgPercentage = avgPercentage,
                    topScore = topScore,
                    isUrdu = isUrdu
                )
            }

            // 3. Search & Class Filter Row (8th, 9th, 10th, 11th, 12th)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.subjectResultsSearchQuery.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("subject_results_search_input"),
                        placeholder = {
                            Text(
                                if (isUrdu) "رول نمبر، طالب علم یا والد کا نام تلاش کریں..."
                                else "Search by Roll Number, Student Name, or Father...",
                                fontSize = 13.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.subjectResultsSearchQuery.value = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = Color(0xFF64748B),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    // Class wing selector chips specifically for 8th through 12th
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isUrdu) "جماعت فلٹر (ہشتم تا دوازدہم):" else "Classes 8th to 12th Wing:",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy
                            )
                            Text(
                                text = "${subjectResults.size} Marksheet(s)",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B),
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.testTag("results_class_filter_row")
                        ) {
                            items(viewModel.classes8To12Options) { classOpt ->
                                val isSelected = selectedClass == classOpt
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.subjectResultsClassFilter.value = classOpt },
                                    label = {
                                        Text(
                                            text = if (classOpt == "ALL") (if (isUrdu) "تمام (8 تا 12)" else "All (8th–12th)")
                                            else "Class $classOpt",
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = DeepNavy,
                                        selectedLabelColor = Color.White
                                    ),
                                    modifier = Modifier.testTag("filter_chip_$classOpt")
                                )
                            }
                        }
                    }
                }
            }

            // 4. Results List
            if (subjectResults.isEmpty()) {
                item {
                    EmptyResultsCard(isUrdu = isUrdu, onReset = {
                        viewModel.subjectResultsClassFilter.value = "ALL"
                        viewModel.subjectResultsSearchQuery.value = ""
                    })
                }
            } else {
                items(subjectResults, key = { it.id }) { result ->
                    StudentSubjectResultCard(
                        result = result,
                        isAdmin = isAdmin,
                        isUrdu = isUrdu,
                        onViewOfficialMarksheet = { viewModel.selectedSubjectResultCard.value = result },
                        onTogglePublish = { viewModel.toggleSubjectResultPublish(result) },
                        onDelete = { viewModel.deleteStudentSubjectResults(result) }
                    )
                }
            }
        }

        // Admin quick entry FAB for subject marksheet
        if (isAdmin) {
            FloatingActionButton(
                onClick = { viewModel.showAddSubjectResultDialog.value = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(contentPadding)
                    .padding(16.dp)
                    .testTag("admin_add_subject_results_fab"),
                containerColor = DeepNavy,
                contentColor = ElegantGold
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Marksheet")
            }
        }
    }

    // Official Marksheet Fullscreen / Printable Dialog
    if (selectedCard != null) {
        OfficialSubjectMarksheetDialog(
            result = selectedCard!!,
            isUrdu = isUrdu,
            onDismiss = { viewModel.selectedSubjectResultCard.value = null },
            onPrint = {
                viewModel.showSnackbar("Official Marksheet sent to print spooler / PDF export.")
            }
        )
    }

    // Admin Add Marksheet Dialog
    if (showAddDialog) {
        AddStudentResultsDialog(
            isUrdu = isUrdu,
            onDismiss = { viewModel.showAddSubjectResultDialog.value = false },
            onSave = { newResult ->
                viewModel.insertStudentSubjectResults(newResult)
            }
        )
    }
}

/**
 * Top Header Banner Card.
 */
@Composable
private fun AcademicBannerCard(
    isUrdu: Boolean,
    isAdmin: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("results_academic_banner"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DeepNavy),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // School Official Emblem
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 2.dp,
                    modifier = Modifier.size(54.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.school_logo),
                            contentDescription = "School Seal",
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isUrdu) "گورنمنٹ ہائر سیکنڈری اسکول تولتی" else "Govt. Higher Secondary School",
                            color = ElegantGold,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Text(
                        text = if (isUrdu) "امتحانی نتائج و سبجیکٹ مارک شیٹ (8 تا 12)" else "Subject-Wise Marksheets (Classes 8th–12th)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = if (isUrdu) "بورڈ آف انٹرمیڈیٹ اینڈ سیکنڈری ایجوکیشن کے مصدقہ نتائج" else "Board & Institutional Academic Mark Ledger",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFCBD5E1),
                        fontSize = 11.sp
                    )
                }

                if (isAdmin) {
                    Surface(
                        color = ElegantGold.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, ElegantGold)
                    ) {
                        Text(
                            text = "Admin",
                            color = ElegantGold,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Metric stats summary cards.
 */
@Composable
private fun PerformanceSummaryRow(
    totalStudents: Int,
    passRate: Int,
    avgPercentage: Double,
    topScore: Double,
    isUrdu: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("results_summary_row"),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Metric 1: Total
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$totalStudents",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = DeepNavy
                )
                Text(
                    text = if (isUrdu) "طلباء" else "Evaluated",
                    fontSize = 10.sp,
                    color = Color(0xFF64748B)
                )
            }
        }

        // Metric 2: Pass Rate
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$passRate%",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = StatusPassGreen
                )
                Text(
                    text = if (isUrdu) "کامیابی شرح" else "Pass Rate",
                    fontSize = 10.sp,
                    color = Color(0xFF64748B)
                )
            }
        }

        // Metric 3: Class Average
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = String.format("%.1f%%", avgPercentage),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = RoyalBlue
                )
                Text(
                    text = if (isUrdu) "اوسط مارکس" else "Average",
                    fontSize = 10.sp,
                    color = Color(0xFF64748B)
                )
            }
        }

        // Metric 4: Top Score
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = String.format("%.1f%%", topScore),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = ElegantGold
                )
                Text(
                    text = if (isUrdu) "ٹاپ پوزیشن" else "Top Score",
                    fontSize = 10.sp,
                    color = Color(0xFF64748B)
                )
            }
        }
    }
}

/**
 * Individual Student Subject Results Card.
 */
@Composable
private fun StudentSubjectResultCard(
    result: StudentResults,
    isAdmin: Boolean,
    isUrdu: Boolean,
    onViewOfficialMarksheet: () -> Unit,
    onTogglePublish: () -> Unit,
    onDelete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("student_subject_card_${result.rollNumber}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Student Identity & Grade Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Avatar circle with class badge
                    Surface(
                        shape = CircleShape,
                        color = RoyalBlue.copy(alpha = 0.1f),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = result.classLevel,
                                fontWeight = FontWeight.Bold,
                                color = RoyalBlue,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Column {
                        Text(
                            text = result.studentName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Roll: ${result.rollNumber}",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF475569)
                            )
                            Text(
                                text = "•",
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8)
                            )
                            Text(
                                text = "Sec ${result.section}",
                                fontSize = 11.5.sp,
                                color = Color(0xFF475569)
                            )
                        }
                    }
                }

                // Grade & Pass/Fail status
                Column(horizontalAlignment = Alignment.End) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = when (result.grade) {
                            "A+" -> Color(0xFF10B981)
                            "A" -> Color(0xFF059669)
                            "B" -> RoyalBlue
                            "C" -> ElegantGold
                            else -> StatusFailRed
                        }
                    ) {
                        Text(
                            text = "Grade ${result.grade}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.5.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = result.status,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = if (result.status == "PASS") StatusPassGreen else StatusFailRed
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Exam Info & Aggregate Marks Progress Bar
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF8FAFC),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = result.examName,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DeepNavy
                        )
                        Text(
                            text = "${result.totalObtainedMarks} / ${result.totalMaxMarks} (${String.format("%.1f%%", result.percentage)})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    LinearProgressIndicator(
                        progress = { (result.percentage / 100f).toFloat().coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = when {
                            result.percentage >= 80 -> StatusPassGreen
                            result.percentage >= 60 -> RoyalBlue
                            result.percentage >= 40 -> ElegantGold
                            else -> StatusFailRed
                        },
                        trackColor = Color(0xFFE2E8F0)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Expandable Subject-Wise Breakdown Preview
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = if (isUrdu) "مضمون وار نمبرات کی تفصیل (Subject Breakdown):" else "Subject-Wise Score Breakdown:",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )

                    val subjects = result.getSubjectList()
                    for (item in subjects) {
                        SubjectScoreRow(item = item)
                    }

                    if (result.remarks.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFFFBEB),
                            border = BorderStroke(1.dp, Color(0xFFFEF3C7)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = ElegantGold,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Academic Incharge: ${result.remarks}",
                                    fontSize = 11.sp,
                                    color = Color(0xFF92400E)
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Actions Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Toggle expansion
                TextButton(
                    onClick = { isExpanded = !isExpanded },
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (isExpanded) "Hide Subjects" else "View ${result.getSubjectList().size} Subjects",
                        fontSize = 11.5.sp,
                        color = RoyalBlue,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = RoyalBlue,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Official Marksheet button
                    Button(
                        onClick = onViewOfficialMarksheet,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DeepNavy,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("btn_view_marksheet_${result.rollNumber}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isUrdu) "سرٹیفکیٹ / مارک شیٹ" else "Official Marksheet",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Admin quick controls
                    if (isAdmin) {
                        IconButton(
                            onClick = onTogglePublish,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (result.isPublished) Icons.Default.Public else Icons.Default.PublicOff,
                                contentDescription = "Toggle Publish",
                                tint = if (result.isPublished) StatusPassGreen else Color(0xFF94A3B8),
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        IconButton(
                            onClick = onDelete,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = StatusFailRed,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Single Subject score row inside the expandable breakdown.
 */
@Composable
private fun SubjectScoreRow(item: SubjectScoreItem) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFF1F5F9),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.subjectName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DeepNavy
                )
                Text(
                    text = "Passing: ${item.passingMarks} / ${item.maxMarks}",
                    fontSize = 10.sp,
                    color = Color(0xFF64748B)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${item.obtainedMarks} / ${item.maxMarks}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = if (item.isPass) DeepNavy else StatusFailRed
                )

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (item.isPass) StatusPassGreen.copy(alpha = 0.15f) else StatusFailRed.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = if (item.isPass) "P" else "F",
                        color = if (item.isPass) StatusPassGreen else StatusFailRed,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

/**
 * Empty state card.
 */
@Composable
private fun EmptyResultsCard(
    isUrdu: Boolean,
    onReset: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Grade,
                contentDescription = null,
                tint = ElegantGold,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = if (isUrdu) "کوئی نتیجہ نہیں ملا" else "No Student Results Found",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = DeepNavy
            )
            Text(
                text = if (isUrdu) "کلاس فلٹر تبدیل کریں یا رول نمبر چیک کریں۔" else "Try clearing your search or selecting 'All (8th–12th)'.",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(14.dp))
            OutlinedButton(
                onClick = onReset,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Show All Results (8th–12th)")
            }
        }
    }
}

/**
 * Official Printable Marksheet Dialog.
 */
@Composable
fun OfficialSubjectMarksheetDialog(
    result: StudentResults,
    isUrdu: Boolean,
    onDismiss: () -> Unit,
    onPrint: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 20.dp)
                .testTag("official_marksheet_dialog"),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Dialog Close & Print Action
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MilitaryTech,
                            contentDescription = null,
                            tint = ElegantGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "OFFICIAL PROVISIONAL MARKSHEET",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElegantGold
                        )
                    }

                    Row {
                        IconButton(onClick = onPrint) {
                            Icon(Icons.Default.Print, contentDescription = "Print", tint = DeepNavy)
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
                        }
                    }
                }

                HorizontalDivider(color = Color(0xFFE2E8F0))
                Spacer(modifier = Modifier.height(12.dp))

                // Official School Seal and Title Header
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.school_logo),
                        contentDescription = "School Logo",
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "GOVT. HIGHER SECONDARY SCHOOL TOLTI",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = DeepNavy,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "DISTRICT KHARMANG, GILGIT-BALTISTAN",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF475569),
                        textAlign = TextAlign.Center
                    )

                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = RoyalBlue.copy(alpha = 0.1f),
                        modifier = Modifier.padding(top = 6.dp)
                    ) {
                        Text(
                            text = "${result.examName.uppercase()} - ACADEMIC SESSION 2025-2026",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = RoyalBlue,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Student Identity Table
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF8FAFC),
                    border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            MarksheetInfoField(label = "Student Name", value = result.studentName, modifier = Modifier.weight(1f))
                            MarksheetInfoField(label = "Father's Name", value = result.fatherName, modifier = Modifier.weight(1f))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            MarksheetInfoField(label = "Roll Number", value = result.rollNumber, modifier = Modifier.weight(1f))
                            MarksheetInfoField(label = "Class & Sec", value = "Class ${result.classLevel} (${result.section})", modifier = Modifier.weight(1f))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            MarksheetInfoField(label = "Board Affiliation", value = result.boardName, modifier = Modifier.weight(1f))
                            MarksheetInfoField(label = "Result Status", value = "${result.status} (Division: ${result.division})", modifier = Modifier.weight(1f))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Subject-Wise Table Header
                Text(
                    text = "SUBJECT-WISE MARKS DISTRIBUTION:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy
                )

                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        // Table Header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(DeepNavy)
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Text(text = "Subject", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.weight(2f))
                            Text(text = "Max", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                            Text(text = "Pass", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                            Text(text = "Obtained", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                            Text(text = "Status", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, textAlign = TextAlign.End, modifier = Modifier.weight(1f))
                        }

                        // Table Rows
                        val subjects = result.getSubjectList()
                        for ((index, sub) in subjects.withIndex()) {
                            val bg = if (index % 2 == 0) Color.White else Color(0xFFF8FAFC)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(bg)
                                    .padding(horizontal = 10.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = sub.subjectName, color = DeepNavy, fontWeight = FontWeight.Medium, fontSize = 11.5.sp, modifier = Modifier.weight(2f))
                                Text(text = "${sub.maxMarks}", color = Color(0xFF475569), fontSize = 11.sp, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                                Text(text = "${sub.passingMarks}", color = Color(0xFF475569), fontSize = 11.sp, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                                Text(text = "${sub.obtainedMarks}", color = DeepNavy, fontWeight = FontWeight.Bold, fontSize = 11.5.sp, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                                Text(
                                    text = if (sub.isPass) "PASS" else "FAIL",
                                    color = if (sub.isPass) StatusPassGreen else StatusFailRed,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            HorizontalDivider(color = Color(0xFFE2E8F0))
                        }

                        // Total Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFEF3C7))
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "GRAND TOTAL", color = DeepNavy, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, modifier = Modifier.weight(2f))
                            Text(text = "${result.totalMaxMarks}", color = DeepNavy, fontWeight = FontWeight.Bold, fontSize = 11.sp, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                            Text(text = "-", color = DeepNavy, fontSize = 11.sp, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                            Text(text = "${result.totalObtainedMarks}", color = DeepNavy, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                            Text(
                                text = "Grade ${result.grade}",
                                color = DeepNavy,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 11.5.sp,
                                textAlign = TextAlign.End,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Signatures & Stamp area
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            shape = CircleShape,
                            color = StatusPassGreen.copy(alpha = 0.1f),
                            border = BorderStroke(1.dp, StatusPassGreen),
                            modifier = Modifier.size(52.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("SEAL\nOFFICE", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = StatusPassGreen, textAlign = TextAlign.Center)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Official Stamp", fontSize = 9.sp, color = Color(0xFF64748B))
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Syed Ghulam Raza", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DeepNavy)
                        HorizontalDivider(modifier = Modifier.width(110.dp), color = DeepNavy)
                        Text(text = "Controller of Exams", fontSize = 9.5.sp, color = Color(0xFF64748B))
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Muhammad Shabbir", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DeepNavy)
                        HorizontalDivider(modifier = Modifier.width(110.dp), color = DeepNavy)
                        Text(text = "Principal", fontSize = 9.5.sp, color = Color(0xFF64748B))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Close Button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Close Marksheet")
                }
            }
        }
    }
}

@Composable
private fun MarksheetInfoField(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = label, fontSize = 10.sp, color = Color(0xFF64748B))
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
    }
}

/**
 * Admin Dialog to Add Subject-Wise Marksheet for Classes 8th through 12th.
 */
@Composable
fun AddStudentResultsDialog(
    isUrdu: Boolean,
    onDismiss: () -> Unit,
    onSave: (StudentResults) -> Unit
) {
    var studentName by remember { mutableStateOf("") }
    var fatherName by remember { mutableStateOf("") }
    var rollNumber by remember { mutableStateOf("") }
    var selectedClass by remember { mutableStateOf("10th") }
    var section by remember { mutableStateOf("A") }
    var examName by remember { mutableStateOf("Annual Board Examination 2026") }

    // Marks inputs
    var engMarks by remember { mutableStateOf("85") }
    var urduMarks by remember { mutableStateOf("80") }
    var mathMarks by remember { mutableStateOf("92") }
    var sciMarks by remember { mutableStateOf("88") }
    var socMarks by remember { mutableStateOf("84") }
    var compMarks by remember { mutableStateOf("90") }
    var islMarks by remember { mutableStateOf("45") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Add Student Marksheet (8th–12th)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy
                )

                OutlinedTextField(
                    value = studentName,
                    onValueChange = { studentName = it },
                    label = { Text("Student Full Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = fatherName,
                    onValueChange = { fatherName = it },
                    label = { Text("Father's Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = rollNumber,
                        onValueChange = { rollNumber = it },
                        label = { Text("Roll Number") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = section,
                        onValueChange = { section = it },
                        label = { Text("Section") },
                        modifier = Modifier.weight(0.5f),
                        singleLine = true
                    )
                }

                // Class selector specifically 8th to 12th
                Text(text = "Target Class:", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf("8th", "9th", "10th", "11th", "12th").forEach { cls ->
                        FilterChip(
                            selected = selectedClass == cls,
                            onClick = { selectedClass = cls },
                            label = { Text(cls, fontSize = 11.sp) }
                        )
                    }
                }

                HorizontalDivider(color = Color(0xFFE2E8F0))
                Text(text = "Subject Marks Obtained:", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = DeepNavy)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedTextField(value = engMarks, onValueChange = { engMarks = it }, label = { Text("Eng (100)") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = urduMarks, onValueChange = { urduMarks = it }, label = { Text("Urdu (100)") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = mathMarks, onValueChange = { mathMarks = it }, label = { Text("Math (100)") }, modifier = Modifier.weight(1f))
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedTextField(value = sciMarks, onValueChange = { sciMarks = it }, label = { Text("Sci/Phy (100)") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = socMarks, onValueChange = { socMarks = it }, label = { Text("Chem/SS (100)") }, modifier = Modifier.weight(1f))
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedTextField(value = compMarks, onValueChange = { compMarks = it }, label = { Text("Comp/Bio (100)") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = islMarks, onValueChange = { islMarks = it }, label = { Text("Islamiat (50)") }, modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (studentName.isNotBlank() && rollNumber.isNotBlank()) {
                                val em = engMarks.toIntOrNull() ?: 0
                                val um = urduMarks.toIntOrNull() ?: 0
                                val mm = mathMarks.toIntOrNull() ?: 0
                                val sm = sciMarks.toIntOrNull() ?: 0
                                val socm = socMarks.toIntOrNull() ?: 0
                                val cm = compMarks.toIntOrNull() ?: 0
                                val im = islMarks.toIntOrNull() ?: 0

                                val totalObt = em + um + mm + sm + socm + cm + im
                                val totalMax = 650
                                val pct = (totalObt.toDouble() / totalMax) * 100
                                val grade = when {
                                    pct >= 80 -> "A+"
                                    pct >= 70 -> "A"
                                    pct >= 60 -> "B"
                                    pct >= 50 -> "C"
                                    pct >= 40 -> "D"
                                    else -> "F"
                                }
                                val status = if (pct >= 40 && em >= 33 && um >= 33 && mm >= 33) "PASS" else "FAIL"

                                val result = StudentResults(
                                    studentName = studentName.trim(),
                                    fatherName = fatherName.trim().ifBlank { "N/A" },
                                    rollNumber = rollNumber.trim().uppercase(),
                                    classLevel = selectedClass,
                                    section = section.trim().uppercase(),
                                    examTerm = examName,
                                    boardName = "BISE / FBISE Gilgit-Baltistan",
                                    englishObtained = em,
                                    urduObtained = um,
                                    mathObtained = mm,
                                    scienceObtained = sm,
                                    scienceSubjectName = if (selectedClass == "8th") "General Science" else "Physics",
                                    chemistryOrSocialObtained = socm,
                                    chemistryOrSocialSubjectName = if (selectedClass == "8th") "Social Studies" else "Chemistry",
                                    electiveObtained = cm,
                                    electiveSubjectName = if (selectedClass == "8th") "Computer Education" else "Computer Science",
                                    islamicStudiesObtained = im,
                                    remarks = "Annual Board Evaluation",
                                    isPublished = true
                                )
                                onSave(result)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DeepNavy)
                    ) {
                        Text("Save Marksheet")
                    }
                }
            }
        }
    }
}
