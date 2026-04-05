package se.hkr.andriod.ui.devices.light

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.hkr.andriod.R
import se.hkr.andriod.data.mock.MockDevices
import se.hkr.andriod.domain.model.device.Device
import se.hkr.andriod.ui.theme.AndriodTheme
import se.hkr.andriod.ui.theme.GrayCardOverlay
import se.hkr.andriod.ui.theme.cardBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorPickerComponent(
    hue: Float,
    onColorSelected: (Float) -> Unit,
    device: Device
) {
    val sliderValue = hue / 360f

    val presets = listOf(
        40f to R.string.preset_warm,
        55f to R.string.preset_neutral,
        200f to R.string.preset_cool
    )

    Card(
        modifier = Modifier.fillMaxWidth(0.9f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.cardBackground
        )
    ) {
        Box {
            Column(modifier = Modifier.padding(20.dp)) {
                // Header
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(
                        imageVector = Icons.Rounded.Palette,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = stringResource(R.string.color),
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Presets
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    presets.forEach { (presetHue, label) ->

                        val isSelected = kotlin.math.abs(hue - presetHue) < 2f

                        Box(
                            modifier = Modifier
                                .height(36.dp)
                                .background(
                                    if (isSelected)
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                    else
                                        MaterialTheme.colorScheme.surface,
                                    RoundedCornerShape(50)
                                )
                                .border(
                                    1.dp,
                                    if (isSelected)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.outline,
                                    RoundedCornerShape(50)
                                )
                                .clickable(enabled = device.online) {
                                    onColorSelected(presetHue)
                                }
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(stringResource(label))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Hue Slider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                            .align(Alignment.Center)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Red,
                                        Color.Yellow,
                                        Color.Green,
                                        Color.Cyan,
                                        Color.Blue,
                                        Color.Magenta,
                                        Color.Red
                                    )
                                ),
                                shape = RoundedCornerShape(50)
                            )
                    )

                    Slider(
                        value = sliderValue,
                        onValueChange = {
                            onColorSelected(it * 360f)
                        },
                        valueRange = 0f..1f,
                        enabled = device.online,
                        thumb = {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .background(Color.White, shape = CircleShape)
                                    .border(2.dp, Color.Black, CircleShape)
                            )
                        },
                        colors = SliderDefaults.colors(
                            activeTrackColor = Color.Transparent,
                            inactiveTrackColor = Color.Transparent,
                            thumbColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                    )
                }
            }

            // Disabled overlay
            if (!device.online) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.GrayCardOverlay)
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}

@Preview(name = "Color Picker Component", showBackground = true)
@Composable
private fun ColorPickerComponentPreview() {
    AndriodTheme {
        ColorPickerComponent(
            45f,
            onColorSelected = {},
            device = MockDevices.light1
        )
    }
}
