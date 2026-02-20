package se.hkr.andriod.ui.screens.devicecard

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import se.hkr.andriod.R
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun DeviceCardScreen(
    viewModel: DeviceCardViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()

    val onlineText = stringResource(
        if (uiState.isOnline) R.string.device_online else R.string.device_offline
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = uiState.deviceName,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${uiState.roomName} • $onlineText",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = uiState.icon,
                            contentDescription = null,
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = uiState.deviceName,
                            modifier = Modifier.weight(1f)
                        )

                        Switch(
                            checked = uiState.isEnabled,
                            onCheckedChange = { viewModel.toggleDevice(it) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.clickable {
                    viewModel.toggleSchedule()
                },
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Rounded.CalendarToday, null)

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = stringResource(R.string.device_schedule),
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            if (uiState.scheduleExpanded)
                                Icons.Rounded.ExpandLess
                            else
                                Icons.Rounded.ExpandMore,
                            null
                        )
                    }

                    if (uiState.scheduleExpanded) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Placeholder")
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = uiState.lastUpdatedText,
                style = MaterialTheme.typography.bodySmall
            )

        }
    }




}