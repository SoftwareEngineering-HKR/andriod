package se.hkr.andriod.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import se.hkr.andriod.R
import se.hkr.andriod.domain.model.device.Device
import se.hkr.andriod.domain.model.device.DeviceType
import se.hkr.andriod.ui.theme.ListItemBackground

@Composable
fun ScanDeviceCard(device: Device) {
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.ListItemBackground),
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Temporary until the icons come from the backend
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
                else -> rememberVectorPainter(Icons.Default.QuestionMark)
            }

            Icon(icon, contentDescription = null)

            Text(
                text = device.name ?: "Unknown Device",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
