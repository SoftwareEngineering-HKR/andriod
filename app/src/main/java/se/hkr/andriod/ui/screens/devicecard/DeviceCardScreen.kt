package se.hkr.andriod.ui.screens.devicecard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import se.hkr.andriod.R
import se.hkr.andriod.domain.model.device.Device
import se.hkr.andriod.data.network.ConnectionManager
import se.hkr.andriod.ui.components.DeviceCardItem
import se.hkr.andriod.ui.components.AppButton
import se.hkr.andriod.ui.components.CustomScreenHeader
import se.hkr.andriod.ui.theme.cardBackground
import se.hkr.andriod.ui.theme.lightBlue

@Composable
fun DeviceCardScreen(
    device: Device,
    viewModel: DeviceCardViewModel,
    connectionManager: ConnectionManager,
    onBackClick: () -> Unit,

    // Dynamic device specific content injected from device layer
    deviceComponent: @Composable (Device) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    // Collect live device from the device store for instant UI updates
    val devices by connectionManager.deviceStore.devices.collectAsState()
    val liveDevice = devices.firstOrNull { it.id == device.id } ?: device

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.lightBlue)
            .padding(top = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Screen header with back button
            CustomScreenHeader(
                title = liveDevice.displayName,
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Top device card (icon, name, room, switch)
            DeviceCardItem(
                device = liveDevice,
                onSwitchToggle = { isOn ->
                    val value = if (isOn) liveDevice.maxValue else liveDevice.minValue
                    connectionManager.deviceStore.updateDeviceValue(liveDevice.id, value)
                },
                onAction = {
                    connectionManager.deviceStore.updateDeviceValue(
                        liveDevice.id,
                        liveDevice.minValue
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dynamic components
            deviceComponent(liveDevice)

            // Schedule selection TODO
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clickable {
                        viewModel.toggleSchedule()
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.cardBackground
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Rounded.CalendarToday,
                            null,
                            modifier = Modifier.size(36.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = stringResource(R.string.device_schedule),
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.titleMedium
                        )

                        Icon(
                            if (uiState.scheduleExpanded)
                                Icons.Rounded.ExpandLess
                            else
                                Icons.Rounded.ExpandMore,
                            null,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    AnimatedVisibility(
                        visible = uiState.scheduleExpanded,
                        enter = expandVertically(
                            animationSpec = tween(250)
                        ) + fadeIn(animationSpec = tween(250)),
                        exit = shrinkVertically(
                            animationSpec = tween(250)
                        ) + fadeOut(animationSpec = tween(250)
                        )
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = stringResource(R.string.no_schedules_configured),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            AppButton(
                                text = stringResource(R.string.add_new_schedule),
                                onClick = { /*TODO*/ }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Footer
            Text(
                text = uiState.lastUpdatedText,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
