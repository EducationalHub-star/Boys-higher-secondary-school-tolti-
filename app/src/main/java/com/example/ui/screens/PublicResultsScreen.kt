package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.PublicOff
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudentResult
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.ElegantGold
import com.example.ui.viewmodel.SchoolViewModel
import com.example.util.UrduLocalization

@Composable
fun PublicResultsScreen(
    viewModel: SchoolViewModel,
    contentPadding: PaddingValues
) {
    val language by viewModel.currentLanguage.collectAsState()
    val isUrdu = language == UrduLocalization.Language.URDU
    var resultSubTab by remember { mutableStateOf(0) } // 0: Subject-wise Marksheets (8th-12th), 1: Course Ledger

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = contentPadding.calculateTopPadding())
    ) {
        // Top Sub-Tab Navigation Bar
        TabRow(
            selectedTabIndex = resultSubTab,
            containerColor = DeepNavy,
            contentColor = ElegantGold,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[resultSubTab]),
                    color = ElegantGold,
                    height = 3.dp
                )
            },
            modifier = Modifier.testTag("results_subtab_bar")
        ) {
            Tab(
                selected = resultSubTab == 0,
                onClick = { resultSubTab = 0 },
                text = {
                    Text(
                        text = if (isUrdu) "مارک شیٹس (8 تا 12)" else "Subject Marksheets (8th–12th)",
                        fontWeight = if (resultSubTab == 0) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 11.5.sp,
                        color = if (resultSubTab == 0) ElegantGold else Color.White.copy(alpha = 0.75f)
                    )
                },
                modifier = Modifier.testTag("subtab_subject_results")
            )
            Tab(
                selected = resultSubTab == 1,
                onClick = { resultSubTab = 1 },
                text = {
                    Text(
                        text = if (isUrdu) "پیپرز لیجر" else "Course Papers",
                        fontWeight = if (resultSubTab == 1) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 11.5.sp,
                        color = if (resultSubTab == 1) ElegantGold else Color.White.copy(alpha = 0.75f)
                    )
                },
                modifier = Modifier.testTag("subtab_single_results")
            )
        }

        if (resultSubTab == 0) {
            // Dedicated Classes 8th through 12th Subject-Wise Results Screen
            StudentResultsScreen(
                viewModel = viewModel,
                contentPadding = PaddingValues(
                    top = 0.dp,
                    bottom = contentPadding.calculateBottomPadding()
                )
            )
        } else {
            // Existing Individual Papers list
            PublicResultsLedgerContent(
                viewModel = viewModel,
                contentPadding = PaddingValues(
                    top = 0.dp,
                    bottom = contentPadding.calculateBottomPadding()
                )
            )
        }
    }
}

@Composable
fun PublicResultsLedgerContent(
    viewModel: SchoolViewModel,
    contentPadding: PaddingValues
) {
    val language by viewModel.currentLanguage.collectAsState()
    val isUrdu = language == UrduLocalization.Language.URDU
    val results by viewModel.filteredResults.collectAsState()
    val selectedClass by viewModel.resultClassFilter.collectAsState()
    val selectedTerm by viewModel.resultExamTermFilter.collectAsState()
    val searchQuery by viewModel.resultSearchQuery.collectAsState()
    val isAdmin by viewModel.isAdminLoggedIn.collectAsState()

    val terms = listOf("ALL", "Mid Term Exam 2026", "Monthly Test October", "Annual Board Exam")

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("public_results_screen"),
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isUrdu) "امتحانی نتائج و مارک شیٹس" else "Academic Results & Marksheets",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0B192C)
                        )
                        Text(
                            text = if (isUrdu) "پانچویں سے بارہویں جماعت تک مصدقہ نتائج" else "Published examination records for Classes 5th to 12th",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF64748B)
                        )
                    }

                    if (isAdmin) {
                        Surface(
                            color = Color(0xFF0B192C),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Admin Mode",
                                color = Color(0xFFFBBF24),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.resultSearchQuery.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("results_search_input"),
                    placeholder = { Text(if (isUrdu) "رول نمبر، نام یا مضمون تلاش کریں..." else "Search by Roll Number, Student Name, or Subject...", fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(20.dp))
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.resultSearchQuery.value = "" }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = Color(0xFF64748B), modifier = Modifier.size(18.dp))
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            // Class Filter Chips
            item {
                Column {
                    Text(text = if (isUrdu) "کلاس سیکشن:" else "Filter by Class:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(viewModel.classOptions) { classOpt ->
                            val isSelected = selectedClass == classOpt
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.resultClassFilter.value = classOpt },
                                label = { Text(if (classOpt == "ALL") "All Classes" else "Class $classOpt", fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF0B192C),
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // Results List
            if (results.isEmpty()) {
                item {
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
                            Icon(imageVector = Icons.Default.Grade, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No Results Found",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0B192C)
                            )
                            Text(
                                text = "Try searching for roll number 'BHSS-2026-104' or clearing class filters.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                }
            } else {
                items(results, key = { it.id }) { res ->
                    ResultCardItem(
                        result = res,
                        isAdmin = isAdmin,
                        onViewCard = { viewModel.selectedResultCard.value = res },
                        onTogglePublish = { viewModel.toggleResultPublish(res) },
                        onDelete = { viewModel.deleteResult(res) }
                    )
                }
            }
        }

        // Admin quick entry FAB
        if (isAdmin) {
            FloatingActionButton(
                onClick = { viewModel.showAdminResultDialog.value = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(contentPadding)
                    .padding(16.dp)
                    .testTag("admin_add_result_fab"),
                containerColor = Color(0xFF0B192C),
                contentColor = Color(0xFFFBBF24)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Enter New Result")
            }
        }
    }
}

@Composable
private fun ResultCardItem(
    result: StudentResult,
    isAdmin: Boolean,
    onViewCard: () -> Unit,
    onTogglePublish: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("result_card_${result.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Exam Term & Grade
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(
                        color = Color(0xFF1D4ED8).copy(alpha = 0.12f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = result.examTerm,
                            fontSize = 10.sp,
                            color = Color(0xFF1D4ED8),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Surface(
                        color = Color(0xFF0B192C).copy(alpha = 0.08f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "Class ${result.classLevel}",
                            fontSize = 10.sp,
                            color = Color(0xFF0B192C),
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    if (isAdmin) {
                        Surface(
                            color = if (result.isPublished) Color(0xFF10B981).copy(alpha = 0.12f) else Color(0xFFEF4444).copy(alpha = 0.12f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = if (result.isPublished) "PUBLISHED" else "UNPUBLISHED",
                                fontSize = 9.sp,
                                color = if (result.isPublished) Color(0xFF059669) else Color(0xFFDC2626),
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // Awarded Grade Pill
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFD97706)
                ) {
                    Text(
                        text = "Grade ${result.grade}",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Student Info & Subject
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = result.studentName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0B192C)
                    )
                    Text(
                        text = "Roll: ${result.rollNumber}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF64748B)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${result.obtainedMarks}/${result.totalMarks}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = Color(0xFF0B192C)
                    )
                    Text(
                        text = String.format("%.1f%% • %s", result.percentage, result.status),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (result.status == "PASS") Color(0xFF10B981) else Color(0xFFEF4444)
                    )
                }
            }

            Text(
                text = "Subject: ${result.subjectName} (${result.subjectCode})",
                fontSize = 12.sp,
                color = Color(0xFF334155),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 4.dp)
            )

            if (result.remarks.isNotBlank()) {
                Text(
                    text = "Remarks: ${result.remarks}",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onViewCard,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0B192C)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(34.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                ) {
                    Icon(imageVector = Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Official Result Card", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                if (isAdmin) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(onClick = onTogglePublish, modifier = Modifier.size(34.dp)) {
                            Icon(
                                imageVector = if (result.isPublished) Icons.Default.Public else Icons.Default.PublicOff,
                                contentDescription = "Toggle Publish",
                                tint = if (result.isPublished) Color(0xFF059669) else Color(0xFFDC2626),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        IconButton(onClick = onDelete, modifier = Modifier.size(34.dp)) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFF94A3B8), modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}
