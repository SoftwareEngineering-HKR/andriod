package se.hkr.andriod.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import se.hkr.andriod.data.mock.MockDevices
import se.hkr.andriod.domain.model.device.Device
import se.hkr.andriod.ui.theme.cardBackground
import se.hkr.andriod.R

@Composable
fun ScanDevicesModal(
    onDismiss: () -> Unit,
    mockDevices: List<Device> = MockDevices.allDevices
) {
    var isLoading by remember { mutableStateOf(true) }
    var foundDevices by remember { mutableStateOf<List<Device>>(emptyList()) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2000) // simulate scanning
        foundDevices = mockDevices
        isLoading = false
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.cardBackground,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isLoading) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(stringResource(R.string.scan_for_devices))
                    }
                } else {
                    if (foundDevices.isEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            stringResource(R.string.no_new_devices_found),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    } else {
                        Text(
                            stringResource(R.string.found_new_devices, foundDevices.size),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 8.dp, bottom = 4.dp)
                        )

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth().weight(1f, fill = false)
                        ) {
                            items(foundDevices) { device ->
                                ScanDeviceCard(device)
                            }
                        }
                    }
                    AppButton(
                        text = stringResource(R.string.close_button),
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
