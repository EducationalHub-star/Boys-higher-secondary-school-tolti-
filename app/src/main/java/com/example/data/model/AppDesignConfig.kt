package com.example.data.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Persistent App Design & Layout Configuration controlled by Admin.
 * id = 1: Published design applied app-wide for all users.
 * id = 2: Saved Admin draft before publishing.
 */
@Entity(tableName = "app_design_config")
data class AppDesignConfig(
    @PrimaryKey
    val id: Int = 1,
    val themeMode: String = "SCHOOL_CUSTOM", // "SCHOOL_CUSTOM", "LIGHT", "DARK"
    val primaryColorHex: String = "#0B192C",
    val secondaryColorHex: String = "#1D4ED8",
    val accentColorHex: String = "#D97706",
    val backgroundColorHex: String = "#F8FAFC",
    val buttonStyle: String = "ROUNDED", // "ROUNDED", "PILL", "SHARP"
    val cardStyle: String = "ELEVATED", // "ELEVATED", "OUTLINED", "GLASS"
    val fontSizeScale: Float = 1.0f, // 0.90f (Compact), 1.0f (Normal), 1.12f (Large)
    val iconNavStyle: String = "DEFAULT", // "DEFAULT", "COMPACT", "PILL"
    val customLogoUri: String? = null,
    val customBannerUri: String? = null,
    val customBackgroundUri: String? = null,
    val homeSectionOrder: String = DEFAULT_HOME_ORDER,
    val hiddenHomeSections: String = "",
    val announcementsLayout: String = "CARD", // "CARD", "COMPACT"
    val resultsLayout: String = "DETAILED", // "DETAILED", "COMPACT"
    val adminDashboardLayout: String = "GRID", // "GRID", "LIST"
    val lastUpdatedTimestamp: Long = System.currentTimeMillis()
) {
    fun orderedHomeSections(): List<String> {
        val defaultList = DEFAULT_HOME_ORDER.split(",")
        val saved = homeSectionOrder.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        val valid = saved.filter { it in defaultList }.toMutableList()
        defaultList.forEach { if (it !in valid) valid.add(it) }
        return valid
    }

    fun isSectionVisible(sectionKey: String): Boolean {
        val hidden = hiddenHomeSections.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        return sectionKey !in hidden
    }

    companion object {
        const val ID_PUBLISHED = 1
        const val ID_DRAFT = 2
        const val DEFAULT_HOME_ORDER = "HERO,QUICK_NAV,HONOR_ROLL,PRINCIPAL,NOTICES,GALLERY_PREVIEW,RESULTS_PORTAL"

        val HOME_SECTION_LABELS = mapOf(
            "HERO" to "School Welcome Banner & Stats",
            "QUICK_NAV" to "Quick Access Services Grid",
            "HONOR_ROLL" to "Scholar Spotlight Card",
            "PRINCIPAL" to "Principal's Message",
            "NOTICES" to "Latest Official Circulars",
            "GALLERY_PREVIEW" to "Campus Gallery Highlights",
            "RESULTS_PORTAL" to "Results Quick Search & Portal"
        )

        fun parseColorSafe(hex: String, fallback: Color): Color {
            return try {
                val cleaned = hex.trim().removePrefix("#")
                val value = cleaned.toLong(16)
                when (cleaned.length) {
                    6 -> Color(0xFF000000L or value)
                    8 -> Color(value)
                    else -> fallback
                }
            } catch (_: Exception) {
                fallback
            }
        }
    }
}

/**
 * Room entity representing managed Classes & Subjects curriculum entries (Classes 5th–12th).
 */
@Entity(tableName = "class_subjects")
data class ClassSubjectRecord(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val classLevel: String, // "5th" .. "12th"
    val subjectName: String,
    val subjectCode: String,
    val teacherName: String = "Faculty Member",
    val maxMarks: Int = 100,
    val passMarks: Int = 40,
    val isCompulsory: Boolean = true
)
