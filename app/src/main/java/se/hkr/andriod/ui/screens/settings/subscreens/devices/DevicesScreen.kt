package se.hkr.andriod.ui.screens.settings.subscreens.devices

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import se.hkr.andriod.R
import se.hkr.andriod.ui.components.CustomScreenHeader
import se.hkr.andriod.ui.screens.settings.components.ConfirmDialog
import se.hkr.andriod.ui.screens.settings.components.DialogOption
import se.hkr.andriod.ui.screens.settings.components.InfoRow
import se.hkr.andriod.ui.screens.settings.components.InputDialog
import se.hkr.andriod.ui.screens.settings.components.SettingsItem
import se.hkr.andriod.ui.screens.settings.components.SingleChoiceDialog
import se.hkr.andriod.ui.theme.cardBackground
import se.hkr.andriod.ui.theme.lightBlue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevicesScreen(
    viewModel: DevicesViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedDevice = uiState.selectedDevice
    val deviceInfo = mapDeviceToInfoUi(selectedDevice, uiState.rooms)

    var expanded by remember { mutableStateOf(false) }

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
            CustomScreenHeader(
                title = stringResource(R.string.devices),
                onBackClick = onBackClick
            )

            Card(
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.cardBackground
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        stringResource(R.string.device),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedDevice?.displayName.orEmpty(),
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            uiState.devices.forEach { device ->
                                DropdownMenuItem(
                                    text = { Text(device.displayName)},
                                    onClick = {
                                        viewModel.onDeviceSelected(device.id)
                                        expanded = false
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(
                                onClick = viewModel::showRenameDialog
                            ) {
                                Icon(
                                    Icons.Rounded.Edit,
                                    contentDescription = stringResource(R.string.rename)
                                )
                            }

                            IconButton(
                                onClick = viewModel::showChangeRoomDialog
                            ) {
                                Icon(
                                    Icons.Rounded.Home,
                                    contentDescription = stringResource(R.string.change_room)
                                )
                            }

                            IconButton(
                                onClick = viewModel::showDeleteDialog
                            ) {
                                Icon(
                                    Icons.Rounded.Delete,
                                    contentDescription = stringResource(R.string.delete)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.cardBackground
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            stringResource(R.string.device_information),
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        InfoRow(
                            label = stringResource(R.string.device_name),
                            value = deviceInfo.name

                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        InfoRow(
                            label = stringResource(R.string.device_type),
                            value = deviceInfo.type
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        InfoRow(
                            label = stringResource(R.string.room),
                            value = deviceInfo.room
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        InfoRow(
                            label = stringResource(R.string.device_status),
                            value = deviceInfo.status
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        InfoRow(
                            label = stringResource(R.string.ip_address),
                            value = deviceInfo.ip
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        InfoRow(
                            label = stringResource(R.string.description),
                            value = deviceInfo.description
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        InfoRow(
                            label = stringResource(R.string.device_value),
                            value = deviceInfo.value
                        )
                    }
                }
            }

            if (uiState.showRenameDialog && selectedDevice != null) {
                InputDialog(
                    title = stringResource(R.string.rename_device),
                    value = uiState.inputText,
                    onValueChange = viewModel::onInputChanged,
                    label = stringResource(R.string.new_name),
                    confirmText = stringResource(R.string.rename),
                    dismissText = stringResource(R.string.cancel),
                    onConfirm = {
                        viewModel.dismissDialogs()
                        // rename device
                    },
                    onDismiss = viewModel::dismissDialogs
                )
            }

            if (uiState.showDeleteDialog && selectedDevice != null) {
                ConfirmDialog(
                    title = stringResource(R.string.delete_device),
                    message = stringResource(
                        R.string.delete_device_confirmation_with_name,
                        selectedDevice.displayName
                    ),
                    confirmText = stringResource(R.string.delete),
                    dismissText = stringResource(R.string.cancel),
                    onConfirm = {
                        viewModel.dismissDialogs()
                        // delete device
                    },
                    onDismiss = viewModel::dismissDialogs
                )
            }

            if (uiState.showChangeRoomDialog && selectedDevice != null) {
                SingleChoiceDialog(
                    title = stringResource(R.string.change_room),
                    options = uiState.availableRooms.map { room ->
                        DialogOption(
                            id = room.id,
                            title = room.name
                        )
                    },
                    selectedOptionId = uiState.selectedRoomIdForDialog,
                    confirmText = stringResource(R.string.save),
                    dismissText = stringResource(R.string.cancel),
                    onOptionSelected = viewModel::onRoomSelectedForDialog,
                    onConfirm = {
                        viewModel.dismissDialogs()
                        // change room
                    },
                    onDismiss = viewModel::dismissDialogs
                )
            }
        }
    }

}
