package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ErrorOutline
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.TeacherProfileRecord
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.ElegantGold
import com.example.ui.theme.RoyalBlue
import com.example.ui.viewmodel.SchoolViewModel
import com.example.util.MediaStorageHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTeacherDialog(
    viewModel: SchoolViewModel,
    existingTeacher: TeacherProfileRecord? = null,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val targetTeacherId = remember(existingTeacher?.id) {
        existingTeacher?.id ?: UUID.randomUUID().toString()
    }

    var name by remember(existingTeacher?.id) { mutableStateOf(existingTeacher?.name ?: "") }
    var designation by remember(existingTeacher?.id) { mutableStateOf(existingTeacher?.designation ?: "Senior Science Teacher (SST)") }
    var department by remember(existingTeacher?.id) { mutableStateOf(existingTeacher?.department ?: "Physics & Applied Sciences") }
    var qualification by remember(existingTeacher?.id) { mutableStateOf(existingTeacher?.qualification ?: "M.Sc Physics, B.Ed") }
    var assignedSubjects by remember(existingTeacher?.id) { mutableStateOf(existingTeacher?.assignedSubjects ?: "Physics, Mathematics") }
    var assignedClasses by remember(existingTeacher?.id) { mutableStateOf(existingTeacher?.assignedClasses ?: "9th, 10th, 11th, 12th") }
    var introduction by remember(existingTeacher?.id) { mutableStateOf(existingTeacher?.introduction ?: "Dedicated faculty member committed to interactive conceptual learning.") }
    var phone by remember(existingTeacher?.id) { mutableStateOf(existingTeacher?.phone ?: "+92 345 ") }
    var email by remember(existingTeacher?.id) { mutableStateOf(existingTeacher?.email ?: "faculty@bhsstolti.edu.pk") }
    var photoUri by remember(existingTeacher?.id) { mutableStateOf(existingTeacher?.photoUri) }
    var photoError by remember(existingTeacher?.id) { mutableStateOf<String?>(null) }
    var isCopyingPhoto by remember(existingTeacher?.id) { mutableStateOf(false) }
    var pendingTempUris by remember(existingTeacher?.id) { mutableStateOf<List<String>>(emptyList()) }

    val resolvedPhotoModel = remember(photoUri) { MediaStorageHelper.resolvePhotoModel(photoUri) }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            isCopyingPhoto = true
            photoError = null
            scope.launch {
                val saved = withContext(Dispatchers.IO) {
                    MediaStorageHelper.saveTeacherPhoto(context, uri, targetTeacherId)
                }
                isCopyingPhoto = false
                if (saved != null && MediaStorageHelper.hasValidPhoto(saved)) {
                    val previousUnsaved = photoUri
                    if (previousUnsaved != null && previousUnsaved != existingTeacher?.photoUri) {
                        MediaStorageHelper.deleteLocalMediaIfManaged(previousUnsaved)
                    }
                    photoUri = saved
                    pendingTempUris = pendingTempUris + saved
                } else {
                    photoError = "Could not load selected photograph from Gallery. Please try another image."
                }
            }
        }
    }

    val handleCancel: () -> Unit = {
        pendingTempUris.forEach { tempUri ->
            if (tempUri != existingTeacher?.photoUri) {
                MediaStorageHelper.deleteLocalMediaIfManaged(tempUri)
            }
        }
        pendingTempUris = emptyList()
        onDismiss()
    }

    var deptExpanded by remember { mutableStateOf(false) }
    val deptList = listOf(
        "Computer Science & Administration",
        "Physics & Applied Sciences",
        "Mathematics",
        "Humanities & Languages",
        "Social Sciences",
        "Life Sciences",
        "Chemical Sciences",
        "General Science & Elementary Wing",
        "Sports & Co-curricular"
    )

    AlertDialog(
        onDismissRequest = handleCancel,
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
                // Teacher Profile Photograph Card
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF8FAFC),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = RoyalBlue.copy(alpha = 0.12f),
                                border = BorderStroke(
                                    2.dp,
                                    if (resolvedPhotoModel != null) ElegantGold else RoyalBlue.copy(alpha = 0.35f)
                                ),
                                modifier = Modifier
                                    .size(76.dp)
                                    .testTag("dialog_teacher_photo_preview")
                            ) {
                                if (resolvedPhotoModel != null) {
                                    AsyncImage(
                                        model = resolvedPhotoModel,
                                        contentDescription = "Teacher Profile Photo Preview",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = "No teacher photo selected",
                                            tint = RoyalBlue,
                                            modifier = Modifier.size(34.dp)
                                        )
                                    }
                                }
                            }

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                val badgeText = when {
                                    isCopyingPhoto -> "Saving photo..."
                                    existingTeacher != null && photoUri == existingTeacher.photoUri && resolvedPhotoModel != null -> "Current Photo Attached"
                                    resolvedPhotoModel != null -> "Gallery Photo Ready"
                                    else -> "No Photo Selected"
                                }
                                Surface(
                                    color = if (resolvedPhotoModel != null) Color(0xFFDCFCE7) else Color(0xFFE2E8F0),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = badgeText,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (resolvedPhotoModel != null) Color(0xFF166534) else Color(0xFF475569),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }

                                OutlinedButton(
                                    onClick = {
                                        photoError = null
                                        MediaStorageHelper.launchImagePickerSafely(
                                            photoPicker,
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        ) {
                                            photoError = "No Gallery or photo picker app available on this device."
                                        }
                                    },
                                    enabled = !isCopyingPhoto,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(min = 48.dp)
                                        .testTag("pick_teacher_photo_btn")
                                ) {
                                    Icon(Icons.Default.AddAPhoto, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (resolvedPhotoModel == null) "Upload from Gallery" else "Replace Photo",
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }

                                if (resolvedPhotoModel != null) {
                                    OutlinedButton(
                                        onClick = {
                                            photoUri = null
                                            photoError = null
                                        },
                                        enabled = !isCopyingPhoto,
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                                        border = BorderStroke(1.dp, Color(0xFFFCA5A5)),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .heightIn(min = 48.dp)
                                            .testTag("delete_teacher_photo_dialog_btn")
                                    ) {
                                        Icon(Icons.Default.DeleteOutline, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Delete Photo", style = MaterialTheme.typography.labelMedium)
                                    }
                                }
                            }
                        }

                        if (photoError != null) {
                            Surface(
                                color = Color(0xFFFEE2E2),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ErrorOutline,
                                        contentDescription = null,
                                        tint = Color(0xFFB91C1C),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = photoError ?: "",
                                        fontSize = 11.sp,
                                        color = Color(0xFF991B1B)
                                    )
                                }
                            }
                        }
                    }
                }

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
                    pendingTempUris.forEach { tempUri ->
                        if (tempUri != photoUri && tempUri != existingTeacher?.photoUri) {
                            MediaStorageHelper.deleteLocalMediaIfManaged(tempUri)
                        }
                    }
                    pendingTempUris = emptyList()

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
                            email = email.trim(),
                            photoUri = photoUri,
                            id = targetTeacherId
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
                                email = email.trim(),
                                photoUri = photoUri
                            )
                        )
                    }
                },
                enabled = name.isNotBlank() && designation.isNotBlank() && !isCopyingPhoto,
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.heightIn(min = 48.dp).testTag("save_teacher_btn")
            ) {
                Text(if (existingTeacher == null) "Add Faculty" else "Save Changes")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = handleCancel,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.heightIn(min = 48.dp).testTag("cancel_teacher_btn")
            ) {
                Text("Cancel")
            }
        }
    )
}


