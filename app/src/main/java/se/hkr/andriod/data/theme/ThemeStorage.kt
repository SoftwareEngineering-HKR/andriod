package se.hkr.andriod.data.theme

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import se.hkr.andriod.data.language.languageDataStore

class ThemeStorage(
    private val context: Context
) {
    private companion object {
        val SELECTED_THEME_KEY = stringPreferencesKey("selected_theme")
    }

    val selectedThemeFlow: Flow<AppTheme> =
        context.languageDataStore.data.map { preferences ->
            when (preferences[SELECTED_THEME_KEY]) {
                "LIGHT" -> AppTheme.LIGHT
                "DARK" -> AppTheme.DARK
                else -> AppTheme.SYSTEM
            }
        }

    suspend fun saveTheme(theme: AppTheme) {
        context.languageDataStore.edit { preferences ->
            preferences[SELECTED_THEME_KEY] = theme.name
        }
    }
}
