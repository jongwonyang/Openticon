package io.ssafy.openticon.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ssafy.openticon.data.model.MemberEntity
import io.ssafy.openticon.domain.model.EmoticonPack
import io.ssafy.openticon.domain.model.SearchEmoticonPacksListItem
import io.ssafy.openticon.domain.usecase.WriterEmoticonPackUsecase
import io.ssafy.openticon.domain.usecase.WriterInfoUsecase
import io.ssafy.openticon.ui.viewmodel.MemberViewModel.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WriterViewModel @Inject constructor(
    private val writerEmoticonPackUsecase : WriterEmoticonPackUsecase,
    private val writerInfoUsecase : WriterInfoUsecase,
) : ViewModel(){
    private val _searchResult = MutableStateFlow(emptyList<SearchEmoticonPacksListItem>())
    private val _isLoading = MutableStateFlow(false)
    private val _lastPageReached = MutableStateFlow(false)
    private val _memberEntity = MutableStateFlow<MemberEntity?>(null)
    private val _uiState = MutableStateFlow<UiState<MemberEntity>>(UiState.Loading)
    val uiState: StateFlow<UiState<MemberEntity>> = _uiState

    val searchResult: StateFlow<List<SearchEmoticonPacksListItem>> = _searchResult
    val isLoading: StateFlow<Boolean> = _isLoading
    val lastPageReached: StateFlow<Boolean> = _lastPageReached
    val memberEntity: StateFlow<MemberEntity?> = _memberEntity

    fun resetState() {
        _searchResult.value = emptyList()
        _lastPageReached.value = false
        page = 0
    }
    private var page = 0
    private var pageSize = 20
    suspend fun loadMoreSearchResult(nickName : String) {
        if (_lastPageReached.value || _isLoading.value) return
        _isLoading.value = true
        val (newItems, isLast) = writerEmoticonPackUsecase(
            searchText = nickName,
            page = page,
            size = pageSize,
        )
        _searchResult.value += newItems
        _lastPageReached.value = isLast
        page++
        _isLoading.value = false
    }

    fun fetchWriterInfo(nickname: String) {
        _uiState.value = UiState.Loading // WriterViewModel에서 정의한 UiState를 사용
        viewModelScope.launch {
            try {
                val response = writerInfoUsecase(nickname).getOrThrow()
                val result = response.data
                val status = response.status
                if (status == 200 && result != null) {
                    Log.d("FetchWriterInfo", "Response: $result")
                    _uiState.value = UiState.Success(result)
                    _memberEntity.value = result
                } else {
                    Log.w("FetchWriterInfo", "Failed with status code: $status")
                    _uiState.value = UiState.Error(
                        Exception("Failed to fetch writer info. Status code: $status")
                    )
                }
            } catch (e: Exception) {
                Log.e("FetchWriterInfo", "Error fetching writer info: ${e.message}")
                _uiState.value = UiState.Error(e)
            }
        }
    }

    sealed class UiState<out T> {
        object Loading : UiState<Nothing>()
        data class Success<T>(val data: T?) : UiState<T>()
        data class Error(val exception: Throwable) : UiState<Nothing>()
    }
}