package io.ssafy.openticon.ui.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ssafy.openticon.R
import io.ssafy.openticon.data.model.Emoticon
import io.ssafy.openticon.data.model.LikeEmoticon
import io.ssafy.openticon.data.model.LikeEmoticonPack
import io.ssafy.openticon.data.model.SampleEmoticonPack
import io.ssafy.openticon.data.repository.LikeEmoticonPackRepository
import io.ssafy.openticon.di.UserSession
import io.ssafy.openticon.domain.usecase.GetLikeEmoticonPack
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class LikeEmoticonViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getLikeEmoticonPack: GetLikeEmoticonPack,
    userSession: UserSession
) : ViewModel(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val _Sample_emoticonPacksLiveData = MutableLiveData<LikeEmoticonPack?>()
    val sampleEmoticonPacksLiveData: MutableLiveData<LikeEmoticonPack?> get() = _Sample_emoticonPacksLiveData

    private val _isLaunched = MutableStateFlow(false)
    val isLaunched: StateFlow<Boolean> get() = _isLaunched.asStateFlow()

    val isLoggedIn: StateFlow<Boolean> = userSession.isLoggedIn
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        initEmoticonDataFromPreferences()
    }

    fun initEmoticonDataFromPreferences(){

        viewModelScope.launch {
            getLikeEmoticonPack.execute().collect { emoticonPacks ->
                _Sample_emoticonPacksLiveData.value = emoticonPacks
            }
        }

        val editor = sharedPreferences.edit()
        val jsonString = Json.encodeToString(sampleEmoticonPacksLiveData.value)
        Log.d("MainJson",jsonString)
        editor.putString("like_emoticon_data", jsonString)
        editor.apply()
    }

    fun loadEmoticonDataFromPreferences() {
        val jsonString = sharedPreferences.getString("like_emoticon_data", null)
        val data = jsonString?.let {
            try {
                Json.decodeFromString<LikeEmoticonPack>(it)
            } catch (e: SerializationException) {
                // JSON 파싱 오류 시 로그를 남기고 기본값 사용
                Log.e("LikeEmoticonViewModel", "JSON 디코딩 실패: ${e.message}")
                null
            }
        } ?: LikeEmoticonPack("default", R.drawable.icon_2, emptyList()) // null일 경우 기본값 설정

        _Sample_emoticonPacksLiveData.value = data
    }


    private suspend fun addEmoticonDataFromPreferences() {
        val jsonString = sharedPreferences.getString("new_like_emoticon_data", null)
        val data = jsonString?.let {
            try {
                Json.decodeFromString<LikeEmoticon>(it)
            } catch (e: SerializationException) {
                // JSON 파싱 오류 시 로그를 남기고 기본값 사용
                Log.e("LikeEmoticonViewModel", "JSON 디코딩 실패: ${e.message}")
                null
            }
        } ?: LikeEmoticon(filePath = "", title = "default", packId = Int.MAX_VALUE) // null일 경우 기본값 설정

        getLikeEmoticonPack.insertLike(data)
    }

    private suspend fun changeLaunched(){
        val isVisible = sharedPreferences.getBoolean("is_visible", false) // 기본값을 false로 설정
        Log.d("LikeViewModelChangeLaunched", isVisible.toString())
        _isLaunched.value = isVisible
    }


    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "like_emoticon_data") {
            Log.d("LikeEmoticonViewModel", "Really Changed")
            loadEmoticonDataFromPreferences()
        }
        else if(key == "new_like_emoticon_data"){
            viewModelScope.launch {
                addEmoticonDataFromPreferences()
            }
        }
        else if(key == "is_visible"){
            viewModelScope.launch {
                changeLaunched()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    fun updateIsLaunched(value: Boolean) {
        _isLaunched.value = value
        Log.d("LikeEmoticonViewModel", "isLaunched set to $value")

        // 타이머로 일정 시간 동안 값이 유지되는지 확인
        viewModelScope.launch {
            delay(5000)  // 5초 동안 기다린 후 상태 확인
            Log.d("LikeEmoticonViewModel", "isLaunched 5초 후 상태: ${_isLaunched.value}")
        }
    }

    fun debugPrint(){
        Log.d("LikeViewModelDebugPrint", _isLaunched.value.toString())
    }
}
