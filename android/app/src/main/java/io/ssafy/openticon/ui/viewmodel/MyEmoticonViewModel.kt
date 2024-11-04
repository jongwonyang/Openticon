package io.ssafy.openticon.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.ssafy.openticon.data.model.SampleEmoticonPack
import io.ssafy.openticon.data.repository.EmoticonPackRepository

class MyEmoticonViewModel : ViewModel() {

    private val repository = EmoticonPackRepository()
    private val _Sample_emoticonPacks = MutableLiveData<List<SampleEmoticonPack>>()
    val sampleEmoticonPacks: LiveData<List<SampleEmoticonPack>> get() = _Sample_emoticonPacks

    val visibleSampleEmoticonPacks = MediatorLiveData<List<SampleEmoticonPack>>().apply {
        addSource(_Sample_emoticonPacks) { list ->
            value = list.filter { it.isVisible }
        }
    }

    val invisibleSampleEmoticonPacks = MediatorLiveData<List<SampleEmoticonPack>>().apply {
        addSource(_Sample_emoticonPacks) { list ->
            value = list.filter { !it.isVisible }
        }
    }

    init {
        loadEmoticonPacks()
    }

    private fun loadEmoticonPacks() {
        _Sample_emoticonPacks.value = repository.getEmoticonPacks()
    }

    fun changeVisible(sampleEmoticonPack: SampleEmoticonPack) {
        val updatedList = _Sample_emoticonPacks.value?.map { pack ->
            if (pack.name == sampleEmoticonPack.name) {
                pack.copy(isVisible = !pack.isVisible)
            } else {
                pack
            }
        } ?: emptyList()
        _Sample_emoticonPacks.value = updatedList
    }

    // 리스트 전체를 업데이트하는 함수 추가
    fun updateEmoticonPacks(newList: List<SampleEmoticonPack>) {
        _Sample_emoticonPacks.value = newList
    }
}
