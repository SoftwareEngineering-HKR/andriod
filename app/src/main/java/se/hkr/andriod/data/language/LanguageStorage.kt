package se.hkr.andriod.data.language

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LanguageStorage (
    private val context: Context
) {
    private companion object {
        val SELECTED_LANGUAGE_KEY = stringPreferencesKey("selected_language")
    }

    val selectedLanguageFlow: Flow<AppLanguage> =
        context.languageDataStore.data.map { preferences ->
            val savedLanguageTag = preferences[SELECTED_LANGUAGE_KEY]
            AppLanguage.fromLanguageTag(savedLanguageTag ?: AppLanguage.ENGLISH.languageTag)
        }

    suspend fun saveLanguage(language: AppLanguage) {
        context.languageDataStore.edit { preferences ->
            preferences[SELECTED_LANGUAGE_KEY] = language.languageTag
        }
    }
}