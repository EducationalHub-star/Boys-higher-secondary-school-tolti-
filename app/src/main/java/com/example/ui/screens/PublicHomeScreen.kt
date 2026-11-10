package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.R
import com.example.ui.components.CentralSchoolLogo
import com.example.ui.components.SchoolEmblem
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.ElegantGold
import com.example.ui.theme.RoyalBlue
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
    val results by viewModel.publishedResults.collectAsState()
    val settings by viewModel.schoolSettings.collectAsState()

    var quickRollNumberSearch by remember { mutableStateOf("") }

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
        // 1. Grand Welcome Hero Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("school_welcome_banner"),
                shape = RoundedCornerShape(22.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFF0B192C), // Deep Navy
                                    Color(0xFF1E3A8A), // Royal Blue Navy
                                    Color(0xFF1D4ED8)
                                )
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Central School Logo Design (Academic Insignia & Crest)
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

                        // Tagline in Gold
                        Surface(
                            color = Color(0xFFD97706),
                            shape = RoundedCornerShape(16.dp)
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

                        // Quick Stats Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            HeroStatPill(number = "5th-12th", label = if (isUrdu) "کلاسیں" else "Class Wings")
                            HeroStatPill(number = "100%", label = if (isUrdu) "بورڈ نتیجہ" else "Pass Rate")
                            HeroStatPill(number = "${teachers.size}", label = if (isUrdu) "اساتذہ" else "Faculty")
                            HeroStatPill(number = "1978", label = if (isUrdu) "قیام" else "Est.")
                        }
                    }
                }
            }
        }

        // 2. Public Navigation Grid (School Portal Services)
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (isUrdu) "اسکول پورٹل شعبہ جات" else "School Portal Services",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0B192C)
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SectionNavCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.EventAvailable,
                        title = if (isUrdu) "حاضری ریکارڈ" else "Attendance",
                        subtitle = if (isUrdu) "یومیہ و مضمون حاضری" else "Daily & Subject Status",
                        color = Color(0xFF0F5132),
                        onClick = { viewModel.currentSection.value = SchoolAppSection.ATTENDANCE }
                    )
                    SectionNavCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Campaign,
                        title = if (isUrdu) "اسکول اعلانات" else "Announcements",
                        subtitle = if (isUrdu) "امتحانی نوٹس و تعطیلات" else "Notices & Circulars",
                        color = Color(0xFF1D4ED8),
                        onClick = { viewModel.currentSection.value = SchoolAppSection.ANNOUNCEMENTS }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SectionNavCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.School,
                        title = if (isUrdu) "اساتذہ کرام" else "Our Teachers",
                        subtitle = if (isUrdu) "فیکلٹی و شعبہ جات" else "Faculty Directory",
                        color = Color(0xFF0B192C),
                        onClick = { viewModel.currentSection.value = SchoolAppSection.TEACHERS }
                    )
                    SectionNavCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.CalendarMonth,
                        title = if (isUrdu) "اسکول شیڈول" else "Timetable",
                        subtitle = if (isUrdu) "کلاس و پیریڈز" else "Class Schedule",
                        color = Color(0xFF059669),
                        onClick = { viewModel.currentSection.value = SchoolAppSection.TIMETABLE }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SectionNavCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.MenuBook,
                        title = if (isUrdu) "ہوم ورک" else "Homework",
                        subtitle = if (isUrdu) "روزانہ اسائنمنٹس" else "Assignments & Due Dates",
                        color = Color(0xFFB45309),
                        onClick = { viewModel.currentSection.value = SchoolAppSection.HOMEWORK }
                    )
                    SectionNavCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Collections,
                        title = if (isUrdu) "اسکول گیلری" else "School Gallery",
                        subtitle = if (isUrdu) "کھیل و سرگرمیاں" else "Photos & Events",
                        color = Color(0xFF7C3AED),
                        onClick = { viewModel.currentSection.value = SchoolAppSection.GALLERY }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SectionNavCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Info,
                        title = if (isUrdu) "ہمارے بارے میں" else "About & Contact",
                        subtitle = if (isUrdu) "پتہ و معلومات" else "Info & Inquiries",
                        color = Color(0xFF0284C7),
                        onClick = { viewModel.currentSection.value = SchoolAppSection.ABOUT_CONTACT }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Bottom BOLDED Result Option
                SectionNavCard(
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.Grade,
                    title = if (isUrdu) "امتحانی نتائج" else "Student Results",
                    subtitle = if (isUrdu) "مارک شیٹس، سالانہ بورڈ نتائج اور رزلٹ کارڈز" else "Official Marksheets, Board Results & Result Cards",
                    color = Color(0xFFD97706),
                    isBold = true,
                    onClick = { viewModel.currentSection.value = SchoolAppSection.RESULTS }
                )
            }
        }

        // Student Honor Roll Spotlight Card (Featuring uploaded student image)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("student_honor_roll_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        border = BorderStroke(2.dp, Color(0xFFD97706)),
                        modifier = Modifier.size(68.dp)
                    ) {
                        Image(
                            painter = painterResource(id = com.example.R.drawable.student_avatar),
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
                            color = Color(0xFF0B192C)
                        )
                        Text(
                            text = if (isUrdu) "کلاس 12ویں (آئی سی ایس) • فرسٹ پوزیشن (95%)" else "Class 12th (ICS) • First Position (95%)",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF1D4ED8),
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = if (isUrdu) "حاضری تناسب: 96% • رول نمبر: BHSS-2026-104" else "Attendance: 96% • Roll: BHSS-2026-104",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }
        }

        // 4. Principal's Message & Leadership
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("principal_message_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF0B192C).copy(alpha = 0.12f),
                            modifier = Modifier.size(46.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color(0xFF0B192C),
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
                                color = Color(0xFF0B192C)
                            )
                            Text(
                                text = if (isUrdu) "ہیڈ ماسٹر / پرنسپل کا پیغام" else "Principal & Head Administrator's Message",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFD97706),
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
                        color = Color(0xFF334155),
                        lineHeight = 22.sp
                    )
                }
            }
        }

        // 5. Recent Official Announcements Carousel
        item {
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
                        color = Color(0xFF0B192C)
                    )
                    Text(
                        text = if (isUrdu) "سب دیکھیں →" else "View All →",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1D4ED8),
                        modifier = Modifier.clickable { viewModel.currentSection.value = SchoolAppSection.ANNOUNCEMENTS }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                announcements.take(2).forEach { ann ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { viewModel.selectedAnnouncementForDetails.value = ann },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Surface(
                                    color = Color(0xFF1D4ED8).copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = ann.category,
                                        fontSize = 10.sp,
                                        color = Color(0xFF1D4ED8),
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
                                color = Color(0xFF111827)
                            )
                            Text(
                                text = ann.content,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF475569),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        // 6. Student Examination Results (Positioned at the bottom and prominently BOLDED)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("bottom_bold_results_section_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                border = BorderStroke(2.dp, Color(0xFFD97706))
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
                                color = Color(0xFFD97706).copy(alpha = 0.15f),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Grade,
                                        contentDescription = null,
                                        tint = Color(0xFFD97706),
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
                                    color = Color(0xFF0B192C),
                                    fontSize = 17.sp
                                )
                                Text(
                                    text = if (isUrdu) "سرکاری مارک شیٹس و امتحانی ریکارڈ" else "Official Marksheets & Examination Records",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFD97706),
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
                        color = Color(0xFF475569)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Roll Number Search input and search button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = quickRollNumberSearch,
                            onValueChange = { quickRollNumberSearch = it },
                            placeholder = { Text("e.g. BHSS-2026-104", fontSize = 13.sp) },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("quick_roll_number_input"),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Button(
                            onClick = {
                                viewModel.resultSearchQuery.value = quickRollNumberSearch
                                viewModel.currentSection.value = SchoolAppSection.RESULTS
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                            modifier = Modifier
                                .height(50.dp)
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

                    // Big Bold Button to view all results
                    Button(
                        onClick = { viewModel.currentSection.value = SchoolAppSection.RESULTS },
                        colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("open_results_portal_btn")
                    ) {
                        Icon(Icons.Default.Grade, contentDescription = null, tint = ElegantGold, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isUrdu) "مکمل امتحانی نتائج پورٹل کھولیں" else "OPEN STUDENT RESULTS PORTAL",
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            fontSize = 13.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroStatPill(number: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = number,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFFFBBF24), // Gold
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
    icon: ImageVector,
    title: String,
    subtitle: String,
    color: Color,
    isBold: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag("nav_card_${title.replace(" ", "_").lowercase()}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isBold) Color(0xFFFFFBEB) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isBold) 4.dp else 2.dp),
        border = if (isBold) BorderStroke(2.dp, Color(0xFFD97706)) else BorderStroke(1.dp, Color(0xFFE2E8F0))
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
                        color = Color(0xFFD97706),
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
                color = if (isBold) Color(0xFFB45309) else Color(0xFF0B192C)
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                color = if (isBold) Color(0xFF92400E) else Color(0xFF64748B),
                fontSize = 10.sp
            )
        }
    }
}
