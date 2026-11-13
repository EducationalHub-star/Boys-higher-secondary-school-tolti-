package com.example.data.local

import com.example.data.model.AdminActivityLog
import com.example.data.model.AdminUser
import com.example.data.model.AttendanceRecord
import com.example.data.model.ClassSubjectRecord
import com.example.data.model.CollegeAnnouncement
import com.example.data.model.DailyAttendance
import com.example.data.model.FeeRecord
import com.example.data.model.GalleryPhoto
import com.example.data.model.NotificationPriority
import com.example.data.model.NotificationType
import com.example.data.model.SchoolNotification
import com.example.data.model.SchoolSettingsRecord
import com.example.data.model.StudentAssignment
import com.example.data.model.StudentProfileRecord
import com.example.data.model.StudentResult
import com.example.data.model.StudentResults
import com.example.data.model.TeacherProfileRecord
import com.example.data.model.TeacherRating
import com.example.data.model.TimetablePeriod
import com.example.util.SecurityUtils

object InitialSchoolData {

    fun getInitialAssignments(): List<StudentAssignment> {
        val now = System.currentTimeMillis()
        val twoHours = now + (2L * 3600 * 1000)
        val oneDay = now + (24L * 3600 * 1000)
        val threeDays = now + (3L * 24 * 3600 * 1000)
        val fiveDays = now + (5L * 24 * 3600 * 1000)
        val twoDaysAgo = now - (2L * 24 * 3600 * 1000)
        val fiveDaysAgo = now - (5L * 24 * 3600 * 1000)

        return listOf(
            // 12th Class Assignments (Muhammad Khan's current class)
            StudentAssignment(
                id = "asg-12-001",
                title = "C++ OOP & Matrix Classes Lab Project",
                subject = "Computer Science",
                courseCode = "CS-12",
                classLevel = "12th",
                teacherName = "Sir Ghulam Nabi",
                description = "Implement operator overloading and 2D matrix multiplication with dynamic memory pointers.",
                dueDateTimestamp = twoHours,
                dueDateFormatted = "Today, 11:59 PM",
                totalMarks = 50,
                isCompleted = false,
                priority = "URGENT"
            ),
            StudentAssignment(
                id = "asg-12-002",
                title = "Galvanometer Calibration & Voltmeter Conversion",
                subject = "Physics",
                courseCode = "PHY-12",
                classLevel = "12th",
                teacherName = "Sir Muhammad Ali",
                description = "Plot the calibration curves for galvanometer to ammeter and calculate series multiplier resistance.",
                dueDateTimestamp = oneDay,
                dueDateFormatted = "Tomorrow, 02:00 PM",
                totalMarks = 50,
                isCompleted = false,
                priority = "NORMAL"
            ),
            StudentAssignment(
                id = "asg-12-003",
                title = "Calculus Definite Integrals Exercise 3.6",
                subject = "Mathematics",
                courseCode = "MATH-12",
                classLevel = "12th",
                teacherName = "Sir Hassan Raza",
                description = "Solve Chapter 3 integral problem sets with step-by-step substitution and limit evaluations.",
                dueDateTimestamp = threeDays,
                dueDateFormatted = "In 3 Days",
                totalMarks = 50,
                isCompleted = true,
                completedAtTimestamp = twoDaysAgo,
                submissionNotes = "Submitted 15 solved proofs in lab folder.",
                priority = "NORMAL"
            ),
            StudentAssignment(
                id = "asg-12-004",
                title = "Essay: Technical Literacy in Baltistan Valleys",
                subject = "English",
                courseCode = "ENG-12",
                classLevel = "12th",
                teacherName = "Sir Sajjad Hussain",
                description = "Write a 500-word essay discussing educational opportunities in Tolti Kharmang.",
                dueDateTimestamp = fiveDaysAgo,
                dueDateFormatted = "Oct 25 (Closed)",
                totalMarks = 50,
                isCompleted = true,
                completedAtTimestamp = fiveDaysAgo,
                submissionNotes = "Handwritten assignment submitted to Sir Sajjad.",
                priority = "LOW"
            ),

            // 11th Class Assignments
            StudentAssignment(
                id = "asg-11-001",
                title = "Logic Gates Truth Tables & Karnaugh Maps",
                subject = "Computer Science",
                courseCode = "CS-11",
                classLevel = "11th",
                teacherName = "Sir Ghulam Nabi",
                description = "Construct truth tables for NAND/NOR universal logic gates and simplify minterms.",
                dueDateTimestamp = threeDays,
                dueDateFormatted = "In 3 Days",
                totalMarks = 50,
                isCompleted = false,
                priority = "NORMAL"
            ),
            StudentAssignment(
                id = "asg-11-002",
                title = "Vectors Equilibrium & Projectile Trajectory",
                subject = "Physics",
                courseCode = "PHY-11",
                classLevel = "11th",
                teacherName = "Sir Muhammad Ali",
                description = "Solve Chapter 2 numerical exercises on projectile range and maximum height.",
                dueDateTimestamp = fiveDays,
                dueDateFormatted = "In 5 Days",
                totalMarks = 50,
                isCompleted = false,
                priority = "NORMAL"
            ),

            // 10th Class (Matric Part 2)
            StudentAssignment(
                id = "asg-10-001",
                title = "Simple Harmonic Motion & Pendulum Experiment",
                subject = "Physics",
                courseCode = "PHY-10",
                classLevel = "10th",
                teacherName = "Sir Muhammad Ali",
                description = "Measure time period vs string length and verify value of acceleration due to gravity (g).",
                dueDateTimestamp = oneDay,
                dueDateFormatted = "Tomorrow, 04:00 PM",
                totalMarks = 50,
                isCompleted = false,
                priority = "NORMAL"
            ),
            StudentAssignment(
                id = "asg-10-002",
                title = "Hydrocarbons & Alkane Combustion Reactions",
                subject = "Chemistry",
                courseCode = "CHEM-10",
                classLevel = "10th",
                teacherName = "Sir Wazir Kazim",
                description = "Balance chemical equations for complete combustion of methane and ethane.",
                dueDateTimestamp = fourDaysFromNow(now),
                dueDateFormatted = "In 4 Days",
                totalMarks = 50,
                isCompleted = true,
                completedAtTimestamp = twoDaysAgo,
                priority = "NORMAL"
            ),

            // 9th Class (Matric Part 1)
            StudentAssignment(
                id = "asg-09-001",
                title = "Vernier Caliper & Screw Gauge Least Count Sheet",
                subject = "Physics",
                courseCode = "PHY-9",
                classLevel = "9th",
                teacherName = "Sir Muhammad Ali",
                description = "Record zero error corrections and measure thickness of glass plate.",
                dueDateTimestamp = threeDays,
                dueDateFormatted = "In 3 Days",
                totalMarks = 50,
                isCompleted = false,
                priority = "NORMAL"
            ),
            StudentAssignment(
                id = "asg-09-002",
                title = "Plant vs Animal Cell Organelles Diagram",
                subject = "Biology",
                courseCode = "BIO-9",
                classLevel = "9th",
                teacherName = "Sir Zakir Hussain",
                description = "Draw neat diagrams of chloroplast and mitochondria with cellular labels.",
                dueDateTimestamp = fiveDays,
                dueDateFormatted = "In 5 Days",
                totalMarks = 50,
                isCompleted = false,
                priority = "NORMAL"
            ),

            // 8th Class (Middle Section)
            StudentAssignment(
                id = "asg-08-001",
                title = "Karakoram Alpine Ecology & Food Web Chart",
                subject = "General Science",
                courseCode = "SCI-8",
                classLevel = "8th",
                teacherName = "Sir Ghulam Abbas",
                description = "Chart producer-consumer trophic levels in the Kharmang mountain ecosystem.",
                dueDateTimestamp = oneDay,
                dueDateFormatted = "Tomorrow",
                totalMarks = 50,
                isCompleted = false,
                priority = "URGENT"
            ),
            StudentAssignment(
                id = "asg-08-002",
                title = "Sets, Subsets & Venn Diagram Chapter 1",
                subject = "Mathematics",
                courseCode = "MATH-8",
                classLevel = "8th",
                teacherName = "Sir Muhammad Ishaq",
                description = "Solve Venn diagram shading questions for intersection and union.",
                dueDateTimestamp = fiveDays,
                dueDateFormatted = "In 5 Days",
                totalMarks = 50,
                isCompleted = true,
                completedAtTimestamp = twoDaysAgo,
                priority = "NORMAL"
            )
        )
    }

    private fun fourDaysFromNow(now: Long) = now + (4L * 24 * 3600 * 1000)

    fun getInitialResults(): List<StudentResult> {
        val now = System.currentTimeMillis()
        val yesterday = now - (24L * 3600 * 1000)
        val threeDaysAgo = now - (3L * 24 * 3600 * 1000)

        return listOf(
            // Muhammad Khan's official results for Class 12th
            StudentResult(
                id = "res-12-001",
                studentName = "Muhammad Khan",
                rollNumber = "BHSS-2026-104",
                classLevel = "12th",
                examTerm = "Mid Term Exam 2026",
                subjectName = "Computer Science",
                subjectCode = "CS-12",
                totalMarks = 100,
                obtainedMarks = 95,
                remarks = "Outstanding command of C++ OOP and database schemas.",
                enteredByAdmin = "Sir Ghulam Nabi (Head Examiner)",
                dateRecorded = yesterday
            ),
            StudentResult(
                id = "res-12-002",
                studentName = "Muhammad Khan",
                rollNumber = "BHSS-2026-104",
                classLevel = "12th",
                examTerm = "Mid Term Exam 2026",
                subjectName = "Mathematics",
                subjectCode = "MATH-12",
                totalMarks = 100,
                obtainedMarks = 92,
                remarks = "Excellent accuracy in differentiation and definite integrals.",
                enteredByAdmin = "Sir Hassan Raza",
                dateRecorded = yesterday
            ),
            StudentResult(
                id = "res-12-003",
                studentName = "Muhammad Khan",
                rollNumber = "BHSS-2026-104",
                classLevel = "12th",
                examTerm = "Mid Term Exam 2026",
                subjectName = "Physics",
                subjectCode = "PHY-12",
                totalMarks = 100,
                obtainedMarks = 88,
                remarks = "Strong theoretical grasp and neat circuit diagrams.",
                enteredByAdmin = "Sir Muhammad Ali",
                dateRecorded = threeDaysAgo
            ),
            StudentResult(
                id = "res-12-004",
                studentName = "Muhammad Khan",
                rollNumber = "BHSS-2026-104",
                classLevel = "12th",
                examTerm = "Mid Term Exam 2026",
                subjectName = "English Compulsory",
                subjectCode = "ENG-12",
                totalMarks = 100,
                obtainedMarks = 86,
                remarks = "Eloquent essay structure and rich vocabulary.",
                enteredByAdmin = "Sir Sajjad Hussain",
                dateRecorded = threeDaysAgo
            ),
            StudentResult(
                id = "res-12-005",
                studentName = "Muhammad Khan",
                rollNumber = "BHSS-2026-104",
                classLevel = "12th",
                examTerm = "Mid Term Exam 2026",
                subjectName = "Urdu & Pak Studies",
                subjectCode = "URDU-12",
                totalMarks = 100,
                obtainedMarks = 89,
                remarks = "Deep analytical answers on constitutional history.",
                enteredByAdmin = "Sir Kazim Ali",
                dateRecorded = threeDaysAgo
            ),

            // Class 10th sample result
            StudentResult(
                id = "res-10-001",
                studentName = "Ali Raza",
                rollNumber = "BHSS-10TH-012",
                classLevel = "10th",
                examTerm = "Monthly Test October",
                subjectName = "Mathematics",
                subjectCode = "MATH-10",
                totalMarks = 50,
                obtainedMarks = 46,
                remarks = "Cramer rule and matrices solved perfectly.",
                enteredByAdmin = "Sir Hassan Raza",
                dateRecorded = yesterday
            ),

            // Class 9th sample result
            StudentResult(
                id = "res-09-001",
                studentName = "Haidar Ali",
                rollNumber = "BHSS-9TH-018",
                classLevel = "9th",
                examTerm = "Monthly Test October",
                subjectName = "Physics",
                subjectCode = "PHY-9",
                totalMarks = 50,
                obtainedMarks = 43,
                remarks = "Very good understanding of motion graphs.",
                enteredByAdmin = "Sir Muhammad Ali",
                dateRecorded = yesterday
            ),

            // Class 8th sample result
            StudentResult(
                id = "res-08-001",
                studentName = "Zafar Iqbal",
                rollNumber = "BHSS-8TH-005",
                classLevel = "8th",
                examTerm = "Monthly Test October",
                subjectName = "General Science",
                subjectCode = "SCI-8",
                totalMarks = 50,
                obtainedMarks = 48,
                remarks = "Top score in ecology and cellular biology.",
                enteredByAdmin = "Sir Ghulam Abbas",
                dateRecorded = yesterday
            )
        )
    }

    fun getInitialAttendance(): List<AttendanceRecord> = listOf(
        AttendanceRecord(
            id = "att-12-1",
            courseCode = "CS-12",
            subjectName = "Computer Science (C++ & DB)",
            classLevel = "12th",
            teacherName = "Sir Ghulam Nabi",
            attendedCount = 28,
            totalCount = 30,
            roomNumber = "CS Lab 1"
        ),
        AttendanceRecord(
            id = "att-12-2",
            courseCode = "PHY-12",
            subjectName = "Physics Practical & Theory",
            classLevel = "12th",
            teacherName = "Sir Muhammad Ali",
            attendedCount = 26,
            totalCount = 28,
            roomNumber = "Physics Lab"
        ),
        AttendanceRecord(
            id = "att-12-3",
            courseCode = "MATH-12",
            subjectName = "Mathematics (Calculus)",
            classLevel = "12th",
            teacherName = "Sir Hassan Raza",
            attendedCount = 27,
            totalCount = 29,
            roomNumber = "Room 102"
        ),
        AttendanceRecord(
            id = "att-12-4",
            courseCode = "ENG-12",
            subjectName = "English Compulsory",
            classLevel = "12th",
            teacherName = "Sir Sajjad Hussain",
            attendedCount = 26,
            totalCount = 28,
            roomNumber = "Room 104"
        ),
        AttendanceRecord(
            id = "att-12-5",
            courseCode = "URDU-12",
            subjectName = "Urdu & Pak Studies",
            classLevel = "12th",
            teacherName = "Sir Kazim Ali",
            attendedCount = 24,
            totalCount = 26,
            roomNumber = "Main Hall"
        )
    )

    fun getInitialNotifications(): List<SchoolNotification> {
        val now = System.currentTimeMillis()
        val tenMinsAgo = now - (10L * 60 * 1000)
        val fortyMinsAgo = now - (40L * 60 * 1000)
        val twoHoursAgo = now - (2L * 3600 * 1000)
        val fiveHoursAgo = now - (5L * 3600 * 1000)
        val yesterday = now - (24L * 3600 * 1000)
        val twoDaysAgo = now - (48L * 3600 * 1000)

        return listOf(
            SchoolNotification(
                id = "notif-001",
                title = "Due in 2 Hours: C++ OOP & Matrix Lab Project",
                message = "Your assignment 'C++ OOP & Matrix Classes Lab Project' is due tonight at 11:59 PM. Submit your solution code on time.",
                type = NotificationType.UPCOMING_DEADLINE,
                priority = NotificationPriority.URGENT,
                timestamp = tenMinsAgo,
                isRead = false,
                targetClass = "12th",
                relatedId = "asg-12-001",
                relatedSubject = "Computer Science"
            ),
            SchoolNotification(
                id = "notif-002",
                title = "Upcoming Deadline: Galvanometer Calibration",
                message = "Physics practical lab submission is due tomorrow at 02:00 PM. Verify galvanometer circuit multiplier calculations.",
                type = NotificationType.UPCOMING_DEADLINE,
                priority = NotificationPriority.HIGH,
                timestamp = fortyMinsAgo,
                isRead = false,
                targetClass = "12th",
                relatedId = "asg-12-002",
                relatedSubject = "Physics"
            ),
            SchoolNotification(
                id = "notif-003",
                title = "New Grade Released: Computer Science (95/100)",
                message = "Sir Ghulam Nabi entered Mid Term exam marks for Muhammad Khan (CS-12). Grade: A+ (95.0%). Remarks: Outstanding command of C++ OOP.",
                type = NotificationType.GRADE_UPDATE,
                priority = NotificationPriority.HIGH,
                timestamp = twoHoursAgo,
                isRead = false,
                targetClass = "12th",
                relatedId = "res-12-001",
                relatedSubject = "Computer Science"
            ),
            SchoolNotification(
                id = "notif-004",
                title = "College Announcement: Winter Examination Schedule",
                message = "Principal Office published the official Board preparation exam timetable starting Nov 15th for classes 9th to 12th.",
                type = NotificationType.COLLEGE_ANNOUNCEMENT,
                priority = NotificationPriority.HIGH,
                timestamp = fiveHoursAgo,
                isRead = false,
                targetClass = "ALL",
                relatedId = "ann-001"
            ),
            SchoolNotification(
                id = "notif-005",
                title = "New Grade Released: Mathematics (92/100)",
                message = "Sir Hassan Raza posted Mid Term Calculus marks. Result: 92/100 (Grade A+). Definite integrals scored top marks in class.",
                type = NotificationType.GRADE_UPDATE,
                priority = NotificationPriority.NORMAL,
                timestamp = yesterday,
                isRead = true,
                targetClass = "12th",
                relatedId = "res-12-002",
                relatedSubject = "Mathematics"
            ),
            SchoolNotification(
                id = "notif-006",
                title = "New Assignment: Logic Gates & Karnaugh Maps",
                message = "Sir Ghulam Nabi assigned new exercises on NAND/NOR logic reduction. Due in 3 days for Intermediate classes.",
                type = NotificationType.NEW_ASSIGNMENT,
                priority = NotificationPriority.NORMAL,
                timestamp = yesterday - (2L * 3600 * 1000),
                isRead = true,
                targetClass = "11th",
                relatedId = "asg-11-001",
                relatedSubject = "Computer Science"
            ),
            SchoolNotification(
                id = "notif-007",
                title = "College Announcement: Science Fair 2026",
                message = "Kharmang Inter-School Science & Innovation Fair registration is open. Form submissions accepted at Academic Office.",
                type = NotificationType.COLLEGE_ANNOUNCEMENT,
                priority = NotificationPriority.INFO,
                timestamp = twoDaysAgo,
                isRead = true,
                targetClass = "ALL",
                relatedId = "ann-002"
            )
        )
    }

    fun getInitialAnnouncements(): List<CollegeAnnouncement> {
        val now = System.currentTimeMillis()
        val fiveHoursAgo = now - (5L * 3600 * 1000)
        val oneDayAgo = now - (24L * 3600 * 1000)
        val threeDaysAgo = now - (3L * 24 * 3600 * 1000)
        val fiveDaysAgo = now - (5L * 24 * 3600 * 1000)

        return listOf(
            CollegeAnnouncement(
                id = "ann-001",
                title = "Winter Pre-Board & Annual Practical Exam Timetable 2026",
                content = "All students of Classes 9th, 10th, 11th, and 12th are notified that pre-board model examinations will commence from November 15, 2026. Practical examinations for Physics and Computer Science will precede theory sessions. Daily attendance of at least 80% is mandatory to obtain roll number slips.",
                category = "Examinations",
                targetAudience = "Classes 9th - 12th",
                author = "Principal Office & Exam Controller",
                timestamp = fiveHoursAgo,
                isPinned = true,
                isUrgent = true
            ),
            CollegeAnnouncement(
                id = "ann-002",
                title = "Baltistan Inter-College Science & Robotics Exhibition",
                content = "Govt. Higher Secondary School Tolti will host student science exhibits in software programming, solar hydrology, and Karakoram environmental monitoring. Interested teams must register their project synopsis with Sir Ghulam Nabi or Sir Muhammad Ali by Friday.",
                category = "Campus Events",
                targetAudience = "All Students",
                author = "Science & Tech Department",
                timestamp = oneDayAgo,
                isPinned = true,
                isUrgent = false
            ),
            CollegeAnnouncement(
                id = "ann-003",
                title = "Extended Practical Lab Timings for ICS & Pre-Engineering",
                content = "To facilitate board practical preparations, the Computer Science and Physics laboratories will remain open from 03:00 PM to 05:30 PM under supervision of lab demonstrators. High-speed internet and equipment stations are available.",
                category = "Academic",
                targetAudience = "11th & 12th Class",
                author = "Vice Principal Academic Affairs",
                timestamp = threeDaysAgo,
                isPinned = false,
                isUrgent = false
            ),
            CollegeAnnouncement(
                id = "ann-004",
                title = "District Kharmang Inter-School Autumn Cricket Cup",
                content = "Trials for the College Cricket team (Under-19) will be conducted at the Tolti River Grounds this Saturday morning at 09:00 AM. Eligible student athletes must bring their sports kit and college ID card.",
                category = "Sports & Co-curricular",
                targetAudience = "All Students",
                author = "Director of Physical Education",
                timestamp = fiveDaysAgo,
                isPinned = false,
                isUrgent = false
            )
        )
    }

    fun getInitialStudents(): List<StudentProfileRecord> = listOf(
        // 12th Class
        StudentProfileRecord(
            id = "std-12-104",
            fullName = "Muhammad Khan",
            fatherName = "Ghulam Muhammad",
            rollNumber = "BHSS-2026-104",
            classLevel = "12th",
            section = "A",
            dateOfBirth = "2008-04-12",
            admissionDate = "2024-04-01",
            contactPhone = "0355-5123456",
            address = "Gound Tolti, Kharmang",
            bloodGroup = "O+",
            status = "ACTIVE",
            notes = "Top academic rank in Computer Science & Mathematics. Class Prefect."
        ),
        StudentProfileRecord(
            id = "std-12-105",
            fullName = "Ali Jawad",
            fatherName = "Hassan Ali",
            rollNumber = "BHSS-2026-105",
            classLevel = "12th",
            section = "A",
            dateOfBirth = "2008-07-20",
            admissionDate = "2024-04-01",
            contactPhone = "0355-5234567",
            address = "Khas Tolti, Kharmang",
            bloodGroup = "A+",
            status = "ACTIVE",
            notes = "Member of College Cricket team. Active participant in Physics lab experiments."
        ),
        StudentProfileRecord(
            id = "std-12-106",
            fullName = "Khadim Hussain",
            fatherName = "Muhammad Baqir",
            rollNumber = "BHSS-2026-106",
            classLevel = "12th",
            section = "A",
            dateOfBirth = "2008-02-18",
            admissionDate = "2024-04-01",
            contactPhone = "0355-5345678",
            address = "Main Bazar Tolti, Kharmang",
            bloodGroup = "B+",
            status = "ACTIVE",
            notes = "Debate society president. High distinction in English Essay competitions."
        ),

        // 11th Class
        StudentProfileRecord(
            id = "std-11-015",
            fullName = "Sajjad Ali",
            fatherName = "Muhammad Kazim",
            rollNumber = "BHSS-11TH-015",
            classLevel = "11th",
            section = "A",
            dateOfBirth = "2009-06-14",
            admissionDate = "2025-04-01",
            contactPhone = "0355-5456789",
            address = "Pari Tolti, Kharmang",
            bloodGroup = "AB+",
            status = "ACTIVE",
            notes = "Keen interest in Digital Electronics and logic circuit prototyping."
        ),
        StudentProfileRecord(
            id = "std-11-016",
            fullName = "Hassan Raza",
            fatherName = "Ali Raza",
            rollNumber = "BHSS-11TH-016",
            classLevel = "11th",
            section = "A",
            dateOfBirth = "2009-09-05",
            admissionDate = "2025-04-01",
            contactPhone = "0355-5567890",
            address = "Mayoor Tolti, Kharmang",
            bloodGroup = "O+",
            status = "ACTIVE",
            notes = "Mathematical Olympiad silver medalist in district qualifiers."
        ),

        // 10th Class (Matric Part 2)
        StudentProfileRecord(
            id = "std-10-012",
            fullName = "Ali Raza",
            fatherName = "Muhammad Musa",
            rollNumber = "BHSS-10TH-012",
            classLevel = "10th",
            section = "A",
            dateOfBirth = "2010-03-22",
            admissionDate = "2024-04-01",
            contactPhone = "0355-5678901",
            address = "Gound Tolti, Kharmang",
            bloodGroup = "A+",
            status = "ACTIVE",
            notes = "Preparing for SSC-II Board Examinations with distinction."
        ),
        StudentProfileRecord(
            id = "std-10-013",
            fullName = "Ahmad Hassan",
            fatherName = "Ghulam Abbas",
            rollNumber = "BHSS-10TH-013",
            classLevel = "10th",
            section = "A",
            dateOfBirth = "2010-11-10",
            admissionDate = "2024-04-01",
            contactPhone = "0355-5789012",
            address = "Kamango Tolti, Kharmang",
            bloodGroup = "B+",
            status = "ACTIVE",
            notes = "School Scout troop leader. Excellent volunteer record."
        ),

        // 9th Class (Matric Part 1)
        StudentProfileRecord(
            id = "std-09-018",
            fullName = "Haidar Ali",
            fatherName = "Muhammad Ibrahim",
            rollNumber = "BHSS-9TH-018",
            classLevel = "9th",
            section = "A",
            dateOfBirth = "2011-08-30",
            admissionDate = "2025-04-01",
            contactPhone = "0355-5890123",
            address = "River Road Tolti, Kharmang",
            bloodGroup = "O+",
            status = "ACTIVE",
            notes = "Excelling in Biology diagrams and Physics Vernier lab work."
        ),

        // 8th Class (Middle Section)
        StudentProfileRecord(
            id = "std-08-005",
            fullName = "Zafar Iqbal",
            fatherName = "Muhammad Shafi",
            rollNumber = "BHSS-8TH-005",
            classLevel = "8th",
            section = "A",
            dateOfBirth = "2012-01-15",
            admissionDate = "2024-04-01",
            contactPhone = "0355-5901234",
            address = "Main Market Tolti, Kharmang",
            bloodGroup = "A-",
            status = "ACTIVE",
            notes = "Top scorer in General Science monthly tests."
        ),

        // 7th Class
        StudentProfileRecord(
            id = "std-07-008",
            fullName = "Muhammad Baqir",
            fatherName = "Ghulam Nabi",
            rollNumber = "BHSS-7TH-008",
            classLevel = "7th",
            section = "A",
            dateOfBirth = "2013-05-19",
            admissionDate = "2024-04-01",
            contactPhone = "0355-6012345",
            address = "Tolti Valley, Kharmang",
            bloodGroup = "B+",
            status = "ACTIVE",
            notes = "Active participant in school morning assembly Qiraat and Naat."
        ),

        // 6th Class
        StudentProfileRecord(
            id = "std-06-004",
            fullName = "Ilyas Hussain",
            fatherName = "Muhammad Hussain",
            rollNumber = "BHSS-6TH-004",
            classLevel = "6th",
            section = "A",
            dateOfBirth = "2014-03-08",
            admissionDate = "2025-04-01",
            contactPhone = "0355-6123456",
            address = "Tolti Upper, Kharmang",
            bloodGroup = "O+",
            status = "ACTIVE",
            notes = "Fast-track learner in English spelling bee and Urdu calligraphy."
        ),

        // 5th Class (Primary to Middle Bridge)
        StudentProfileRecord(
            id = "std-05-001",
            fullName = "Abbas Ali",
            fatherName = "Muhammad Kazim",
            rollNumber = "BHSS-5TH-001",
            classLevel = "5th",
            section = "A",
            dateOfBirth = "2015-09-12",
            admissionDate = "2025-04-01",
            contactPhone = "0355-6234567",
            address = "Tolti Village, Kharmang",
            bloodGroup = "A+",
            status = "ACTIVE",
            notes = "Newly admitted student with 100% attendance in elementary math."
        )
    )

    fun getInitialTeachers(): List<TeacherProfileRecord> = listOf(
        TeacherProfileRecord(
            id = "tch-001",
            name = "Sir Ghulam Nabi",
            designation = "Principal / Senior Subject Specialist",
            department = "Computer Science & Administration",
            qualification = "M.Sc Computer Science, M.Ed (Gold Medalist)",
            assignedSubjects = "Computer Science (C++ & Database Systems)",
            assignedClasses = "11th, 12th",
            introduction = "Serving as Principal of Boys Higher Secondary School Tolti with over 18 years of academic leadership. Passionate about bringing modern IT infrastructure and digital literacy to the student youth of Kharmang.",
            phone = "0355-920101",
            email = "principal.tolti@edu.pk",
            joiningYear = "2012"
        ),
        TeacherProfileRecord(
            id = "tch-002",
            name = "Sir Muhammad Ali",
            designation = "Senior Subject Specialist (Physics)",
            department = "Physics & Applied Sciences",
            qualification = "M.Sc Physics (KU), B.Ed",
            assignedSubjects = "Physics (Mechanics, Electromagnetism, Electronics)",
            assignedClasses = "9th, 10th, 11th, 12th",
            introduction = "Experienced physics educator dedicated to practical experimentation, circuit design, and guiding students towards high board scores and engineering admissions.",
            phone = "0355-920102",
            email = "muhammadali.phy@edu.pk",
            joiningYear = "2015"
        ),
        TeacherProfileRecord(
            id = "tch-003",
            name = "Sir Hassan Raza",
            designation = "Subject Specialist (Mathematics)",
            department = "Mathematics",
            qualification = "M.Phil Applied Mathematics, B.Ed",
            assignedSubjects = "Calculus, Analytical Geometry, Algebra, Statistics",
            assignedClasses = "10th, 11th, 12th",
            introduction = "Expert in analytical calculus and linear algebra. Trained numerous students who scored 90%+ in FBSE and Karakoram board examinations.",
            phone = "0355-920103",
            email = "hassan.math@edu.pk",
            joiningYear = "2016"
        ),
        TeacherProfileRecord(
            id = "tch-004",
            name = "Sir Sajjad Hussain",
            designation = "Senior Teacher (English Literature & Language)",
            department = "Humanities & Languages",
            qualification = "M.A English Literature, B.Ed",
            assignedSubjects = "English Compulsory, Grammar, Essay Writing",
            assignedClasses = "9th, 10th, 11th, 12th",
            introduction = "Focuses on English communication, essay composition, and literary appreciation to build self-confidence and presentation skills for mountainous region youth.",
            phone = "0355-920104",
            email = "sajjad.eng@edu.pk",
            joiningYear = "2014"
        ),
        TeacherProfileRecord(
            id = "tch-005",
            name = "Sir Kazim Ali",
            designation = "Lecturer (Urdu & Pakistan Studies)",
            department = "Social Sciences",
            qualification = "M.A Urdu, M.A Pakistan Studies",
            assignedSubjects = "Urdu Compulsory, Pakistan Studies, Islamic Studies",
            assignedClasses = "8th, 9th, 10th, 11th, 12th",
            introduction = "Dedicated to imparting constitutional history, regional culture, and Urdu prose and poetry with depth and patriotic spirit.",
            phone = "0355-920105",
            email = "kazim.urdu@edu.pk",
            joiningYear = "2017"
        ),
        TeacherProfileRecord(
            id = "tch-006",
            name = "Sir Zakir Hussain",
            designation = "Subject Specialist (Biology)",
            department = "Life Sciences",
            qualification = "M.Sc Zoology, B.Ed",
            assignedSubjects = "Biology (Cytology, Physiology, Genetics, Ecology)",
            assignedClasses = "9th, 10th, 11th, 12th",
            introduction = "Guiding students into medical and ecological fields through hands-on microscope sessions and Karakoram botanical field surveys.",
            phone = "0355-920106",
            email = "zakir.bio@edu.pk",
            joiningYear = "2018"
        ),
        TeacherProfileRecord(
            id = "tch-007",
            name = "Sir Wazir Kazim",
            designation = "Lecturer (Chemistry)",
            department = "Chemical Sciences",
            qualification = "M.Sc Organic Chemistry",
            assignedSubjects = "Chemistry (Inorganic, Organic, Chemical Equilibrium)",
            assignedClasses = "9th, 10th, 11th, 12th",
            introduction = "Supervises safe chemical experiments, titration benches, and stoichiometric reaction analyses in the campus chemistry lab.",
            phone = "0355-920107",
            email = "wazir.chem@edu.pk",
            joiningYear = "2019"
        ),
        TeacherProfileRecord(
            id = "tch-008",
            name = "Sir Ghulam Abbas",
            designation = "Elementary Science Master",
            department = "General Science & Elementary Wing",
            qualification = "B.Sc, B.Ed",
            assignedSubjects = "General Science, Environmental Studies",
            assignedClasses = "5th, 6th, 7th, 8th",
            introduction = "Inspiring curiosity in foundational science, ecological webs, and hands-on demonstrations for middle section students.",
            phone = "0355-920108",
            email = "abbas.sci@edu.pk",
            joiningYear = "2016"
        ),
        TeacherProfileRecord(
            id = "tch-009",
            name = "Sir Ali Haider",
            designation = "Director Physical Education (DPE)",
            department = "Sports & Co-curricular",
            qualification = "Master in Physical Education (MPEd)",
            assignedSubjects = "Physical Training, Cricket, Table Tennis, Football",
            assignedClasses = "All Classes (5th - 12th)",
            introduction = "Mentoring student athletes for inter-district championships, track-and-field events, and instilling teamwork and discipline.",
            phone = "0355-920109",
            email = "sports.tolti@edu.pk",
            joiningYear = "2020"
        )
    )

    fun getInitialFees(): List<FeeRecord> = listOf(
        FeeRecord(
            id = "fee-12-104-1",
            studentRollNumber = "BHSS-2026-104",
            studentName = "Muhammad Khan",
            classLevel = "12th",
            feeType = "Monthly Tuition (October 2026)",
            amount = 1200.0,
            dueDate = "2026-10-10",
            status = "PAID",
            paymentDate = "2026-10-08",
            receiptNumber = "REC-2026-104",
            paymentMode = "Cash at Accounts Office"
        ),
        FeeRecord(
            id = "fee-12-104-2",
            studentRollNumber = "BHSS-2026-104",
            studentName = "Muhammad Khan",
            classLevel = "12th",
            feeType = "Board Examination Registration Fee",
            amount = 2500.0,
            dueDate = "2026-11-20",
            status = "PAID",
            paymentDate = "2026-10-25",
            receiptNumber = "REC-2026-189",
            paymentMode = "Bank Challan NBP"
        ),
        FeeRecord(
            id = "fee-12-104-3",
            studentRollNumber = "BHSS-2026-104",
            studentName = "Muhammad Khan",
            classLevel = "12th",
            feeType = "Computer Lab & IT Fund (Annual)",
            amount = 800.0,
            dueDate = "2026-11-15",
            status = "PAID",
            paymentDate = "2026-10-12",
            receiptNumber = "REC-2026-210",
            paymentMode = "Cash at Accounts Office"
        ),
        FeeRecord(
            id = "fee-12-105-1",
            studentRollNumber = "BHSS-2026-105",
            studentName = "Ali Jawad",
            classLevel = "12th",
            feeType = "Monthly Tuition (October 2026)",
            amount = 1200.0,
            dueDate = "2026-10-10",
            status = "PAID",
            paymentDate = "2026-10-09",
            receiptNumber = "REC-2026-112",
            paymentMode = "Cash at Accounts Office"
        ),
        FeeRecord(
            id = "fee-10-012-1",
            studentRollNumber = "BHSS-10TH-012",
            studentName = "Ali Raza",
            classLevel = "10th",
            feeType = "Matric Board Examination Registration",
            amount = 2200.0,
            dueDate = "2026-11-25",
            status = "PAID",
            paymentDate = "2026-10-28",
            receiptNumber = "REC-2026-304",
            paymentMode = "Bank Challan NBP"
        ),
        FeeRecord(
            id = "fee-09-018-1",
            studentRollNumber = "BHSS-9TH-018",
            studentName = "Haidar Ali",
            classLevel = "9th",
            feeType = "Science Laboratory Consumables Fund",
            amount = 600.0,
            dueDate = "2026-11-15",
            status = "PENDING",
            paymentDate = null,
            receiptNumber = null,
            paymentMode = "Pending"
        )
    )

    fun getInitialDailyAttendance(): List<DailyAttendance> {
        val today = try {
            java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
        } catch (e: Exception) {
            "2026-11-07"
        }
        val records = mutableListOf<DailyAttendance>()
        val dates = listOf(today, "2026-11-03").distinct()
        for (d in dates) {
            records.add(DailyAttendance("att-d-12-1-$d", "BHSS-2026-104", "Muhammad Khan", "12th", d, "PRESENT", "Sir Ghulam Nabi"))
            records.add(DailyAttendance("att-d-12-2-$d", "BHSS-2026-105", "Ali Jawad", "12th", d, "LATE", "Sir Ghulam Nabi"))
            records.add(DailyAttendance("att-d-12-3-$d", "BHSS-2026-106", "Khadim Hussain", "12th", d, "ABSENT", "Sir Ghulam Nabi"))
            records.add(DailyAttendance("att-d-11-1-$d", "BHSS-11TH-001", "Zahid Hussain", "11th", d, "PRESENT", "Sir Muhammad Ali"))
            records.add(DailyAttendance("att-d-11-2-$d", "BHSS-11TH-002", "Syed Mehdi", "11th", d, "LATE", "Sir Muhammad Ali"))
            records.add(DailyAttendance("att-d-10-1-$d", "BHSS-10TH-012", "Ali Raza", "10th", d, "PRESENT", "Sir Hassan Raza"))
            records.add(DailyAttendance("att-d-10-2-$d", "BHSS-10TH-013", "Sajjad Hussain", "10th", d, "ABSENT", "Sir Hassan Raza"))
            records.add(DailyAttendance("att-d-9-1-$d", "BHSS-9TH-018", "Haidar Ali", "9th", d, "LATE", "Sir Muhammad Ali"))
            records.add(DailyAttendance("att-d-9-2-$d", "BHSS-9TH-019", "Qasim Ali", "9th", d, "PRESENT", "Sir Muhammad Ali"))
            records.add(DailyAttendance("att-d-8-1-$d", "BHSS-8TH-001", "Basharat Ali", "8th", d, "PRESENT", "Sir Ghulam Abbas"))
            records.add(DailyAttendance("att-d-8-2-$d", "BHSS-8TH-002", "Murtaza Khan", "8th", d, "ABSENT", "Sir Ghulam Abbas"))
            records.add(DailyAttendance("att-d-7-1-$d", "BHSS-7TH-001", "Irfan Ali", "7th", d, "PRESENT", "Sir Muhammad Ishaq"))
            records.add(DailyAttendance("att-d-6-1-$d", "BHSS-6TH-001", "Zulfiqar Ali", "6th", d, "PRESENT", "Sir Fida Hussain"))
            records.add(DailyAttendance("att-d-5-1-$d", "BHSS-5TH-001", "Muhammad Abbas", "5th", d, "PRESENT", "Sir Zakir Hussain"))
        }
        return records
    }

    fun getInitialTimetable(): List<TimetablePeriod> = listOf(
        // Class 12th Schedule (Monday)
        TimetablePeriod(
            id = "tt-12-m1",
            classLevel = "12th",
            dayOfWeek = "Monday",
            periodNumber = 1,
            timeRange = "08:30 AM - 09:15 AM",
            subjectName = "Computer Science (Theory)",
            teacherName = "Sir Ghulam Nabi",
            roomName = "CS Lab 1"
        ),
        TimetablePeriod(
            id = "tt-12-m2",
            classLevel = "12th",
            dayOfWeek = "Monday",
            periodNumber = 2,
            timeRange = "09:15 AM - 10:00 AM",
            subjectName = "Mathematics (Calculus)",
            teacherName = "Sir Hassan Raza",
            roomName = "Room 102"
        ),
        TimetablePeriod(
            id = "tt-12-m3",
            classLevel = "12th",
            dayOfWeek = "Monday",
            periodNumber = 3,
            timeRange = "10:00 AM - 10:45 AM",
            subjectName = "Physics Practical",
            teacherName = "Sir Muhammad Ali",
            roomName = "Physics Lab"
        ),
        TimetablePeriod(
            id = "tt-12-m4",
            classLevel = "12th",
            dayOfWeek = "Monday",
            periodNumber = 4,
            timeRange = "10:45 AM - 11:30 AM",
            subjectName = "English Compulsory",
            teacherName = "Sir Sajjad Hussain",
            roomName = "Room 104"
        ),
        TimetablePeriod(
            id = "tt-12-m5",
            classLevel = "12th",
            dayOfWeek = "Monday",
            periodNumber = 5,
            timeRange = "11:50 AM - 12:35 PM",
            subjectName = "Urdu & Pak Studies",
            teacherName = "Sir Kazim Ali",
            roomName = "Main Hall"
        ),
        TimetablePeriod(
            id = "tt-12-m6",
            classLevel = "12th",
            dayOfWeek = "Monday",
            periodNumber = 6,
            timeRange = "12:35 PM - 01:20 PM",
            subjectName = "C++ OOP Lab Practical",
            teacherName = "Sir Ghulam Nabi",
            roomName = "CS Lab 1"
        ),

        // Class 10th Schedule (Monday)
        TimetablePeriod(
            id = "tt-10-m1",
            classLevel = "10th",
            dayOfWeek = "Monday",
            periodNumber = 1,
            timeRange = "08:30 AM - 09:15 AM",
            subjectName = "Mathematics (Matrices)",
            teacherName = "Sir Hassan Raza",
            roomName = "Room 201"
        ),
        TimetablePeriod(
            id = "tt-10-m2",
            classLevel = "10th",
            dayOfWeek = "Monday",
            periodNumber = 2,
            timeRange = "09:15 AM - 10:00 AM",
            subjectName = "Physics (SHM & Waves)",
            teacherName = "Sir Muhammad Ali",
            roomName = "Room 201"
        ),
        TimetablePeriod(
            id = "tt-10-m3",
            classLevel = "10th",
            dayOfWeek = "Monday",
            periodNumber = 3,
            timeRange = "10:00 AM - 10:45 AM",
            subjectName = "Chemistry (Hydrocarbons)",
            teacherName = "Sir Wazir Kazim",
            roomName = "Chemistry Lab"
        ),
        TimetablePeriod(
            id = "tt-10-m4",
            classLevel = "10th",
            dayOfWeek = "Monday",
            periodNumber = 4,
            timeRange = "10:45 AM - 11:30 AM",
            subjectName = "English Compulsory",
            teacherName = "Sir Sajjad Hussain",
            roomName = "Room 201"
        )
    )

    fun getInitialGallery(): List<GalleryPhoto> = listOf(
        GalleryPhoto(
            id = "gal-001",
            title = "Tolti School Campus & Valley Grounds",
            description = "Main academic building of Boys Higher Secondary School Tolti surrounded by the majestic Karakoram peaks.",
            category = "School Building",
            drawableResName = "reel_fields",
            isPublished = true
        ),
        GalleryPhoto(
            id = "gal-002",
            title = "Annual Inter-School Cricket Championship",
            description = "College Under-19 cricket team in action during the autumn Kharmang sports cup.",
            category = "Sports Activities",
            drawableResName = "reel_cricket",
            isPublished = true
        ),
        GalleryPhoto(
            id = "gal-003",
            title = "Faculty & Student Assembly",
            description = "Morning assembly and educational discourse at the central campus ground.",
            category = "School Events",
            drawableResName = "reel_chai",
            isPublished = true
        ),
        GalleryPhoto(
            id = "gal-004",
            title = "Computer Science Laboratory & Digital Workstations",
            description = "ICS and Matric students coding C++ algorithms and mastering database architectures.",
            category = "Classrooms",
            drawableResName = "school_logo",
            isPublished = true
        ),
        GalleryPhoto(
            id = "gal-005",
            title = "District Kharmang Science Exhibition Honors",
            description = "Tolti students awarded top prizes for hydel energy models and Karakoram glaciology charts.",
            category = "Achievements",
            drawableResName = "student_avatar",
            isPublished = true
        )
    )

    fun getInitialSettings(): SchoolSettingsRecord = SchoolSettingsRecord(
        id = 1,
        schoolName = "BOYS HIGHER SECONDARY SCHOOL TOLTI",
        tagline = "LEARN • GROW • SUCCEED",
        address = "Main Bazar, Tolti, District Kharmang, Gilgit-Baltistan",
        phone = "+92 5816 920101",
        email = "info.bhsstolti@edu.pk",
        principalName = "Sir Ghulam Nabi (M.Sc, M.Ed)",
        showStampPlaceholder = true,
        showSignaturePlaceholder = true,
        aboutHistory = "Established as the premier secondary and higher secondary institution in the historic Kharmang valley, Boys Higher Secondary School Tolti delivers rigorous science and humanities education from Class 5th through Class 12th (Matric & Intermediate ICS / Pre-Engineering). The school prides itself on discipline, academic merit, character building, and Karakoram student talent development.",
        language = "EN"
    )

    fun getInitialAdminUser(): AdminUser {
        val salt = "tolti_salt_9281a"
        val passwordHash = SecurityUtils.hashPassword("Tolti@2026!", salt)
        val answerHash = SecurityUtils.hashPassword("kharmang", salt)

        return AdminUser(
            id = "admin-root-001",
            username = "admin",
            passwordHash = passwordHash,
            salt = salt,
            fullName = "Principal Office / Head Administrator",
            role = "SUPER_ADMIN",
            securityQuestion = "What valley is the school located in?",
            securityAnswerHash = answerHash
        )
    }

    fun getInitialActivityLogs(): List<AdminActivityLog> {
        val now = System.currentTimeMillis()
        return listOf(
            AdminActivityLog(
                id = "log-001",
                timestamp = now - (3600 * 1000 * 5),
                adminUsername = "admin",
                action = "SYSTEM_INITIALIZED",
                details = "Boys Higher Secondary School Tolti management portal bootstrapped successfully."
            ),
            AdminActivityLog(
                id = "log-002",
                timestamp = now - (3600 * 1000 * 2),
                adminUsername = "admin",
                action = "RESULTS_PUBLISHED",
                details = "Mid Term Exam 2026 official marksheet records verified and published."
            )
        )
    }

    fun getInitialStudentSubjectResults(): List<StudentResults> = listOf(
        // Class 8th (Middle Section)
        StudentResults(
            id = "res-sub-8-001",
            studentName = "Basharat Ali",
            rollNumber = "BHSS-8TH-001",
            classLevel = "8th",
            section = "A",
            examTerm = "Annual Examination 2026",
            academicYear = "2025-2026",
            englishSubjectName = "English Compulsory",
            englishObtained = 82,
            englishTotal = 100,
            urduSubjectName = "Urdu Compulsory",
            urduObtained = 85,
            urduTotal = 100,
            mathSubjectName = "Mathematics",
            mathObtained = 90,
            mathTotal = 100,
            scienceSubjectName = "General Science",
            scienceObtained = 88,
            scienceTotal = 100,
            chemistryOrSocialSubjectName = "Social Studies",
            chemistryOrSocialObtained = 78,
            chemistryOrSocialTotal = 100,
            electiveSubjectName = "Computer Education",
            electiveObtained = 85,
            electiveTotal = 100,
            islamicStudiesSubjectName = "Islamiyat",
            islamicStudiesObtained = 44,
            islamicStudiesTotal = 50,
            remarks = "Excellent overall score in Middle Section Board Exam.",
            isPublished = true
        ),
        StudentResults(
            id = "res-sub-8-002",
            studentName = "Murtaza Khan",
            rollNumber = "BHSS-8TH-002",
            classLevel = "8th",
            section = "A",
            examTerm = "Annual Examination 2026",
            academicYear = "2025-2026",
            englishSubjectName = "English Compulsory",
            englishObtained = 70,
            englishTotal = 100,
            urduSubjectName = "Urdu Compulsory",
            urduObtained = 75,
            urduTotal = 100,
            mathSubjectName = "Mathematics",
            mathObtained = 68,
            mathTotal = 100,
            scienceSubjectName = "General Science",
            scienceObtained = 74,
            scienceTotal = 100,
            chemistryOrSocialSubjectName = "Social Studies",
            chemistryOrSocialObtained = 71,
            chemistryOrSocialTotal = 100,
            electiveSubjectName = "Computer Education",
            electiveObtained = 76,
            electiveTotal = 100,
            islamicStudiesSubjectName = "Islamiyat",
            islamicStudiesObtained = 38,
            islamicStudiesTotal = 50,
            remarks = "Good performance; advised to practice Mathematics algebra questions.",
            isPublished = true
        ),

        // Class 9th (Matric Part 1)
        StudentResults(
            id = "res-sub-9-018",
            studentName = "Haidar Ali",
            rollNumber = "BHSS-9TH-018",
            classLevel = "9th",
            section = "A",
            examTerm = "FBISE SSC-I Board Exam",
            academicYear = "2025-2026",
            englishSubjectName = "English Compulsory",
            englishObtained = 85,
            englishTotal = 100,
            urduSubjectName = "Urdu Compulsory",
            urduObtained = 82,
            urduTotal = 100,
            mathSubjectName = "Mathematics Science",
            mathObtained = 94,
            mathTotal = 100,
            scienceSubjectName = "Physics",
            scienceObtained = 89,
            scienceTotal = 100,
            chemistryOrSocialSubjectName = "Chemistry",
            chemistryOrSocialObtained = 86,
            chemistryOrSocialTotal = 100,
            electiveSubjectName = "Computer Science",
            electiveObtained = 92,
            electiveTotal = 100,
            islamicStudiesSubjectName = "Islamiyat Compulsory",
            islamicStudiesObtained = 45,
            islamicStudiesTotal = 50,
            remarks = "Distinction rank in Class 9th Science wing.",
            isPublished = true
        ),
        StudentResults(
            id = "res-sub-9-019",
            studentName = "Qasim Ali",
            rollNumber = "BHSS-9TH-019",
            classLevel = "9th",
            section = "A",
            examTerm = "FBISE SSC-I Board Exam",
            academicYear = "2025-2026",
            englishSubjectName = "English Compulsory",
            englishObtained = 74,
            englishTotal = 100,
            urduSubjectName = "Urdu Compulsory",
            urduObtained = 78,
            urduTotal = 100,
            mathSubjectName = "Mathematics Science",
            mathObtained = 80,
            mathTotal = 100,
            scienceSubjectName = "Physics",
            scienceObtained = 76,
            scienceTotal = 100,
            chemistryOrSocialSubjectName = "Chemistry",
            chemistryOrSocialObtained = 72,
            chemistryOrSocialTotal = 100,
            electiveSubjectName = "Biology",
            electiveObtained = 79,
            electiveTotal = 100,
            islamicStudiesSubjectName = "Islamiyat Compulsory",
            islamicStudiesObtained = 41,
            islamicStudiesTotal = 50,
            remarks = "Consistent performance in Pre-Medical foundation.",
            isPublished = true
        ),

        // Class 10th (Matric Part 2)
        StudentResults(
            id = "res-sub-10-012",
            studentName = "Ali Raza",
            rollNumber = "BHSS-10TH-012",
            classLevel = "10th",
            section = "A",
            examTerm = "FBISE SSC-II Annual Exam",
            academicYear = "2025-2026",
            englishSubjectName = "English Compulsory",
            englishObtained = 88,
            englishTotal = 100,
            urduSubjectName = "Urdu Compulsory",
            urduObtained = 84,
            urduTotal = 100,
            mathSubjectName = "Mathematics",
            mathObtained = 96,
            mathTotal = 100,
            scienceSubjectName = "Physics",
            scienceObtained = 92,
            scienceTotal = 100,
            chemistryOrSocialSubjectName = "Chemistry",
            chemistryOrSocialObtained = 90,
            chemistryOrSocialTotal = 100,
            electiveSubjectName = "Computer Science",
            electiveObtained = 95,
            electiveTotal = 100,
            islamicStudiesSubjectName = "Pakistan Studies",
            islamicStudiesObtained = 47,
            islamicStudiesTotal = 50,
            remarks = "First Position in SSC-II Annual Examinations with 91%.",
            isPublished = true
        ),
        StudentResults(
            id = "res-sub-10-013",
            studentName = "Sajjad Hussain",
            rollNumber = "BHSS-10TH-013",
            classLevel = "10th",
            section = "A",
            examTerm = "FBISE SSC-II Annual Exam",
            academicYear = "2025-2026",
            englishSubjectName = "English Compulsory",
            englishObtained = 72,
            englishTotal = 100,
            urduSubjectName = "Urdu Compulsory",
            urduObtained = 75,
            urduTotal = 100,
            mathSubjectName = "Mathematics",
            mathObtained = 70,
            mathTotal = 100,
            scienceSubjectName = "Physics",
            scienceObtained = 74,
            scienceTotal = 100,
            chemistryOrSocialSubjectName = "Chemistry",
            chemistryOrSocialObtained = 68,
            chemistryOrSocialTotal = 100,
            electiveSubjectName = "Biology",
            electiveObtained = 73,
            electiveTotal = 100,
            islamicStudiesSubjectName = "Pakistan Studies",
            islamicStudiesObtained = 39,
            islamicStudiesTotal = 50,
            remarks = "Passed SSC-II Board exam successfully.",
            isPublished = true
        ),

        // Class 11th (HSSC Part 1 - Intermediate)
        StudentResults(
            id = "res-sub-11-001",
            studentName = "Zahid Hussain",
            rollNumber = "BHSS-11TH-001",
            classLevel = "11th",
            section = "A",
            examTerm = "FBISE HSSC-I Board Exam",
            academicYear = "2025-2026",
            englishSubjectName = "English Compulsory",
            englishObtained = 86,
            englishTotal = 100,
            urduSubjectName = "Urdu Compulsory",
            urduObtained = 80,
            urduTotal = 100,
            mathSubjectName = "Mathematics",
            mathObtained = 92,
            mathTotal = 100,
            scienceSubjectName = "Physics",
            scienceObtained = 88,
            scienceTotal = 100,
            chemistryOrSocialSubjectName = "Statistics / Chemistry",
            chemistryOrSocialObtained = 85,
            chemistryOrSocialTotal = 100,
            electiveSubjectName = "Computer Science",
            electiveObtained = 90,
            electiveTotal = 100,
            islamicStudiesSubjectName = "Islamic Education",
            islamicStudiesObtained = 44,
            islamicStudiesTotal = 50,
            remarks = "High Distinction in Intermediate Part-I ICS Wing.",
            isPublished = true
        ),
        StudentResults(
            id = "res-sub-11-002",
            studentName = "Syed Mehdi",
            rollNumber = "BHSS-11TH-002",
            classLevel = "11th",
            section = "A",
            examTerm = "FBISE HSSC-I Board Exam",
            academicYear = "2025-2026",
            englishSubjectName = "English Compulsory",
            englishObtained = 76,
            englishTotal = 100,
            urduSubjectName = "Urdu Compulsory",
            urduObtained = 79,
            urduTotal = 100,
            mathSubjectName = "Mathematics",
            mathObtained = 82,
            mathTotal = 100,
            scienceSubjectName = "Physics",
            scienceObtained = 78,
            scienceTotal = 100,
            chemistryOrSocialSubjectName = "Chemistry",
            chemistryOrSocialObtained = 75,
            chemistryOrSocialTotal = 100,
            electiveSubjectName = "Computer Science",
            electiveObtained = 80,
            electiveTotal = 100,
            islamicStudiesSubjectName = "Islamic Education",
            islamicStudiesObtained = 40,
            islamicStudiesTotal = 50,
            remarks = "Very good academic performance in First Year Pre-Engineering.",
            isPublished = true
        ),

        // Class 12th (HSSC Part 2 - Final Board)
        StudentResults(
            id = "res-sub-12-104",
            studentName = "Muhammad Khan",
            rollNumber = "BHSS-2026-104",
            classLevel = "12th",
            section = "A",
            examTerm = "Annual Board Examination 2026",
            academicYear = "2025-2026",
            englishSubjectName = "English Compulsory",
            englishObtained = 92,
            englishTotal = 100,
            urduSubjectName = "Urdu Compulsory",
            urduObtained = 88,
            urduTotal = 100,
            mathSubjectName = "Mathematics",
            mathObtained = 98,
            mathTotal = 100,
            scienceSubjectName = "Physics",
            scienceObtained = 95,
            scienceTotal = 100,
            chemistryOrSocialSubjectName = "Statistics",
            chemistryOrSocialObtained = 91,
            chemistryOrSocialTotal = 100,
            electiveSubjectName = "Computer Science",
            electiveObtained = 97,
            electiveTotal = 100,
            islamicStudiesSubjectName = "Pakistan Studies",
            islamicStudiesObtained = 48,
            islamicStudiesTotal = 50,
            remarks = "College Topper & Distinction award recipient with 93.7%.",
            isPublished = true
        ),
        StudentResults(
            id = "res-sub-12-105",
            studentName = "Ali Jawad",
            rollNumber = "BHSS-2026-105",
            classLevel = "12th",
            section = "A",
            examTerm = "Annual Board Examination 2026",
            academicYear = "2025-2026",
            englishSubjectName = "English Compulsory",
            englishObtained = 84,
            englishTotal = 100,
            urduSubjectName = "Urdu Compulsory",
            urduObtained = 81,
            urduTotal = 100,
            mathSubjectName = "Mathematics",
            mathObtained = 89,
            mathTotal = 100,
            scienceSubjectName = "Physics",
            scienceObtained = 86,
            scienceTotal = 100,
            chemistryOrSocialSubjectName = "Chemistry",
            chemistryOrSocialObtained = 83,
            chemistryOrSocialTotal = 100,
            electiveSubjectName = "Computer Science",
            electiveObtained = 91,
            electiveTotal = 100,
            islamicStudiesSubjectName = "Pakistan Studies",
            islamicStudiesObtained = 43,
            islamicStudiesTotal = 50,
            remarks = "A+ Grade. Outstanding performance in Science Practicals.",
            isPublished = true
        ),
        StudentResults(
            id = "res-sub-12-106",
            studentName = "Khadim Hussain",
            rollNumber = "BHSS-2026-106",
            classLevel = "12th",
            section = "A",
            examTerm = "Annual Board Examination 2026",
            academicYear = "2025-2026",
            englishSubjectName = "English Compulsory",
            englishObtained = 80,
            englishTotal = 100,
            urduSubjectName = "Urdu Compulsory",
            urduObtained = 85,
            urduTotal = 100,
            mathSubjectName = "Mathematics",
            mathObtained = 78,
            mathTotal = 100,
            scienceSubjectName = "Physics",
            scienceObtained = 82,
            scienceTotal = 100,
            chemistryOrSocialSubjectName = "Chemistry",
            chemistryOrSocialObtained = 79,
            chemistryOrSocialTotal = 100,
            electiveSubjectName = "Biology",
            electiveObtained = 84,
            electiveTotal = 100,
            islamicStudiesSubjectName = "Pakistan Studies",
            islamicStudiesObtained = 42,
            islamicStudiesTotal = 50,
            remarks = "A Grade recipient. Passed Pre-Medical FSc Board with distinction.",
            isPublished = true
        )
    )

    fun getInitialTeacherRatings(): List<TeacherRating> = listOf(
        TeacherRating(
            id = "rate-001",
            teacherId = "tch-001",
            teacherName = "Sir Ghulam Nabi",
            studentName = "Muhammad Khan",
            studentRollNumber = "BHSS-2026-104",
            rating = 5,
            feedback = "Inspiring principal and CS teacher. Made pointers and OOP concepts crystal clear.",
            subjectName = "Computer Science"
        ),
        TeacherRating(
            id = "rate-002",
            teacherId = "tch-001",
            teacherName = "Sir Ghulam Nabi",
            studentName = "Ali Raza",
            studentRollNumber = "BHSS-2026-105",
            rating = 5,
            feedback = "Very supportive and always encourages practical programming in the computer lab.",
            subjectName = "Computer Science"
        ),
        TeacherRating(
            id = "rate-003",
            teacherId = "tch-002",
            teacherName = "Sir Muhammad Ali",
            studentName = "Muhammad Khan",
            studentRollNumber = "BHSS-2026-104",
            rating = 5,
            feedback = "Hands-on physics experiments and electromagnetism lectures are best in the valley.",
            subjectName = "Physics"
        ),
        TeacherRating(
            id = "rate-004",
            teacherId = "tch-003",
            teacherName = "Sir Hassan Raza",
            studentName = "Khadim Hussain",
            studentRollNumber = "BHSS-2026-106",
            rating = 5,
            feedback = "Excellent mastery over calculus and analytical geometry formulas.",
            subjectName = "Mathematics"
        )
    )

    fun getInitialClassSubjects(): List<ClassSubjectRecord> = listOf(
        ClassSubjectRecord(id = "sub-12-cs", classLevel = "12th", subjectName = "Computer Science", subjectCode = "CS-12", teacherName = "Sir Ghulam Nabi", maxMarks = 100, passMarks = 40, isCompulsory = false),
        ClassSubjectRecord(id = "sub-12-phy", classLevel = "12th", subjectName = "Physics", subjectCode = "PHY-12", teacherName = "Sir Muhammad Ali", maxMarks = 100, passMarks = 40, isCompulsory = true),
        ClassSubjectRecord(id = "sub-12-math", classLevel = "12th", subjectName = "Mathematics", subjectCode = "MATH-12", teacherName = "Sir Hassan Raza", maxMarks = 100, passMarks = 40, isCompulsory = true),
        ClassSubjectRecord(id = "sub-12-eng", classLevel = "12th", subjectName = "English Compulsory", subjectCode = "ENG-12", teacherName = "Sir Manzoor Hussain", maxMarks = 100, passMarks = 40, isCompulsory = true),
        ClassSubjectRecord(id = "sub-11-cs", classLevel = "11th", subjectName = "Computer Science", subjectCode = "CS-11", teacherName = "Sir Ghulam Nabi", maxMarks = 100, passMarks = 40, isCompulsory = false),
        ClassSubjectRecord(id = "sub-10-sci", classLevel = "10th", subjectName = "Physics", subjectCode = "PHY-10", teacherName = "Sir Muhammad Ali", maxMarks = 100, passMarks = 33, isCompulsory = true),
        ClassSubjectRecord(id = "sub-10-math", classLevel = "10th", subjectName = "Mathematics", subjectCode = "MATH-10", teacherName = "Sir Hassan Raza", maxMarks = 100, passMarks = 33, isCompulsory = true),
        ClassSubjectRecord(id = "sub-09-chem", classLevel = "9th", subjectName = "Chemistry", subjectCode = "CHEM-09", teacherName = "Sir Jaffar Hussain", maxMarks = 100, passMarks = 33, isCompulsory = true),
        ClassSubjectRecord(id = "sub-08-gen", classLevel = "8th", subjectName = "General Science", subjectCode = "SCI-08", teacherName = "Sir Zakir Hussain", maxMarks = 100, passMarks = 33, isCompulsory = true),
        ClassSubjectRecord(id = "sub-05-eng", classLevel = "5th", subjectName = "English Primary", subjectCode = "ENG-05", teacherName = "Sir Manzoor Hussain", maxMarks = 100, passMarks = 33, isCompulsory = true)
    )
}


