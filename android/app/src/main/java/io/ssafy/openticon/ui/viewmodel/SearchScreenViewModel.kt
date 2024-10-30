package io.ssafy.openticon.ui.viewmodel

import androidx.lifecycle.ViewModel
import io.ssafy.openticon.domain.model.EmoticonPack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SearchScreenViewModel : ViewModel() {

    private val _key = MutableStateFlow("제목")
    private val _query = MutableStateFlow("")
    private val _searchResult = MutableStateFlow(emptyList<EmoticonPack>())

    val key: StateFlow<String> = _key
    val query: StateFlow<String> = _query

    fun onQueryChange(value: String) {
        _query.value = value
    }

    fun onKeyChange(value: String) {
        _key.value = value
    }
}