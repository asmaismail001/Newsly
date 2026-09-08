package com.example.newsly.ui.theme

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "newsly_preferences")

enum class AppThemeMode(val title: String) {
    SYSTEM("System Default"),
    LIGHT("Light Mode"),
    DARK("Dark Mode")
}

class ThemeManager(private val context: Context) {

    private val THEME_KEY = stringPreferencesKey("app_theme_mode")
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _themeMode = MutableStateFlow(AppThemeMode.SYSTEM)
    val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

    init {
        scope.launch {
            val savedMode = context.dataStore.data.map { preferences ->
                val name = preferences[THEME_KEY] ?: AppThemeMode.SYSTEM.name
                try {
                    AppThemeMode.valueOf(name)
                } catch (_: Exception) {
                    AppThemeMode.SYSTEM
                }
            }.first()
            _themeMode.value = savedMode
        }
    }

    fun setThemeMode(mode: AppThemeMode) {
        _themeMode.value = mode
        scope.launch {
            context.dataStore.edit { preferences ->
                preferences[THEME_KEY] = mode.name
            }
        }
    }
}
