package se.hkr.andriod.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import se.hkr.andriod.R
import se.hkr.andriod.domain.model.device.Device
import se.hkr.andriod.domain.model.device.DeviceType
import se.hkr.andriod.ui.theme.cardBackground

@Composable
fun DeviceCardItem(
    modifier: Modifier = Modifier,
    device: Device,
    onClick: (() -> Unit)? = null,
    onSwitchToggle: ((Boolean) -> Unit)? = null,
    elevation: Dp = 0.dp
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.cardBackground)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            // Icon box
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                val icon = when (device.deviceTypeEnum) {
                    DeviceType.LIGHT -> androidx.compose.material.icons.Icons.Default.Lightbulb
                    DeviceType.LOCK -> androidx.compose.material.icons.Icons.Default.Lock
                    DeviceType.SENSOR -> androidx.compose.material.icons.Icons.Default.Sensors
                }

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = device.displayName,
                    style = MaterialTheme.typography.titleMedium
                )

                Row {
                    Text(
                        text = device.room ?: "Unknown Room",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(" - ", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = stringResource(
                            if (device.online) R.string.device_online else R.string.device_offline
                        ),
                        color = if (device.online) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            if (onSwitchToggle != null) {
                // For initial state, treat device value > minValue as "on"
                val checked = device.value > device.minValue
                Switch(
                    checked = checked,
                    onCheckedChange = onSwitchToggle
                )
            }
        }
    }
}
