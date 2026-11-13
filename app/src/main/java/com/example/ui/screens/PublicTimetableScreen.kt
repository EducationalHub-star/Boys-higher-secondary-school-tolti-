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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TimetablePeriod
import com.example.ui.theme.LocalAppDesignConfig
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.designCardBorder
import com.example.ui.theme.designCardColors
import com.example.ui.theme.designCardShape
import com.example.ui.viewmodel.DeleteTarget
import com.example.ui.viewmodel.SchoolViewModel
import com.example.util.UrduLocalization

@Composable
fun PublicTimetableScreen(
    viewModel: SchoolViewModel,
    contentPadding: PaddingValues
) {
    val language by viewModel.currentLanguage.collectAsState()
    val isUrdu = language == UrduLocalization.Language.URDU
    val allPeriods by viewModel.allTimetablePeriods.collectAsState()
    val selectedClass by viewModel.timetableClassFilter.collectAsState()
    val selectedDay by viewModel.timetableDayFilter.collectAsState()
    val isAdmin by viewModel.isAdminLoggedIn.collectAsState()

    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

    val periodsForClassAndDay = allPeriods.filter {
        it.classLevel == selectedClass && it.dayOfWeek.equals(selectedDay, ignoreCase = true)
    }.sortedBy { it.periodNumber }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("public_timetable_screen"),
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
                        text = if (isUrdu) "کلاس ہفتہ وار ٹائم ٹیبل" else "Class Weekly Timetable",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0B192C)
                    )
                    Text(
                        text = if (isUrdu) "پانچویں سے بارہویں جماعت تک باقاعدہ پیریڈز کا نظام الاوقات" else "Official class schedule, periods, subjects and assigned teachers",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF64748B)
                    )
                }
            }

            // Class Selector
            item {
                Column {
                    Text("Select Class:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(viewModel.gradeLevelOptions) { classOpt ->
                            val isSelected = selectedClass == classOpt
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.timetableClassFilter.value = classOpt },
                                label = { Text("Class $classOpt", fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF0B192C),
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // Day Selector
            item {
                Column {
                    Text("Select Day:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(daysOfWeek) { day ->
                            val isSelected = selectedDay == day
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.timetableDayFilter.value = day },
                                label = { Text(day, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF1D4ED8),
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // Periods List
            if (periodsForClassAndDay.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(40.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No Periods Scheduled for Class $selectedClass ($selectedDay)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0B192C)
                            )
                            Text(
                                text = "Switch to Class 12th or Monday to view master timetable entries.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                }
            } else {
                items(periodsForClassAndDay, key = { it.id }) { period ->
                    PeriodCardItem(
                        period = period,
                        isAdmin = isAdmin,
                        onEdit = {
                            viewModel.editingTimetable.value = period
                            viewModel.showAddTimetableDialog.value = true
                        },
                        onDelete = { viewModel.requestDelete(DeleteTarget.Timetable(period)) }
                    )
                }
            }
        }

        if (isAdmin) {
            FloatingActionButton(
                onClick = {
                    viewModel.editingTimetable.value = null
                    viewModel.showAddTimetableDialog.value = true
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(contentPadding)
                    .padding(16.dp)
                    .testTag("admin_add_timetable_fab"),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color(0xFFFBBF24)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Period")
            }
        }
    }
}

@Composable
private fun PeriodCardItem(
    period: TimetablePeriod,
    isAdmin: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val designConfig = LocalAppDesignConfig.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("period_item_${period.id}"),
        shape = designCardShape(designConfig),
        colors = designCardColors(designConfig),
        border = designCardBorder(designConfig),
        elevation = CardDefaults.cardElevation(defaultElevation = if (designConfig.cardStyle == "ELEVATED") 2.dp else 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFF0B192C),
                modifier = Modifier.size(48.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Period", fontSize = 8.sp, color = Color(0xFFFBBF24), fontWeight = FontWeight.Bold)
                    Text("${period.periodNumber}", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = period.subjectName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0B192C)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Icon(imageVector = Icons.Default.AccessTime, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(period.timeRange, fontSize = 11.sp, color = Color(0xFFD97706), fontWeight = FontWeight.Bold)
                }

                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(period.teacherName, fontSize = 11.sp, color = Color(0xFF334155))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.MeetingRoom, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(period.roomName, fontSize = 11.sp, color = Color(0xFF64748B))
                    }
                }
            }

            if (isAdmin) {
                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    IconButton(onClick = onEdit, modifier = Modifier.testTag("edit_period_${period.id}")) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Period", tint = RoyalBlue, modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.testTag("delete_period_${period.id}")) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Period", tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}
