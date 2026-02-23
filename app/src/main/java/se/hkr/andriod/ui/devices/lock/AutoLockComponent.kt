package se.hkr.andriod.ui.devices.lock

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessAlarm
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.hkr.andriod.R
import se.hkr.andriod.ui.theme.AndriodTheme
import se.hkr.andriod.ui.theme.cardBackground


@Composable
fun AutoLockComponent(
    autoLockSeconds: Int?,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onSelect: (Int?) -> Unit
) {

    val options = listOf(
        null,
        30,
        60,
        120
    )

    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .animateContentSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.cardBackground
        )
    ) {
        Column(
            modifier = Modifier
                .clickable { onToggleExpand() }
                .padding(20.dp)
        ) {

            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    imageVector = Icons.Rounded.AccessAlarm,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = stringResource(R.string.auto_lock),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = autoLockSeconds?.let { "$it s" }
                        ?: stringResource(R.string.auto_lock_off),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = if (isExpanded)
                        Icons.Rounded.ExpandLess
                    else
                        Icons.Rounded.ExpandMore,
                    contentDescription = null
                )
            }

            if (isExpanded) {

                Spacer(modifier = Modifier.height(16.dp))

                options.forEach { option ->

                    val label = option?.let { "$it s" }
                        ?: stringResource(R.string.auto_lock_off)

                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                onSelect(option)
                            }
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}

@Preview(
    name = "AutoLock - Collapsed (60s)",
    showBackground = true
)
@Composable
private fun AutoLockPreview_Collapsed() {
    AndriodTheme {
        AutoLockComponent(
            autoLockSeconds = 60,
            isExpanded = false,
            onToggleExpand = {},
            onSelect = {}
        )
    }
}

@Preview(
    name = "AutoLock - Expanded (60s Selected)",
    showBackground = true
)
@Composable
private fun AutoLockPreview_Expanded() {
    AndriodTheme {
        AutoLockComponent(
            autoLockSeconds = 60,
            isExpanded = true,
            onToggleExpand = {},
            onSelect = {}
        )
    }
}

@Preview(
    name = "AutoLock - Off",
    showBackground = true
)
@Composable
private fun AutoLockPreview_Off() {
    AndriodTheme {
        AutoLockComponent(
            autoLockSeconds = null,
            isExpanded = false,
            onToggleExpand = {},
            onSelect = {}
        )
    }
}