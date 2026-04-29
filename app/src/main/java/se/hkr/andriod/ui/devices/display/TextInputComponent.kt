package se.hkr.andriod.ui.devices.display

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.hkr.andriod.domain.model.device.Device
import se.hkr.andriod.ui.components.AppButton
import se.hkr.andriod.ui.components.AppTextField
import se.hkr.andriod.ui.theme.cardBackground

@Composable
fun TextInputComponent(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    device: Device
) {
    Card(
        modifier = Modifier.fillMaxWidth(0.9f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.cardBackground
        )
    ) {
        AppTextField(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            label = "Display Text",
            value = value,
            onValueChange = onValueChange,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${value.length}/32",
                modifier = Modifier.weight(1f).padding(start = 20.dp, bottom = 4.dp)
            )

            AppButton(
                text = "Send",
                onClick = onSend,
                enabled = device.online,
                modifier = Modifier.width(120.dp).padding(end = 16.dp, bottom = 16.dp),
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}
