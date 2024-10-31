package io.ssafy.openticon.ui.viewmodel

import androidx.lifecycle.ViewModel
import io.ssafy.openticon.domain.model.EmoticonPack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SearchScreenViewModel : ViewModel() {

    private val _searchKey = MutableStateFlow("제목")
    private val _searchText = MutableStateFlow("")
    private val _searchResult = MutableStateFlow(emptyList<EmoticonPack>())

    val searchKey: StateFlow<String> = _searchKey
    val searchText: StateFlow<String> = _searchText

    fun onSearchTextChange(value: String) {
        _searchText.value = value
    }

    fun onSearchKeyChange(value: String) {
        _searchKey.value = value
    }
}