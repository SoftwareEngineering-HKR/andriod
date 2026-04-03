package se.hkr.andriod.ui.screens.settings.subscreens.users

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
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
import androidx.compose.ui.Alignment
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
    val selectedUser = viewModel.getSelectedUser()
    val permissionItems = remember { mapPermissionsToUi() }

    var userDropdownExpanded by remember { mutableStateOf(false) }
    var roleDropdownExpanded by remember { mutableStateOf(false) }

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
                title = stringResource(R.string.settings_users_permissions),
                onBackClick = onBackClick
            )

            Card(
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.cardBackground
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.user),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ExposedDropdownMenuBox(
                        expanded = userDropdownExpanded,
                        onExpandedChange = { userDropdownExpanded = !userDropdownExpanded }
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
                                    }
                                )
                            }
                        }
                    }
                }
            }

            if (selectedUser != null) {
                val selectedRole = viewModel.getDisplayedRole(selectedUser)
                val selectedPermissions = viewModel.getDisplayedPermissions(selectedUser)

                val userInfo = mapUserToInfoUi(selectedUser, selectedRole, selectedPermissions)

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.cardBackground
                    )
                ) {
                    Column(
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
                                text = stringResource(userInfo.roleRes),
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

                Spacer(modifier = Modifier.height(16.dp))

                UserPermissionsCard(
                    selectedRole = selectedRole,
                    selectedPermissions = selectedPermissions,
                    permissionItems = permissionItems,
                    roleExpanded = roleDropdownExpanded,
                    onRoleExpandedChange = { roleDropdownExpanded = !roleDropdownExpanded },
                    onRoleDismissRequest = { roleDropdownExpanded = false },
                    onRoleChanged = { role ->
                        viewModel.onRoleChanged(role)
                        roleDropdownExpanded = false
                    },
                    onPermissionToggle = viewModel::onPermissionToggled
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.cardBackground
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        ActionRow(
                            title = stringResource(R.string.save),
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Save,
                                    contentDescription = stringResource(R.string.save)
                                )
                            },
                            onClick = viewModel::saveSelectedUser,
                            enabled = viewModel.isSaveEnabled(selectedUser)
                        )
                    }
                }
            }
        }
    }


}
