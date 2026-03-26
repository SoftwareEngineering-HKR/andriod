package se.hkr.andriod.data.language

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.languageDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "language_settings"
)
