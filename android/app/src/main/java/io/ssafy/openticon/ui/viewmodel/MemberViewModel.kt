package io.ssafy.openticon.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ssafy.openticon.data.local.TokenDataSource
import io.ssafy.openticon.data.model.MemberEntity
import io.ssafy.openticon.di.UserSession
import io.ssafy.openticon.domain.usecase.GetMemberInfoUseCase
import io.ssafy.openticon.domain.usecase.SyncPurchasedPacksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemberViewModel @Inject constructor(
    private val getMemberInfoUseCase: GetMemberInfoUseCase,
    val baseUrl: String,
    private val userSession: UserSession,
    private val syncPurchasedPacksUseCase: SyncPurchasedPacksUseCase
): ViewModel() {

    // 로그인 상태
    val isLoggedIn: StateFlow<Boolean> = userSession.isLoggedIn
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val memberEntity: StateFlow<MemberEntity?> = userSession.memberEntity
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)


    private val _uiState = MutableStateFlow<UiState<MemberEntity>>(UiState.Loading)

    val uiState: StateFlow<UiState<MemberEntity>> = _uiState


    // 회원 정보 수정하는 함수
    suspend fun fetchMemberInfo() {
        val tokenDataSource = TokenDataSource
        val token = tokenDataSource.token.firstOrNull()

        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = getMemberInfoUseCase().getOrThrow()
                val result = response.data
                val status = response.status

                if (status == 200) {
                    if (result != null) {
                        userSession.login(result)
                    }

                    // 구매 목록 동기화
                    syncPurchasedPacksUseCase()

                    _uiState.value = UiState.Success<MemberEntity>(result)
                } else if (status == 401 || status == 403) {Log.w("FetchMemberInfo", "Unauthorized or Forbidden response, status: $status")
                    _uiState.value = UiState.UnAuth
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e)

            }
        }
    }

    suspend fun syncPurchasedList() {

    }


    sealed class UiState<out T> {
        data object Loading : UiState<Nothing>()
        data class Success<T>(val data: MemberEntity?) : UiState<T>()
        data class Error(val exception: Throwable) : UiState<Nothing>()
        data object UnAuth : UiState<Nothing>()
    }
}
