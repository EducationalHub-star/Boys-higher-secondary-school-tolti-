package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SportsHandball
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.SchoolEmblem
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.ElegantGold
import com.example.ui.theme.RoyalBlue
import com.example.ui.viewmodel.SchoolViewModel
import com.example.util.UrduLocalization

@Composable
fun PublicAboutContactScreen(
    viewModel: SchoolViewModel,
    contentPadding: PaddingValues = PaddingValues()
) {
    val lang by viewModel.currentLanguage.collectAsState()
    val isUrdu = lang == UrduLocalization.Language.URDU
    val settings by viewModel.schoolSettings.collectAsState()

    var selectedSubTab by remember { mutableIntStateOf(0) } // 0: About School, 1: Contact & Inquiry

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .background(Color(0xFFF8FAFC))
            .testTag("about_contact_screen")
    ) {
        // Tab header
        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = Color.White,
            contentColor = RoyalBlue
        ) {
            Tab(
                selected = selectedSubTab == 0,
                onClick = { selectedSubTab = 0 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isUrdu) "اسکول کے متعلق" else "About Our School",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
            Tab(
                selected = selectedSubTab == 1,
                onClick = { selectedSubTab = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isUrdu) "رابطہ اور معلومات" else "Contact & Inquiries",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }

        if (selectedSubTab == 0) {
            AboutSchoolContent(isUrdu = isUrdu, settings = settings)
        } else {
            ContactSchoolContent(
                isUrdu = isUrdu,
                settings = settings,
                onSubmitInquiry = { name, email, msg ->
                    viewModel.showSnackbar("Thank you $name! Your message has been submitted to BHSS Tolti.")
                }
            )
        }
    }
}

@Composable
private fun AboutSchoolContent(
    isUrdu: Boolean,
    settings: com.example.data.model.SchoolSettingsRecord?
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DeepNavy),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SchoolEmblem(size = 90.dp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isUrdu) "گورنمنٹ بوائز ہائر سیکنڈری اسکول تولتی" else "BOYS HIGHER SECONDARY SCHOOL TOLTI",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = if (isUrdu) "سیکھو • آگے بڑھو • کامیاب ہو" else "LEARN • GROW • SUCCEED",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = ElegantGold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (isUrdu)
                            "ضلع کھرمنگ، گلگت بلتستان کا ایک تاریخی اور باوقار تعلیمی ادارہ جہاں طلباء کی تعلیمی اور اخلاقی تربیت کی جاتی ہے۔"
                        else
                            "A prestigious and historic educational institution in District Kharmang, Gilgit-Baltistan, dedicated to academic excellence, discipline, and moral grooming.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        // Mission & Vision
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = RoyalBlue.copy(alpha = 0.12f),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.School, contentDescription = null, tint = RoyalBlue, modifier = Modifier.size(20.dp))
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isUrdu) "ہمارا مشن" else "Our Mission",
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isUrdu)
                                "معیاری تعلیم، جدید سائنسی علوم، اور بہترین اخلاقی تربیت کے ذریعے علاقے کے طلباء کو روشن مستقبل کے لیے تیار کرنا۔"
                            else
                                "To provide holistic, high-quality education, modern scientific inquiry, and strong character development to empower youths of Kharmang.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF475569)
                        )
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = ElegantGold.copy(alpha = 0.15f),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.MenuBook, contentDescription = null, tint = ElegantGold, modifier = Modifier.size(20.dp))
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isUrdu) "ہمارا وژن" else "Our Vision",
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isUrdu)
                                "ایک ایسا مثالی مرکز جہاں ہر طالب علم کو اپنی فکری، سائنسی اور اخلاقی صلاحیتوں کو مکمل نکھارنے کا موقع ملے۔"
                            else
                                "To be a beacon of academic distinction in Gilgit-Baltistan, nurturing responsible leaders and creative thinkers.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF475569)
                        )
                    }
                }
            }
        }

        // Academic Programs (5th to 12th)
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isUrdu) "تعلیمی پروگرامز (کلاس 5 سے 12)" else "Academic Programs (Classes 5th - 12th)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    ProgramBullet(
                        title = if (isUrdu) "مڈل سیکشن (کلاس 5 تا 8)" else "Middle School (Classes 5th - 8th)",
                        desc = if (isUrdu) "انگریزی، ریاضی، جنرل سائنس، اردو، اسلامیات، اور مطالعہ پاکستان کی بنیادی تعلیم۔" else "Core foundational literacy, arithmetic, general science, social studies, and languages."
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ProgramBullet(
                        title = if (isUrdu) "سیکنڈری / میٹرک (کلاس 9 تا 10)" else "Secondary / Matriculation (Classes 9th - 10th)",
                        desc = if (isUrdu) "سائنس گروپ: فزکس، کیمسٹری، بیالوجی / کمپیوٹر، اور ریاضی بمعہ پریکٹیکل لیب تجربات۔" else "Science group affiliated with FBISE / Karakoram Board: Physics, Chemistry, Biology/Comp, Math."
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ProgramBullet(
                        title = if (isUrdu) "ہائر سیکنڈری / انٹرمیڈیٹ (کلاس 11 تا 12)" else "Higher Secondary / Intermediate (Classes 11th - 12th)",
                        desc = if (isUrdu) "ایف ایس سی پری انجینئرنگ اور آئی سی ایس (کمپیوٹر سائنس، شماریات اور ریاضی) کے جدید مضامین۔" else "F.Sc Pre-Engineering and ICS (Computer Science, Mathematics & Physics) with university pathways."
                    )
                }
            }
        }

        // Campus Facilities
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isUrdu) "کیمپس کی سہولیات" else "Campus Facilities",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    FacilityRow(icon = Icons.Default.Science, name = if (isUrdu) "سائنس لیبارٹریز" else "Science Laboratories", desc = if (isUrdu) "فزکس، کیمسٹری اور بیالوجی کے جدید آلات" else "Well-equipped Physics, Chemistry & Biology experimental labs")
                    Spacer(modifier = Modifier.height(10.dp))
                    FacilityRow(icon = Icons.Default.Business, name = if (isUrdu) "کمپیوٹر اور آئی ٹی لیب" else "Computer & IT Lab", desc = if (isUrdu) "ہائی اسپیڈ انٹرنیٹ اور پروگرامنگ سسٹم" else "Modern desktop workstations with networking and coding tools")
                    Spacer(modifier = Modifier.height(10.dp))
                    FacilityRow(icon = Icons.Default.MenuBook, name = if (isUrdu) "سنٹرل لائبریری" else "Central Academic Library", desc = if (isUrdu) "ہزاروں کتب، سائنسی جرائد اور نصابی مواد" else "Extensive collection of textbooks, reference volumes, and periodicals")
                    Spacer(modifier = Modifier.height(10.dp))
                    FacilityRow(icon = Icons.Default.SportsHandball, name = if (isUrdu) "کھیلوں کا وسیع میدان" else "Spacious Sports Grounds", desc = if (isUrdu) "کرکٹ، فٹ بال اور والی بال کے مقابلے" else "Scenic mountain-view grounds for Cricket, Football, and Athletics")
                }
            }
        }

        // Principal Message
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = RoyalBlue,
                        modifier = Modifier.size(50.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = if (isUrdu) "پرنسپل کا پیغام" else "Message from the Principal",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                        Text(
                            text = settings?.principalName ?: "Prof. Muhammad Hussain",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = RoyalBlue
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isUrdu)
                                "\"ہمارا عزم ہر طالب علم کو اعلیٰ تعلیم اور اخلاقی کردار کے زیور سے آراستہ کرنا ہے۔ ہم والدین اور اساتذہ کے باہمی تعاون کی قدر کرتے ہیں۔\""
                            else
                                "\"At BHSS Tolti, we are committed to unlocking each young scholar's potential. We invite parents and the community to join hands in building our students' prosperous tomorrow.\"",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF334155)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ProgramBullet(title: String, desc: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF16A34A), modifier = Modifier.size(18.dp).padding(top = 2.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium, color = DeepNavy)
            Text(text = desc, style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
        }
    }
}

@Composable
private fun FacilityRow(icon: ImageVector, name: String, desc: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFF1F5F9),
            modifier = Modifier.size(38.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = RoyalBlue, modifier = Modifier.size(20.dp))
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = name, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium, color = DeepNavy)
            Text(text = desc, style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
        }
    }
}

@Composable
private fun ContactSchoolContent(
    isUrdu: Boolean,
    settings: com.example.data.model.SchoolSettingsRecord?,
    onSubmitInquiry: (name: String, email: String, msg: String) -> Unit
) {
    var visitorName by remember { mutableStateOf("") }
    var visitorContact by remember { mutableStateOf("") }
    var inquiryRole by remember { mutableStateOf("Parent") }
    var visitorMessage by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Contact Details Card
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isUrdu) "سرکاری رابطے کی معلومات" else "Official Contact Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    ContactItemRow(
                        icon = Icons.Default.LocationOn,
                        title = if (isUrdu) "اسکول کا پتہ" else "Campus Address",
                        value = settings?.address ?: "Main Bazar Road, Tolti, District Kharmang, Gilgit-Baltistan"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    ContactItemRow(
                        icon = Icons.Default.Phone,
                        title = if (isUrdu) "فون نمبر" else "Phone Numbers",
                        value = "${settings?.phone ?: "+92 5816 920011"} / +92 344 5566778"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    ContactItemRow(
                        icon = Icons.Default.Email,
                        title = if (isUrdu) "ای میل ایڈریس" else "Official Email",
                        value = settings?.email ?: "contact@bhsstolti.edu.pk"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    ContactItemRow(
                        icon = Icons.Default.AccessTime,
                        title = if (isUrdu) "دفتری اوقات" else "Office Hours",
                        value = if (isUrdu) "پیر تا ہفتہ: صبح 8:00 بجے تا دوپہر 2:00 بجے" else "Monday - Saturday: 08:00 AM – 02:00 PM"
                    )
                }
            }
        }

        // Send Inquiry / Message Form
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isUrdu) "پیغام یا معلوماتی فارم" else "Send an Inquiry / Message",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                    Text(
                        text = if (isUrdu)
                            "والدین اور معزز مہمان اسکول انتظامیہ کو اپنا سوال یا تجویز براہ راست بھیج سکتے ہیں۔"
                        else
                            "Parents, alumni, and visitors can send questions or inquiries directly to school administration.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    if (isSubmitted) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFDCFCE7),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF16A34A))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = if (isUrdu)
                                        "آپ کا پیغام کامیابی کے ساتھ وصول پا چکا ہے۔ انتظامیہ جلد جواب دے گی۔"
                                    else
                                        "Your message has been sent successfully. The administration office will review it.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF166534),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    } else {
                        OutlinedTextField(
                            value = visitorName,
                            onValueChange = { visitorName = it },
                            label = { Text(if (isUrdu) "آپ کا نام" else "Your Full Name") },
                            modifier = Modifier.fillMaxWidth().testTag("inquiry_name_input"),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = visitorContact,
                            onValueChange = { visitorContact = it },
                            label = { Text(if (isUrdu) "فون نمبر یا ای میل" else "Phone / Email Address") },
                            modifier = Modifier.fillMaxWidth().testTag("inquiry_contact_input"),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = visitorMessage,
                            onValueChange = { visitorMessage = it },
                            label = { Text(if (isUrdu) "پیغام یا سوال" else "Your Message or Inquiry") },
                            modifier = Modifier.fillMaxWidth().height(110.dp).testTag("inquiry_msg_input"),
                            maxLines = 4
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = {
                                if (visitorName.isNotBlank() && visitorMessage.isNotBlank()) {
                                    onSubmitInquiry(visitorName, visitorContact, visitorMessage)
                                    isSubmitted = true
                                }
                            },
                            enabled = visitorName.isNotBlank() && visitorMessage.isNotBlank(),
                            modifier = Modifier.fillMaxWidth().testTag("inquiry_submit_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = if (isUrdu) "پیغام بھیجیں" else "Submit Inquiry")
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ContactItemRow(icon: ImageVector, title: String, value: String) {
    Row(verticalAlignment = Alignment.Top) {
        Surface(
            shape = CircleShape,
            color = RoyalBlue.copy(alpha = 0.1f),
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = RoyalBlue, modifier = Modifier.size(18.dp))
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold)
            Text(text = value, style = MaterialTheme.typography.bodyMedium, color = DeepNavy, fontWeight = FontWeight.Bold)
        }
    }
}
