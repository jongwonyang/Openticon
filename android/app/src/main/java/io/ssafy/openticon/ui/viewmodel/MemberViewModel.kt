package io.ssafy.openticon.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ssafy.openticon.data.local.TokenDataSource
import io.ssafy.openticon.data.model.MemberEntity
import io.ssafy.openticon.data.model.PurchasePointRequestDto
import io.ssafy.openticon.di.UserSession
import io.ssafy.openticon.domain.usecase.DeleteMemberUseCase
import io.ssafy.openticon.domain.usecase.GetMemberInfoUseCase
import io.ssafy.openticon.domain.usecase.PurchasePointUseCase
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
    private val deleteMemberUseCase: DeleteMemberUseCase,
    private val syncPurchasedPacksUseCase: SyncPurchasedPacksUseCase,
    private val purchasePointUseCase: PurchasePointUseCase
): ViewModel() {
    // 로그인 상태
    val isLoggedIn: StateFlow<Boolean> = userSession.isLoggedIn
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val memberEntity: StateFlow<MemberEntity?> = userSession.memberEntity
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)


    private val _uiState = MutableStateFlow<UiState<MemberEntity>>(UiState.Loading)
    val uiState: StateFlow<UiState<MemberEntity>> = _uiState

    private val _purchaseLoading = MutableStateFlow<Boolean>(false)
    val purchaseLoading: StateFlow<Boolean> = _purchaseLoading

    private val _purchaseSuccess = MutableStateFlow<Boolean?>(null)
    val purchaseSuccess: StateFlow<Boolean?> = _purchaseSuccess



    suspend fun deleteMember(): Result<Unit> {
        return try {
            val result = deleteMemberUseCase()
            if (result.isSuccess) {
                userSession.logout()
                Log.i("DeleteMember", "회원 삭제 성공")
                Result.success(Unit)
            } else {
                val exception = result.exceptionOrNull()
                Log.w("DeleteMember", "회원 삭제 실패: ${exception?.message ?: "알 수 없는 오류"}")
                Result.failure(exception ?: Exception("알 수 없는 오류"))
            }
        } catch (e: Exception) {
            Log.e("DeleteMember", "회원 삭제 중 오류 발생: ${e.message}")
            Result.failure(e)
        }
    }
    suspend fun logout() {
        userSession.logout()
        val tokenDataSource = TokenDataSource
        tokenDataSource.clearToken()
    }
    // 회원 정보 수정하는 함수
    fun fetchMemberInfo() {
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

    fun purchasePoint(amount: Int, impUid: String) {
        viewModelScope.launch {
            _purchaseLoading.value = true
            val result = purchasePointUseCase(amount, impUid)
            if (result.isSuccess) {
                _purchaseSuccess.value = true
                Log.i("PointRecharge", "포인트 충전 성공")
                fetchMemberInfo()
            } else {
                _purchaseSuccess.value = false
                Log.w("PointRecharge", "포인트 충전 실패")
            }
            _purchaseLoading.value = false
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
