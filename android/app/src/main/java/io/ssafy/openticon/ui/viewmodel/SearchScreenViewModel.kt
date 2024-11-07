package io.ssafy.openticon.ui.viewmodel

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ssafy.openticon.domain.model.SearchEmoticonPacksListItem
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
class SearchScreenViewModel @Inject constructor(
    private val searchEmoticonPacksUseCase: SearchEmoticonPacksUseCase,
    private val searchEmoticonPacksByImageUseCase: SearchEmoticonPacksByImageUseCase
) : ViewModel() {
    private val _searchKey = MutableStateFlow(SearchKey.Title)
    private val _searchText = MutableStateFlow("")
    private val _searchResult = MutableStateFlow(emptyList<SearchEmoticonPacksListItem>())
    private val _isLoading = MutableStateFlow(false)
    private val _selectedImageUri = MutableStateFlow<Uri?>(null)

    val searchKey: StateFlow<SearchKey> = _searchKey
    val searchText: StateFlow<String> = _searchText
    val searchResult: StateFlow<List<SearchEmoticonPacksListItem>> = _searchResult
    val isLoading: StateFlow<Boolean> = _isLoading
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri

    private var page = 0
    private var pageSize = 20
    private var lastPageReached = false

    fun clearSelectedImage() {
        _selectedImageUri.value = null
    }

    fun onSearchTextChange(value: String) {
        _searchText.value = value
    }

    fun onSearchKeyChange(value: SearchKey) {
        _searchKey.value = value
    }

    fun search() {
        page = 0
        lastPageReached = false
        _searchResult.value = emptyList()

        loadMoreSearchResult()
    }

    fun loadMoreSearchResult() {
        if (lastPageReached || _isLoading.value) return
        Log.d("SEARCH", "viewModelScope.launch")
        viewModelScope.launch {
            _isLoading.value = true
            Log.d("SEARCH", "_searchKey.value: ${_searchKey.value}")
            Log.d("SEARCH", "_searchText.value: ${_searchText.value}")
            Log.d("SEARCH", "page: $page")
            Log.d("SEARCH", "pageSize: $pageSize")
            val (newItems, isLast) = searchEmoticonPacksUseCase(
                searchKey = _searchKey.value.key,
                searchText = _searchText.value,
                page = page,
                size = pageSize
            )
            Log.d("SEARCH", "newItems: $newItems")
            _searchResult.value += newItems
            lastPageReached = isLast
            page++
            _isLoading.value = false
            Log.d("SEARCH", "searchResult.value: ${searchResult.value}")
        }
    }

    //image Search
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
        page = 0
        lastPageReached = false
        _searchResult.value = emptyList()

        loadMoreImageSearchResult(contentResolver)
    }


    fun loadMoreImageSearchResult(contentResolver: ContentResolver) {
        if (lastPageReached || _isLoading.value) return
        Log.d("SEARCH", "viewModelScope.launch")
        viewModelScope.launch {
            _isLoading.value = true
            Log.d("SEARCH", "_searchKey.value: ${_searchKey.value}")
            Log.d("SEARCH", "_searchText.value: ${_searchText.value}")
            Log.d("SEARCH", "page: $page")
            Log.d("SEARCH", "pageSize: $pageSize")

            val currentUri = _selectedImageUri.value
            if (currentUri != null) {
                try {
                    val imagePart = currentUri.let { uriToMultipartBody(contentResolver, it) }

                    val (newItems, isLast) = searchEmoticonPacksByImageUseCase(
                        size = pageSize,
                        page = page,
                        image = imagePart
                    )

                    Log.d("SEARCH", "newItems: $newItems")
                    _searchResult.value += newItems
                    lastPageReached = isLast
                    page++
                    _isLoading.value = false
                    Log.d("SEARCH", "searchResult.value: ${searchResult.value}")
                } catch (e: Exception) {
                    Log.e("ImageUpload", "Error uploading image", e)
                    _isLoading.value = false
                }
            } else {
                Log.e("e", "!!")
            }
        }
    }
}

enum class SearchKey(
    val key: String,
    val displayName: String
) {
    Title("title", "제목"),
    Author("author", "작가"),
    Tag("tag", "태그")
}

enum class Sort(
    val value: String,
    val displayName: String
) {
    New("new", "최신순"),
    Most("most", "인기순")
}
