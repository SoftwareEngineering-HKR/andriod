package se.hkr.andriod.ui.screens.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import se.hkr.andriod.R
import se.hkr.andriod.domain.model.user.Permission
import se.hkr.andriod.domain.model.user.UserRole
import se.hkr.andriod.ui.screens.settings.subscreens.users.PermissionUi
import se.hkr.andriod.ui.screens.settings.subscreens.users.toRoleTextRes
import se.hkr.andriod.ui.theme.cardBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserPermissionsCard(
    selectedRole: UserRole,
    selectedPermissions: Set<Permission>,
    permissionItems: List<PermissionUi>,
    roleExpanded: Boolean,
    onRoleExpandedChange: () -> Unit,
    onRoleDismissRequest: () -> Unit,
    onRoleChanged: (UserRole) -> Unit,
    onPermissionToggle: (Permission) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(0.9f),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.cardBackground
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.role),
                style = MaterialTheme.typography.titleMedium
            )

            ExposedDropdownMenuBox(
                expanded = roleExpanded,
                onExpandedChange = { onRoleExpandedChange() }
            ) {
                OutlinedTextField(
                    value = stringResource(selectedRole.toRoleTextRes()),
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = roleExpanded)
                    },
                    shape = RoundedCornerShape(14.dp)
                )

                ExposedDropdownMenu(
                    expanded = roleExpanded,
                    onDismissRequest = onRoleDismissRequest
                ) {
                    UserRole.entries.forEach { role ->
                        DropdownMenuItem(
                            text = { Text(stringResource(role.toRoleTextRes())) },
                            onClick = {
                                onRoleChanged(role)
                            }
                        )
                    }
                }
            }

            Text(
                text = stringResource(R.string.extra_permissions),
                style = MaterialTheme.typography.titleMedium
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                permissionItems.forEach { permissionUi ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(permissionUi.labelRes),
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Checkbox(
                            checked = permissionUi.permission in selectedPermissions,
                            onCheckedChange = {
                                onPermissionToggle(permissionUi.permission)
                            }
                        )
                    }
                }
            }
        }
    }
}