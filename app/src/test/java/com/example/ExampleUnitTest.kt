package com.example

import com.example.data.local.InitialSchoolData
import com.example.data.model.StudentAssignment
import com.example.data.model.StudentResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testGradeCalculation() {
        assertEquals("A+", StudentResult.calculateGrade(95, 100))
        assertEquals("A", StudentResult.calculateGrade(78, 100))
        assertEquals("B", StudentResult.calculateGrade(68, 100))
        assertEquals("C", StudentResult.calculateGrade(58, 100))
        assertEquals("D", StudentResult.calculateGrade(45, 100))
        assertEquals("F", StudentResult.calculateGrade(35, 100))
        assertEquals("F", StudentResult.calculateGrade(0, 100))
    }

    @Test
    fun testAssignmentOverdueLogic() {
        val now = System.currentTimeMillis()
        val pastDue = now - (24L * 3600 * 1000)
        val futureDue = now + (24L * 3600 * 1000)

        val overdueAssignment = StudentAssignment(
            title = "Past Assignment",
            subject = "Physics",
            courseCode = "PHY-12",
            classLevel = "12th",
            teacherName = "Sir Muhammad Ali",
            description = "Test description",
            dueDateTimestamp = pastDue,
            dueDateFormatted = "Yesterday",
            isCompleted = false
        )
        assertTrue(overdueAssignment.isOverdue)

        val completedPastAssignment = overdueAssignment.copy(
            isCompleted = true,
            completedAtTimestamp = pastDue
        )
        assertFalse(completedPastAssignment.isOverdue)

        val upcomingAssignment = overdueAssignment.copy(
            dueDateTimestamp = futureDue,
            dueDateFormatted = "Tomorrow",
            isCompleted = false
        )
        assertFalse(upcomingAssignment.isOverdue)
    }

    @Test
    fun testInitialSchoolAssignments() {
        val assignments = InitialSchoolData.getInitialAssignments()
        assertTrue(assignments.isNotEmpty())

        val classes = assignments.map { it.classLevel }.toSet()
        assertTrue(classes.contains("12th"))
        assertTrue(classes.contains("11th"))
        assertTrue(classes.contains("10th"))
        assertTrue(classes.contains("9th"))
        assertTrue(classes.contains("8th"))

        val cs12 = assignments.firstOrNull { it.courseCode == "CS-12" }
        assertNotNull(cs12)
        assertEquals("Computer Science", cs12!!.subject)
    }

    @Test
    fun testInitialSchoolResults() {
        val results = InitialSchoolData.getInitialResults()
        assertTrue(results.isNotEmpty())

        val muhammadKhanResults = results.filter { it.rollNumber == "BHSS-2026-104" }
        assertTrue(muhammadKhanResults.size >= 4)

        val csResult = muhammadKhanResults.firstOrNull { it.subjectCode == "CS-12" }
        assertNotNull(csResult)
        assertEquals(95, csResult!!.obtainedMarks)
        assertEquals("A+", csResult.grade)
        assertEquals("PASS", csResult.status)
    }

    @Test
    fun testStudentResultStatusPassFail() {
        val passResult = StudentResult(
            studentName = "Test Student",
            rollNumber = "BHSS-TEST-01",
            classLevel = "10th",
            examTerm = "Mid Term",
            subjectName = "Mathematics",
            subjectCode = "MATH-10",
            totalMarks = 100,
            obtainedMarks = 40
        )
        assertEquals("PASS", passResult.status)

        val failResult = passResult.copy(obtainedMarks = 39)
        assertEquals("FAIL", failResult.status)
    }

    @Test
    fun testInitialSchoolNotifications() {
        val notifications = InitialSchoolData.getInitialNotifications()
        assertTrue(notifications.isNotEmpty())

        val types = notifications.map { it.type }.toSet()
        assertTrue(types.contains(com.example.data.model.NotificationType.UPCOMING_DEADLINE))
        assertTrue(types.contains(com.example.data.model.NotificationType.GRADE_UPDATE))
        assertTrue(types.contains(com.example.data.model.NotificationType.NEW_ASSIGNMENT))
        assertTrue(types.contains(com.example.data.model.NotificationType.COLLEGE_ANNOUNCEMENT))

        val deadlineNotif = notifications.firstOrNull { it.type == com.example.data.model.NotificationType.UPCOMING_DEADLINE }
        assertNotNull(deadlineNotif)
        assertTrue(deadlineNotif!!.priority == com.example.data.model.NotificationPriority.URGENT || deadlineNotif.priority == com.example.data.model.NotificationPriority.HIGH)
    }

    @Test
    fun testInitialCollegeAnnouncements() {
        val announcements = InitialSchoolData.getInitialAnnouncements()
        assertTrue(announcements.isNotEmpty())

        val pinnedAnn = announcements.firstOrNull { it.isPinned }
        assertNotNull(pinnedAnn)
        assertEquals("Examinations", pinnedAnn!!.category)
    }
}
