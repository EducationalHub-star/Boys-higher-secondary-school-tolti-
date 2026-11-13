package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.GalleryPhoto
import com.example.ui.theme.LocalAppDesignConfig
import com.example.ui.theme.designButtonShape
import com.example.ui.theme.designCardBorder
import com.example.ui.theme.designCardColors
import com.example.ui.theme.designCardShape
import com.example.ui.viewmodel.DeleteTarget
import com.example.ui.viewmodel.SchoolViewModel
import com.example.util.MediaStorageHelper
import com.example.util.UrduLocalization

@Composable
fun PublicGalleryScreen(
    viewModel: SchoolViewModel,
    contentPadding: PaddingValues
) {
    val language by viewModel.currentLanguage.collectAsState()
    val isUrdu = language == UrduLocalization.Language.URDU
    val allPhotos by viewModel.allGalleryPhotos.collectAsState()
    val publishedPhotos by viewModel.publishedGalleryPhotos.collectAsState()
    val selectedCategory by viewModel.galleryCategoryFilter.collectAsState()
    val selectedPhoto by viewModel.selectedGalleryPhoto.collectAsState()
    val isAdmin by viewModel.isAdminLoggedIn.collectAsState()
    val designConfig = LocalAppDesignConfig.current

    val categories = listOf(
        "ALL",
        "School Building",
        "Classrooms",
        "Teachers",
        "School Events",
        "Sports Activities",
        "Students' Activities",
        "Achievements"
    )

    val sourcePhotos = if (isAdmin) allPhotos else publishedPhotos
    val filtered = sourcePhotos.filter {
        selectedCategory == "ALL" || it.category == selectedCategory
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("public_gallery_screen"),
            contentPadding = PaddingValues(
                top = contentPadding.calculateTopPadding() + 8.dp,
                bottom = contentPadding.calculateBottomPadding() + 80.dp,
                start = 14.dp,
                end = 14.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            item {
                Column {
                    Text(
                        text = if (isUrdu) "اسکول تصویری گیلری" else "Campus Photo Gallery",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = if (isUrdu) "بوائز ہائر سیکنڈری اسکول تولتی کی علمی، تدریسی و کھیلوں کی سرگرمیاں" else "Visual moments from academic labs, campus grounds, sports and achievements",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Admin Direct Upload Card
            if (isAdmin) {
                item {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = designCardShape(designConfig),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Admin Photo Manager",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = "Upload single or multiple photos from your phone gallery",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    viewModel.editingPhoto.value = null
                                    viewModel.showUploadPhotoDialog.value = true
                                },
                                shape = designButtonShape(designConfig),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                modifier = Modifier.testTag("gallery_admin_upload_btn")
                            ) {
                                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Upload", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Student Privacy Safeguard Notice
            item {
                Surface(
                    color = Color(0xFFEFF6FF),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFBFDBFE))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = Color(0xFF1D4ED8), modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Student Privacy Protected: Demonstrations and co-curricular photographs are published with guardian consent in accordance with school policy.",
                            fontSize = 11.sp,
                            color = Color(0xFF1E3A8A),
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Categories LazyRow
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categories) { cat ->
                        val isSelected = selectedCategory == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.galleryCategoryFilter.value = cat },
                            label = { Text(cat, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }
            }

            // Photo Cards Grid/List
            items(filtered, key = { it.id }) { photo ->
                GalleryPhotoCard(
                    photo = photo,
                    isAdmin = isAdmin,
                    onClick = { viewModel.selectedGalleryPhoto.value = photo },
                    onEdit = {
                        viewModel.editingPhoto.value = photo
                        viewModel.showUploadPhotoDialog.value = true
                    },
                    onTogglePublish = { viewModel.toggleGalleryPhotoPublish(photo) },
                    onDelete = { viewModel.requestDelete(DeleteTarget.Photo(photo)) }
                )
            }
        }

        // Upload FAB for Admin
        if (isAdmin) {
            FloatingActionButton(
                onClick = {
                    viewModel.editingPhoto.value = null
                    viewModel.showUploadPhotoDialog.value = true
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(contentPadding)
                    .padding(16.dp)
                    .testTag("admin_upload_photo_fab"),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color(0xFFFBBF24)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Upload Photo")
            }
        }
    }

    // Photo Lightbox Dialog
    selectedPhoto?.let { photo ->
        PhotoLightboxDialog(
            photo = photo,
            onDismiss = { viewModel.selectedGalleryPhoto.value = null }
        )
    }
}

@Composable
private fun GalleryPhotoCard(
    photo: GalleryPhoto,
    isAdmin: Boolean,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onTogglePublish: () -> Unit,
    onDelete: () -> Unit
) {
    val designConfig = LocalAppDesignConfig.current
    val drawableRes = when (photo.drawableResName) {
        "reel_cricket" -> R.drawable.reel_cricket
        "reel_chai" -> R.drawable.reel_chai
        "reel_fields" -> R.drawable.reel_fields
        "student_avatar" -> R.drawable.student_avatar
        "reel_music" -> R.drawable.reel_music
        else -> R.drawable.school_logo
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(designCardShape(designConfig))
            .clickable { onClick() }
            .testTag("gallery_card_${photo.id}"),
        shape = designCardShape(designConfig),
        colors = designCardColors(designConfig),
        border = designCardBorder(designConfig),
        elevation = CardDefaults.cardElevation(defaultElevation = if (designConfig.cardStyle == "OUTLINED") 0.dp else 3.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
            ) {
                val imageModel = remember(photo.photoUri, drawableRes) {
                    MediaStorageHelper.resolvePhotoModel(photo.photoUri) ?: drawableRes
                }
                AsyncImage(
                    model = imageModel,
                    contentDescription = photo.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Category tag on image
                Surface(
                    color = Color(0xFF0B192C).copy(alpha = 0.85f),
                    shape = RoundedCornerShape(bottomEnd = 12.dp),
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        text = photo.category,
                        color = Color(0xFFFBBF24),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                if (isAdmin && !photo.isPublished) {
                    Surface(
                        color = Color(0xFFDC2626).copy(alpha = 0.9f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "HIDDEN",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                // Zoom icon
                Surface(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = CircleShape,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Default.ZoomIn, contentDescription = "Enlarge", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = photo.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )

                    if (isAdmin) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = onEdit,
                                modifier = Modifier.size(48.dp).testTag("edit_photo_btn_${photo.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Photo",
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            IconButton(
                                onClick = onTogglePublish,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    imageVector = if (photo.isPublished) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (photo.isPublished) "Hide Photo" else "Publish Photo",
                                    tint = if (photo.isPublished) Color(0xFF059669) else Color(0xFF64748B),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            IconButton(
                                onClick = onDelete,
                                modifier = Modifier.size(48.dp).testTag("delete_photo_btn_${photo.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Photo",
                                    tint = Color(0xFFDC2626),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                Text(
                    text = photo.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun PhotoLightboxDialog(
    photo: GalleryPhoto,
    onDismiss: () -> Unit
) {
    val drawableRes = when (photo.drawableResName) {
        "reel_cricket" -> R.drawable.reel_cricket
        "reel_chai" -> R.drawable.reel_chai
        "reel_fields" -> R.drawable.reel_fields
        "student_avatar" -> R.drawable.student_avatar
        "reel_music" -> R.drawable.reel_music
        else -> R.drawable.school_logo
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp)),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                ) {
                    val detailImageModel = remember(photo.photoUri, drawableRes) {
                        MediaStorageHelper.resolvePhotoModel(photo.photoUri) ?: drawableRes
                    }
                    AsyncImage(
                        model = detailImageModel,
                        contentDescription = photo.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                            .size(48.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    Surface(color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.12f), shape = RoundedCornerShape(4.dp)) {
                        Text(photo.category, fontSize = 10.sp, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(photo.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(photo.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

