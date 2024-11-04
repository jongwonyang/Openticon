package io.ssafy.openticon.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ssafy.openticon.data.local.TokenDataSource
import io.ssafy.openticon.di.UserSession
import io.ssafy.openticon.domain.usecase.GetMemberInfoUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userSession: UserSession,
    private val getMemberInfoUseCase: GetMemberInfoUseCase
) : ViewModel() {

    // 로그인 상태
    val isLoggedIn: StateFlow<Boolean> = userSession.isLoggedIn
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        fetchMemberInfo()
    }

    // 회원 정보 수정하는 함수
    private fun fetchMemberInfo() {
        viewModelScope.launch {
            try {
                Log.d("MainViewModel", "Fetching member info...")
                val token = TokenDataSource.token.firstOrNull()
                if (token.isNullOrEmpty()) {
                    Log.w("MainViewModel", "Token is null or empty.")
                    return@launch
                }

                val response = getMemberInfoUseCase().getOrThrow()
                val result = response.data
                val status = response.status

                Log.d("MainViewModel", "Response status: $status")
                if (status == 200 && result != null) {
                    Log.d("MainViewModel", "Member info fetched successfully.")
                    userSession.login(result)
                } else if (status == 401 || status == 403) {
                    Log.w("MainViewModel", "Unauthorized or forbidden response.")
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error fetching member info", e)
            }
        }
    }
}
