package se.hkr.andriod.ui.devices.light

import se.hkr.andriod.ui.screens.devicecard.DeviceComponent

sealed interface LightComponent : DeviceComponent {
    data class Brightness(val value: Float) : LightComponent
    data class ColorPicker(val color: androidx.compose.ui.graphics.Color) : LightComponent
}
