package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NotificationPriority
import com.example.data.model.NotificationType
import com.example.data.model.SchoolNotification

@Composable
fun InAppNotificationBanner(
    notification: SchoolNotification?,
    onDismiss: () -> Unit,
    onActionClick: (SchoolNotification) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = notification != null,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
        modifier = modifier
    ) {
        if (notification != null) {
            val (icon, tintColor, gradientStart, gradientEnd, typeLabel) = when (notification.type) {
                NotificationType.UPCOMING_DEADLINE -> {
                    val color = if (notification.priority == NotificationPriority.URGENT) {
                        Color(0xFFDC2626) // Urgent Crimson
                    } else {
                        Color(0xFFD97706) // Amber
                    }
                    Quint(Icons.Default.HourglassTop, color, color.copy(alpha = 0.95f), color, "DEADLINE ALERT")
                }
                NotificationType.GRADE_UPDATE -> {
                    val color = Color(0xFFB45309) // Academic Gold
                    Quint(Icons.Default.Grade, color, color, Color(0xFF78350F), "GRADE UPDATE")
                }
                NotificationType.NEW_ASSIGNMENT -> {
                    val color = Color(0xFF0F5132) // School Emerald
                    Quint(Icons.Default.Assignment, color, color, Color(0xFF064E3B), "NEW ASSIGNMENT")
                }
                NotificationType.COLLEGE_ANNOUNCEMENT -> {
                    val color = Color(0xFF1E3A8A) // College Blue
                    Quint(Icons.Default.Campaign, color, color, Color(0xFF1E40AF), "COLLEGE NOTICE")
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onActionClick(notification) }
                    .testTag("in_app_notification_banner"),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Box {
                    // Top accent bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(height = 4.dp, width = 0.dp)
                            .background(Brush.horizontalGradient(listOf(gradientStart, gradientEnd)))
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Icon circle
                        Surface(
                            modifier = Modifier.size(42.dp),
                            shape = CircleShape,
                            color = tintColor.copy(alpha = 0.12f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = tintColor,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Text contents
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Surface(
                                    color = tintColor,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = typeLabel,
                                        color = Color.White,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Text(
                                    text = "Real-Time Alert",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF6B7280),
                                    fontSize = 10.sp
                                )
                            }

                            Text(
                                text = notification.title,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = Color(0xFF111827),
                                modifier = Modifier.padding(top = 2.dp)
                            )

                            Text(
                                text = notification.message,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                color = Color(0xFF4B5563)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Dismiss button
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("dismiss_notification_banner_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Dismiss",
                                tint = Color(0xFF9CA3AF),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class Quint<A, B, C, D, E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E
)
