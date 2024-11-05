package io.ssafy.openticon.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ssafy.openticon.data.model.EmoticonPackWithEmotions
import io.ssafy.openticon.data.model.SampleEmoticonPack
import io.ssafy.openticon.data.repository.EmoticonPackRepository
import io.ssafy.openticon.domain.usecase.GetEmoticonPacksWithEmotionsUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmoticonViewModel @Inject constructor(
    private val getEmoticonPacksWithEmotionsUseCase: GetEmoticonPacksWithEmotionsUseCase
) : ViewModel() {

    private val _Sample_emoticonPacks = MutableLiveData<List<EmoticonPackWithEmotions>>()
    val sampleEmoticonPacks: LiveData<List<EmoticonPackWithEmotions>> get() = _Sample_emoticonPacks

    init {
        loadEmoticonPacks()
    }

    private fun loadEmoticonPacks() {
        viewModelScope.launch {
            getEmoticonPacksWithEmotionsUseCase.execute().collect { emoticonPacks ->
                _Sample_emoticonPacks.value = emoticonPacks
            }
        }
    }
}
