package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.viewmodel.SchoolViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAssignmentDialog(
    viewModel: SchoolViewModel,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedSubject by remember { mutableStateOf("Computer Science") }
    var courseCode by remember { mutableStateOf("CS-12") }
    var selectedClass by remember { mutableStateOf("12th") }
    var teacherName by remember { mutableStateOf("Sir Ghulam Nabi") }
    var dueDateFormatted by remember { mutableStateOf("Tomorrow, 11:59 PM") }
    var totalMarksText by remember { mutableStateOf("50") }
    var priority by remember { mutableStateOf("NORMAL") }
    var description by remember { mutableStateOf("") }

    var isSubjectDropdownExpanded by remember { mutableStateOf(false) }

    val subjectOptions = listOf(
        "Computer Science" to "CS",
        "Physics" to "PHY",
        "Mathematics" to "MATH",
        "Chemistry" to "CHEM",
        "Biology" to "BIO",
        "English" to "ENG",
        "Urdu & Pak Studies" to "URDU",
        "General Science" to "SCI"
    )

    val classLevels = listOf("8th", "9th", "10th", "11th", "12th")

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .testTag("add_assignment_dialog"),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
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
                        Icon(
                            imageVector = Icons.Default.Assignment,
                            contentDescription = null,
                            tint = Color(0xFF0F5132)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "New Student Assignment",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Title
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Assignment Title *") },
                    placeholder = { Text("e.g. C++ Classes & Memory Management") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("assignment_title_input"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Subject Dropdown
                ExposedDropdownMenuBox(
                    expanded = isSubjectDropdownExpanded,
                    onExpandedChange = { isSubjectDropdownExpanded = !isSubjectDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedSubject,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Subject") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isSubjectDropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = isSubjectDropdownExpanded,
                        onDismissRequest = { isSubjectDropdownExpanded = false }
                    ) {
                        subjectOptions.forEach { (subName, codePrefix) ->
                            DropdownMenuItem(
                                text = { Text(subName) },
                                onClick = {
                                    selectedSubject = subName
                                    courseCode = "$codePrefix-$selectedClass"
                                    isSubjectDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Class Level Selection Chips
                Text(
                    text = "Class Level (8th to 12th):",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    classLevels.forEach { lvl ->
                        FilterChip(
                            selected = selectedClass == lvl,
                            onClick = {
                                selectedClass = lvl
                                val prefix = subjectOptions.firstOrNull { it.first == selectedSubject }?.second ?: "SUB"
                                courseCode = "$prefix-$lvl"
                            },
                            label = { Text("Class $lvl", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF0F5132),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Teacher & Due Date
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = teacherName,
                        onValueChange = { teacherName = it },
                        label = { Text("Teacher Name") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = totalMarksText,
                        onValueChange = { totalMarksText = it.filter { char -> char.isDigit() } },
                        label = { Text("Total Marks") },
                        modifier = Modifier.weight(0.7f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Due Date Formatted
                OutlinedTextField(
                    value = dueDateFormatted,
                    onValueChange = { dueDateFormatted = it },
                    label = { Text("Due Date & Time *") },
                    placeholder = { Text("e.g. In 2 Days, 05:00 PM") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Priority selector
                Text(
                    text = "Priority Level:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("URGENT" to Color(0xFFEF4444), "NORMAL" to Color(0xFF3B82F6), "LOW" to Color(0xFF10B981)).forEach { (p, col) ->
                        FilterChip(
                            selected = priority == p,
                            onClick = { priority = p },
                            label = { Text(p, fontSize = 11.sp, fontWeight = if (priority == p) FontWeight.Bold else FontWeight.Normal) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = col.copy(alpha = 0.2f),
                                selectedLabelColor = col
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Instructions & Guidelines") },
                    placeholder = { Text("State assignment requirements, submission format, and topics.") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                val dueMillis = System.currentTimeMillis() + (2L * 24 * 3600 * 1000)
                                viewModel.addAssignment(
                                    title = title.trim(),
                                    subject = selectedSubject,
                                    courseCode = courseCode,
                                    classLevel = selectedClass,
                                    teacherName = teacherName.ifBlank { "Subject Instructor" },
                                    description = description.ifBlank { "Complete all questions and submit neatly." },
                                    dueDateTimestamp = dueMillis,
                                    dueDateFormatted = dueDateFormatted.ifBlank { "In 2 Days" },
                                    totalMarks = totalMarksText.toIntOrNull() ?: 50,
                                    priority = priority
                                )
                            }
                        },
                        enabled = title.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F5132)),
                        modifier = Modifier.testTag("save_assignment_button")
                    ) {
                        Text("Save Assignment")
                    }
                }
            }
        }
    }
}
