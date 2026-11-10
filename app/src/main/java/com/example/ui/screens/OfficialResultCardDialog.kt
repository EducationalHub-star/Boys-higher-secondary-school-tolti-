package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.model.SchoolSettingsRecord
import com.example.data.model.StudentResult
import com.example.ui.components.SchoolEmblem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OfficialResultCardDialog(
    result: StudentResult,
    settings: SchoolSettingsRecord? = null,
    onDismiss: () -> Unit,
    onPrintOrDownload: () -> Unit = {}
) {
    val dateStr = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault()).format(Date(result.dateRecorded))

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .testTag("official_result_card_dialog"),
            color = Color.White,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Top Action Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Official Academic Transcript",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFD97706),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
                    }
                }

                // Official School Certificate Container
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, Color(0xFFD97706), RoundedCornerShape(12.dp))
                        .padding(2.dp)
                        .border(1.dp, Color(0xFF0B192C), RoundedCornerShape(10.dp)),
                    color = Color(0xFFFEFDF9),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // School Emblem & Letterhead
                        SchoolEmblem(size = 56.dp, showBorder = true)

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = settings?.schoolName ?: "BOYS HIGHER SECONDARY SCHOOL TOLTI",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0B192C),
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = settings?.tagline ?: "LEARN • GROW • SUCCEED",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFD97706),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )

                        Text(
                            text = "District Kharmang, Gilgit-Baltistan • Official Examination Card",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF64748B),
                            fontSize = 10.sp
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 10.dp),
                            color = Color(0xFFE2E8F0)
                        )

                        // Exam Term Title
                        Surface(
                            color = Color(0xFF0B192C),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = result.examTerm.uppercase(),
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Student Metadata Grid
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF8FAFC), RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            ResultMetaRow("Student Name:", result.studentName)
                            ResultMetaRow("Roll Number:", result.rollNumber)
                            ResultMetaRow("Class Wing:", "Class ${result.classLevel}")
                            ResultMetaRow("Subject:", "${result.subjectName} (${result.subjectCode})")
                            ResultMetaRow("Examination Date:", dateStr)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Marks Table
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                        ) {
                            Column {
                                // Table Header
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFF1E3A8A))
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Component", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Text("Score", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                ResultScoreRow("Total Marks", "${result.totalMarks}")
                                ResultScoreRow("Obtained Marks", "${result.obtainedMarks}")
                                ResultScoreRow("Percentage", String.format("%.1f%%", result.percentage))
                                ResultScoreRow("Awarded Grade", result.grade, isGrade = true)
                                ResultScoreRow("Academic Status", result.status, isStatus = true)
                            }
                        }

                        if (result.remarks.isNotBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = Color(0xFFF1F5F9),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "Examiner Remarks: \"${result.remarks}\"",
                                    fontSize = 11.sp,
                                    color = Color(0xFF334155),
                                    modifier = Modifier.padding(8.dp),
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Official Stamp and Signature Placeholders
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            // Official School Stamp Placeholder
                            if (settings?.showStampPlaceholder != false) {
                                Surface(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .border(2.dp, Color(0xFF1D4ED8).copy(alpha = 0.5f), CircleShape),
                                    shape = CircleShape,
                                    color = Color(0xFF1D4ED8).copy(alpha = 0.05f)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier.padding(4.dp)
                                    ) {
                                        Text("OFFICIAL SEAL", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1D4ED8))
                                        Text("BHSS TOLTI", fontSize = 7.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0B192C))
                                        Text("KHARMANG", fontSize = 6.sp, color = Color(0xFF64748B))
                                    }
                                }
                            } else {
                                Spacer(modifier = Modifier.width(1.dp))
                            }

                            // Principal Signature Placeholder
                            if (settings?.showSignaturePlaceholder != false) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Ghulam Nabi",
                                        fontFamily = FontFamily.Cursive,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0B192C)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .width(110.dp)
                                            .height(1.dp)
                                            .background(Color(0xFF0B192C))
                                    )
                                    Text(
                                        text = settings?.principalName ?: "Principal / Headmaster",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF334155)
                                    )
                                    Text(
                                        text = "BHSS Tolti, Kharmang",
                                        fontSize = 9.sp,
                                        color = Color(0xFF64748B)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onPrintOrDownload,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("print_result_card_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0B192C)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Print, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Print / Save Card", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.height(44.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Close", color = Color(0xFF64748B))
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultMetaRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 11.sp, color = Color(0xFF64748B))
        Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0B192C))
    }
}

@Composable
private fun ResultScoreRow(
    label: String,
    value: String,
    isGrade: Boolean = false,
    isStatus: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 11.sp, color = Color(0xFF334155))
        Text(
            text = value,
            fontSize = if (isGrade || isStatus) 13.sp else 11.sp,
            fontWeight = FontWeight.Bold,
            color = when {
                isGrade -> Color(0xFFD97706)
                isStatus && value == "PASS" -> Color(0xFF10B981)
                isStatus && value == "FAIL" -> Color(0xFFEF4444)
                else -> Color(0xFF0B192C)
            }
        )
    }
}
