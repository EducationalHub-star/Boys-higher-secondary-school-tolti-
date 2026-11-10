package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.data.model.StudentResult
import com.example.ui.viewmodel.SchoolViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminResultInputDialog(
    viewModel: SchoolViewModel,
    onDismiss: () -> Unit
) {
    var studentName by remember { mutableStateOf("Muhammad Khan") }
    var rollNumber by remember { mutableStateOf("BHSS-2026-104") }
    var selectedClass by remember { mutableStateOf("12th") }
    var selectedTerm by remember { mutableStateOf("Mid Term Exam 2026") }
    var selectedSubject by remember { mutableStateOf("Computer Science") }
    var subjectCode by remember { mutableStateOf("CS-12") }
    var totalMarksText by remember { mutableStateOf("100") }
    var obtainedMarksText by remember { mutableStateOf("88") }
    var remarks by remember { mutableStateOf("Strong conceptual understanding and programming skills.") }
    var adminName by remember { mutableStateOf("Sir Ghulam Nabi (Exam Incharge)") }

    var isSubjectDropdownExpanded by remember { mutableStateOf(false) }
    var isTermDropdownExpanded by remember { mutableStateOf(false) }

    val subjectList = listOf(
        "Computer Science" to "CS",
        "Physics" to "PHY",
        "Mathematics" to "MATH",
        "Chemistry" to "CHEM",
        "Biology" to "BIO",
        "English Compulsory" to "ENG",
        "Urdu & Pak Studies" to "URDU",
        "General Science" to "SCI"
    )

    val termList = listOf(
        "Annual Board Exam 2026",
        "Mid Term Exam 2026",
        "Monthly Assessment (Oct)",
        "Pre-Board Mock Examination"
    )

    val classLevels = listOf("8th", "9th", "10th", "11th", "12th")

    // Live calculation
    val totalMarks = totalMarksText.toIntOrNull() ?: 100
    val obtainedMarks = obtainedMarksText.toIntOrNull() ?: 0
    val percentage = if (totalMarks > 0) (obtainedMarks.toDouble() / totalMarks.toDouble()) * 100.0 else 0.0
    val calculatedGrade = StudentResult.calculateGrade(obtainedMarks, totalMarks)
    val isPass = obtainedMarks >= (totalMarks * 0.40)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .testTag("admin_result_input_dialog"),
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
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF0F5132).copy(alpha = 0.15f),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AdminPanelSettings,
                                    contentDescription = null,
                                    tint = Color(0xFF0F5132),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Admin Marks Input",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Save verified academic grades to Room DB",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Student Identity Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = studentName,
                        onValueChange = { studentName = it },
                        label = { Text("Student Name *") },
                        modifier = Modifier
                            .weight(1.3f)
                            .testTag("admin_student_name_input"),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = rollNumber,
                        onValueChange = { rollNumber = it },
                        label = { Text("Roll Number *") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("admin_roll_number_input"),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Class Level Filter Chips
                Text(
                    text = "Select Class:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    classLevels.forEach { lvl ->
                        FilterChip(
                            selected = selectedClass == lvl,
                            onClick = {
                                selectedClass = lvl
                                val prefix = subjectList.firstOrNull { it.first == selectedSubject }?.second ?: "SUB"
                                subjectCode = "$prefix-$lvl"
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

                // Exam Term Dropdown
                ExposedDropdownMenuBox(
                    expanded = isTermDropdownExpanded,
                    onExpandedChange = { isTermDropdownExpanded = !isTermDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedTerm,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Examination Term") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTermDropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = isTermDropdownExpanded,
                        onDismissRequest = { isTermDropdownExpanded = false }
                    ) {
                        termList.forEach { term ->
                            DropdownMenuItem(
                                text = { Text(term) },
                                onClick = {
                                    selectedTerm = term
                                    isTermDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

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
                        label = { Text("Subject Name & Code") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isSubjectDropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = isSubjectDropdownExpanded,
                        onDismissRequest = { isSubjectDropdownExpanded = false }
                    ) {
                        subjectList.forEach { (subName, codePrefix) ->
                            DropdownMenuItem(
                                text = { Text("$subName ($codePrefix)") },
                                onClick = {
                                    selectedSubject = subName
                                    subjectCode = "$codePrefix-$selectedClass"
                                    isSubjectDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Marks Input with Live Grade Preview Card
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = totalMarksText,
                        onValueChange = { totalMarksText = it.filter { c -> c.isDigit() } },
                        label = { Text("Total Marks *") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("admin_total_marks_input"),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = obtainedMarksText,
                        onValueChange = { obtainedMarksText = it.filter { c -> c.isDigit() } },
                        label = { Text("Obtained Marks *") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("admin_obtained_marks_input"),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Live Grade Calculator Badge Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Live Computation:",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = String.format("%.1f%%", percentage),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isPass) "Status: PASSED" else "Status: NEEDS IMPROVEMENT",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isPass) Color(0xFF10B981) else Color(0xFFEF4444),
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Surface(
                            shape = CircleShape,
                            color = if (isPass) Color(0xFF0F5132) else Color(0xFFEF4444),
                            modifier = Modifier.size(46.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = calculatedGrade,
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Teacher/Admin Remarks
                OutlinedTextField(
                    value = remarks,
                    onValueChange = { remarks = it },
                    label = { Text("Examiner / Teacher Remarks") },
                    placeholder = { Text("e.g. Excellent analytical performance in board curriculum.") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Recorded By
                OutlinedTextField(
                    value = adminName,
                    onValueChange = { adminName = it },
                    label = { Text("Recorded By (Admin Name/Title)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
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
                            if (studentName.isNotBlank() && rollNumber.isNotBlank()) {
                                viewModel.saveStudentResult(
                                    studentName = studentName.trim(),
                                    rollNumber = rollNumber.trim(),
                                    classLevel = selectedClass,
                                    examTerm = selectedTerm,
                                    subjectName = selectedSubject,
                                    subjectCode = subjectCode,
                                    totalMarks = totalMarks,
                                    obtainedMarks = obtainedMarks,
                                    remarks = remarks.trim(),
                                    enteredBy = adminName.trim()
                                )
                            }
                        },
                        enabled = studentName.isNotBlank() && rollNumber.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F5132)),
                        modifier = Modifier.testTag("save_result_button")
                    ) {
                        Icon(Icons.Default.Grade, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save Result to Room")
                    }
                }
            }
        }
    }
}
