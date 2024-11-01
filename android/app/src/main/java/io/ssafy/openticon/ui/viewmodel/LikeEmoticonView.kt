package io.ssafy.openticon.ui.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ssafy.openticon.data.model.SampleEmoticonPack
import io.ssafy.openticon.data.repository.LikeEmoticonPackRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class LikeEmoticonViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val repository = LikeEmoticonPackRepository()

    private val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val _Sample_emoticonPacksLiveData = MutableLiveData<SampleEmoticonPack?>()
    val sampleEmoticonPacksLiveData: MutableLiveData<SampleEmoticonPack?> get() = _Sample_emoticonPacksLiveData

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        initEmoticonDataFromPreferences()
    }

    private fun initEmoticonDataFromPreferences(){
        _Sample_emoticonPacksLiveData.value = repository.getLikeEmoticonPack()

        val editor = sharedPreferences.edit()
        val jsonString = Json.encodeToString(sampleEmoticonPacksLiveData.value)
        Log.d("MainJson",jsonString)
        editor.putString("like_emoticon_data", jsonString)
        editor.apply()
    }

    private fun loadEmoticonDataFromPreferences() {
        val jsonString = sharedPreferences.getString("like_emoticon_data", null)
        val data = jsonString?.let {
            Json.decodeFromString<SampleEmoticonPack>(it)
        }
        _Sample_emoticonPacksLiveData.value = data
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "like_emoticon_data") {
            Log.d("LikeEmoticonViewModel", "Really Changed")
            loadEmoticonDataFromPreferences()
        }
    }

    override fun onCleared() {
        super.onCleared()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }
}
