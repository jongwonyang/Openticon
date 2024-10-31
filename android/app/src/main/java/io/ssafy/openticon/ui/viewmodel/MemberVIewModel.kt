package io.ssafy.openticon.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ssafy.openticon.data.model.MemberEntity
import io.ssafy.openticon.di.ApiServiceSingleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MemberViewModel() : ViewModel() {
    private val memberApiService = ApiServiceSingleton.instance
    // 로그인 상태
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _memberEntity = MutableStateFlow<MemberEntity?>(null)
    val memberEntity: StateFlow<MemberEntity?> = _memberEntity

    // 회원 정보 수정하는 함수
    suspend fun fetchMemberInfo() {
        try {
            Log.d("try", "로그인 시도")
            val response = memberApiService.getMemberInfo()
            if (response.stateCode == 200) {
                Log.d("success", "로그인 성공")
                _isLoggedIn.value = true
                _memberEntity.value = response.memberEntity
            } else {
                _isLoggedIn.value = false
                _memberEntity.value = null
            }
        } catch (e: Exception) {
            _isLoggedIn.value = false
            _memberEntity.value = null
        }
    }


}
