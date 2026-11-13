package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DashboardCustomize
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.AppDesignConfig
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.designButtonShape
import com.example.ui.theme.designCardBorder
import com.example.ui.theme.designCardColors
import com.example.ui.theme.designCardShape
import com.example.ui.viewmodel.SchoolViewModel
import com.example.util.MediaStorageHelper

private data class ThemePreset(
    val name: String,
    val primaryHex: String,
    val secondaryHex: String,
    val accentHex: String,
    val bgHex: String
)

private val SCHOOL_PRESETS = listOf(
    ThemePreset("Tolti Classic", "#0B192C", "#1D4ED8", "#D97706", "#F8FAFC"),
    ThemePreset("Karakoram Emerald", "#064E3B", "#059669", "#D97706", "#F0FDF4"),
    ThemePreset("Royal Indigo", "#1E1B4B", "#4F46E5", "#F59E0B", "#EEF2FF"),
    ThemePreset("Crimson Crest", "#450A0A", "#B91C1C", "#D97706", "#FEF2F2"),
    ThemePreset("Slate Modern", "#0F172A", "#0284C7", "#10B981", "#F1F5F9")
)

private val COLOR_SWATCHES = listOf(
    "#0B192C", "#1D4ED8", "#064E3B", "#059669",
    "#1E1B4B", "#7C3AED", "#7F1D1D", "#D97706",
    "#0284C7", "#F8FAFC", "#F1F5F9", "#FFFBEB"
)

@Composable
fun AdminDesignTab(viewModel: SchoolViewModel) {
    val context = LocalContext.current
    val publishedConfig by viewModel.publishedDesignConfig.collectAsState()
    val savedDraft by viewModel.draftDesignConfig.collectAsState()
    val isPreviewMode by viewModel.isPreviewMode.collectAsState()

    var draft by remember(publishedConfig.lastUpdatedTimestamp, savedDraft?.lastUpdatedTimestamp) {
        mutableStateOf(savedDraft ?: publishedConfig)
    }

    fun updateDraft(transform: (AppDesignConfig) -> AppDesignConfig) {
        val next = transform(draft).copy(lastUpdatedTimestamp = System.currentTimeMillis())
        draft = next
        viewModel.updateLivePreview(next)
    }

    val logoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val path = MediaStorageHelper.copyPickerUriToInternalStorage(context, uri, "branding")
            if (path != null) {
                updateDraft { it.copy(customLogoUri = path) }
                viewModel.showSnackbar("School logo updated in draft.")
            }
        }
    }

    val bannerPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val path = MediaStorageHelper.copyPickerUriToInternalStorage(context, uri, "branding")
            if (path != null) {
                updateDraft { it.copy(customBannerUri = path) }
                viewModel.showSnackbar("Welcome banner updated in draft.")
            }
        }
    }

    val backgroundPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val path = MediaStorageHelper.copyPickerUriToInternalStorage(context, uri, "branding")
            if (path != null) {
                updateDraft { it.copy(customBackgroundUri = path) }
                viewModel.showSnackbar("App background image updated in draft.")
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("admin_design_tab"),
        contentPadding = PaddingValues(vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header & Control Actions
        item {
            Card(
                shape = designCardShape(draft),
                colors = designCardColors(draft),
                border = designCardBorder(draft),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Palette, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Design & Appearance Studio",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Customize theme, colors, cards, branding & home layout without editing code.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    // Action Buttons Row 1: Preview & Save Draft
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = {
                                if (isPreviewMode) viewModel.exitDesignPreview()
                                else viewModel.startDesignPreview(draft)
                            },
                            shape = designButtonShape(draft),
                            modifier = Modifier.weight(1f).height(48.dp).testTag("design_preview_btn")
                        ) {
                            Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isPreviewMode) "Exit Preview" else "Preview Live", fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = { viewModel.saveDraftDesign(draft) },
                            shape = designButtonShape(draft),
                            modifier = Modifier.weight(1f).height(48.dp).testTag("design_save_draft_btn")
                        ) {
                            Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Save Changes", fontSize = 12.sp)
                        }
                    }

                    // Action Buttons Row 2: Publish & Reset
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { viewModel.publishDesign(draft) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                            shape = designButtonShape(draft),
                            modifier = Modifier.weight(1f).height(48.dp).testTag("design_publish_btn")
                        ) {
                            Icon(Icons.Default.Publish, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Publish Design", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.resetDesignToDefault()
                                draft = AppDesignConfig()
                            },
                            shape = designButtonShape(draft),
                            modifier = Modifier.weight(1f).height(48.dp).testTag("design_reset_btn")
                        ) {
                            Icon(Icons.Default.RestartAlt, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Reset Default", fontSize = 12.sp, color = Color(0xFFDC2626))
                        }
                    }
                }
            }
        }

        // Mini Live Preview Card
        item {
            val pColor = AppDesignConfig.parseColorSafe(draft.primaryColorHex, DeepNavy)
            val sColor = AppDesignConfig.parseColorSafe(draft.secondaryColorHex, RoyalBlue)
            val aColor = AppDesignConfig.parseColorSafe(draft.accentColorHex, Color(0xFFD97706))
            val bgColor = AppDesignConfig.parseColorSafe(draft.backgroundColorHex, Color(0xFFF8FAFC))

            Surface(
                shape = designCardShape(draft),
                color = if (draft.themeMode == "DARK") Color(0xFF0F172A) else bgColor,
                border = BorderStroke(1.5.dp, sColor.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth().testTag("design_mini_preview_card")
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "LIVE STYLE PREVIEW (${draft.themeMode} • ${draft.cardStyle} • ${draft.buttonStyle})",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (draft.themeMode == "DARK") Color(0xFF93C5FD) else sColor
                    )
                    Surface(
                        shape = designCardShape(draft),
                        color = if (draft.themeMode == "DARK") Color(0xFF1E293B) else Color.White,
                        border = BorderStroke(1.dp, aColor.copy(alpha = 0.5f)),
                        shadowElevation = if (draft.cardStyle == "ELEVATED") 3.dp else 0.dp
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "BHSS Tolti Header",
                                    fontSize = (15f * draft.fontSizeScale).sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (draft.themeMode == "DARK") Color.White else pColor
                                )
                                Text(
                                    text = "Sample card preview text",
                                    fontSize = (11.5f * draft.fontSizeScale).sp,
                                    color = if (draft.themeMode == "DARK") Color(0xFFCBD5E1) else Color(0xFF64748B)
                                )
                            }
                            Button(
                                onClick = {},
                                colors = ButtonDefaults.buttonColors(containerColor = sColor),
                                shape = designButtonShape(draft),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("Action", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        // A. Theme Mode & Presets
        item {
            Card(
                shape = designCardShape(draft),
                colors = designCardColors(draft),
                border = designCardBorder(draft)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ColorLens, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Theme Mode & Color Palette", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("SCHOOL_CUSTOM" to "School Custom", "LIGHT" to "Light", "DARK" to "Dark").forEach { (mode, label) ->
                            FilterChip(
                                selected = draft.themeMode == mode,
                                onClick = { updateDraft { it.copy(themeMode = mode) } },
                                label = { Text(label, fontSize = 11.5.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Text("Quick School Palettes:", fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(SCHOOL_PRESETS) { preset ->
                            Surface(
                                onClick = {
                                    updateDraft {
                                        it.copy(
                                            primaryColorHex = preset.primaryHex,
                                            secondaryColorHex = preset.secondaryHex,
                                            accentColorHex = preset.accentHex,
                                            backgroundColorHex = preset.bgHex
                                        )
                                    }
                                },
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(
                                    width = if (draft.primaryColorHex.equals(preset.primaryHex, true)) 2.dp else 1.dp,
                                    color = if (draft.primaryColorHex.equals(preset.primaryHex, true)) RoyalBlue else Color(0xFFCBD5E1)
                                ),
                                color = MaterialTheme.colorScheme.surface
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(14.dp)
                                            .clip(CircleShape)
                                            .background(AppDesignConfig.parseColorSafe(preset.primaryHex, DeepNavy))
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(14.dp)
                                            .clip(CircleShape)
                                            .background(AppDesignConfig.parseColorSafe(preset.secondaryHex, RoyalBlue))
                                    )
                                    Text(preset.name, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }

                    ColorHexPickerRow(
                        label = "Primary Color",
                        currentHex = draft.primaryColorHex,
                        onSelectHex = { hex -> updateDraft { it.copy(primaryColorHex = hex) } }
                    )
                    ColorHexPickerRow(
                        label = "Secondary Color",
                        currentHex = draft.secondaryColorHex,
                        onSelectHex = { hex -> updateDraft { it.copy(secondaryColorHex = hex) } }
                    )
                    ColorHexPickerRow(
                        label = "Background Color",
                        currentHex = draft.backgroundColorHex,
                        onSelectHex = { hex -> updateDraft { it.copy(backgroundColorHex = hex) } }
                    )
                }
            }
        }

        // B. Shapes, Cards, Typography & Nav Appearance
        item {
            Card(
                shape = designCardShape(draft),
                colors = designCardColors(draft),
                border = designCardBorder(draft)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.TextFields, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Cards, Buttons, Icons & Text Scale", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }

                    Text("Button & Corner Shape:", fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("ROUNDED" to "Rounded", "PILL" to "Pill", "SHARP" to "Sharp").forEach { (key, label) ->
                            FilterChip(
                                selected = draft.buttonStyle == key,
                                onClick = { updateDraft { it.copy(buttonStyle = key) } },
                                label = { Text(label, fontSize = 11.5.sp) }
                            )
                        }
                    }

                    Text("Card Style:", fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("ELEVATED" to "Elevated", "OUTLINED" to "Outlined", "GLASS" to "Glass").forEach { (key, label) ->
                            FilterChip(
                                selected = draft.cardStyle == key,
                                onClick = { updateDraft { it.copy(cardStyle = key) } },
                                label = { Text(label, fontSize = 11.5.sp) }
                            )
                        }
                    }

                    Text("Navigation & Icon Style:", fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("DEFAULT" to "Standard", "COMPACT" to "Compact", "PILL" to "Pill Accent").forEach { (key, label) ->
                            FilterChip(
                                selected = draft.iconNavStyle == key,
                                onClick = { updateDraft { it.copy(iconNavStyle = key) } },
                                label = { Text(label, fontSize = 11.5.sp) }
                            )
                        }
                    }

                    Text("Font Size Scale:", fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(0.90f to "Compact", 1.0f to "Normal", 1.12f to "Large").forEach { (scale, label) ->
                            FilterChip(
                                selected = kotlin.math.abs(draft.fontSizeScale - scale) < 0.03f,
                                onClick = { updateDraft { it.copy(fontSizeScale = scale) } },
                                label = { Text(label, fontSize = 11.5.sp) }
                            )
                        }
                    }
                }
            }
        }

        // C. Branding Media (Logo, Welcome Banner, Background)
        item {
            Card(
                shape = designCardShape(draft),
                colors = designCardColors(draft),
                border = designCardBorder(draft)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Image, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("School Logo, Banner & Background", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }

                    MediaBrandingPickerRow(
                        label = "School Emblem / Logo",
                        uri = draft.customLogoUri,
                        onPick = {
                            MediaStorageHelper.launchImagePickerSafely(
                                logoPicker,
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            ) {
                                viewModel.showSnackbar("No Gallery or photo picker app available on this device.")
                            }
                        },
                        onClear = { updateDraft { it.copy(customLogoUri = null) } }
                    )

                    MediaBrandingPickerRow(
                        label = "Home Welcome Banner Photo",
                        uri = draft.customBannerUri,
                        onPick = {
                            MediaStorageHelper.launchImagePickerSafely(
                                bannerPicker,
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            ) {
                                viewModel.showSnackbar("No Gallery or photo picker app available on this device.")
                            }
                        },
                        onClear = { updateDraft { it.copy(customBannerUri = null) } }
                    )

                    MediaBrandingPickerRow(
                        label = "App Background Image",
                        uri = draft.customBackgroundUri,
                        onPick = {
                            MediaStorageHelper.launchImagePickerSafely(
                                backgroundPicker,
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            ) {
                                viewModel.showSnackbar("No Gallery or photo picker app available on this device.")
                            }
                        },
                        onClear = { updateDraft { it.copy(customBackgroundUri = null) } }
                    )
                }
            }
        }

        // D. Home Layout Order, Visibility & Screen Layouts
        item {
            val orderedSections = draft.orderedHomeSections()
            Card(
                shape = designCardShape(draft),
                colors = designCardColors(draft),
                border = designCardBorder(draft)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DashboardCustomize, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Home Sections & Screen Layouts", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }

                    orderedSections.forEachIndexed { index, key ->
                        val isVisible = draft.isSectionVisible(key)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = AppDesignConfig.HOME_SECTION_LABELS[key] ?: key,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (isVisible) "Visible on Home" else "Hidden",
                                    fontSize = 10.sp,
                                    color = if (isVisible) Color(0xFF16A34A) else Color(0xFFDC2626)
                                )
                            }

                            IconButton(
                                onClick = {
                                    if (index > 0) {
                                        val mutable = orderedSections.toMutableList()
                                        val item = mutable.removeAt(index)
                                        mutable.add(index - 1, item)
                                        updateDraft { it.copy(homeSectionOrder = mutable.joinToString(",")) }
                                    }
                                },
                                enabled = index > 0,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(Icons.Default.ArrowUpward, contentDescription = "Move Up", modifier = Modifier.size(16.dp))
                            }

                            IconButton(
                                onClick = {
                                    if (index < orderedSections.lastIndex) {
                                        val mutable = orderedSections.toMutableList()
                                        val item = mutable.removeAt(index)
                                        mutable.add(index + 1, item)
                                        updateDraft { it.copy(homeSectionOrder = mutable.joinToString(",")) }
                                    }
                                },
                                enabled = index < orderedSections.lastIndex,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(Icons.Default.ArrowDownward, contentDescription = "Move Down", modifier = Modifier.size(16.dp))
                            }

                            Switch(
                                checked = isVisible,
                                onCheckedChange = { checked ->
                                    val hiddenSet = draft.hiddenHomeSections
                                        .split(",")
                                        .map { it.trim() }
                                        .filter { it.isNotEmpty() }
                                        .toMutableSet()
                                    if (checked) hiddenSet.remove(key) else hiddenSet.add(key)
                                    updateDraft { it.copy(hiddenHomeSections = hiddenSet.joinToString(",")) }
                                }
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    Text("Notices Screen Layout:", fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("CARD" to "Detailed Cards", "COMPACT" to "Compact List").forEach { (key, label) ->
                            FilterChip(
                                selected = draft.announcementsLayout == key,
                                onClick = { updateDraft { it.copy(announcementsLayout = key) } },
                                label = { Text(label, fontSize = 11.5.sp) }
                            )
                        }
                    }

                    Text("Results Screen Layout:", fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("DETAILED" to "Detailed Bars", "COMPACT" to "Compact Cards").forEach { (key, label) ->
                            FilterChip(
                                selected = draft.resultsLayout == key,
                                onClick = { updateDraft { it.copy(resultsLayout = key) } },
                                label = { Text(label, fontSize = 11.5.sp) }
                            )
                        }
                    }

                    Text("Admin Overview Layout:", fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("GRID" to "2-Column Grid", "LIST" to "Compact Rows").forEach { (key, label) ->
                            FilterChip(
                                selected = draft.adminDashboardLayout == key,
                                onClick = { updateDraft { it.copy(adminDashboardLayout = key) } },
                                label = { Text(label, fontSize = 11.5.sp) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorHexPickerRow(
    label: String,
    currentHex: String,
    onSelectHex: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF334155))
            OutlinedTextField(
                value = currentHex,
                onValueChange = { next ->
                    if (next.length <= 9) onSelectHex(next)
                },
                singleLine = true,
                modifier = Modifier.width(120.dp),
                textStyle = MaterialTheme.typography.labelSmall
            )
        }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(COLOR_SWATCHES) { hex ->
                val color = AppDesignConfig.parseColorSafe(hex, DeepNavy)
                val selected = currentHex.equals(hex, ignoreCase = true)
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(if (selected) 2.5.dp else 1.dp, if (selected) RoyalBlue else Color(0xFFCBD5E1), CircleShape)
                        .clickable { onSelectHex(hex) },
                    contentAlignment = Alignment.Center
                ) {
                    if (selected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected $hex",
                            tint = if (hex.startsWith("#F") || hex.startsWith("#E")) DeepNavy else Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MediaBrandingPickerRow(
    label: String,
    uri: String?,
    onPick: () -> Unit,
    onClear: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFE2E8F0),
            modifier = Modifier.size(48.dp)
        ) {
            if (!uri.isNullOrBlank()) {
                AsyncImage(
                    model = uri,
                    contentDescription = label,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Image, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(22.dp))
                }
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text(
                text = if (!uri.isNullOrBlank()) "Custom image active" else "Using default asset",
                fontSize = 10.5.sp,
                color = Color(0xFF64748B)
            )
        }
        IconButton(onClick = onPick, modifier = Modifier.size(48.dp)) {
            Icon(Icons.Default.PhotoLibrary, contentDescription = "Choose $label", tint = RoyalBlue)
        }
        if (!uri.isNullOrBlank()) {
            IconButton(onClick = onClear, modifier = Modifier.size(48.dp)) {
                Icon(Icons.Default.Close, contentDescription = "Reset $label", tint = Color(0xFFDC2626))
            }
        }
    }
}
