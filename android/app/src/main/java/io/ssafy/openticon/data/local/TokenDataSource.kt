package io.ssafy.openticon.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_preferences")

object TokenDataSource {

    private lateinit var dataStore: DataStore<Preferences>

    fun initialize(context: Context) {
        dataStore = context.dataStore
    }

    private fun getDataStore(): DataStore<Preferences> {
        if (!this::dataStore.isInitialized) {
            throw UninitializedPropertyAccessException("TokenDataSource is not initialized. Call initialize() first.")
        }
        return dataStore
    }

    private val TOKEN_KEY = stringPreferencesKey("token_key")
    private val DEVICE_TOKEN_KEY = stringPreferencesKey("device_token_key")

    // 토큰 가져오기
    val token: Flow<String?> get() = getDataStore().data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    // 토큰 저장
    suspend fun saveToken(token: String) {
        getDataStore().edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    // 토큰 삭제
    suspend fun clearToken() {
        getDataStore().edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }

    // 디바이스 토큰 가져오기
    val deviceToken: Flow<String?> get() = getDataStore().data.map { preferences ->
        preferences[DEVICE_TOKEN_KEY]
    }

    // 디바이스 토큰 저장 (덮어쓰기)
    suspend fun saveDeviceToken(deviceToken: String) {
        getDataStore().edit { preferences ->
            preferences[DEVICE_TOKEN_KEY] = deviceToken
        }
    }
}
