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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.AppDesignConfig
import com.example.data.model.CollegeAnnouncement
import com.example.data.model.GalleryPhoto
import com.example.data.model.SchoolSettingsRecord
import com.example.ui.components.CentralSchoolLogo
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.ElegantGold
import com.example.ui.theme.LocalAppDesignConfig
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.designButtonShape
import com.example.ui.theme.designCardBorder
import com.example.ui.theme.designCardColors
import com.example.ui.theme.designCardShape
import com.example.ui.viewmodel.SchoolAppSection
import com.example.ui.viewmodel.SchoolViewModel
import com.example.util.UrduLocalization

@Composable
fun PublicHomeScreen(
    viewModel: SchoolViewModel,
    contentPadding: PaddingValues
) {
    val language by viewModel.currentLanguage.collectAsState()
    val isUrdu = language == UrduLocalization.Language.URDU
    val announcements by viewModel.allAnnouncements.collectAsState()
    val teachers by viewModel.allTeachers.collectAsState()
    val settings by viewModel.schoolSettings.collectAsState()
    val publishedGalleryPhotos by viewModel.publishedGalleryPhotos.collectAsState()
    val isAdmin by viewModel.isAdminLoggedIn.collectAsState()
    val designConfig = LocalAppDesignConfig.current

    val visibleNotices = remember(announcements, isAdmin) {
        if (isAdmin) announcements else announcements.filter { it.isPublished }
    }
    val visibleSections = remember(designConfig.homeSectionOrder, designConfig.hiddenHomeSections) {
        designConfig.orderedHomeSections().filter { designConfig.isSectionVisible(it) }
    }

    var quickRollNumberSearch by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        // Optional Admin-configured background wallpaper with readable high-contrast scrim
        if (!designConfig.customBackgroundUri.isNullOrBlank()) {
            AsyncImage(
                model = designConfig.customBackgroundUri,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.85f))
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("public_home_screen"),
            contentPadding = PaddingValues(
                top = contentPadding.calculateTopPadding() + 8.dp,
                bottom = contentPadding.calculateBottomPadding() + 80.dp,
                start = 14.dp,
                end = 14.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(visibleSections, key = { it }) { sectionKey ->
                when (sectionKey) {
                    "HERO" -> HomeHeroBannerSection(
                        designConfig = designConfig,
                        isUrdu = isUrdu,
                        teacherCount = teachers.size
                    )

                    "QUICK_NAV" -> HomeQuickNavSection(
                        designConfig = designConfig,
                        isUrdu = isUrdu,
                        onNavigate = { viewModel.currentSection.value = it }
                    )

                    "HONOR_ROLL" -> HomeHonorRollSection(
                        designConfig = designConfig,
                        isUrdu = isUrdu
                    )

                    "PRINCIPAL" -> HomePrincipalMessageSection(
                        designConfig = designConfig,
                        isUrdu = isUrdu,
                        settings = settings
                    )

                    "NOTICES" -> HomeNoticesSection(
                        designConfig = designConfig,
                        isUrdu = isUrdu,
                        notices = visibleNotices,
                        onViewAll = { viewModel.currentSection.value = SchoolAppSection.ANNOUNCEMENTS },
                        onNoticeClick = { viewModel.selectedAnnouncementForDetails.value = it }
                    )

                    "GALLERY_PREVIEW" -> HomeGalleryPreviewSection(
                        designConfig = designConfig,
                        isUrdu = isUrdu,
                        photos = publishedGalleryPhotos,
                        onViewAll = { viewModel.currentSection.value = SchoolAppSection.GALLERY },
                        onPhotoClick = { viewModel.selectedGalleryPhoto.value = it }
                    )

                    "RESULTS_PORTAL" -> HomeResultsPortalSection(
                        designConfig = designConfig,
                        isUrdu = isUrdu,
                        quickRollNumberSearch = quickRollNumberSearch,
                        onRollNumberChange = { quickRollNumberSearch = it },
                        onSearchClick = {
                            viewModel.resultSearchQuery.value = quickRollNumberSearch
                            viewModel.subjectResultsSearchQuery.value = quickRollNumberSearch
                            viewModel.currentSection.value = SchoolAppSection.RESULTS
                        },
                        onOpenPortalClick = { viewModel.currentSection.value = SchoolAppSection.RESULTS }
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeHeroBannerSection(
    designConfig: AppDesignConfig,
    isUrdu: Boolean,
    teacherCount: Int
) {
    val primaryColor = AppDesignConfig.parseColorSafe(designConfig.primaryColorHex, DeepNavy)
    val secondaryColor = AppDesignConfig.parseColorSafe(designConfig.secondaryColorHex, RoyalBlue)
    val accentColor = AppDesignConfig.parseColorSafe(designConfig.accentColorHex, ElegantGold)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("school_welcome_banner"),
        shape = designCardShape(designConfig),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (!designConfig.customBannerUri.isNullOrBlank()) {
                AsyncImage(
                    model = designConfig.customBannerUri,
                    contentDescription = "School Campus Welcome Banner",
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    primaryColor.copy(alpha = 0.82f),
                                    secondaryColor.copy(alpha = 0.72f),
                                    primaryColor.copy(alpha = 0.88f)
                                )
                            )
                        )
                )
            } else {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    primaryColor,
                                    secondaryColor,
                                    secondaryColor.copy(alpha = 0.88f)
                                )
                            )
                        )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CentralSchoolLogo(
                    size = 76.dp,
                    isUrdu = isUrdu
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (isUrdu) "گورنمنٹ بوائز ہائر سیکنڈری اسکول تولتی" else "BOYS HIGHER SECONDARY SCHOOL TOLTI",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = if (isUrdu) 20.sp else 18.sp,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    color = accentColor,
                    shape = designButtonShape(designConfig)
                ) {
                    Text(
                        text = if (isUrdu) "علم حاصل کرو • آگے بڑھو • کامیاب بنو" else "LEARN • GROW • SUCCEED",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (isUrdu) "ضلع کھرمنگ، گلگت بلتستان • 5ویں سے 12ویں جماعت تک معیاری تعلیم" else "District Kharmang, Gilgit-Baltistan • Classes 5th to 12th (Matric & ICS)",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFE2E8F0),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    HeroStatPill(number = "5th-12th", label = if (isUrdu) "کلاسیں" else "Class Wings")
                    HeroStatPill(number = "100%", label = if (isUrdu) "بورڈ نتیجہ" else "Pass Rate")
                    HeroStatPill(number = "$teacherCount", label = if (isUrdu) "اساتذہ" else "Faculty")
                    HeroStatPill(number = "1978", label = if (isUrdu) "قیام" else "Est.")
                }
            }
        }
    }
}

@Composable
private fun HomeQuickNavSection(
    designConfig: AppDesignConfig,
    isUrdu: Boolean,
    onNavigate: (SchoolAppSection) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = if (isUrdu) "اسکول پورٹل شعبہ جات" else "School Portal Services",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SectionNavCard(
                modifier = Modifier.weight(1f),
                designConfig = designConfig,
                icon = Icons.Default.EventAvailable,
                title = if (isUrdu) "حاضری ریکارڈ" else "Attendance",
                subtitle = if (isUrdu) "یومیہ و مضمون حاضری" else "Daily & Subject Status",
                color = Color(0xFF0F5132),
                onClick = { onNavigate(SchoolAppSection.ATTENDANCE) }
            )
            SectionNavCard(
                modifier = Modifier.weight(1f),
                designConfig = designConfig,
                icon = Icons.Default.Campaign,
                title = if (isUrdu) "اسکول اعلانات" else "Announcements",
                subtitle = if (isUrdu) "امتحانی نوٹس و تعطیلات" else "Notices & Circulars",
                color = MaterialTheme.colorScheme.secondary,
                onClick = { onNavigate(SchoolAppSection.ANNOUNCEMENTS) }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SectionNavCard(
                modifier = Modifier.weight(1f),
                designConfig = designConfig,
                icon = Icons.Default.School,
                title = if (isUrdu) "اساتذہ کرام" else "Our Teachers",
                subtitle = if (isUrdu) "فیکلٹی و شعبہ جات" else "Faculty Directory & Rating",
                color = MaterialTheme.colorScheme.primary,
                onClick = { onNavigate(SchoolAppSection.TEACHERS) }
            )
            SectionNavCard(
                modifier = Modifier.weight(1f),
                designConfig = designConfig,
                icon = Icons.Default.CalendarMonth,
                title = if (isUrdu) "اسکول شیڈول" else "Timetable",
                subtitle = if (isUrdu) "کلاس و پیریڈز" else "Class Schedule",
                color = Color(0xFF059669),
                onClick = { onNavigate(SchoolAppSection.TIMETABLE) }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SectionNavCard(
                modifier = Modifier.weight(1f),
                designConfig = designConfig,
                icon = Icons.Default.MenuBook,
                title = if (isUrdu) "ہوم ورک" else "Homework",
                subtitle = if (isUrdu) "روزانہ اسائنمنٹس" else "Assignments & Due Dates",
                color = Color(0xFFB45309),
                onClick = { onNavigate(SchoolAppSection.HOMEWORK) }
            )
            SectionNavCard(
                modifier = Modifier.weight(1f),
                designConfig = designConfig,
                icon = Icons.Default.Collections,
                title = if (isUrdu) "اسکول گیلری" else "School Gallery",
                subtitle = if (isUrdu) "کھیل و سرگرمیاں" else "Photos & Events",
                color = Color(0xFF7C3AED),
                onClick = { onNavigate(SchoolAppSection.GALLERY) }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SectionNavCard(
                modifier = Modifier.weight(1f),
                designConfig = designConfig,
                icon = Icons.Default.Info,
                title = if (isUrdu) "ہمارے بارے میں" else "About & Contact",
                subtitle = if (isUrdu) "پتہ و معلومات" else "Info & Inquiries",
                color = Color(0xFF0284C7),
                onClick = { onNavigate(SchoolAppSection.ABOUT_CONTACT) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        SectionNavCard(
            modifier = Modifier.fillMaxWidth(),
            designConfig = designConfig,
            icon = Icons.Default.Grade,
            title = if (isUrdu) "امتحانی نتائج" else "Student Results",
            subtitle = if (isUrdu) "مارک شیٹس، سالانہ بورڈ نتائج اور رزلٹ کارڈز" else "Official Marksheets, Board Results & Result Cards",
            color = AppDesignConfig.parseColorSafe(designConfig.accentColorHex, Color(0xFFD97706)),
            isBold = true,
            onClick = { onNavigate(SchoolAppSection.RESULTS) }
        )
    }
}

@Composable
private fun HomeHonorRollSection(
    designConfig: AppDesignConfig,
    isUrdu: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("student_honor_roll_card"),
        shape = designCardShape(designConfig),
        colors = designCardColors(designConfig),
        border = designCardBorder(designConfig),
        elevation = CardDefaults.cardElevation(defaultElevation = if (designConfig.cardStyle == "OUTLINED") 0.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary),
                modifier = Modifier.size(68.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.student_avatar),
                    contentDescription = "Honor Roll Student",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    color = Color(0xFFFEF3C7),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = if (isUrdu) "نمایاں طالبعلم • سیشن 2026" else "HONOR ROLL SCHOLAR • 2026",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB45309),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Muhammad Khan",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (isUrdu) "کلاس 12ویں (آئی سی ایس) • فرسٹ پوزیشن (95%)" else "Class 12th (ICS) • First Position (95%)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = if (isUrdu) "حاضری تناسب: 96% • رول نمبر: BHSS-2026-104" else "Attendance: 96% • Roll: BHSS-2026-104",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun HomePrincipalMessageSection(
    designConfig: AppDesignConfig,
    isUrdu: Boolean,
    settings: SchoolSettingsRecord?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("principal_message_card"),
        shape = designCardShape(designConfig),
        colors = designCardColors(designConfig),
        border = designCardBorder(designConfig),
        elevation = CardDefaults.cardElevation(defaultElevation = if (designConfig.cardStyle == "OUTLINED") 0.dp else 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                    modifier = Modifier.size(46.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = settings?.principalName ?: "Sir Ghulam Nabi (M.Sc, M.Ed)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isUrdu) "ہیڈ ماسٹر / پرنسپل کا پیغام" else "Principal & Head Administrator's Message",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (isUrdu)
                    "\"ہمارا بنیادی مشن کھرمنگ کے نونہالان کو اعلیٰ درجے کی جدید سائنسی تعلیم، انفارمیشن ٹیکنالوجی اور اسلامی و اخلاقی اقدار سے آراستہ کرنا ہے۔ ہم ہر طالب علم کو چیلنجز کا سامنا کرنے اور وطن کا روشن مستقبل بننے کے لیے تیار کرتے ہیں۔\""
                else
                    "\"Boys Higher Secondary School Tolti stands as a beacon of academic rigor and moral excellence in Kharmang. From modern Computer Science labs to state-of-the-art physics practicals, our mission is empowering our youth with knowledge, critical thought, and character.\"",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
private fun HomeNoticesSection(
    designConfig: AppDesignConfig,
    isUrdu: Boolean,
    notices: List<CollegeAnnouncement>,
    onViewAll: () -> Unit,
    onNoticeClick: (CollegeAnnouncement) -> Unit
) {
    val maxCount = if (designConfig.announcementsLayout == "COMPACT") 3 else 2

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isUrdu) "تازہ ترین سرکاری اعلانات" else "Latest Official Circulars",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = if (isUrdu) "سب دیکھیں →" else "View All →",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .clickable { onViewAll() }
                    .padding(horizontal = 4.dp, vertical = 6.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        notices.take(maxCount).forEach { ann ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onNoticeClick(ann) },
                shape = designCardShape(designConfig),
                colors = designCardColors(designConfig),
                border = designCardBorder(designConfig),
                elevation = CardDefaults.cardElevation(defaultElevation = if (designConfig.cardStyle == "OUTLINED") 0.dp else 1.dp)
            ) {
                Column(modifier = Modifier.padding(if (designConfig.announcementsLayout == "COMPACT") 10.dp else 12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = ann.category,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        if (ann.isUrgent) {
                            Surface(color = Color(0xFFDC2626), shape = RoundedCornerShape(4.dp)) {
                                Text(
                                    text = "URGENT",
                                    fontSize = 9.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = ann.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (designConfig.announcementsLayout != "COMPACT") {
                        Text(
                            text = ann.content,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeGalleryPreviewSection(
    designConfig: AppDesignConfig,
    isUrdu: Boolean,
    photos: List<GalleryPhoto>,
    onViewAll: () -> Unit,
    onPhotoClick: (GalleryPhoto) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("home_gallery_preview_section")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isUrdu) "اسکول تصویری جھلکیاں" else "Campus Photo Highlights",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = if (isUrdu) "گیلری دیکھیں →" else "View All →",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .clickable { onViewAll() }
                    .padding(horizontal = 4.dp, vertical = 6.dp)
                    .testTag("home_gallery_view_all")
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (photos.isEmpty()) {
            Card(
                shape = designCardShape(designConfig),
                colors = designCardColors(designConfig),
                border = designCardBorder(designConfig)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isUrdu) "گیلری میں مزید تصاویر شامل کی جا رہی ہیں" else "Explore campus moments in the School Gallery",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(onClick = onViewAll, shape = designButtonShape(designConfig)) {
                        Text(if (isUrdu) "کھولیں" else "Open", fontSize = 11.sp)
                    }
                }
            }
        } else {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(photos.take(6), key = { it.id }) { photo ->
                    val fallbackRes = resolveHomeGalleryDrawable(photo.drawableResName)
                    Card(
                        modifier = Modifier
                            .width(170.dp)
                            .clip(designCardShape(designConfig))
                            .clickable { onPhotoClick(photo) }
                            .testTag("home_gallery_item_${photo.id}"),
                        shape = designCardShape(designConfig),
                        colors = designCardColors(designConfig),
                        border = designCardBorder(designConfig),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(104.dp)
                            ) {
                                if (!photo.photoUri.isNullOrBlank()) {
                                    AsyncImage(
                                        model = photo.photoUri,
                                        contentDescription = photo.title,
                                        placeholder = painterResource(id = fallbackRes),
                                        error = painterResource(id = fallbackRes),
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(id = fallbackRes),
                                        contentDescription = photo.title,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                Surface(
                                    color = Color(0xFF0B192C).copy(alpha = 0.8f),
                                    shape = RoundedCornerShape(bottomEnd = 8.dp),
                                    modifier = Modifier.align(Alignment.TopStart)
                                ) {
                                    Text(
                                        text = photo.category,
                                        color = Color(0xFFFBBF24),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = photo.title,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeResultsPortalSection(
    designConfig: AppDesignConfig,
    isUrdu: Boolean,
    quickRollNumberSearch: String,
    onRollNumberChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onOpenPortalClick: () -> Unit
) {
    val accentColor = AppDesignConfig.parseColorSafe(designConfig.accentColorHex, Color(0xFFD97706))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("bottom_bold_results_section_card"),
        shape = designCardShape(designConfig),
        colors = designCardColors(designConfig),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = BorderStroke(2.dp, accentColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = accentColor.copy(alpha = 0.15f),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Grade,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isUrdu) "امتحانی نتائج" else "Student Results",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 17.sp
                        )
                        Text(
                            text = if (isUrdu) "سرکاری مارک شیٹس و امتحانی ریکارڈ" else "Official Marksheets & Examination Records",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = accentColor,
                            fontSize = 11.sp
                        )
                    }
                }

                Surface(
                    color = Color(0xFFFEF3C7),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFF59E0B))
                ) {
                    Text(
                        text = "★ OFFICIAL",
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFB45309),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (isUrdu)
                    "بورڈ امتحانات، سالانہ، ششماہی اور ماہانہ امتحانات کے تصدیق شدہ نتائج، تفصیلی مارک شیٹس اور گریڈز حاصل کریں۔"
                else
                    "Access official annual board, midterm, and monthly exam results, detailed marks cards, percentage, and pass/fail certificates.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = quickRollNumberSearch,
                    onValueChange = onRollNumberChange,
                    placeholder = { Text("e.g. BHSS-2026-104", fontSize = 13.sp) },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("quick_roll_number_input"),
                    shape = designButtonShape(designConfig)
                )

                Button(
                    onClick = onSearchClick,
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                    shape = designButtonShape(designConfig),
                    border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                    modifier = Modifier
                        .height(52.dp)
                        .testTag("search_result_btn")
                ) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isUrdu) "تلاش کریں" else "SEARCH",
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onOpenPortalClick,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = designButtonShape(designConfig),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("open_results_portal_btn")
            ) {
                Icon(Icons.Default.Grade, contentDescription = null, tint = ElegantGold, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isUrdu) "مکمل امتحانی نتائج پورٹل کھولیں" else "OPEN STUDENT RESULTS PORTAL",
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 13.sp,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

private fun resolveHomeGalleryDrawable(drawableResName: String): Int {
    return when (drawableResName) {
        "reel_cricket" -> R.drawable.reel_cricket
        "reel_chai" -> R.drawable.reel_chai
        "reel_fields" -> R.drawable.reel_fields
        "student_avatar" -> R.drawable.student_avatar
        "reel_music" -> R.drawable.reel_music
        else -> R.drawable.school_logo
    }
}

@Composable
private fun HeroStatPill(number: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = number,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFFFBBF24),
            fontSize = 17.sp
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = Color.White.copy(alpha = 0.9f)
        )
    }
}

@Composable
private fun SectionNavCard(
    modifier: Modifier = Modifier,
    designConfig: AppDesignConfig,
    icon: ImageVector,
    title: String,
    subtitle: String,
    color: Color,
    isBold: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(designCardShape(designConfig))
            .clickable { onClick() }
            .testTag("nav_card_${title.replace(" ", "_").lowercase()}"),
        shape = designCardShape(designConfig),
        colors = if (isBold) {
            CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB))
        } else {
            designCardColors(designConfig)
        },
        elevation = CardDefaults.cardElevation(defaultElevation = if (isBold) 4.dp else if (designConfig.cardStyle == "OUTLINED") 0.dp else 2.dp),
        border = if (isBold) BorderStroke(2.dp, color) else designCardBorder(designConfig)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = color.copy(alpha = if (isBold) 0.22f else 0.12f),
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = color,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                if (isBold) {
                    Surface(
                        color = color,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "★ OFFICIAL",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = if (isBold) FontWeight.Black else FontWeight.Bold,
                fontSize = if (isBold) 15.sp else 14.sp,
                color = if (isBold) Color(0xFFB45309) else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                color = if (isBold) Color(0xFF92400E) else MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.sp
            )
        }
    }
}
