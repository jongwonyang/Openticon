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
import io.ssafy.openticon.domain.usecase.GetLikeEmoticonPack
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class LikeEmoticonViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getLikeEmoticonPack: GetLikeEmoticonPack
) : ViewModel(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val _Sample_emoticonPacksLiveData = MutableLiveData<LikeEmoticonPack?>()
    val sampleEmoticonPacksLiveData: MutableLiveData<LikeEmoticonPack?> get() = _Sample_emoticonPacksLiveData

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
    }

    override fun onCleared() {
        super.onCleared()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }
}
