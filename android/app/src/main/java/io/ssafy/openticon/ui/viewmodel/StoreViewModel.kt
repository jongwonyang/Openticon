package io.ssafy.openticon.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ssafy.openticon.di.UserSession
import io.ssafy.openticon.domain.model.SearchEmoticonPacksListItem
import io.ssafy.openticon.domain.usecase.SearchEmoticonPacksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StoreViewModel @Inject constructor(
    private val searchEmoticonPacksUseCase: SearchEmoticonPacksUseCase,
    private val userSession: UserSession
) : ViewModel() {
    private val _newEmoticonPack = MutableStateFlow(emptyList<SearchEmoticonPacksListItem>())
    private val _popularEmoticonPack = MutableStateFlow(emptyList<SearchEmoticonPacksListItem>())
    private val _tag1EmoticonPack = MutableStateFlow(emptyList<SearchEmoticonPacksListItem>())
    private val _tag2EmoticonPack = MutableStateFlow(emptyList<SearchEmoticonPacksListItem>())
    private val _tag3EmoticonPack = MutableStateFlow(emptyList<SearchEmoticonPacksListItem>())
    private val _isLoading = MutableStateFlow(false)

    val searchResult: StateFlow<List<SearchEmoticonPacksListItem>> = _newEmoticonPack
    val popularEmoticonPack: StateFlow<List<SearchEmoticonPacksListItem>> = _popularEmoticonPack
    val tag1EmoticonPack: StateFlow<List<SearchEmoticonPacksListItem>> = _tag1EmoticonPack
    val tag2EmoticonPack: StateFlow<List<SearchEmoticonPacksListItem>> = _tag2EmoticonPack
    val tag3EmoticonPack: StateFlow<List<SearchEmoticonPacksListItem>> = _tag3EmoticonPack
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _tagQuery1 = MutableStateFlow("알파카")
    private val _tagQuery2 = MutableStateFlow("레니콘")
    private val _tagQuery3 = MutableStateFlow("고양이")

    // Exposed state for tag queries
    val tagQuery1: StateFlow<String> get() = _tagQuery1
    val tagQuery2: StateFlow<String> get() = _tagQuery2
    val tagQuery3: StateFlow<String> get() = _tagQuery3

    init {
        loadEmoticonPack()
    }

    private fun loadEmoticonPack() {
        viewModelScope.launch {
            // 태그별 이모티콘 팩 로드하는 로직 추가
            try {
                val (newItems, _) = searchEmoticonPacksUseCase("title", "", "most", 9, 0)
                val updatedNewItems = mutableListOf<SearchEmoticonPacksListItem>()

                // Add empty items to the beginning and end of the list
                updatedNewItems.add(
                    SearchEmoticonPacksListItem(
                        0,
                        "",
                        "",
                        "https://www.htmlcsscolor.com/preview/128x128/FAF9FD.png",
                        ""
                    )
                )
                updatedNewItems.addAll(newItems)
                updatedNewItems.add(
                    SearchEmoticonPacksListItem(
                        0,
                        "",
                        "",
                        "https://www.htmlcsscolor.com/preview/128x128/FAF9FD.png",
                        ""
                    )
                )

                _newEmoticonPack.value = updatedNewItems

                val (popularItems, _) = searchEmoticonPacksUseCase("title", "", "most", 9, 0)
                _popularEmoticonPack.value = popularItems

                val (tag1Items, _) = searchEmoticonPacksUseCase(
                    "tag",
                    _tagQuery1.value,
                    "most",
                    10,
                    0
                )
                _tag1EmoticonPack.value = tag1Items

                val (tag2Items, _) = searchEmoticonPacksUseCase(
                    "tag",
                    _tagQuery2.value,
                    "most",
                    10,
                    0
                )
                _tag2EmoticonPack.value = tag2Items

                val (tag3Items, _) = searchEmoticonPacksUseCase(
                    "tag",
                    _tagQuery3.value,
                    "most",
                    10,
                    0
                )
                _tag3EmoticonPack.value = tag3Items
            } catch (e: Exception) {
                Log.e("StoreViewModel", "Error loading tag emoticon packs", e)
            }
        }
    }


}