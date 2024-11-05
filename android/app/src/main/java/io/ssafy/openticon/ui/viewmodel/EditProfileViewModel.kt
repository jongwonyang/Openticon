package io.ssafy.openticon.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ssafy.openticon.data.model.MemberEntity
import io.ssafy.openticon.di.UserSession
import io.ssafy.openticon.domain.usecase.EditProfileUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    userSession: UserSession,
    private val editProfileUseCase: EditProfileUseCase
) : ViewModel() {
    val memberEntity: StateFlow<MemberEntity?> = userSession.memberEntity
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun editProfile() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading // 로딩 상태 설정

            val result = editProfileUseCase()
            result.onSuccess {
                _uiState.value = UiState.Success // 성공 상태 설정
            }.onFailure { exception ->
                _uiState.value = UiState.Error(exception) // 오류 상태 설정
            }
        }
    }

    sealed class UiState {
        object Loading : UiState()
        object Success : UiState()
        data class Error(val exception: Throwable) : UiState()
    }
}
