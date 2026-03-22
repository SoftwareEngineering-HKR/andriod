package se.hkr.andriod.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import se.hkr.andriod.R
import se.hkr.andriod.ui.theme.cardBackground

data class DeviceItemModel(
    val id: String,
    val name: String,
    val room: String,
    val isOnline: Boolean,
    val icon: ImageVector
)

@Composable
fun DeviceCardItem(
    modifier: Modifier = Modifier,
    device: DeviceItemModel,
    onClick: (() -> Unit)? = null,
    onSwitchToggle: ((Boolean) -> Unit)? = null,
    elevation: Dp = 0.dp
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .then(
                if (onClick != null) Modifier.clickable { onClick() } else Modifier
            ),
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
                Icon(
                    imageVector = device.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = device.name,
                    style = MaterialTheme.typography.titleMedium
                )

                Row {
                    Text(
                        text = device.room,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(" - ", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = stringResource(
                            if (device.isOnline) R.string.device_online else R.string.device_offline
                        ),
                        color = if (device.isOnline) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            if (onSwitchToggle != null) {
                Switch(
                    checked = device.isOnline,
                    onCheckedChange = onSwitchToggle
                )
            }
        }
    }
}
