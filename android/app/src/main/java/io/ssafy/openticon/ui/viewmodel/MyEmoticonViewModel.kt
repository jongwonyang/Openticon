package io.ssafy.openticon.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.ssafy.openticon.data.model.SampleEmoticonPack
import io.ssafy.openticon.data.repository.EmoticonPackRepository

class MyEmoticonViewModel: ViewModel() {

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

//    private val _visiblePacks = MutableLiveData<List<EmoticonPack>>()
//    private val _invisiblePacks = MutableLiveData<List<EmoticonPack>>()
//
//    val visiblePacks: LiveData<List<EmoticonPack>> get() = _visiblePacks
//    val invisiblePacks: LiveData<List<EmoticonPack>> get() = _invisiblePacks

    init {
        loadEmoticonPacks()
    }

    private fun loadEmoticonPacks() {
       // _visiblePacks.value = repository.getEmoticonPacks().filter { it.isVisible }
       // _invisiblePacks.value = repository.getEmoticonPacks().filter { !it.isVisible }
        _Sample_emoticonPacks.value = repository.getEmoticonPacks()
    }


    fun changeVisible(sampleEmoticonPack: SampleEmoticonPack) {
        // 기존 리스트를 가져와서 수정한 리스트 생성
        val updatedList = _Sample_emoticonPacks.value?.map { pack ->
            if (pack.name == sampleEmoticonPack.name) {
                pack.copy(isVisible = !pack.isVisible) // isVisible 값만 반대로 변경
            } else {
                pack
            }
        } ?: emptyList()

        // _emoticonPacks LiveData 업데이트
        _Sample_emoticonPacks.value = updatedList
    }
}