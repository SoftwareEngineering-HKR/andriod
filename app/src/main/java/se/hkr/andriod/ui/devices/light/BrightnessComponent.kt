package se.hkr.andriod.ui.devices.light

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.hkr.andriod.R
import se.hkr.andriod.ui.components.AppButton
import se.hkr.andriod.ui.theme.AndriodTheme
import se.hkr.andriod.ui.theme.cardBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrightnessComponent(
    value: Float,
    onValueChange: (Float) -> Unit
) {

    // Brightness Card
    Card(
        modifier = Modifier.fillMaxWidth(0.9f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.cardBackground
        )
    ) {

        Column(modifier = Modifier.padding(20.dp)) {

            // Header: Icon Title Percentage
            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    imageVector = Icons.Rounded.LightMode,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = stringResource(R.string.brightness),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                // Display current brightness percentage
                Text(
                    text = "${(value * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Slider for brightness
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
                                listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                    MaterialTheme.colorScheme.primary
                                )
                            ),
                            shape = RoundedCornerShape(50)
                        )
                )

                Slider(
                    value = value,
                    onValueChange = onValueChange,
                    valueRange = 0f..1f,
                    thumb = {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .background(Color.White, CircleShape)
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
    }

    Spacer(modifier = Modifier.height(16.dp))
}

@Preview(name = "Brightness Component", showBackground = true)
@Composable
private fun BrightnessComponentPreview() {
    AndriodTheme {
        BrightnessComponent(
            value = 0.5f,
            onValueChange = {}
        )
    }
}