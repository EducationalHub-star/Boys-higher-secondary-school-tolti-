package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.data.model.StudentProfileRecord
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.RoyalBlue
import com.example.ui.viewmodel.SchoolViewModel
import com.example.util.MediaStorageHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStudentDialog(
    viewModel: SchoolViewModel,
    existingStudent: StudentProfileRecord? = null,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var fullName by remember(existingStudent?.id) { mutableStateOf(existingStudent?.fullName ?: "") }
    var fatherName by remember(existingStudent?.id) { mutableStateOf(existingStudent?.fatherName ?: "") }
    var rollNumber by remember(existingStudent?.id) { mutableStateOf(existingStudent?.rollNumber ?: "BHSS-2026-${(110..999).random()}") }
    var classLevel by remember(existingStudent?.id) { mutableStateOf(existingStudent?.classLevel ?: "12th") }
    var section by remember(existingStudent?.id) { mutableStateOf(existingStudent?.section ?: "A") }
    var dateOfBirth by remember(existingStudent?.id) { mutableStateOf(existingStudent?.dateOfBirth ?: "2008-04-15") }
    var admissionDate by remember(existingStudent?.id) { mutableStateOf(existingStudent?.admissionDate ?: "2024-03-01") }
    var contactPhone by remember(existingStudent?.id) { mutableStateOf(existingStudent?.contactPhone ?: "+92 344 ") }
    var address by remember(existingStudent?.id) { mutableStateOf(existingStudent?.address ?: "Tolti, District Kharmang") }
    var bloodGroup by remember(existingStudent?.id) { mutableStateOf(existingStudent?.bloodGroup ?: "O+") }
    var notes by remember(existingStudent?.id) { mutableStateOf(existingStudent?.notes ?: "Enrolled in good standing.") }
    var photoUri by remember(existingStudent?.id) { mutableStateOf(existingStudent?.photoUri) }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val saved = MediaStorageHelper.copyPickerUriToInternalStorage(context, uri, "student")
            if (saved != null) photoUri = saved
        }
    }

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
                // Optional Student Avatar Picker
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = RoyalBlue.copy(alpha = 0.12f),
                        modifier = Modifier.size(56.dp)
                    ) {
                        if (!photoUri.isNullOrBlank()) {
                            AsyncImage(
                                model = photoUri,
                                contentDescription = "Student Avatar",
                                modifier = Modifier.fillMaxSize().clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = RoyalBlue, modifier = Modifier.size(28.dp))
                            }
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        OutlinedButton(
                            onClick = {
                                MediaStorageHelper.launchImagePickerSafely(
                                    photoPicker,
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                ) {
                                    viewModel.showSnackbar("No Gallery or photo picker app available on this device.")
                                }
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.AddAPhoto, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (photoUri == null) "Add Photo" else "Change Photo", style = MaterialTheme.typography.labelMedium)
                        }
                        if (!photoUri.isNullOrBlank()) {
                            TextButton(onClick = { photoUri = null }) {
                                Text("Remove Photo", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }

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
                            notes = notes.trim(),
                            photoUri = photoUri
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
                                notes = notes.trim(),
                                photoUri = photoUri
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
