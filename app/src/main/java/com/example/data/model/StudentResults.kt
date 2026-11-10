package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Room entity representing comprehensive subject-wise examination results for
 * students across classes 8th through 12th at Govt. Boys Higher Secondary School Tolti.
 */
@Entity(tableName = "student_subject_results")
data class StudentResults(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val studentName: String,
    val fatherName: String = "N/A",
    val rollNumber: String,
    val classLevel: String, // "8th", "9th", "10th", "11th", "12th"
    val section: String = "A",
    val examTerm: String = "Annual Board Examination 2026",
    val boardName: String = "BISE / FBISE Gilgit-Baltistan",
    val academicYear: String = "2025-2026",

    // Subject 1: English
    val englishSubjectName: String = "English Compulsory",
    val englishObtained: Int = 0,
    val englishTotal: Int = 100,

    // Subject 2: Urdu
    val urduSubjectName: String = "Urdu Compulsory",
    val urduObtained: Int = 0,
    val urduTotal: Int = 100,

    // Subject 3: Mathematics
    val mathSubjectName: String = "Mathematics",
    val mathObtained: Int = 0,
    val mathTotal: Int = 100,

    // Subject 4: Physics / General Science
    val scienceSubjectName: String = "Physics", // or "General Science" for 8th
    val scienceObtained: Int = 0,
    val scienceTotal: Int = 100,

    // Subject 5: Chemistry / Social Studies / Statistics
    val chemistryOrSocialSubjectName: String = "Chemistry", // or "Social Studies" for 8th
    val chemistryOrSocialObtained: Int = 0,
    val chemistryOrSocialTotal: Int = 100,

    // Subject 6: Computer Science / Biology
    val electiveSubjectName: String = "Computer Science", // or "Biology"
    val electiveObtained: Int = 0,
    val electiveTotal: Int = 100,

    // Subject 7: Islamic Studies & Pakistan Studies
    val islamicStudiesSubjectName: String = "Islamic Studies & Pak Studies",
    val islamicStudiesObtained: Int = 0,
    val islamicStudiesTotal: Int = 50,

    // Remarks & Metadata
    val remarks: String = "Promoted with Distinction",
    val enteredBy: String = "Exam Controller / Principal Office",
    val dateRecorded: Long = System.currentTimeMillis(),
    val isPublished: Boolean = true
) {
    // Computed aggregate totals
    val totalObtainedMarks: Int
        get() = englishObtained + urduObtained + mathObtained + scienceObtained +
                chemistryOrSocialObtained + electiveObtained + islamicStudiesObtained

    val maximumMarks: Int
        get() = englishTotal + urduTotal + mathTotal + scienceTotal +
                chemistryOrSocialTotal + electiveTotal + islamicStudiesTotal

    val percentage: Double
        get() = if (maximumMarks > 0) (totalObtainedMarks.toDouble() / maximumMarks.toDouble()) * 100.0 else 0.0

    val grade: String
        get() = calculateGrade(totalObtainedMarks, maximumMarks)

    val isPassed: Boolean
        get() = percentage >= 40.0 &&
                (englishObtained >= englishTotal * 0.33) &&
                (urduObtained >= urduTotal * 0.33) &&
                (mathObtained >= mathTotal * 0.33)

    val resultStatus: String
        get() = if (isPassed) "PASS" else "FAIL"

    val status: String
        get() = resultStatus

    val totalMaxMarks: Int
        get() = maximumMarks

    val examName: String
        get() = examTerm

    val division: String
        get() = if (percentage >= 60.0) "1st Division" else if (percentage >= 45.0) "2nd Division" else "3rd Division"

    /**
     * Converts to structured subject-wise score cards for easy UI iteration
     */
    fun toSubjectScoreList(): List<SubjectScoreItem> {
        return listOf(
            SubjectScoreItem(englishSubjectName, englishObtained, englishTotal),
            SubjectScoreItem(urduSubjectName, urduObtained, urduTotal),
            SubjectScoreItem(mathSubjectName, mathObtained, mathTotal),
            SubjectScoreItem(scienceSubjectName, scienceObtained, scienceTotal),
            SubjectScoreItem(chemistryOrSocialSubjectName, chemistryOrSocialObtained, chemistryOrSocialTotal),
            SubjectScoreItem(electiveSubjectName, electiveObtained, electiveTotal),
            SubjectScoreItem(islamicStudiesSubjectName, islamicStudiesObtained, islamicStudiesTotal)
        )
    }

    fun getSubjectList(): List<SubjectScoreItem> = toSubjectScoreList()

    companion object {
        fun calculateGrade(obtained: Int, total: Int): String {
            if (total <= 0) return "F"
            val pct = (obtained.toDouble() / total.toDouble()) * 100.0
            return when {
                pct >= 85.0 -> "A+"
                pct >= 75.0 -> "A"
                pct >= 65.0 -> "B"
                pct >= 55.0 -> "C"
                pct >= 40.0 -> "D"
                else -> "F"
            }
        }
    }
}

/**
 * Single subject score item for grid display and marksheet breakdown.
 */
data class SubjectScoreItem(
    val subjectName: String,
    val obtainedMarks: Int,
    val totalMarks: Int
) {
    val maxMarks: Int
        get() = totalMarks

    val passingMarks: Int
        get() = (totalMarks * 0.33).toInt()

    val percentage: Double
        get() = if (totalMarks > 0) (obtainedMarks.toDouble() / totalMarks.toDouble()) * 100.0 else 0.0

    val grade: String
        get() = StudentResults.calculateGrade(obtainedMarks, totalMarks)

    val isPassed: Boolean
        get() = obtainedMarks >= (totalMarks * 0.33)

    val isPass: Boolean
        get() = isPassed
}
