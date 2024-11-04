package io.ssafy.openticon.di

import io.ssafy.openticon.data.model.MemberEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSession @Inject constructor() {

    // 사용자 로그인 상태 및 정보 저장
    private val _memberEntity = MutableStateFlow<MemberEntity?>(null)
    val memberEntity = _memberEntity.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    // 로그인 메서드
    fun login(memberEntity: MemberEntity) {
        _memberEntity.value = memberEntity
        _isLoggedIn.value = true
    }

    // 로그아웃 메서드
    fun logout() {
        _memberEntity.value = null
        _isLoggedIn.value = false
    }

}