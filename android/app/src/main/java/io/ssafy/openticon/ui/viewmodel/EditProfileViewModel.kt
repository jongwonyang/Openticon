package io.ssafy.openticon.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ssafy.openticon.data.model.MemberEntity
import io.ssafy.openticon.di.UserSession
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userSession: UserSession,
) : ViewModel(){

    val isLoggedIn: StateFlow<Boolean> = userSession.isLoggedIn
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val memberEntity: StateFlow<MemberEntity?> = userSession.memberEntity
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)


}