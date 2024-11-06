package io.ssafy.openticon.ui.viewmodel

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.net.Uri
import android.net.http.HttpException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ssafy.openticon.data.repository.EmoticonPacksRepository
import io.ssafy.openticon.domain.usecase.SearchEmoticonPacksByImageUseCase
import io.ssafy.openticon.domain.usecase.SearchEmoticonPacksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ImageSearchViewModel @Inject constructor(
    private val searchEmoticonPacksUseCase: SearchEmoticonPacksUseCase,
    private val searchEmoticonPacksByImageUseCase: SearchEmoticonPacksByImageUseCase
) : ViewModel() {
    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri


    private val _searchResult = MutableStateFlow<String?>(null)
    val searchResult: StateFlow<String?> = _searchResult

    // 이미지 선택 처리
    fun setImageUri(uri: Uri?) {
        viewModelScope.launch {
            _selectedImageUri.emit(uri)
        }
    }

    // Uri를 MultipartBody.Part로 변환하는 함수
    fun uriToMultipartBody(contentResolver: ContentResolver, imageUri: Uri): MultipartBody.Part {
        val file = uriToFile(contentResolver, imageUri)
        val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData("image", file.name, requestBody)
    }

    // 기존의 uriToFile 함수
    private fun uriToFile(contentResolver: ContentResolver, uri: Uri): File {
        val inputStream = contentResolver.openInputStream(uri)
        val file = File.createTempFile("upload", ".jpg")
        inputStream.use { input ->
            file.outputStream().use { output ->
                input?.copyTo(output)
            }
        }
        return file
    }

    // 검색 기능
    fun performSearch(contentResolver: ContentResolver) {
        viewModelScope.launch {
            val currentUri = _selectedImageUri.value
            if (currentUri != null) {
                try {
                    // 이미지 파일을 MultipartBody.Part로 변환
                    val imagePart = uriToMultipartBody(contentResolver, currentUri)

                    // Retrofit을 사용하여 이미지 업로드 요청
                    val response = searchEmoticonPacksByImageUseCase.invoke(size = 20, page = 0, imagePart)

                    // 서버 응답 처리
                    _searchResult.emit("검색 성공: $response")

                } catch (@SuppressLint("NewApi") e: HttpException) {
                    _searchResult.emit("서버 요청 중 오류 발생: ${e.message}")
                } catch (e: Exception) {
                    Log.e("ImageUpload", "Error uploading image", e)
                    _searchResult.emit("이미지 업로드 중 오류가 발생했습니다.")
                }
            } else {
                _searchResult.emit("이미지가 선택되지 않았습니다.")
            }
        }
    }
}
