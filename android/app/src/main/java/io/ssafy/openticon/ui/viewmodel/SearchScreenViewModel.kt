package io.ssafy.openticon.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ssafy.openticon.domain.model.SearchEmoticonPacksListItem
import io.ssafy.openticon.domain.usecase.SearchEmoticonPacksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val searchEmoticonPacksUseCase: SearchEmoticonPacksUseCase
) : ViewModel() {

    private val _searchKey = MutableStateFlow("제목")
    private val _searchText = MutableStateFlow("")
    private val _searchResult = MutableStateFlow(emptyList<SearchEmoticonPacksListItem>())
    private val _isLoading = MutableStateFlow(false)

    val searchKey: StateFlow<String> = _searchKey
    val searchText: StateFlow<String> = _searchText
    val searchResult: StateFlow<List<SearchEmoticonPacksListItem>> = _searchResult
    val isLoading: StateFlow<Boolean> = _isLoading

    private var page = 0
    private var pageSize = 20
    private var lastPageReached = false

    fun onSearchTextChange(value: String) {
        _searchText.value = value
    }

    fun onSearchKeyChange(value: String) {
        _searchKey.value = value
    }

    fun search() {
        page = 0
        lastPageReached = false
        _searchResult.value = emptyList()

        loadMoreSearchResult()
    }

    fun loadMoreSearchResult() {
        if (lastPageReached || _isLoading.value) return
        viewModelScope.launch {
            _isLoading.value = true
            val (newItems, isLast) = searchEmoticonPacksUseCase(
                searchKey = _searchKey.value,
                searchText = _searchText.value,
                page = page,
                size = pageSize
            )
            _searchResult.value += newItems
            lastPageReached = isLast
            page++
            _isLoading.value = false
        }
    }
}