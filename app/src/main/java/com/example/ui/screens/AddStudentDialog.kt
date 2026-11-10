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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Phone
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
import com.example.data.model.StudentProfileRecord
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.RoyalBlue
import com.example.ui.viewmodel.SchoolViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStudentDialog(
    viewModel: SchoolViewModel,
    existingStudent: StudentProfileRecord? = null,
    onDismiss: () -> Unit
) {
    var fullName by remember { mutableStateOf(existingStudent?.fullName ?: "") }
    var fatherName by remember { mutableStateOf(existingStudent?.fatherName ?: "") }
    var rollNumber by remember { mutableStateOf(existingStudent?.rollNumber ?: "BHSS-2026-${(110..999).random()}") }
    var classLevel by remember { mutableStateOf(existingStudent?.classLevel ?: "12th") }
    var section by remember { mutableStateOf(existingStudent?.section ?: "A") }
    var dateOfBirth by remember { mutableStateOf(existingStudent?.dateOfBirth ?: "2008-04-15") }
    var admissionDate by remember { mutableStateOf(existingStudent?.admissionDate ?: "2024-03-01") }
    var contactPhone by remember { mutableStateOf(existingStudent?.contactPhone ?: "+92 344 ") }
    var address by remember { mutableStateOf(existingStudent?.address ?: "Tolti, District Kharmang") }
    var bloodGroup by remember { mutableStateOf(existingStudent?.bloodGroup ?: "O+") }
    var notes by remember { mutableStateOf(existingStudent?.notes ?: "Enrolled in good standing.") }

    var classExpanded by remember { mutableStateOf(false) }
    val classList = listOf("5th", "6th", "7th", "8th", "9th", "10th", "11th", "12th")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.PersonAdd, contentDescription = null, tint = RoyalBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (existingStudent == null) "Enroll New Student" else "Edit Student Profile",
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
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Student Full Name *") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("student_name_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = fatherName,
                    onValueChange = { fatherName = it },
                    label = { Text("Father's Name *") },
                    modifier = Modifier.fillMaxWidth().testTag("student_father_input"),
                    singleLine = true
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = rollNumber,
                        onValueChange = { rollNumber = it },
                        label = { Text("Roll Number *") },
                        leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                        modifier = Modifier.weight(1.2f).testTag("student_roll_input"),
                        singleLine = true
                    )

                    ExposedDropdownMenuBox(
                        expanded = classExpanded,
                        onExpandedChange = { classExpanded = it },
                        modifier = Modifier.weight(0.8f)
                    ) {
                        OutlinedTextField(
                            value = classLevel,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Class") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = classExpanded) },
                            modifier = Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = classExpanded,
                            onDismissRequest = { classExpanded = false }
                        ) {
                            classList.forEach { cls ->
                                DropdownMenuItem(
                                    text = { Text(cls) },
                                    onClick = {
                                        classLevel = cls
                                        classExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = section,
                        onValueChange = { section = it },
                        label = { Text("Section") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = bloodGroup,
                        onValueChange = { bloodGroup = it },
                        label = { Text("Blood Group") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = dateOfBirth,
                        onValueChange = { dateOfBirth = it },
                        label = { Text("Date of Birth") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = admissionDate,
                        onValueChange = { admissionDate = it },
                        label = { Text("Admission Date") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = contactPhone,
                    onValueChange = { contactPhone = it },
                    label = { Text("Parent / Guardian Phone") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("student_phone_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Village / Residential Address") },
                    leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("student_address_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Academic Notes / Remarks") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (existingStudent == null) {
                        viewModel.addStudent(
                            fullName = fullName.trim(),
                            fatherName = fatherName.trim(),
                            rollNumber = rollNumber.trim(),
                            classLevel = classLevel,
                            section = section.trim(),
                            dateOfBirth = dateOfBirth.trim(),
                            admissionDate = admissionDate.trim(),
                            contactPhone = contactPhone.trim(),
                            address = address.trim(),
                            bloodGroup = bloodGroup.trim(),
                            notes = notes.trim()
                        )
                    } else {
                        viewModel.updateStudent(
                            existingStudent.copy(
                                fullName = fullName.trim(),
                                fatherName = fatherName.trim(),
                                rollNumber = rollNumber.trim(),
                                classLevel = classLevel,
                                section = section.trim(),
                                dateOfBirth = dateOfBirth.trim(),
                                admissionDate = admissionDate.trim(),
                                contactPhone = contactPhone.trim(),
                                address = address.trim(),
                                bloodGroup = bloodGroup.trim(),
                                notes = notes.trim()
                            )
                        )
                    }
                    onDismiss()
                },
                enabled = fullName.isNotBlank() && fatherName.isNotBlank() && rollNumber.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("save_student_btn")
            ) {
                Text(if (existingStudent == null) "Enroll Student" else "Save Changes")
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
