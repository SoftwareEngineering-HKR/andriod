package se.hkr.andriod.ui.screens.settings.subscreens.users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import se.hkr.andriod.R
import se.hkr.andriod.ui.components.CustomScreenHeader
import se.hkr.andriod.ui.screens.settings.components.UserCard
import se.hkr.andriod.ui.theme.cardBackground
import se.hkr.andriod.ui.theme.lightBlue

@Composable
fun UsersScreen(
    viewModel: UsersViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

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
                title = stringResource(id = R.string.settings_users_permissions),
                onBackClick = onBackClick
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.cardBackground
                )
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = uiState.users,
                        key = { it.id }
                    ) { user ->
                        UserCard(
                            user = user,
                            selectedRole = viewModel.getDisplayedRole(user),
                            selectedPermissions = viewModel.getDisplayedPermissions(user),
                            isSaveEnabled = viewModel.isSaveEnabled(user),
                            onRoleChanged = { newRole ->
                                viewModel.onRoleChanged(user.id, newRole)
                            },
                            onPermissionToggle = { permission ->
                                viewModel.onPermissionToggled(user.id, permission)
                            },
                            onSaveClick = {
                                viewModel.saveUser(user.id)
                            }
                        )
                    }
                }
            }
        }
    }
}
