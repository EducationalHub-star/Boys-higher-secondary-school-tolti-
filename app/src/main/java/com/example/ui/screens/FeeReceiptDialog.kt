package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.FeeRecord
import com.example.ui.components.SchoolEmblem
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.ElegantGold
import com.example.ui.theme.RoyalBlue

@Composable
fun FeeReceiptDialog(
    fee: FeeRecord,
    onDismiss: () -> Unit,
    onPrintOrDownload: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 16.dp)
                .testTag("fee_receipt_dialog"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header with close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Official Payment Receipt",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Receipt Content Box
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, DeepNavy.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                        .padding(16.dp)
                ) {
                    // School Banner
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SchoolEmblem(size = 46.dp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "BOYS HIGHER SECONDARY SCHOOL TOLTI",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepNavy
                            )
                            Text(
                                text = "District Kharmang, Gilgit-Baltistan • LEARN • GROW • SUCCEED",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = DeepNavy.copy(alpha = 0.2f), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "RECEIPT NO:", style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B))
                            Text(text = fee.receiptNumber ?: "REC-2026-PENDING", fontWeight = FontWeight.Bold, color = DeepNavy)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "DATE:", style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B))
                            Text(text = fee.paymentDate ?: fee.dueDate, fontWeight = FontWeight.Bold, color = DeepNavy)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Student & Fee Details Table
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFF8FAFC),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            ReceiptField(label = "Student Name", value = fee.studentName)
                            ReceiptField(label = "Roll Number", value = fee.studentRollNumber)
                            ReceiptField(label = "Class", value = "Class ${fee.classLevel}")
                            ReceiptField(label = "Fee Particulars", value = fee.feeType)
                            ReceiptField(label = "Payment Mode", value = fee.paymentMode)
                            ReceiptField(
                                label = "Status",
                                value = fee.status,
                                valueColor = if (fee.status == "PAID") Color(0xFF16A34A) else Color(0xFFDC2626)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Total Amount Bar
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = DeepNavy,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "TOTAL AMOUNT PAID:", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(text = "PKR ${fee.amount.toInt()} /-", color = ElegantGold, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Signatures & Stamp
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        // School Stamp
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFEFF6FF),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, RoyalBlue),
                            modifier = Modifier.size(70.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "BHSS TOLTI\nACCOUNTS\nOFFICE",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RoyalBlue,
                                    lineHeight = 10.sp,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }

                        // Cashier / Principal Signature
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Ghulam Nabi",
                                fontFamily = FontFamily.Cursive,
                                fontSize = 16.sp,
                                color = DeepNavy,
                                fontStyle = FontStyle.Italic
                            )
                            Divider(modifier = Modifier.width(110.dp), color = DeepNavy, thickness = 1.dp)
                            Text(text = "Accounts In-Charge", fontSize = 10.sp, color = Color(0xFF64748B))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Close")
                    }

                    Button(
                        onClick = {
                            onPrintOrDownload()
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f).testTag("print_receipt_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Print / Save")
                    }
                }
            }
        }
    }
}

@Composable
private fun ReceiptField(label: String, value: String, valueColor: Color = DeepNavy) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
        Text(text = value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = valueColor)
    }
}
