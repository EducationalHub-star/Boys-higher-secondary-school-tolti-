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
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
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
import com.example.data.model.TeacherProfileRecord
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.RoyalBlue
import com.example.ui.viewmodel.SchoolViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTeacherDialog(
    viewModel: SchoolViewModel,
    existingTeacher: TeacherProfileRecord? = null,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(existingTeacher?.name ?: "") }
    var designation by remember { mutableStateOf(existingTeacher?.designation ?: "Senior Science Teacher (SST)") }
    var department by remember { mutableStateOf(existingTeacher?.department ?: "Science") }
    var qualification by remember { mutableStateOf(existingTeacher?.qualification ?: "M.Sc Physics, B.Ed") }
    var assignedSubjects by remember { mutableStateOf(existingTeacher?.assignedSubjects ?: "Physics, Mathematics") }
    var assignedClasses by remember { mutableStateOf(existingTeacher?.assignedClasses ?: "9th, 10th, 11th, 12th") }
    var introduction by remember { mutableStateOf(existingTeacher?.introduction ?: "Dedicated faculty member committed to interactive conceptual learning.") }
    var phone by remember { mutableStateOf(existingTeacher?.phone ?: "+92 345 ") }
    var email by remember { mutableStateOf(existingTeacher?.email ?: "faculty@bhsstolti.edu.pk") }

    var deptExpanded by remember { mutableStateOf(false) }
    val deptList = listOf("Science", "Mathematics", "Computer Science", "Languages & Humanities", "Islamic & Social Studies")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.PersonAdd, contentDescription = null, tint = RoyalBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (existingTeacher == null) "Add Faculty Member" else "Edit Faculty Profile",
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
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Faculty Full Name *") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("teacher_name_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = designation,
                    onValueChange = { designation = it },
                    label = { Text("Designation *") },
                    leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("teacher_designation_input"),
                    singleLine = true
                )

                ExposedDropdownMenuBox(
                    expanded = deptExpanded,
                    onExpandedChange = { deptExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = department,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Department") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = deptExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = deptExpanded,
                        onDismissRequest = { deptExpanded = false }
                    ) {
                        deptList.forEach { dept ->
                            DropdownMenuItem(
                                text = { Text(dept) },
                                onClick = {
                                    department = dept
                                    deptExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = qualification,
                    onValueChange = { qualification = it },
                    label = { Text("Academic Qualification") },
                    leadingIcon = { Icon(Icons.Default.School, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = assignedSubjects,
                    onValueChange = { assignedSubjects = it },
                    label = { Text("Assigned Subjects") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = assignedClasses,
                    onValueChange = { assignedClasses = it },
                    label = { Text("Assigned Classes (e.g. 9th, 10th)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = introduction,
                    onValueChange = { introduction = it },
                    label = { Text("Faculty Introduction / Bio") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (existingTeacher == null) {
                        viewModel.addTeacher(
                            name = name.trim(),
                            designation = designation.trim(),
                            department = department.trim(),
                            qualification = qualification.trim(),
                            assignedSubjects = assignedSubjects.trim(),
                            assignedClasses = assignedClasses.trim(),
                            introduction = introduction.trim(),
                            phone = phone.trim(),
                            email = email.trim()
                        )
                    } else {
                        viewModel.updateTeacher(
                            existingTeacher.copy(
                                name = name.trim(),
                                designation = designation.trim(),
                                department = department.trim(),
                                qualification = qualification.trim(),
                                assignedSubjects = assignedSubjects.trim(),
                                assignedClasses = assignedClasses.trim(),
                                introduction = introduction.trim(),
                                phone = phone.trim(),
                                email = email.trim()
                            )
                        )
                    }
                    onDismiss()
                },
                enabled = name.isNotBlank() && designation.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("save_teacher_btn")
            ) {
                Text(if (existingTeacher == null) "Add Faculty" else "Save Changes")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Cancel")
            }
        }
    )
}
