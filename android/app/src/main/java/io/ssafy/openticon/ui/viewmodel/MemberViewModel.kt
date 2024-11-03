package io.ssafy.openticon.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ssafy.openticon.data.model.MemberEntity
import io.ssafy.openticon.domain.usecase.GetMemberInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemberViewModel @Inject constructor(
    private val getMemberInfoUseCase: GetMemberInfoUseCase,
    val baseUrl: String
): ViewModel() {
    // 로그인 상태
    private val _memberEntity = MutableStateFlow<MemberEntity?>(null)
    private val _uiState = MutableStateFlow<UiState<MemberEntity>>(UiState.Loading)
    private val _isLoggedIn = MutableStateFlow(false)

    val uiState: StateFlow<UiState<MemberEntity>> = _uiState
    val memberEntity: StateFlow<MemberEntity?> = _memberEntity
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    // 회원 정보 수정하는 함수
    suspend fun fetchMemberInfo() {
        _uiState.value = UiState.Loading
        try {
            viewModelScope.launch {
                val response = getMemberInfoUseCase().getOrThrow()
                val result = response.data
                val status = response.status
                if(status == 200) {
                    _memberEntity.value = result
                    _uiState.value = UiState.Success<MemberEntity>(result)
                    _isLoggedIn.value = true
                    Log.d("isLoggedin2", _isLoggedIn.value.toString())
                } else if(status == 401 || status == 403) {
                    _uiState.value = UiState.UnAuth
                }
            }
        } catch (e: Exception) {
            _uiState.value = UiState.Error(e)
        }
    }

    sealed class UiState<out T> {
        data object Loading : UiState<Nothing>()
        data class Success<T>(val data: MemberEntity?) : UiState<T>()
        data class Error(val exception: Throwable) : UiState<Nothing>()
        data object UnAuth : UiState<Nothing>()
    }
}
