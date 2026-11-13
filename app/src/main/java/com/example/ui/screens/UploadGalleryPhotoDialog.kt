package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.data.model.GalleryPhoto
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.RoyalBlue
import com.example.ui.viewmodel.SchoolViewModel
import com.example.util.MediaStorageHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadGalleryPhotoDialog(
    viewModel: SchoolViewModel,
    existingPhoto: GalleryPhoto? = null,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val isEditing = existingPhoto != null

    var title by remember(existingPhoto?.id) { mutableStateOf(existingPhoto?.title ?: "") }
    var description by remember(existingPhoto?.id) { mutableStateOf(existingPhoto?.description ?: "") }
    var category by remember(existingPhoto?.id) { mutableStateOf(existingPhoto?.category ?: "School Events") }
    var selectedDrawable by remember(existingPhoto?.id) { mutableStateOf(existingPhoto?.drawableResName ?: "reel_fields") }
    var isPublished by remember(existingPhoto?.id) { mutableStateOf(existingPhoto?.isPublished ?: true) }
    var pickedUriStrings by remember(existingPhoto?.id) {
        mutableStateOf(
            existingPhoto?.photoUri?.takeIf { it.isNotBlank() }?.let { listOf(it) } ?: emptyList()
        )
    }

    var catExpanded by remember { mutableStateOf(false) }
    var drawableExpanded by remember { mutableStateOf(false) }

    val singlePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val saved = MediaStorageHelper.copyPickerUriToInternalStorage(context, uri, "gallery")
            if (saved != null) {
                pickedUriStrings = listOf(saved)
                if (title.isBlank()) {
                    title = "$category Photo"
                }
            } else {
                viewModel.showSnackbar("Could not read selected image file.")
            }
        }
    }

    val multiPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 10)
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            val savedList = MediaStorageHelper.copyMultiplePickerUris(context, uris, "gallery")
            if (savedList.isNotEmpty()) {
                pickedUriStrings = if (isEditing) listOf(savedList.first()) else (pickedUriStrings + savedList).distinct()
                if (title.isBlank()) {
                    title = if (savedList.size > 1) "$category Highlight" else "$category Photo"
                }
            } else {
                viewModel.showSnackbar("Unable to import selected photos.")
            }
        }
    }

    val categories = listOf(
        "School Building",
        "Classrooms",
        "Teachers",
        "School Events",
        "Sports Activities",
        "Students' Activities",
        "Achievements"
    )

    val availableDrawables = listOf(
        "reel_fields" to "Campus Grounds & Mountain Backdrop",
        "reel_cricket" to "Inter-School Sports & Cricket",
        "reel_music" to "Cultural Function & Morning Assembly",
        "reel_chai" to "Staff Room & Faculty Gathering",
        "school_logo" to "School Official Crest Emblem",
        "student_avatar" to "Honor Roll Scholar Portrait"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, tint = RoyalBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isEditing) "Edit / Replace Gallery Photo" else "Upload Photos to Gallery",
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
                // Storage transparency notice
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFEFF6FF),
                    border = BorderStroke(1.dp, Color(0xFFBFDBFE))
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = RoyalBlue, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Photos are persisted locally in app storage. Firebase Storage bucket is not provisioned yet for remote CDN sync.",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF1E3A8A),
                            lineHeight = 15.sp
                        )
                    }
                }

                // Native Android Photo Picker buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            singlePickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).testTag("pick_single_photo_btn")
                    ) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isEditing) "Replace Photo" else "Pick Photo", fontSize = 12.sp)
                    }

                    if (!isEditing) {
                        OutlinedButton(
                            onClick = {
                                multiPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).testTag("pick_multi_photo_btn")
                        ) {
                            Icon(Icons.Default.Collections, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Pick Multiple", fontSize = 12.sp)
                        }
                    }
                }

                // Selected Photo Previews
                if (pickedUriStrings.isNotEmpty()) {
                    Text(
                        text = "Selected Photo Preview (${pickedUriStrings.size})",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        itemsIndexed(pickedUriStrings) { index, uriStr ->
                            Box(
                                modifier = Modifier
                                    .size(96.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            ) {
                                AsyncImage(
                                    model = uriStr,
                                    contentDescription = "Selected preview ${index + 1}",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                IconButton(
                                    onClick = {
                                        pickedUriStrings = pickedUriStrings.filterIndexed { i, _ -> i != index }
                                    },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp)
                                        .size(24.dp)
                                        .background(Color.Black.copy(alpha = 0.65f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove photo",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Student privacy disclaimer
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFEF3C7)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = Color(0xFFD97706))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Student Privacy Safeguard: Ensure guardian consent is maintained before publishing identifiable student photos.",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF92400E)
                        )
                    }
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Photo Title *") },
                    leadingIcon = { Icon(Icons.Default.Image, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("gallery_title_input"),
                    singleLine = true
                )

                ExposedDropdownMenuBox(
                    expanded = catExpanded,
                    onExpandedChange = { catExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        leadingIcon = { Icon(Icons.Default.Category, contentDescription = null) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = catExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = catExpanded,
                        onDismissRequest = { catExpanded = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    category = cat
                                    catExpanded = false
                                }
                            )
                        }
                    }
                }

                if (pickedUriStrings.isEmpty()) {
                    ExposedDropdownMenuBox(
                        expanded = drawableExpanded,
                        onExpandedChange = { drawableExpanded = it },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = availableDrawables.find { it.first == selectedDrawable }?.second ?: selectedDrawable,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Fallback Campus Asset (if no device photo picked)") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = drawableExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = drawableExpanded,
                            onDismissRequest = { drawableExpanded = false }
                        ) {
                            availableDrawables.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item.second) },
                                    onClick = {
                                        selectedDrawable = item.first
                                        drawableExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Photo Description") },
                    leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("gallery_desc_input"),
                    maxLines = 3
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Visible to Students & Public",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = DeepNavy
                    )
                    Switch(
                        checked = isPublished,
                        onCheckedChange = { isPublished = it }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (existingPhoto != null) {
                        val updated = existingPhoto.copy(
                            title = title.trim(),
                            description = description.trim(),
                            category = category,
                            drawableResName = selectedDrawable,
                            photoUri = pickedUriStrings.firstOrNull(),
                            isPublished = isPublished
                        )
                        viewModel.updateGalleryPhoto(updated)
                    } else if (pickedUriStrings.size > 1) {
                        viewModel.uploadMultipleGalleryPhotos(
                            titleBase = title.trim(),
                            description = description.trim(),
                            category = category,
                            photoUris = pickedUriStrings
                        )
                    } else {
                        viewModel.uploadGalleryPhotoWithUri(
                            title = title.trim(),
                            description = description.trim(),
                            category = category,
                            photoUri = pickedUriStrings.firstOrNull(),
                            drawableResName = selectedDrawable
                        )
                    }
                    onDismiss()
                },
                enabled = title.isNotBlank() || pickedUriStrings.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("save_photo_btn")
            ) {
                Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    when {
                        isEditing -> "Save Changes"
                        pickedUriStrings.size > 1 -> "Upload (${pickedUriStrings.size}) Photos"
                        else -> "Upload & Publish"
                    }
                )
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

