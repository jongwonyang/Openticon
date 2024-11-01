package io.ssafy.openticon.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ssafy.openticon.domain.model.EmoticonPackDetail
import io.ssafy.openticon.domain.usecase.GetPublicPackDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmoticonPackDetailScreenViewModel @Inject constructor(
    private val getPublicPackDetailUseCase: GetPublicPackDetailUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<EmoticonPackDetail>>(UiState.Loading)
    val uiState: StateFlow<UiState<EmoticonPackDetail>> = _uiState

    fun fetchEmoticonPackDetail(emoticonPackId: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = getPublicPackDetailUseCase(emoticonPackId)
            if (result.isSuccess) {
                _uiState.value = UiState.Success(result.getOrThrow())
            } else {
                _uiState.value = UiState.Error(result.exceptionOrNull() ?: Exception("Unknown error"))
            }
        }
    }

    sealed class UiState<out T> {
        data object Loading : UiState<Nothing>()
        data class Success<T>(val data: T) : UiState<T>()
        data class Error(val exception: Throwable) : UiState<Nothing>()
    }
}
