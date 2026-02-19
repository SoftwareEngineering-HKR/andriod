package se.hkr.andriod.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import se.hkr.andriod.ui.theme.lightBlue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.hkr.andriod.R
import se.hkr.andriod.ui.theme.AndriodTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHomeTopBar(
    title: String,
    onlineCount: Int? = null,
    offlineCount: Int? = null,
    onAddClick: () -> Unit
) {
    TopAppBar(
        windowInsets = WindowInsets(top = 40),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )

                if (onlineCount != null && offlineCount != null) {
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = stringResource(
                            id = R.string.device_status,
                            onlineCount,
                            offlineCount
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = onAddClick) {
                Icon(
                    imageVector = Icons.Default.AddCircleOutline,
                    contentDescription = stringResource(R.string.add_icon_description)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.lightBlue,
            titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@Preview(name = "AppHomeTopBar", showBackground = true)
@Composable
private fun AddDeviceBottomSheetPreview() {
    AndriodTheme {
        AppHomeTopBar(
            title = "Device Management",
            onlineCount = 10,
            offlineCount = 2,
            onAddClick = {}
        )
    }
}
