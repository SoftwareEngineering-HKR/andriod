package se.hkr.andriod.ui.screens.settings.subscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import se.hkr.andriod.ui.theme.lightBlue

@Composable
fun RoomsScreen() {
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.lightBlue),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Rooms",
            style = MaterialTheme.typography.titleLarge
        )
    }
}
