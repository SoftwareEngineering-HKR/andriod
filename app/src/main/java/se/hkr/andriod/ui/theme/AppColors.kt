package se.hkr.andriod.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.material3.ColorScheme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color

// Blue
val ColorScheme.darkBlue: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) DarkBlueDark else DarkBlueLight

val ColorScheme.disabledButtonBlue: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) DisabledButtonBlueDark else DisabledButtonBlueLight

val ColorScheme.middleBlue: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) MiddleBlueDark else MiddleBlueLight

val ColorScheme.lightBlue: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) LightBlueDark else LightBlueLight


// Green
val ColorScheme.darkGreen: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) DarkGreenDark else DarkGreenLight

val ColorScheme.middleGreen: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) MiddleGreenDark else MiddleGreenLight

val ColorScheme.lightGreen: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) LightGreenDark else LightGreenLight


// Red
val ColorScheme.darkRed: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) DarkRedDark else DarkRedLight

val ColorScheme.middleRed: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) MiddleRedDark else MiddleRedLight

val ColorScheme.lightRed: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) LightRedDark else LightRedLight


// Input fields
val ColorScheme.inputFieldFill: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) InputFieldFillDark else InputFieldFillLight

val ColorScheme.inputFieldOutline: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) InputFieldOutlineDark else InputFieldOutlineLight

val ColorScheme.inputFieldPlaceholder: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) InputFieldPlaceholderDark else InputFieldPlaceholderLight


// Card
val ColorScheme.cardBackground: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) CardBackgroundDark else CardBackgroundLight
