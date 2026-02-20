package se.hkr.andriod.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.hkr.andriod.ui.components.AppButton

@Composable
fun SettingsScreen(
    onLogoutClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Settings Page")

        Spacer(modifier = Modifier.height(24.dp))

        AppButton(
            text = "Log Out",
            onClick = onLogoutClicked,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
