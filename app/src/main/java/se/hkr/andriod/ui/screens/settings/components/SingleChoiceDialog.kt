package se.hkr.andriod.ui.screens.settings.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class DialogOption(
    val id: String,
    val title: String
)

@Composable
fun SingleChoiceDialog(
    title: String,
    options: List<DialogOption>,
    selectedOptionId: String,
    confirmText: String,
    dismissText: String,
    onOptionSelected: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissText)
            }
        },
        title = {
            Text(text = title)
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
            ) {
                items(options) { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = selectedOptionId == option.id,
                                onClick = { onOptionSelected(option.id) }
                            )
                    ) {
                        RadioButton(
                            selected = selectedOptionId == option.id,
                            onClick = { onOptionSelected(option.id) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = option.title)
                    }
                }
            }
        }
    )
}