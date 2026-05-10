package se.hkr.andriod.ui.screens.settings.subscreens.accountinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import se.hkr.andriod.ui.screens.settings.components.ActionRow
import se.hkr.andriod.ui.screens.settings.components.InfoRow
import se.hkr.andriod.ui.theme.cardBackground
import se.hkr.andriod.ui.theme.lightBlue

@Composable
fun AccountInfoScreen(
    viewModel: AccountInfoViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

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
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            CustomScreenHeader(
                title = stringResource(id = R.string.settings_account_info),
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
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = uiState.username,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    // Currently disabled
                    // InfoRow(text = user?.role?.name.orEmpty())
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.cardBackground
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.username),
                        style = MaterialTheme.typography.titleMedium
                    )

                    OutlinedTextField(
                        value = uiState.usernameInput,
                        onValueChange = viewModel::onUsernameChanged,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = {
                            Text(text = stringResource(R.string.username))
                        },
                        shape = RoundedCornerShape(14.dp)
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.cardBackground
                )
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    ActionRow(
                        title = stringResource(id = R.string.save),
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Save,
                                contentDescription = null
                            )
                        },
                        onClick = viewModel::saveUsername,
                        enabled = uiState.isSaveEnabled
                    )
                }
            }
        }
    }
}

