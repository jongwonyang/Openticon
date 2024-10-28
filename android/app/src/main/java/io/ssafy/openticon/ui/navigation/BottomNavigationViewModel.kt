package io.ssafy.openticon.ui.navigation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class BottomNavigationViewModel @Inject constructor() : ViewModel() {
    private val _selectedItemId = MutableStateFlow(0)
    val selectedItem: StateFlow<Int> = _selectedItemId

    fun onItemSelected(index: Int) {
        _selectedItemId.value = index
    }
}