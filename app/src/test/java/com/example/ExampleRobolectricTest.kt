package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Boys Higher Secondary School Tolti", appName)
  }

  @Test
  fun `verify StudentResults calculation and subject list for classes 8th through 12th`() {
    val sampleResult = com.example.data.model.StudentResults(
      studentName = "Muhammad Khan",
      fatherName = "Ghulam Muhammad",
      rollNumber = "BHSS-2026-104",
      classLevel = "12th",
      section = "A",
      examTerm = "FBISE HSSC-II Annual Examination",
      englishSubjectName = "English Compulsory",
      englishObtained = 88,
      englishTotal = 100,
      urduSubjectName = "Urdu Compulsory",
      urduObtained = 85,
      urduTotal = 100,
      mathSubjectName = "Mathematics",
      mathObtained = 96,
      mathTotal = 100,
      scienceSubjectName = "Physics",
      scienceObtained = 92,
      scienceTotal = 100,
      chemistryOrSocialSubjectName = "Statistics",
      chemistryOrSocialObtained = 90,
      chemistryOrSocialTotal = 100,
      electiveSubjectName = "Computer Science",
      electiveObtained = 95,
      electiveTotal = 100,
      islamicStudiesSubjectName = "Pakistan Studies",
      islamicStudiesObtained = 46,
      islamicStudiesTotal = 50
    )

    assertEquals("12th", sampleResult.classLevel)
    assertEquals(592, sampleResult.totalObtainedMarks)
    assertEquals("A+", sampleResult.grade)
    assertEquals("PASS", sampleResult.status)

    val subjects = sampleResult.getSubjectList()
    assertEquals(7, subjects.size)
    assertEquals("English Compulsory", subjects[0].subjectName)
    assertEquals(88, subjects[0].obtainedMarks)
    assertEquals(true, subjects[0].isPass)
  }
}
