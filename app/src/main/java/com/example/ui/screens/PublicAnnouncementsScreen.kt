package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.data.model.CollegeAnnouncement
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.ElegantGold
import com.example.ui.theme.RoyalBlue
import com.example.ui.viewmodel.SchoolViewModel
import com.example.util.UrduLocalization
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicAnnouncementsScreen(
    viewModel: SchoolViewModel,
    contentPadding: PaddingValues = PaddingValues()
) {
    val announcements by viewModel.allAnnouncements.collectAsState()
    val isAdmin by viewModel.isAdminLoggedIn.collectAsState()
    val lang by viewModel.currentLanguage.collectAsState()
    val isUrdu = lang == UrduLocalization.Language.URDU

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("ALL") }

    val categories = listOf("ALL", "Academic", "Exams", "Faculty", "Admission", "Sports", "General")

    val filteredList = announcements.filter { ann ->
        val matchesCategory = if (selectedCategory == "ALL") true else ann.category.equals(selectedCategory, ignoreCase = true)
        val matchesSearch = searchQuery.isBlank() ||
                ann.title.contains(searchQuery, ignoreCase = true) ||
                ann.content.contains(searchQuery, ignoreCase = true) ||
                ann.author.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .background(Color(0xFFF8FAFC))
            .testTag("public_announcements_screen")
    ) {
        // Header
        Surface(
            color = Color.White,
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isUrdu) "سرکاری اعلانات اور نوٹس بورڈ" else "Official Circulars & Notices",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = DeepNavy
                        )
                        Text(
                            text = if (isUrdu) "بوائز ہائر سیکنڈری اسکول تولتی کی اہم خبریں اور نوٹیفیکیشنز" else "Official announcements from the Principal Office, BHSS Tolti",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF64748B)
                        )
                    }

                    if (isAdmin) {
                        Button(
                            onClick = { viewModel.showBroadcastAnnouncementDialog.value = true },
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("admin_broadcast_notice_btn")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isUrdu) "نیا اعلان" else "Broadcast")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Search
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(if (isUrdu) "عنوان یا متن کے ذریعے تلاش کریں..." else "Search notices by keyword or subject...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = RoyalBlue) },
                    modifier = Modifier.fillMaxWidth().testTag("announcement_search_input"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Category Chips
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(if (cat == "ALL") "All Categories" else cat) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = RoyalBlue,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Campaign, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isUrdu) "کوئی اعلان نہیں ملا" else "No announcements found.",
                        color = Color(0xFF64748B),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredList, key = { it.id }) { ann ->
                    AnnouncementCard(
                        announcement = ann,
                        onClick = { viewModel.selectedAnnouncementForDetails.value = ann }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun AnnouncementCard(
    announcement: CollegeAnnouncement,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("notice_card_${announcement.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (announcement.isPinned) {
                        Surface(
                            shape = CircleShape,
                            color = ElegantGold.copy(alpha = 0.2f),
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.PushPin, contentDescription = "Pinned", tint = ElegantGold, modifier = Modifier.size(14.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = when (announcement.category.uppercase()) {
                            "EXAMS" -> Color(0xFFFEF2F2)
                            "ACADEMIC" -> Color(0xFFEFF6FF)
                            "SPORTS" -> Color(0xFFF0FDF4)
                            else -> Color(0xFFF8FAFC)
                        }
                    ) {
                        Text(
                            text = announcement.category.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (announcement.category.uppercase()) {
                                "EXAMS" -> Color(0xFFDC2626)
                                "ACADEMIC" -> RoyalBlue
                                "SPORTS" -> Color(0xFF16A34A)
                                else -> DeepNavy
                            },
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Text(
                    text = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(Date(announcement.timestamp)),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF94A3B8)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = announcement.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = DeepNavy
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = announcement.content,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF475569),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "By ${announcement.author}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Tap to read full circular →",
                    style = MaterialTheme.typography.labelSmall,
                    color = RoyalBlue,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
