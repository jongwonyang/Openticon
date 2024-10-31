package io.ssafy.openticon.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.ssafy.openticon.data.model.EmoticonPack
import io.ssafy.openticon.data.repository.EmoticonPackRepository

class EmoticonViewModel : ViewModel() {

    private val repository = EmoticonPackRepository()

    private val _emoticonPacks = MutableLiveData<List<EmoticonPack>>()
    val emoticonPacks: LiveData<List<EmoticonPack>> get() = _emoticonPacks

    init {
        loadEmoticonPacks()
    }

    private fun loadEmoticonPacks() {
        _emoticonPacks.value = repository.getEmoticonPacks()
    }
}
