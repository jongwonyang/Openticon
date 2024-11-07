package io.ssafy.openticon.ui.viewmodel

import android.util.Log
import android.view.View
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
class EmoticonAllViewModel @Inject constructor(
    private val searchEmoticonPacksUseCase: SearchEmoticonPacksUseCase,
    private val userSession: UserSession
) : ViewModel(){
    private val _emoticonPack = MutableStateFlow(emptyList<SearchEmoticonPacksListItem>());
    val emoticonPack: StateFlow<List<SearchEmoticonPacksListItem>> = _emoticonPack;
    private val _lastPageReached = MutableStateFlow(false) // 마지막 페이지 여부 추적
    val lastPageReached: StateFlow<Boolean> = _lastPageReached
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var currentPage = 0
    private var pageSize = 10

    fun resetState() {
        _emoticonPack.value = emptyList()
        _lastPageReached.value = false
        currentPage = 0
    }

    fun fetchEmoticonPack(type: String, tag: String?, isLoadMore: Boolean = false) {
        if (_lastPageReached.value || _isLoading.value) return
        _isLoading.value = true
        viewModelScope.launch {
            try {
                if (!isLoadMore) resetState() // 새로운 데이터 로드 시 초기화

                val sortType = when (type) {
                    "popular" -> "most"
                    else -> "new"
                }
                val searchKey = if (type == "tag") "tag" else "title"
                val searchTag = tag ?: ""

                val (items, isLast) = searchEmoticonPacksUseCase(
                    searchKey, searchTag, sortType, pageSize, currentPage
                )

                _lastPageReached.value = isLast
                _emoticonPack.value = if (isLoadMore) {
                    _emoticonPack.value + items
                } else {
                    items
                }

                if (items.isNotEmpty()) {
                    currentPage += 1 // 페이지 번호 증가
                }

                Log.d("EmoticonAllViewModel", "Fetched ${items.size} items")
                _isLoading.value = false
            } catch (e: Exception) {
                // 에러 처리
                e.printStackTrace()
            }
        }
    }

    sealed class UiState {
        object Loading : UiState()
        object Success : UiState()
        data class Error(val exception: Throwable) : UiState()
    }
}