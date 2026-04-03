package se.hkr.andriod.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

// Blue
val ColorScheme.darkBlue: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) DarkBlueDark else DarkBlueLight

val ColorScheme.disabledButtonBlue: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) DisabledButtonBlueDark else DisabledButtonBlueLight

val ColorScheme.middleBlue: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) MiddleBlueDark else MiddleBlueLight

val ColorScheme.lightBlue: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) LightBlueDark else LightBlueLight

// Green
val ColorScheme.darkGreen: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) DarkGreenDark else DarkGreenLight

val ColorScheme.middleGreen: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) MiddleGreenDark else MiddleGreenLight

val ColorScheme.lightGreen: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) LightGreenDark else LightGreenLight

// Red
val ColorScheme.darkRed: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) DarkRedDark else DarkRedLight

val ColorScheme.middleRed: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) MiddleRedDark else MiddleRedLight

val ColorScheme.lightRed: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) LightRedDark else LightRedLight

// Input fields
val ColorScheme.inputFieldFill: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) InputFieldFillDark else InputFieldFillLight

val ColorScheme.inputFieldOutline: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) InputFieldOutlineDark else InputFieldOutlineLight

val ColorScheme.inputFieldPlaceholder: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) InputFieldPlaceholderDark else InputFieldPlaceholderLight

// Card
val ColorScheme.cardBackground: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) CardBackgroundDark else CardBackgroundLight

// Add device list item
val ColorScheme.ListItemBackground: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) ListItemBackgroundDark else ListItemBackgroundLight
