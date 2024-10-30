package io.ssafy.openticon.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.ssafy.openticon.data.model.EmoticonPack
import io.ssafy.openticon.data.repository.EmoticonPackRepository

class MyEmoticonViewModel: ViewModel() {

    private val repository = EmoticonPackRepository()
    private val _emoticonPacks = MutableLiveData<List<EmoticonPack>>()
    val emoticonPacks: LiveData<List<EmoticonPack>> get() = _emoticonPacks

    val visibleEmoticonPacks = MediatorLiveData<List<EmoticonPack>>().apply {
        addSource(_emoticonPacks) { list ->
            value = list.filter { it.isVisible }
        }
    }

    val invisibleEmoticonPacks = MediatorLiveData<List<EmoticonPack>>().apply {
        addSource(_emoticonPacks) { list ->
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
        _emoticonPacks.value = repository.getEmoticonPacks()
    }


    fun changeVisible(emoticonPack: EmoticonPack) {
        // 기존 리스트를 가져와서 수정한 리스트 생성
        val updatedList = _emoticonPacks.value?.map { pack ->
            if (pack.name == emoticonPack.name) {
                pack.copy(isVisible = !pack.isVisible) // isVisible 값만 반대로 변경
            } else {
                pack
            }
        } ?: emptyList()

        // _emoticonPacks LiveData 업데이트
        _emoticonPacks.value = updatedList
    }
}