package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ZoomIn
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.model.GalleryPhoto
import com.example.ui.viewmodel.SchoolViewModel
import com.example.util.UrduLocalization

@Composable
fun PublicGalleryScreen(
    viewModel: SchoolViewModel,
    contentPadding: PaddingValues
) {
    val language by viewModel.currentLanguage.collectAsState()
    val isUrdu = language == UrduLocalization.Language.URDU
    val allPhotos by viewModel.allGalleryPhotos.collectAsState()
    val selectedCategory by viewModel.galleryCategoryFilter.collectAsState()
    val selectedPhoto by viewModel.selectedGalleryPhoto.collectAsState()
    val isAdmin by viewModel.isAdminLoggedIn.collectAsState()

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

    val filtered = allPhotos.filter {
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
                        color = Color(0xFF0B192C)
                    )
                    Text(
                        text = if (isUrdu) "بوائز ہائر سیکنڈری اسکول تولتی کی علمی، تدریسی و کھیلوں کی سرگرمیاں" else "Visual moments from academic labs, campus grounds, sports and achievements",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF64748B)
                    )
                }
            }

            // Student Privacy Safeguard Notice
            item {
                Surface(
                    color = Color(0xFFEFF6FF),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE))
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
                                selectedContainerColor = Color(0xFF0B192C),
                                selectedLabelColor = Color.White
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
                    onDelete = { viewModel.deleteGalleryPhoto(photo) }
                )
            }
        }

        // Upload FAB for Admin
        if (isAdmin) {
            FloatingActionButton(
                onClick = { viewModel.showUploadPhotoDialog.value = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(contentPadding)
                    .padding(16.dp)
                    .testTag("admin_upload_photo_fab"),
                containerColor = Color(0xFF0B192C),
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
    onDelete: () -> Unit
) {
    val drawableRes = when (photo.drawableResName) {
        "reel_cricket" -> R.drawable.reel_cricket
        "reel_chai" -> R.drawable.reel_chai
        "reel_fields" -> R.drawable.reel_fields
        "student_avatar" -> R.drawable.student_avatar
        else -> R.drawable.school_logo
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("gallery_card_${photo.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Image(
                    painter = painterResource(id = drawableRes),
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
                        color = Color(0xFF0B192C),
                        modifier = Modifier.weight(1f)
                    )

                    if (isAdmin) {
                        IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFDC2626), modifier = Modifier.size(18.dp))
                        }
                    }
                }

                Text(
                    text = photo.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF64748B),
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
        else -> R.drawable.school_logo
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp)),
            color = Color.White,
            tonalElevation = 8.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                ) {
                    Image(
                        painter = painterResource(id = drawableRes),
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
                            .size(32.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    Surface(color = Color(0xFF1D4ED8).copy(alpha = 0.12f), shape = RoundedCornerShape(4.dp)) {
                        Text(photo.category, fontSize = 10.sp, color = Color(0xFF1D4ED8), fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(photo.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFF0B192C))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(photo.description, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF475569))
                }
            }
        }
    }
}
