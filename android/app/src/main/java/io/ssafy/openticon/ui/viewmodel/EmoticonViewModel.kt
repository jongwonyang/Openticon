package io.ssafy.openticon.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.ssafy.openticon.data.model.SampleEmoticonPack
import io.ssafy.openticon.data.repository.EmoticonPackRepository

class EmoticonViewModel : ViewModel() {

    private val repository = EmoticonPackRepository()

    private val _Sample_emoticonPacks = MutableLiveData<List<SampleEmoticonPack>>()
    val sampleEmoticonPacks: LiveData<List<SampleEmoticonPack>> get() = _Sample_emoticonPacks

    init {
        loadEmoticonPacks()
    }

    private fun loadEmoticonPacks() {
        _Sample_emoticonPacks.value = repository.getEmoticonPacks()
    }
}
