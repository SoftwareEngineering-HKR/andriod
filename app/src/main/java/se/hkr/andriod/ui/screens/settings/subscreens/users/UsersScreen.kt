package se.hkr.andriod.ui.screens.settings.subscreens.users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import se.hkr.andriod.R
import se.hkr.andriod.ui.components.CustomScreenHeader
import se.hkr.andriod.ui.screens.settings.components.ActionRow
import se.hkr.andriod.ui.screens.settings.components.InfoRow
import se.hkr.andriod.ui.screens.settings.components.UserPermissionsCard
import se.hkr.andriod.ui.theme.cardBackground
import se.hkr.andriod.ui.theme.lightBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersScreen(
    viewModel: UsersViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val permissionItems = remember { mapPermissionsToUi() }

    val selectedUser = getSelectedUser(
        users = uiState.users,
        selectedUserId = uiState.selectedUserId
    )

    val selectedRole = selectedUser?.let {
        getDisplayedRole(
            user = it,
            editedRoles = uiState.editedRoles
        )
    }

    val selectedExtraPermissions = selectedUser?.let {
        getDisplayedExtraPermissions(
            user = it,
            editedPermissions = uiState.editedPermissions
        )
    }

    val effectivePermissions = selectedUser?.let {
        getEffectivePermissions(
            user = it,
            editedRoles = uiState.editedRoles,
            editedPermissions = uiState.editedPermissions
        )
    }.orEmpty()

    val userInfo = mapUserToInfoUi(
        user = selectedUser,
        selectedRole = selectedRole,
        selectedExtraPermissions = selectedExtraPermissions
    )

    var userDropdownExpanded by remember { mutableStateOf(false) }
    var roleDropdownExpanded by remember { mutableStateOf(false) }
    var permissionsExpanded by remember { mutableStateOf(false) }


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
                title = stringResource(R.string.settings_users_permissions),
                onBackClick = onBackClick
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.cardBackground
                )
            ) {
                androidx.compose.foundation.layout.Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.user),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ExposedDropdownMenuBox(
                        expanded = userDropdownExpanded,
                        onExpandedChange = { expanded ->
                            userDropdownExpanded = expanded
                        }
                    ) {
                        OutlinedTextField(
                            value = selectedUser?.username.orEmpty(),
                            onValueChange = {},
                            readOnly = true,
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = userDropdownExpanded)
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
                                        roleDropdownExpanded = false
                                        permissionsExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (selectedUser != null && selectedRole != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.cardBackground
                    )
                ) {
                    androidx.compose.foundation.layout.Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = userInfo.name,
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            InfoRow(
                                text = stringResource(userInfo.roleRes)
                            )

                            InfoRow(
                                text = stringResource(
                                    R.string.permissions_active,
                                    userInfo.activePermissionCount
                                )
                            )
                        }
                    }
                }
            }

            item {
                UserPermissionsCard(
                    selectedRole = selectedRole,
                    effectivePermissions = effectivePermissions,
                    permissionItems = permissionItems,
                    roleExpanded = roleDropdownExpanded,
                    permissionsExpanded = permissionsExpanded,
                    onRoleExpandedChange = { expanded ->
                        roleDropdownExpanded = expanded
                    },
                    onPermissionsExpandedChange = { expanded ->
                        permissionsExpanded = expanded
                    },
                    onRoleChanged = { role ->
                        viewModel.onRoleChanged(selectedUser.id, role)
                    },
                    onPermissionToggle = { permission ->
                        viewModel.onPermissionToggled(selectedUser.id, permission)
                    },
                    isPermissionEditable = { permission ->
                        isPermissionEditable(
                            user = selectedUser,
                            permission = permission,
                            editedRoles = uiState.editedRoles
                        )
                    }
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.cardBackground
                    )
                ) {
                    androidx.compose.foundation.layout.Column(
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        ActionRow(
                            title = stringResource(R.string.save),
                            icon = {
                                Icon(
                                    imageVector = Icons.Rounded.Save,
                                    contentDescription = null
                                )
                            },
                            onClick = viewModel::saveSelectedUser,
                            enabled =
                                selectedRole != selectedUser.role ||
                                        selectedExtraPermissions != selectedUser.extraPermissions
                        )
                    }
                }
            }
        }
    }
}