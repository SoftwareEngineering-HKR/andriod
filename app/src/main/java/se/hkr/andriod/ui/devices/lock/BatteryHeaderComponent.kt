package se.hkr.andriod.ui.devices.lock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BatteryFull
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.hkr.andriod.ui.theme.AndriodTheme


// CURRENTLY NOT USED
@Composable
fun BatteryHeaderComponent(
    percentage: Int
) {

    val color = when {
        percentage >= 60 -> Color(0xFF2E7D32)
        percentage >= 30 -> Color(0xFFF9A825)
        else -> Color(0xFFD32F2F)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = color.copy(alpha = 0.12f),
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {

        Icon(
            imageVector = Icons.Rounded.BatteryFull,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = "$percentage%",
            color = color,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview(
    name = "Battery - High (82%)",
    showBackground = true
)
@Composable
private fun BatteryHeaderPreview_High() {
    AndriodTheme {
        BatteryHeaderComponent(
            percentage = 82
        )
    }
}

@Preview(
    name = "Battery - Medium (45%)",
    showBackground = true
)
@Composable
private fun BatteryHeaderPreview_Medium() {
    AndriodTheme {
        BatteryHeaderComponent(
            percentage = 45
        )
    }
}

@Preview(
    name = "Battery - Low (15%)",
    showBackground = true
)
@Composable
private fun BatteryHeaderPreview_Low() {
    AndriodTheme {
        BatteryHeaderComponent(
            percentage = 15
        )
    }
}