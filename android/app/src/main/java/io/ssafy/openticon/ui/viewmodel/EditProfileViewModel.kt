package io.ssafy.openticon.ui.viewmodel

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ssafy.openticon.data.model.MemberEntity
import io.ssafy.openticon.di.UserSession
import io.ssafy.openticon.domain.usecase.EditProfileUseCase
import io.ssafy.openticon.domain.usecase.GetMemberInfoUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import io.ssafy.openticon.domain.usecase.DuplicateCheckUseCase

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userSession: UserSession,
    private val getMemberInfoUseCase: GetMemberInfoUseCase,
    private val editProfileUseCase: EditProfileUseCase,
    private val duplicateCheckUseCase : DuplicateCheckUseCase
) : ViewModel() {
    val memberEntity: StateFlow<MemberEntity?> = userSession.memberEntity
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri.asStateFlow()

    private val _isDuplicate = MutableStateFlow<Boolean>(false)
    val isDuplicate: StateFlow<Boolean> = _isDuplicate

    fun editProfile(contentResolver: ContentResolver, nickname: String, bio: String) {
        viewModelScope.launch {
            val currentUri = _selectedImageUri.value
            Log.d("EditProfileViewModel", "Selected Image URI: $currentUri")
            val result: Result<*>
            val imagePart = if (currentUri != null) {
                uriToMultipartBody(contentResolver, currentUri)
            } else {
                null
            }
            _uiState.value = UiState.Loading // 로딩 상태 설정

            result = editProfileUseCase(nickname = nickname, bio = bio, profileImage = imagePart)

            result.onSuccess {
                _uiState.value = UiState.Success // 성공 상태 설정
                updateMemberEntity()
            }.onFailure { exception ->
                Log.d("editProfile", exception.toString())
                _uiState.value = UiState.Error(exception) // 오류 상태 설정
            }
        }
    }

    fun setImageUri(uri: Uri?) {
        viewModelScope.launch {
            _selectedImageUri.emit(uri)
        }
    }

    // Uri를 MultipartBody.Part로 변환하는 함수
    private fun uriToMultipartBody(contentResolver: ContentResolver, imageUri: Uri): MultipartBody.Part {
        val file = uriToFile(contentResolver, imageUri)
        val mimeType = contentResolver.getType(imageUri) ?: "application/octet-stream"
        val requestBody = file.asRequestBody(mimeType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("profile_img", file.name, requestBody)
    }

    // Uri를 File로 변환하는 함수
    private fun uriToFile(contentResolver: ContentResolver, uri: Uri): File {
        val inputStream = contentResolver.openInputStream(uri) ?: throw IllegalArgumentException("Could not open URI")
        val file = File.createTempFile("upload_temp_file", ".png") // 캐시 디렉터리에 임시 파일 생성
        inputStream.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return file
    }
        fun updateMemberEntity() {
            viewModelScope.launch {
                try {
                    val response = getMemberInfoUseCase().getOrThrow()
                    val result = response.data
                    val status = response.status

                    if (status == 200) {
                        if (result != null) {
                            userSession.login(result)
                        }
                    } else if (status == 401 || status == 403) {
                        Log.w("FetchMemberInfo", "Unauthorized or Forbidden response, status: $status")
                    }
                } catch (e: Exception) {
                }
            }
        }

    fun checkDuplicateNickname(newNickname: String) {
        viewModelScope.launch {
            try {
                val response = duplicateCheckUseCase(newNickname)
                _isDuplicate.value = response.getOrNull() == true
            } catch (e: Exception) {
                Log.e("DuplicateCheckViewModel", "Error checking nickname: ${e.message}")
                _isDuplicate.value = false
            }
        }
    }


    sealed class UiState {
        object Loading : UiState()
        object Success : UiState()
        data class Error(val exception: Throwable) : UiState()
    }
}
