package se.hkr.andriod.ui.screens.settings.subscreens.users

import android.R.id.message
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import se.hkr.andriod.R
import se.hkr.andriod.domain.model.user.UserRole
import se.hkr.andriod.ui.components.CustomScreenHeader
import se.hkr.andriod.ui.screens.settings.components.ActionRow
import se.hkr.andriod.ui.screens.settings.components.ConfirmDialog
import se.hkr.andriod.ui.theme.cardBackground
import se.hkr.andriod.ui.theme.lightBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersScreen(
    viewModel: UsersViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val selectedUser = uiState.users.find { it.id == uiState.selectedUserId }

    var userDropdownExpanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.lightBlue)
            .padding(top = 24.dp),
        contentPadding = PaddingValues(
            start = 20.dp,
            end = 20.dp,
            top = 24.dp,
            bottom = 32.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            CustomScreenHeader(
                title = stringResource(R.string.settings_users_devices),
                onBackClick = onBackClick
            )
        }

        // Users
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.cardBackground
                )
            ) {
                Column(Modifier.padding(20.dp)) {

                    Text(
                        text = stringResource(R.string.user),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(16.dp))

                    ExposedDropdownMenuBox(
                        expanded = userDropdownExpanded,
                        onExpandedChange = { userDropdownExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedUser?.username.orEmpty(),
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(userDropdownExpanded)
                            },
                            shape = RoundedCornerShape(14.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = userDropdownExpanded,
                            onDismissRequest = { userDropdownExpanded = false }
                        ) {
                            uiState.users.forEach { user ->
                                DropdownMenuItem(
                                    text = { Text(user.username) },
                                    onClick = {
                                        viewModel.onUserSelected(user.id)
                                        userDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // User info
        if (selectedUser != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.cardBackground
                    )
                ) {
                    Column(Modifier.padding(20.dp)) {

                        Text(
                            text = selectedUser.username,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(12.dp))

                        RoleSelector(
                            selectedRole = selectedUser.role,
                            onRoleSelected = { role ->
                                viewModel.onUserRoleChanged(selectedUser.username, role)
                            }
                        )
                    }
                }
            }

            // Delete user
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.cardBackground)
                ) {
                    Column(Modifier.padding(vertical = 8.dp)) {

                        ActionRow(
                            title = stringResource(R.string.delete_user_title),
                            icon = {
                                Icon(Icons.Rounded.Delete, contentDescription = null)
                            },
                            onClick = viewModel::showDeleteUserDialog
                        )
                    }
                }
            }

            // Devices
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.cardBackground
                    )
                ) {
                    Column(Modifier.padding(20.dp)) {

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.Devices, contentDescription = null)

                            Spacer(Modifier.width(10.dp))

                            Text(
                                text = stringResource(R.string.assigned_devices),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(Modifier.height(16.dp))

                        uiState.devices.forEachIndexed { index, device ->

                            val isAssigned =
                                device.users.any { it.id == selectedUser.id }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.onDeviceToggled(
                                            selectedUser.id,
                                            device.id
                                        )
                                    }
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Column(Modifier.weight(1f)) {

                                    Text(
                                        text = device.displayName(),
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Spacer(Modifier.height(4.dp))

                                    val typeText = stringResource(device.deviceTypeEnum.toTextRes())
                                    val roomName = device.room

                                    Text(
                                        text = if (!roomName.isNullOrBlank() && roomName != "null") {
                                            "$typeText • $roomName"
                                        } else {
                                            typeText
                                        },
                                        style = MaterialTheme.typography.bodyMedium
                                    )

                                    Spacer(Modifier.height(4.dp))

                                    Text(
                                        text = stringResource(deviceStatusToTextRes(device.online)),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                Checkbox(
                                    checked = isAssigned,
                                    onCheckedChange = {
                                        viewModel.onDeviceToggled(
                                            selectedUser.id,
                                            device.id
                                        )
                                    }
                                )
                            }

                            if (index != uiState.devices.lastIndex) {
                                Divider()
                            }
                        }
                    }
                }
            }
        }
    }

    // Delete dialog
    if (uiState.showDeleteUserDialog && selectedUser != null) {
        ConfirmDialog(
            title = stringResource(R.string.delete_user_title),
            message = stringResource(
                R.string.delete_user_confirmation_with_name,
                selectedUser.username
            ),
            confirmText = stringResource(R.string.delete),
            dismissText = stringResource(R.string.cancel),
            onConfirm = {
                viewModel.deleteSelectedUser(selectedUser.username)
            },
            onDismiss = viewModel::dismissDialogs
        )
    }
}


@Composable
fun RoleSelector(
    selectedRole: UserRole,
    onRoleSelected: (UserRole) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        UserRole.entries.forEach { role ->

            val selected = role == selectedRole

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(
                        if (selected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                    .clickable { onRoleSelected(role) }
                    .padding(vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = role.name,
                    color = if (selected)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
