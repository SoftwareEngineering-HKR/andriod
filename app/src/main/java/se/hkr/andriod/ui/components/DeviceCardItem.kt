package se.hkr.andriod.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import se.hkr.andriod.R
import se.hkr.andriod.domain.model.device.Device
import se.hkr.andriod.domain.model.device.DeviceType
import se.hkr.andriod.ui.theme.GrayCardOverlay
import se.hkr.andriod.ui.theme.cardBackground

@Composable
fun DeviceCardItem(
    modifier: Modifier = Modifier,
    device: Device,
    onClick: (() -> Unit)? = null,
    onSwitchToggle: ((Boolean) -> Unit)? = null,
    elevation: Dp = 0.dp
) {
    val isSensor = device.deviceTypeEnum in listOf(
        DeviceType.GAS,
        DeviceType.STEAM,
        DeviceType.HUMIDITY,
        DeviceType.SENSOR,
        DeviceType.PHOTO,
        DeviceType.BRIGHTNESS,
        DeviceType.MOTION,
        DeviceType.TEMPERATURE,
        DeviceType.TILT
    )

    val isSwitchDevice = device.deviceTypeEnum in listOf(
        DeviceType.LIGHT,
        DeviceType.LOCK,
        DeviceType.BUZZ,
        DeviceType.FAN,
        DeviceType.SERVO,
        DeviceType.DOOR,
        DeviceType.WINDOW,
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .then(
                if (onClick != null && device.online)
                    Modifier.clickable { onClick() }
                else Modifier
            ),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.cardBackground)
    ) {
        Box {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                // ICON
                val icon = when (device.deviceTypeEnum) {
                    DeviceType.LIGHT -> rememberVectorPainter(Icons.Default.Lightbulb)
                    DeviceType.LOCK -> rememberVectorPainter(Icons.Default.Lock)
                    DeviceType.BUZZ -> painterResource(R.drawable.brand_awareness_24px)
                    DeviceType.FAN -> painterResource(R.drawable.mode_fan_24px)
                    DeviceType.SERVO -> painterResource(R.drawable.door_front_24px)
                    DeviceType.DOOR -> painterResource(R.drawable.door_front_24px)
                    DeviceType.WINDOW -> painterResource(R.drawable.window_24px)
                    DeviceType.GAS -> painterResource(R.drawable.detector_co_24px)
                    DeviceType.STEAM -> painterResource(R.drawable.heat_24px)
                    DeviceType.HUMIDITY -> painterResource(R.drawable.humidity_percentage_24px)
                    DeviceType.DISPLAY -> painterResource(R.drawable.assistant_on_hub_24px)
                    DeviceType.PHOTO -> painterResource(R.drawable.light_mode_24px)
                    DeviceType.BRIGHTNESS -> painterResource(R.drawable.light_mode_24px)
                    DeviceType.MOTION -> painterResource(R.drawable.detector_24px)
                    DeviceType.TEMPERATURE -> painterResource(R.drawable.thermometer_24px)
                    DeviceType.TILT -> painterResource(R.drawable.diagonal_line_24px)
                    else -> rememberVectorPainter(Icons.Default.QuestionMark)
                }

                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // Display name
                    Text(
                        text = device.displayName,
                        style = MaterialTheme.typography.titleMedium
                    )

                    // Room + online status
                    Row {
                        device.displayRoom?.let { roomText ->
                            Text(
                                text = roomText,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(" - ", style = MaterialTheme.typography.bodyMedium)
                        }

                        // Always show online/offline status
                        Text(
                            text = stringResource(
                                if (device.online) R.string.device_online else R.string.device_offline
                            ),
                            color = if (device.online) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                if (isSwitchDevice && onSwitchToggle != null) {
                    val checked = device.value > device.minValue
                    Switch(
                        checked = checked,
                        onCheckedChange = { if (device.online) onSwitchToggle(it) },
                        enabled = device.online
                    )
                } else if (isSensor) {
                    val valueText = when (device.deviceTypeEnum) {
                        DeviceType.HUMIDITY,
                        DeviceType.BRIGHTNESS,
                        DeviceType.PHOTO -> "${device.value}%"

                        DeviceType.TEMPERATURE -> "${device.value}°C"

                        else -> device.value.toString()
                    }

                    Box(
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Text(
                            text = valueText,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Gray overlay when offline
            if (!device.online) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(MaterialTheme.colorScheme.GrayCardOverlay)
                )
            }
        }
    }
}
