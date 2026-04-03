package se.hkr.andriod.ui.screens.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.hkr.andriod.R
import se.hkr.andriod.data.theme.AppTheme
import se.hkr.andriod.data.theme.ThemeStorage
import se.hkr.andriod.ui.theme.cardBackground
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch

@Composable
fun ThemeSelectorDialog(
    show: Boolean,
    onDismiss: () -> Unit
) {
    if (!show) return

    val context = LocalContext.current
    val themeStorage = remember { ThemeStorage(context) }
    val coroutineScope = rememberCoroutineScope()
    val selectedTheme by themeStorage.selectedThemeFlow.collectAsState(initial = null)

    selectedTheme?.let { theme ->
        Dialog(onDismissRequest = onDismiss) {
            Card(
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.cardBackground),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = stringResource(R.string.select_theme),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    listOf(AppTheme.LIGHT, AppTheme.DARK, AppTheme.SYSTEM).forEach { t ->
                        ThemeOptionItem(
                            title = when (t) {
                                AppTheme.LIGHT -> stringResource(R.string.theme_light)
                                AppTheme.DARK -> stringResource(R.string.theme_dark)
                                AppTheme.SYSTEM -> stringResource(R.string.theme_system)
                            },
                            selected = theme == t,
                            onClick = {
                                coroutineScope.launch {
                                    themeStorage.saveTheme(t)
                                    onDismiss()
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ThemeOptionItem(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )

        RadioButton(
            selected = selected,
            onClick = onClick
        )
    }
}
