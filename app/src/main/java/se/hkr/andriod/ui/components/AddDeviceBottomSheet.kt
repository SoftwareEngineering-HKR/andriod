package se.hkr.andriod.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.hkr.andriod.R
import se.hkr.andriod.data.network.AuthSession.getUser
import se.hkr.andriod.ui.theme.AndriodTheme
import se.hkr.andriod.ui.theme.cardBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDeviceBottomSheet(
    onDismiss: () -> Unit,
    onSchedulesClick: () -> Unit,
    onCreateRoomClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val currentUser = getUser()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.cardBackground,
        shape = MaterialTheme.shapes.medium,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 8.dp)
                    .width(64.dp)
                    .height(4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        shape = MaterialTheme.shapes.extraSmall
                    )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                stringResource(R.string.would_you_like_to),
                style = MaterialTheme.typography.headlineSmall
            )

            if (currentUser.canManageSchedules()) {
                AppButton(text = stringResource(R.string.create_a_schedule), onClick = onSchedulesClick)
            }
            if (currentUser.canAddRoom()) {
                AppButton(text = stringResource(R.string.create_a_room), onClick = onCreateRoomClick)
            }
        }
    }
}


@Preview(name = "AddDeviceBottomSheet", showBackground = true)
@Composable
private fun AddDeviceBottomSheetPreview() {
    AndriodTheme {
        AddDeviceBottomSheet(
            onDismiss = {},
            onSchedulesClick = {},
            onCreateRoomClick = {}
        )
    }
}
