package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudentAssignment
import com.example.ui.viewmodel.SchoolViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentsScreen(
    viewModel: SchoolViewModel,
    contentPadding: PaddingValues
) {
    val allAssignments by viewModel.allAssignments.collectAsState()
    val filteredAssignments by viewModel.filteredAssignments.collectAsState()
    val selectedClass by viewModel.selectedClassLevel.collectAsState()
    val statusFilter by viewModel.assignmentStatusFilter.collectAsState()
    val searchQuery by viewModel.assignmentSearchQuery.collectAsState()

    val pendingCount = allAssignments.count { !it.isCompleted && !it.isOverdue }
    val completedCount = allAssignments.count { it.isCompleted }
    val overdueCount = allAssignments.count { it.isOverdue }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showAddAssignmentDialog.value = true },
                containerColor = Color(0xFF0F5132),
                contentColor = Color.White,
                modifier = Modifier.testTag("add_assignment_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Assignment")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("assignments_screen"),
            contentPadding = PaddingValues(
                top = contentPadding.calculateTopPadding() + 8.dp,
                bottom = contentPadding.calculateBottomPadding() + 80.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Summary Cards Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AssignmentStatCard(
                        modifier = Modifier.weight(1f),
                        title = "Pending",
                        count = pendingCount,
                        color = Color(0xFFD97706),
                        icon = Icons.Default.HourglassTop,
                        isSelected = statusFilter == "PENDING",
                        onClick = {
                            viewModel.assignmentStatusFilter.value =
                                if (statusFilter == "PENDING") "ALL" else "PENDING"
                        }
                    )
                    AssignmentStatCard(
                        modifier = Modifier.weight(1f),
                        title = "Completed",
                        count = completedCount,
                        color = Color(0xFF10B981),
                        icon = Icons.Default.CheckCircle,
                        isSelected = statusFilter == "COMPLETED",
                        onClick = {
                            viewModel.assignmentStatusFilter.value =
                                if (statusFilter == "COMPLETED") "ALL" else "COMPLETED"
                        }
                    )
                    AssignmentStatCard(
                        modifier = Modifier.weight(1f),
                        title = "Overdue",
                        count = overdueCount,
                        color = Color(0xFFEF4444),
                        icon = Icons.Default.Warning,
                        isSelected = statusFilter == "OVERDUE",
                        onClick = {
                            viewModel.assignmentStatusFilter.value =
                                if (statusFilter == "OVERDUE") "ALL" else "OVERDUE"
                        }
                    )
                }
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.assignmentSearchQuery.value = it },
                    placeholder = { Text("Search by subject, title, or teacher...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.assignmentSearchQuery.value = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear search")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("assignment_search_field"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            // Class Level Filter Bar (8th to 12th)
            item {
                Column {
                    Text(
                        text = "Filter by Class Section:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(viewModel.classOptions) { classOpt ->
                            val isSelected = selectedClass == classOpt
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.selectedClassLevel.value = classOpt },
                                label = {
                                    Text(
                                        text = if (classOpt == "ALL") "All Classes" else "Class $classOpt",
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF0F5132),
                                    selectedLabelColor = Color.White
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }
                }
            }

            // Status Filter Tabs
            item {
                val tabs = listOf("ALL" to "All (${allAssignments.size})", "PENDING" to "Due ($pendingCount)", "COMPLETED" to "Done ($completedCount)", "OVERDUE" to "Late ($overdueCount)")
                val currentTabIndex = tabs.indexOfFirst { it.first == statusFilter }.coerceAtLeast(0)

                TabRow(
                    selectedTabIndex = currentTabIndex,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = Color(0xFF0F5132)
                ) {
                    tabs.forEachIndexed { index, (key, label) ->
                        Tab(
                            selected = statusFilter == key,
                            onClick = { viewModel.assignmentStatusFilter.value = key },
                            text = {
                                Text(
                                    text = label,
                                    fontSize = 12.sp,
                                    fontWeight = if (statusFilter == key) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }
            }

            // Assignments List Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Assigned Homework (${filteredAssignments.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Tap circle to toggle completion",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Assignment Items
            if (filteredAssignments.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Assignment,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No assignments match your filters",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Use the '+' button to assign homework for Class ${selectedClass}.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(
                    items = filteredAssignments,
                    key = { it.id }
                ) { assignment ->
                    AssignmentRowCard(
                        assignment = assignment,
                        onToggleCompletion = { viewModel.toggleAssignmentCompletion(assignment) },
                        onDelete = { viewModel.deleteAssignment(assignment) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AssignmentRowCard(
    assignment: StudentAssignment,
    onToggleCompletion: () -> Unit,
    onDelete: () -> Unit
) {
    val cardBg by animateColorAsState(
        targetValue = if (assignment.isCompleted) Color(0xFFF0FDF4) else MaterialTheme.colorScheme.surface,
        label = "assignment_bg"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("assignment_item_${assignment.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Badges & Due Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF0F5132).copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "Class ${assignment.classLevel}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F5132),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF1E3A8A).copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = "${assignment.courseCode} • ${assignment.subject}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1E3A8A),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Due status badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when {
                        assignment.isCompleted -> Color(0xFF10B981).copy(alpha = 0.15f)
                        assignment.isOverdue -> Color(0xFFEF4444).copy(alpha = 0.15f)
                        else -> Color(0xFFD97706).copy(alpha = 0.15f)
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = when {
                                assignment.isCompleted -> Icons.Default.Check
                                assignment.isOverdue -> Icons.Default.Warning
                                else -> Icons.Default.CalendarToday
                            },
                            contentDescription = null,
                            tint = when {
                                assignment.isCompleted -> Color(0xFF10B981)
                                assignment.isOverdue -> Color(0xFFEF4444)
                                else -> Color(0xFFD97706)
                            },
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (assignment.isCompleted) "Completed" else assignment.dueDateFormatted,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                assignment.isCompleted -> Color(0xFF10B981)
                                assignment.isOverdue -> Color(0xFFEF4444)
                                else -> Color(0xFFD97706)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Checkbox and Title Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Interactive completion circle
                IconButton(
                    onClick = onToggleCompletion,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("toggle_complete_${assignment.id}")
                ) {
                    if (assignment.isCompleted) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Mark Pending",
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.RadioButtonUnchecked,
                            contentDescription = "Mark Complete",
                            tint = Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = assignment.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textDecoration = if (assignment.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                        color = if (assignment.isCompleted) Color.Gray else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = assignment.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Gray.copy(alpha = 0.7f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Footer: Teacher & Marks
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Instructor: ${assignment.teacherName}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${assignment.totalMarks} Marks • Status: ${if (assignment.isCompleted) "Completed" else "Pending"}",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = if (assignment.isCompleted) Color(0xFF10B981) else Color(0xFFD97706)
                )
            }
        }
    }
}

@Composable
private fun AssignmentStatCard(
    modifier: Modifier = Modifier,
    title: String,
    count: Int,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick)
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) color else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = color,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
