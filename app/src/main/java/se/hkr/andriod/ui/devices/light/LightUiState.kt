package se.hkr.andriod.ui.devices.light

import androidx.compose.ui.graphics.Color

data class LightUiState(
    val brightness: Float = 0.5f,
    val color: Color = Color.Blue
)