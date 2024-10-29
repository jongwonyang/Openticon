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

class TokenDataSource(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("token_key")
        private val DEVICE_TOKEN_KEY = stringPreferencesKey("device_token_key")
    }

    // 토큰 가져오기
    val token: Flow<String?> = dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    // 토큰 저장
    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    // 토큰 삭제
    suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }

    // 디바이스 토큰 가져오기
    val deviceToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[DEVICE_TOKEN_KEY]
    }

    // 디바이스 토큰 저장 (덮어쓰기)
    suspend fun saveDeviceToken(deviceToken: String) {
        dataStore.edit { preferences ->
            preferences[DEVICE_TOKEN_KEY] = deviceToken
        }
    }

}
