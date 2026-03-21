package se.hkr.andriod.ui.screens.scan

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.hkr.andriod.R
import se.hkr.andriod.ui.components.AppButton

@Composable
fun FailedScanComponent(
    isRetrying: Boolean,
    onRetry: () -> Unit,
    onManualAdd: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(120.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)

            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(48.dp)
                )
            }

            Text(
                text = stringResource(R.string.no_new_devices_found),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = stringResource(R.string.devices_hint),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppButton(
                text = stringResource(R.string.retry),
                onClick = onRetry,
                isLoading = isRetrying,
                loadingText = stringResource(R.string.retrying),
                modifier = Modifier.fillMaxWidth(0.6f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppButton(
                text = stringResource(R.string.manual_add),
                onClick = onManualAdd,
                enabled = !isRetrying,
                modifier = Modifier.fillMaxWidth(0.6f)
            )
        }
    }
}

@Preview(
    name = "Failed Scan - Default",
    showBackground = true
)
@Composable
fun FailedScan_Default_Preview() {
    MaterialTheme {
        FailedScanComponent(
            isRetrying = false,
            onRetry = {},
            onManualAdd = {}
        )

    }
}

@Preview(
    name = "Failed Scan - Retrying",
    showBackground = true
)
@Composable
fun FailedScan_Retrying_Preview() {
    MaterialTheme {
        FailedScanComponent(
            isRetrying = true,
            onRetry = {},
            onManualAdd = {}
        )
    }
}