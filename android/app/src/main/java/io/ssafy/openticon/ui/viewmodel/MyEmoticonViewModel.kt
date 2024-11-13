package io.ssafy.openticon.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ssafy.openticon.data.model.Emoticon
import io.ssafy.openticon.data.model.EmoticonPackEntity
import io.ssafy.openticon.data.model.EmoticonPackWithEmotions
import io.ssafy.openticon.data.model.SampleEmoticonPack
import io.ssafy.openticon.data.repository.EmoticonPackRepository
import io.ssafy.openticon.domain.usecase.DeleteAllOrderUseCase
import io.ssafy.openticon.domain.usecase.DeleteOrderUseCase
import io.ssafy.openticon.domain.usecase.DeletePackUseCase
import io.ssafy.openticon.domain.usecase.GetEmoticonPacksUseCase
import io.ssafy.openticon.domain.usecase.GetEmoticonPacksWithEmotionsUseCase
import io.ssafy.openticon.domain.usecase.InsertOrderUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.PrivateKey
import javax.inject.Inject

@HiltViewModel
class MyEmoticonViewModel @Inject constructor(
    private val getEmoticonPacksUseCase: GetEmoticonPacksUseCase,
    private val deletePackUseCase: DeletePackUseCase,
    private val deleteOrderUseCase: DeleteOrderUseCase,
    private val deleteAllOrderUseCase: DeleteAllOrderUseCase,
    private val insertOrderUseCase: InsertOrderUseCase
): ViewModel() {

    private val _Sample_emoticonPacks = MutableLiveData<List<EmoticonPackEntity>>()
    val sampleEmoticonPacks: LiveData<List<EmoticonPackEntity>> get() = _Sample_emoticonPacks

    val visibleSampleEmoticonPacks = MediatorLiveData<List<EmoticonPackEntity>>().apply {
        addSource(_Sample_emoticonPacks) { list ->
            value = list.filter { it.downloaded }
        }
    }

    val invisibleSampleEmoticonPacks = MediatorLiveData<List<EmoticonPackEntity>>().apply {
        addSource(_Sample_emoticonPacks) { list ->
            value = list.filter { !it.downloaded }
        }
    }

    init {
        loadEmoticonPacks()
    }

    fun loadEmoticonPacks() {
        viewModelScope.launch {
            getEmoticonPacksUseCase.execute().collect { emoticonPacks ->
                _Sample_emoticonPacks.value = emoticonPacks
            }
        }
    }

    fun changeVisible(emoticonPackEntity: EmoticonPackEntity) {
        val updatedList = _Sample_emoticonPacks.value?.map { pack ->
            if (pack.id == emoticonPackEntity.id) {
                pack.copy(downloaded = !pack.downloaded)
            } else {
                pack
            }
        } ?: emptyList()
        _Sample_emoticonPacks.value = updatedList

        viewModelScope.launch {
            updatedList.find { it.id == emoticonPackEntity.id }?.let { updatedPack ->
                getEmoticonPacksUseCase.updateEmoticonPack(updatedPack) // DB 업데이트 호출
                if(!updatedPack.downloaded){
                    viewModelScope.launch {
                        deletePackUseCase.invoke(updatedPack.id)
                        getEmoticonPacksUseCase.deleteEmoticons(updatedPack.id)
                        deleteOrderUseCase.invoke(updatedPack.id)
                    }
                }
            }
        }
    }

    // 리스트 전체를 업데이트하는 함수 추가
    fun updateEmoticonPacks(newList: List<EmoticonPackEntity>) {
        _Sample_emoticonPacks.value = newList
        viewModelScope.launch {
            deleteAllOrderUseCase.invoke()
            for (emoticonPack in _Sample_emoticonPacks.value!!) {
                withContext(Dispatchers.IO) {
                    // 1번 작업 완료 후 2번 작업 시작
                    insertOrderUseCase.invoke(emoticonPack.id)
                }
            }
        }
    }
}
