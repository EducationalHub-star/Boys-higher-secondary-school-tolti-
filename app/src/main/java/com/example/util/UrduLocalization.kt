package com.example.util

/**
 * Provides comprehensive Urdu & English bilingual strings and translations
 * for Boys Higher Secondary School Tolti.
 */
object UrduLocalization {

    enum class Language(val code: String, val label: String) {
        ENGLISH("EN", "English"),
        URDU("UR", "اردو")
    }

    fun getSchoolName(lang: Language): String = when (lang) {
        Language.ENGLISH -> "BOYS HIGHER SECONDARY SCHOOL TOLTI"
        Language.URDU -> "گورنمنٹ بوائز ہائر سیکنڈری اسکول تولتی"
    }

    fun getTagline(lang: Language): String = when (lang) {
        Language.ENGLISH -> "LEARN • GROW • SUCCEED"
        Language.URDU -> "علم حاصل کرو • آگے بڑھو • کامیاب بنو"
    }

    fun getSubTitle(lang: Language): String = when (lang) {
        Language.ENGLISH -> "District Kharmang, Gilgit-Baltistan • Established for Excellence"
        Language.URDU -> "ضلع کھرمنگ، گلگت بلتستان • معیاری تعلیم و تربیت کا ادارہ"
    }

    fun navHome(lang: Language): String = when (lang) {
        Language.ENGLISH -> "Home"
        Language.URDU -> "ہوم پیج"
    }

    fun navResults(lang: Language): String = when (lang) {
        Language.ENGLISH -> "Results"
        Language.URDU -> "نتائج"
    }

    fun navAnnouncements(lang: Language): String = when (lang) {
        Language.ENGLISH -> "Notices"
        Language.URDU -> "اعلانات"
    }

    fun navTeachers(lang: Language): String = when (lang) {
        Language.ENGLISH -> "Teachers"
        Language.URDU -> "اساتذہ"
    }

    fun navTimetable(lang: Language): String = when (lang) {
        Language.ENGLISH -> "Timetable"
        Language.URDU -> "شیڈول"
    }

    fun navHomework(lang: Language): String = when (lang) {
        Language.ENGLISH -> "Homework"
        Language.URDU -> "ہوم ورک"
    }

    fun navGallery(lang: Language): String = when (lang) {
        Language.ENGLISH -> "Gallery"
        Language.URDU -> "گیلری"
    }

    fun navAbout(lang: Language): String = when (lang) {
        Language.ENGLISH -> "About Us"
        Language.URDU -> "ہمارے بارے میں"
    }

    fun navContact(lang: Language): String = when (lang) {
        Language.ENGLISH -> "Contact"
        Language.URDU -> "رابطہ"
    }

    fun navAdmin(lang: Language): String = when (lang) {
        Language.ENGLISH -> "Admin Portal"
        Language.URDU -> "ایڈمن پینل"
    }

    fun searchPlaceholder(lang: Language): String = when (lang) {
        Language.ENGLISH -> "Search students, results, teachers or notices..."
        Language.URDU -> "طلباء، نتائج، اساتذہ یا نوٹس تلاش کریں..."
    }

    fun welcomeHeader(lang: Language): String = when (lang) {
        Language.ENGLISH -> "Welcome to BHSS Tolti"
        Language.URDU -> "بوائز ہائر سیکنڈری اسکول تولتی میں خوش آمدید"
    }

    fun welcomeDescription(lang: Language): String = when (lang) {
        Language.ENGLISH -> "Fostering academic excellence, moral leadership, digital skills, and character in the heart of Kharmang valley from 5th to 12th class."
        Language.URDU -> "کھرمنگ کے دل میں پانچویں سے بارہویں جماعت تک اعلیٰ معیار تعلیم، کردار سازی اور جدید سائنسی و ڈیجیٹل علوم کی ترویج۔"
    }
}
