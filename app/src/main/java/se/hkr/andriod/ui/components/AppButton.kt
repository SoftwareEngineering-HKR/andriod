package se.hkr.andriod.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.hkr.andriod.ui.theme.AndriodTheme
import se.hkr.andriod.ui.theme.DisabledButtonBLue

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    text: String? = null,
    loadingText: String? = null,
    icon: ImageVector? = null,
    onClick: () -> Unit,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier.height(48.dp),
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = DisabledButtonBLue,
            disabledContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
                if (!loadingText.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = loadingText)
                }
            } else {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null
                    )
                }

                // Only show spacer if both icon AND text exist
                if (icon != null && !text.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))
                }

                if (!text.isNullOrEmpty()) {
                    Text(text = text)
                }
            }
        }
    }
}


@Preview(name = "AppButton - Text only", showBackground = true)
@Composable
private fun AppButtonTextPreview() {
    AndriodTheme {
        AppButton(
            text = "Sign In",
            onClick = {}
        )
    }
}

@Preview(name = "AppButton - Icon and text", showBackground = true)
@Composable
private fun AppButtonIconTextPreview() {
    AndriodTheme {
        AppButton(
            text = "Sign In",
            icon = Icons.Default.Lock,
            onClick = {}
        )
    }
}

@Preview(name = "AppButton - Icon only", showBackground = true)
@Composable
private fun AppButtonIconOnlyPreview() {
    AndriodTheme {
        AppButton(
            text = "",
            icon = Icons.Default.Lock,
            onClick = {}
        )
    }
}

@Preview(name = "AppButton - Disabled", showBackground = true)
@Composable
private fun AppButtonDisabledPreview() {
    AndriodTheme {
        AppButton(
            text = "Sign In",
            icon = Icons.Default.Lock,
            onClick = {},
            enabled = false
        )
    }
}

@Preview(name = "AppButton - Loading only", showBackground = true)
@Composable
private fun AppButtonOnlyLoadingPreview() {
    AndriodTheme {
        AppButton(
            text = "Scan for devices",
            icon = Icons.Default.Lock,
            onClick = {},
            isLoading = true
        )
    }
}

@Preview(name = "AppButton - Loading", showBackground = true)
@Composable
private fun AppButtonLoadingPreview() {
    AndriodTheme {
        AppButton(
            text = "Scan for devices",
            loadingText = "Scanning for devices...",
            icon = Icons.Default.Lock,
            onClick = {},
            isLoading = true
        )
    }
}
