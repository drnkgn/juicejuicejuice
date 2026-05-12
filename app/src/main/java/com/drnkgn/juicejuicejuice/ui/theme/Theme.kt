package com.drnkgn.juicejuicejuice.ui.theme

import android.annotation.SuppressLint
import android.hardware.lights.Light
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class ExtColors(
    val success: Color = SuccessA20,
    val onSuccess: Color = DarkA0,
    val successIcon: Color = SuccessA0,

    val disabled: Color = SurfaceA50,
    val onDisabled: Color = DarkA0,

    val inputBgError: Color = DangerA0,
    val onInputBgError: Color = DarkA0,

    val placeholder: Color = SurfaceA50
)

@SuppressLint("CompositionLocalNaming")
val ExtendedColors = staticCompositionLocalOf { ExtColors() }

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryA20,
    onPrimary = DarkA0,

    secondary = SurfaceA40,
    onSecondary = LightA0,
    secondaryContainer = SurfaceA20,
    onSecondaryContainer = LightA0,

    tertiary = SurfaceA10,
    onTertiary = LightA0,

    background = SurfaceA0,
    onBackground = LightA0,

    primaryContainer = PrimaryA20,
    onPrimaryContainer = DarkA0,

    error = DangerA20,
    onError = DarkA0,

    outline = SurfaceA30,
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryA20,
    onPrimary = DarkA0,

    secondary = SurfaceA40,
    onSecondary = LightA0,

    tertiary = DarkA0,

    background = SurfaceA0,
    onBackground = LightA0

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun JuiceJuiceJuiceTheme(
    // darkTheme: Boolean = isSystemInDarkTheme(),
    darkTheme: Boolean = true,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val extColors = ExtColors()

    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(ExtendedColors provides extColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content,
        )
    }
}

val MaterialTheme.extColors: ExtColors
    @Composable get() = ExtendedColors.current