package se.hkr.andriod.ui.screens.devicecard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import se.hkr.andriod.R
import se.hkr.andriod.ui.components.AppButton
import se.hkr.andriod.ui.theme.cardBackground


@Composable
fun DeviceCardScreen(
    viewModel: DeviceCardViewModel,

    // Dynamic device specific content injected from device layer
    deviceComponent: @Composable () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Device online/offline text
    val onlineText = stringResource(
        if (uiState.isOnline) R.string.device_online else R.string.device_offline
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4FC3F7)) // TEST COLOR
            .padding(top = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top row: back button, device name
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, null)
                }

                Text(
                    text = uiState.deviceName,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.size(48.dp))
            }

            // Main info card: icon, name, room, switch
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.cardBackground
                )
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(20.dp)
                ) {
                    // Device icon container
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(MaterialTheme.shapes.small)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = uiState.icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = uiState.deviceName,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        // Room + Online status
                        Row {
                            Text(
                                text = uiState.roomName,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Text(" - ", style = MaterialTheme.typography.bodyMedium)

                            Text(
                                text = onlineText,
                                color = if (uiState.isOnline)
                                    Color(0xFF2E7D32)
                                else
                                    MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Switch(
                        checked = uiState.isOnline,
                        onCheckedChange = { viewModel.toggleDevice(it) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Dynamic components
            deviceComponent()

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