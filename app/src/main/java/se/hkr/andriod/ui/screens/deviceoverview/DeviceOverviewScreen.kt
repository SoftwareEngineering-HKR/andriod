package se.hkr.andriod.ui.screens.deviceoverview

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.hkr.andriod.ui.components.AppButton

sealed interface DeviceOverviewEvent {
    object LogOutClicked : DeviceOverviewEvent
}

@Composable
fun DeviceOverviewScreen(
    onEvent: (DeviceOverviewEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Device Overview Page")

        Spacer(modifier = Modifier.height(24.dp))

        AppButton(
            text = "Log Out",
            onClick = { onEvent(DeviceOverviewEvent.LogOutClicked) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}