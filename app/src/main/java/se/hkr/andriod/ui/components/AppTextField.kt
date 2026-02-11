package se.hkr.andriod.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.hkr.andriod.ui.theme.AndriodTheme
import se.hkr.andriod.ui.theme.inputFieldFill
import se.hkr.andriod.ui.theme.inputFieldOutline
import se.hkr.andriod.ui.theme.inputFieldPlaceholder
import se.hkr.andriod.ui.theme.lightRed
import se.hkr.andriod.ui.theme.middleRed

@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    label: String? = null,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String? = null,
    isPassword: Boolean = false,
    errorText: String? = null,
    helperText: String? = null,
    leadingIcon: ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    showLabel: Boolean = true
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        // Label above text field
        if (showLabel && !label.isNullOrEmpty()) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.W500,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                if (!placeholder.isNullOrEmpty()) Text(placeholder)
            },
            isError = errorText != null,
            singleLine = true,
            leadingIcon = if (leadingIcon != null) {
                { Icon(imageVector = leadingIcon, contentDescription = null) }
            } else null,
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null,
                        )
                    }
                }
            } else null,
            visualTransformation = if (isPassword && !passwordVisible)
                PasswordVisualTransformation()
            else
                VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.inputFieldFill,
                focusedContainerColor = MaterialTheme.colorScheme.inputFieldFill,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.inputFieldOutline,
                focusedPlaceholderColor = MaterialTheme.colorScheme.inputFieldPlaceholder,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.inputFieldPlaceholder,
                errorContainerColor = MaterialTheme.colorScheme.lightRed,
                errorIndicatorColor = MaterialTheme.colorScheme.middleRed
            )
        )

        // Error or helper text
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .defaultMinSize(minHeight = 16.dp)
        ) {
            val supportingText = errorText ?: helperText

            if (supportingText != null) {
                Text(
                    text = supportingText,
                    color = if (errorText != null)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}


@Preview(name = "TextField - Email", showBackground = true)
@Composable
private fun AppTextFieldEmailPreview() {
    val email = remember { mutableStateOf("") }
    AndriodTheme {
        AppTextField(
            label = "Email",
            value = email.value,
            onValueChange = { email.value = it },
            placeholder = "example@email.com",
            keyboardType = KeyboardType.Email
        )
    }
}

@Preview(name = "TextField - Password", showBackground = true)
@Composable
private fun AppTextFieldPasswordPreview() {
    val password = remember { mutableStateOf("") }
    AndriodTheme {
        AppTextField(
            label = "Password",
            value = password.value,
            onValueChange = { password.value = it },
            isPassword = true
        )
    }
}

@Preview(name = "TextField - Error state", showBackground = true)
@Composable
private fun AppTextFieldErrorPreview() {
    val email = remember { mutableStateOf("wrong-email") }
    AndriodTheme {
        AppTextField(
            label = "Email",
            value = email.value,
            onValueChange = { email.value = it },
            errorText = "Invalid email address"
        )
    }
}

@Preview(name = "TextField - With icon", showBackground = true)
@Composable
private fun AppTextFieldWithIconPreview() {
    val search = remember { mutableStateOf("") }
    AndriodTheme {
        AppTextField(
            value = search.value,
            onValueChange = { search.value = it },
            placeholder = "Search here...",
            leadingIcon = Icons.Default.Search
        )
    }
}
