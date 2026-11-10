package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.RoyalBlue
import com.example.ui.viewmodel.SchoolViewModel

@Composable
fun EditSchoolSettingsDialog(
    viewModel: SchoolViewModel,
    onDismiss: () -> Unit
) {
    val currentSettings by viewModel.schoolSettings.collectAsState()

    var address by remember { mutableStateOf(currentSettings?.address ?: "Main Bazar Road, Tolti, District Kharmang, Gilgit-Baltistan") }
    var phone by remember { mutableStateOf(currentSettings?.phone ?: "+92 5816 920011") }
    var email by remember { mutableStateOf(currentSettings?.email ?: "contact@bhsstolti.edu.pk") }
    var principalName by remember { mutableStateOf(currentSettings?.principalName ?: "Prof. Muhammad Hussain") }
    var aboutHistory by remember {
        mutableStateOf(
            currentSettings?.aboutHistory
                ?: "Boys Higher Secondary School Tolti is a premier educational institution in District Kharmang, Gilgit-Baltistan, empowering youth with academic and moral excellence since 1974."
        )
    }
    var showStamp by remember { mutableStateOf(currentSettings?.showStampPlaceholder ?: true) }
    var showSignature by remember { mutableStateOf(currentSettings?.showSignaturePlaceholder ?: true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Settings, contentDescription = null, tint = RoyalBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Edit School Information & Settings",
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
                OutlinedTextField(
                    value = "BOYS HIGHER SECONDARY SCHOOL TOLTI",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("School Name (Locked Official)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = principalName,
                    onValueChange = { principalName = it },
                    label = { Text("Principal Name & Title") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("settings_principal_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Official Contact Phone") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("settings_phone_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Official Email Address") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("settings_email_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Campus Address") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("settings_address_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = aboutHistory,
                    onValueChange = { aboutHistory = it },
                    label = { Text("School Introduction & History") },
                    modifier = Modifier.fillMaxWidth().testTag("settings_history_input"),
                    maxLines = 3
                )

                // Toggles for result card & receipt stamps
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Show School Stamp Seal", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Include official stamp seal on result cards and fee receipts.", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
                    }
                    Switch(
                        checked = showStamp,
                        onCheckedChange = { showStamp = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = RoyalBlue)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Show Principal Signature", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Include principal signature placeholder on transcripts.", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
                    }
                    Switch(
                        checked = showSignature,
                        onCheckedChange = { showSignature = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = RoyalBlue)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.updateSchoolSettings(
                        address = address.trim(),
                        phone = phone.trim(),
                        email = email.trim(),
                        principalName = principalName.trim(),
                        aboutHistory = aboutHistory.trim(),
                        showStamp = showStamp,
                        showSignature = showSignature
                    )
                    onDismiss()
                },
                enabled = address.isNotBlank() && phone.isNotBlank() && principalName.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("save_settings_btn")
            ) {
                Text("Save Settings")
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
