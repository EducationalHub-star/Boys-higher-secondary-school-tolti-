package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.ElegantGold
import com.example.ui.theme.LocalAppDesignConfig
import com.example.ui.theme.RoyalBlue

/**
 * Official School Emblem for Govt. Higher Secondary School Tolti, Kharmang.
 * Uses Admin custom logo URI if uploaded, or falls back to the authentic official school_logo.
 */
@Composable
fun SchoolEmblem(
    size: Dp = 56.dp,
    modifier: Modifier = Modifier,
    showBorder: Boolean = true,
    customLogoUri: String? = null
) {
    val designConfig = LocalAppDesignConfig.current
    val resolvedLogoUri = customLogoUri?.takeIf { it.isNotBlank() } ?: designConfig.customLogoUri?.takeIf { it.isNotBlank() }

    Surface(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .then(
                if (showBorder) {
                    Modifier.border(2.dp, ElegantGold, CircleShape)
                } else Modifier
            ),
        shape = CircleShape,
        color = Color.White,
        shadowElevation = if (showBorder) 4.dp else 0.dp
    ) {
        Box(
            modifier = Modifier.padding(if (showBorder) 2.dp else 0.dp),
            contentAlignment = Alignment.Center
        ) {
            if (resolvedLogoUri != null) {
                AsyncImage(
                    model = resolvedLogoUri,
                    contentDescription = "Official Logo of Govt. Higher Secondary School Tolti, Kharmang",
                    placeholder = painterResource(id = R.drawable.school_logo),
                    error = painterResource(id = R.drawable.school_logo),
                    modifier = Modifier
                        .size(size)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.school_logo),
                    contentDescription = "Official Logo of Govt. Higher Secondary School Tolti, Kharmang",
                    modifier = Modifier
                        .size(size)
                        .clip(CircleShape),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

/**
 * Central School Logo Design for the top of the Main Home Screen.
 * High-definition academic centerpiece featuring the open book, rising sun,
 * graduation cap symbols, golden laurels, and school branding.
 */
@Composable
fun CentralSchoolLogo(
    modifier: Modifier = Modifier,
    size: Dp = 76.dp,
    isUrdu: Boolean = false,
    customLogoUri: String? = null
) {
    val designConfig = LocalAppDesignConfig.current
    val resolvedLogoUri = customLogoUri?.takeIf { it.isNotBlank() } ?: designConfig.customLogoUri?.takeIf { it.isNotBlank() }

    Column(
        modifier = modifier
            .testTag("central_school_logo_component"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Glowing Outer Badge
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(size + 10.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFDE68A).copy(alpha = 0.35f),
                            Color(0xFFD97706).copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        ) {
            Surface(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .border(BorderStroke(2.dp, ElegantGold), CircleShape)
                    .shadow(elevation = 4.dp, shape = CircleShape),
                shape = CircleShape,
                color = Color.White
            ) {
                Box(
                    modifier = Modifier.padding(3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (resolvedLogoUri != null) {
                        AsyncImage(
                            model = resolvedLogoUri,
                            contentDescription = "Govt. Higher Secondary School Tolti Crest",
                            placeholder = painterResource(id = R.drawable.school_logo),
                            error = painterResource(id = R.drawable.school_logo),
                            modifier = Modifier
                                .size(size - 6.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.school_logo),
                            contentDescription = "Govt. Higher Secondary School Tolti Crest",
                            modifier = Modifier
                                .size(size - 6.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Established Year Tag
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = ElegantGold.copy(alpha = 0.2f),
            border = BorderStroke(1.dp, ElegantGold.copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = ElegantGold,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = if (isUrdu) "قائم شدہ 1978 • ضلع کھرمنگ" else "ESTD. 1978 • KHARMANG, GB",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFDE68A),
                    letterSpacing = 0.8.sp
                )
            }
        }
    }
}
