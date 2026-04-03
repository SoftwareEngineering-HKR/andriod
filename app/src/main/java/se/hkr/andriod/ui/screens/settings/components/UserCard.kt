package se.hkr.andriod.ui.screens.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import se.hkr.andriod.R
import se.hkr.andriod.domain.model.user.Permission
import se.hkr.andriod.domain.model.user.User
import se.hkr.andriod.domain.model.user.UserRole
import se.hkr.andriod.ui.theme.cardBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserCard(
    user: User,
    selectedRole: UserRole,
    selectedPermissions: Set<Permission>,
    isSaveEnabled: Boolean,
    onRoleChanged: (UserRole) -> Unit,
    onPermissionToggle: (Permission) -> Unit,
    onSaveClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    // TODO Should be changed once the roles are finalized
    val roleText = when (selectedRole) {
        UserRole.ADMIN -> "Admin"
        UserRole.BASE -> "Base user"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.cardBackground
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = user.username,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = stringResource(R.string.role),
                style = MaterialTheme.typography.labelLarge
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = roleText,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    shape = RoundedCornerShape(14.dp),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    }
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    UserRole.entries.forEach { role ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = when (role) {
                                        UserRole.ADMIN -> "Admin"
                                        UserRole.BASE -> "Base user"
                                    }
                                )
                            },
                            onClick = {
                                expanded = false
                                onRoleChanged(role)
                            }
                        )
                    }
                }
            }

            Text(
                text = stringResource(R.string.extra_permissions),
                style = MaterialTheme.typography.labelLarge
            )

            // TODO Should be changed once permissions are filanized
            Permission.entries.forEach { permission ->
                val permissionText = when (permission) {
                    Permission.ADD_ROOM -> "Add room"
                    Permission.REMOVE_ROOM -> "Remove room"
                    Permission.RENAME_ROOM -> "Rename room"
                    Permission.ADD_DEVICE -> "Add device"
                    Permission.REMOVE_DEVICE -> "Remove device"
                    Permission.RENAME_DEVICE -> "Rename device"
                    Permission.MOVE_DEVICE -> "Move device"
                    Permission.MANAGE_SCHEDULES -> "Manage schedules"
                    Permission.MANAGE_USERS -> "Manage users"
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = permissionText,
                        modifier = Modifier.weight(1f)
                    )

                    Checkbox(
                        checked = permission in selectedPermissions,
                        onCheckedChange = {
                            onPermissionToggle(permission)
                        }
                    )
                }
            }

            ActionRow(
                title = stringResource(R.string.save),
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Save,
                        contentDescription = null
                    )
                },
                onClick = onSaveClick,
                enabled = isSaveEnabled
            )
        }
    }
}