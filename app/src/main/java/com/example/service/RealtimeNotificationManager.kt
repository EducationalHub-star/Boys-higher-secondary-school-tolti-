package com.example.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.R
import com.example.data.model.NotificationPriority
import com.example.data.model.NotificationType
import com.example.data.model.SchoolNotification

/**
 * Handles Android system notification channels and dispatches heads-up
 * system tray notifications for academic events, approaching deadlines,
 * grade publications, and college announcements.
 */
object RealtimeNotificationManager {

    const val CHANNEL_ASSIGNMENTS = "channel_school_assignments"
    const val CHANNEL_DEADLINES = "channel_school_deadlines"
    const val CHANNEL_GRADES = "channel_school_grades"
    const val CHANNEL_ANNOUNCEMENTS = "channel_school_announcements"

    /**
     * Initializes Android 8.0+ notification channels with proper importance and vibration patterns.
     */
    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
                    ?: return

            // 1. Deadlines Channel (High Importance with alert & vibration)
            val deadlinesChannel = NotificationChannel(
                CHANNEL_DEADLINES,
                "Academic Deadlines & Urgencies",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Urgent alerts when assignments, practicals, or tests are due soon"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 300, 200, 300)
                setShowBadge(true)
            }

            // 2. Grade Updates Channel (High Importance)
            val gradesChannel = NotificationChannel(
                CHANNEL_GRADES,
                "Exam Results & Grades",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications when faculty or exam controllers publish new test results"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 200, 150, 200)
                setShowBadge(true)
            }

            // 3. New Assignments Channel (High Importance)
            val assignmentsChannel = NotificationChannel(
                CHANNEL_ASSIGNMENTS,
                "New Course Assignments",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alerts when teachers post new class homework or projects"
                enableVibration(true)
                setShowBadge(true)
            }

            // 4. College Announcements Channel (Default Importance)
            val announcementsChannel = NotificationChannel(
                CHANNEL_ANNOUNCEMENTS,
                "College Announcements & Notices",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "General campus circulars, weather advisories, sports events, and exam timetables"
                setShowBadge(true)
            }

            notificationManager.createNotificationChannels(
                listOf(deadlinesChannel, gradesChannel, assignmentsChannel, announcementsChannel)
            )
        }
    }

    /**
     * Posts a system status bar notification for the given academic alert.
     */
    fun dispatchSystemNotification(
        context: Context,
        notification: SchoolNotification,
        enableSoundAndVibrate: Boolean = true
    ) {
        // Check POST_NOTIFICATIONS permission on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!hasPermission) {
                return
            }
        }

        val channelId = when (notification.type) {
            NotificationType.UPCOMING_DEADLINE -> CHANNEL_DEADLINES
            NotificationType.GRADE_UPDATE -> CHANNEL_GRADES
            NotificationType.NEW_ASSIGNMENT -> CHANNEL_ASSIGNMENTS
            NotificationType.COLLEGE_ANNOUNCEMENT -> CHANNEL_ANNOUNCEMENTS
        }

        // Tap action intent to open MainActivity
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("EXTRA_NOTIFICATION_ID", notification.id)
            putExtra("EXTRA_NOTIFICATION_TYPE", notification.type.name)
            putExtra("EXTRA_RELATED_ID", notification.relatedId)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notification.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val priorityCompat = when (notification.priority) {
            NotificationPriority.URGENT -> NotificationCompat.PRIORITY_MAX
            NotificationPriority.HIGH -> NotificationCompat.PRIORITY_HIGH
            NotificationPriority.NORMAL -> NotificationCompat.PRIORITY_DEFAULT
            NotificationPriority.INFO -> NotificationCompat.PRIORITY_LOW
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.school_logo)
            .setContentTitle(notification.title)
            .setContentText(notification.message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(notification.message))
            .setPriority(priorityCompat)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setCategory(
                when (notification.type) {
                    NotificationType.UPCOMING_DEADLINE -> NotificationCompat.CATEGORY_ALARM
                    NotificationType.GRADE_UPDATE -> NotificationCompat.CATEGORY_EVENT
                    else -> NotificationCompat.CATEGORY_MESSAGE
                }
            )

        if (enableSoundAndVibrate) {
            builder.setSound(defaultSoundUri)
            if (notification.priority == NotificationPriority.URGENT) {
                builder.setVibrate(longArrayOf(0, 350, 150, 350))
            } else {
                builder.setVibrate(longArrayOf(0, 200, 100, 200))
            }
        }

        try {
            NotificationManagerCompat.from(context).notify(
                notification.id.hashCode(),
                builder.build()
            )
        } catch (_: SecurityException) {
            // Permission revoked or not granted at runtime
        }
    }
}
