package com.nexus.quiz.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val NexusLightColorScheme = lightColorScheme(
    primary = DeepPine,
    onPrimary = White,
    primaryContainer = AccentBlueSoft,
    onPrimaryContainer = DeepPine,

    secondary = ForestGreen,
    onSecondary = White,
    secondaryContainer = LightGray,
    onSecondaryContainer = TextPrimary,

    tertiary = AntiqueGold,
    onTertiary = White,
    tertiaryContainer = LightGray,
    onTertiaryContainer = DeepPine,

    background = WarmBackground,
    onBackground = TextPrimary,

    surface = White,
    onSurface = TextPrimary,
    surfaceVariant = OffWhite,
    onSurfaceVariant = TextSecondary,

    outline = BorderLight,
    outlineVariant = BorderMedium,

    error = SystemRed,
    onError = White,
    errorContainer = Color(0xFFFBEBE8),
    onErrorContainer = SystemRed,

    inverseSurface = DeepPine,
    inverseOnSurface = White,
    inversePrimary = SageGreen
)

@Composable
fun NexusQuizTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = NexusLightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = WarmBackground.toArgb()
            window.navigationBarColor = WarmBackground.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = true
                isAppearanceLightNavigationBars = true
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = NexusTypography,
        content = content
    )
}

@Composable
fun NexusTheme(content: @Composable () -> Unit) = NexusQuizTheme(content = content)
