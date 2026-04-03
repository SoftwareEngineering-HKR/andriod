package se.hkr.andriod

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.foundation.isSystemInDarkTheme
import se.hkr.andriod.navigation.AppNavGraph
import se.hkr.andriod.ui.theme.AndriodTheme
import se.hkr.andriod.data.theme.ThemeStorage
import se.hkr.andriod.data.theme.AppTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val themeStorage = remember { ThemeStorage(applicationContext) }

            val selectedTheme by themeStorage.selectedThemeFlow
                .collectAsState(initial = AppTheme.SYSTEM)

            val isDarkTheme = when (selectedTheme) {
                AppTheme.DARK -> true
                AppTheme.LIGHT -> false
                AppTheme.SYSTEM -> isSystemInDarkTheme()
            }

            AndriodTheme(
                darkTheme = isDarkTheme
            ) {
                AppNavGraph()
            }
        }
    }
}
