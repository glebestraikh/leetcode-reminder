package com.glebestraikh.leetcodereminder.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF85E048),
    onPrimary = Color(0xFF142900),
    primaryContainer = Color(0xFF25481A),
    onPrimaryContainer = Color(0xFFD5F9BC),
    secondary = Color(0xFF66D4F3),
    onSecondary = Color(0xFF003543),
    secondaryContainer = Color(0xFF164351),
    onSecondaryContainer = Color(0xFFC4F1FC),
    tertiary = Color(0xFFFFD064),
    onTertiary = Color(0xFF382800),
    tertiaryContainer = Color(0xFF51401A),
    onTertiaryContainer = Color(0xFFFFE6A7),
    error = Color(0xFFFF9894),
    onError = Color(0xFF4C1316),
    errorContainer = Color(0xFF51252A),
    onErrorContainer = Color(0xFFFFDAD7),
    background = Color(0xFF101E24),
    onBackground = Color(0xFFEDF5F6),
    surface = Color(0xFF101E24),
    onSurface = Color(0xFFEDF5F6),
    surfaceVariant = Color(0xFF293D46),
    onSurfaceVariant = Color(0xFFB4C7CE),
    surfaceContainerLowest = Color(0xFF0B171C),
    surfaceContainerLow = Color(0xFF15262D),
    surfaceContainer = Color(0xFF1B2E36),
    surfaceContainerHigh = Color(0xFF223740),
    surfaceContainerHighest = Color(0xFF293D46),
    surfaceDim = Color(0xFF101E24),
    surfaceBright = Color(0xFF354A52),
    outline = Color(0xFF78919A),
    outlineVariant = Color(0xFF405861),
    inverseSurface = Color(0xFFDCE8EB),
    inverseOnSurface = Color(0xFF1B2E36),
    inversePrimary = Color(0xFF386B10),
)

@Composable
fun LeetCodeReminderTheme(
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        dynamicDarkColorScheme(LocalContext.current)
    } else {
        DarkColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
        )
}
