package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ClassSubjectRecord
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.RoyalBlue
import com.example.ui.viewmodel.SchoolViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddClassSubjectDialog(
    viewModel: SchoolViewModel,
    existingSubject: ClassSubjectRecord? = null,
    onDismiss: () -> Unit
) {
    val isEditing = existingSubject != null
    var classLevel by remember(existingSubject?.id) { mutableStateOf(existingSubject?.classLevel ?: "10th") }
    var subjectName by remember(existingSubject?.id) { mutableStateOf(existingSubject?.subjectName ?: "") }
    var subjectCode by remember(existingSubject?.id) { mutableStateOf(existingSubject?.subjectCode ?: "") }
    var teacherName by remember(existingSubject?.id) { mutableStateOf(existingSubject?.teacherName ?: "") }
    var maxMarksText by remember(existingSubject?.id) { mutableStateOf((existingSubject?.maxMarks ?: 100).toString()) }
    var passMarksText by remember(existingSubject?.id) { mutableStateOf((existingSubject?.passMarks ?: 40).toString()) }
    var isCompulsory by remember(existingSubject?.id) { mutableStateOf(existingSubject?.isCompulsory ?: true) }

    var classExpanded by remember { mutableStateOf(false) }
    val classList = listOf("5th", "6th", "7th", "8th", "9th", "10th", "11th", "12th")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.MenuBook, contentDescription = null, tint = RoyalBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isEditing) "Edit Class Subject" else "Add Class Subject",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = classExpanded,
                    onExpandedChange = { classExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = classLevel,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Class Level (5th–12th)") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = classExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = classExpanded,
                        onDismissRequest = { classExpanded = false }
                    ) {
                        classList.forEach { cls ->
                            DropdownMenuItem(
                                text = { Text("Class $cls") },
                                onClick = {
                                    classLevel = cls
                                    classExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = subjectName,
                    onValueChange = { subjectName = it },
                    label = { Text("Subject Name *") },
                    placeholder = { Text("e.g. Computer Science") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("subject_name_input")
                )

                OutlinedTextField(
                    value = subjectCode,
                    onValueChange = { subjectCode = it },
                    label = { Text("Subject Code *") },
                    placeholder = { Text("e.g. CS-10") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("subject_code_input")
                )

                OutlinedTextField(
                    value = teacherName,
                    onValueChange = { teacherName = it },
                    label = { Text("Subject Teacher") },
                    placeholder = { Text("e.g. Sir Ghulam Nabi") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = maxMarksText,
                        onValueChange = { maxMarksText = it.filter { c -> c.isDigit() } },
                        label = { Text("Max Marks") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = passMarksText,
                        onValueChange = { passMarksText = it.filter { c -> c.isDigit() } },
                        label = { Text("Pass Marks") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Compulsory Subject", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    Switch(checked = isCompulsory, onCheckedChange = { isCompulsory = it })
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val maxM = maxMarksText.toIntOrNull() ?: 100
                    val passM = passMarksText.toIntOrNull() ?: 40
                    if (existingSubject == null) {
                        viewModel.addClassSubject(
                            classLevel = classLevel,
                            subjectName = subjectName,
                            subjectCode = subjectCode,
                            teacherName = teacherName.ifBlank { "Faculty Member" },
                            maxMarks = maxM,
                            passMarks = passM,
                            isCompulsory = isCompulsory
                        )
                    } else {
                        viewModel.updateClassSubject(
                            existingSubject.copy(
                                classLevel = classLevel,
                                subjectName = subjectName.trim(),
                                subjectCode = subjectCode.trim().uppercase(),
                                teacherName = teacherName.trim().ifBlank { existingSubject.teacherName },
                                maxMarks = maxM,
                                passMarks = passM,
                                isCompulsory = isCompulsory
                            )
                        )
                    }
                    onDismiss()
                },
                enabled = subjectName.isNotBlank() && subjectCode.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("save_class_subject_btn")
            ) {
                Text(if (isEditing) "Update Subject" else "Add Subject")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(8.dp)) {
                Text("Cancel")
            }
        }
    )
}
