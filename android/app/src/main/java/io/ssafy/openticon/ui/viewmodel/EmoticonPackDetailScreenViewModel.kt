package io.ssafy.openticon.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ssafy.openticon.di.UserSession
import io.ssafy.openticon.domain.model.EmoticonPackDetail
import io.ssafy.openticon.domain.model.PurchaseInfo
import io.ssafy.openticon.domain.usecase.DownloadEmoticonPackUseCase
import io.ssafy.openticon.domain.usecase.GetDownloadPackInfoUseCase
import io.ssafy.openticon.domain.usecase.GetPublicPackDetailUseCase
import io.ssafy.openticon.domain.usecase.GetPurchaseInfoUseCase
import io.ssafy.openticon.domain.usecase.PurchaseEmoticonPackUseCase
import io.ssafy.openticon.domain.usecase.ReportPackUseCase
import io.ssafy.openticon.domain.usecase.UpdateDownloadedStatusUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmoticonPackDetailScreenViewModel @Inject constructor(
    private val getPublicPackDetailUseCase: GetPublicPackDetailUseCase,
    private val getPurchaseInfoUseCase: GetPurchaseInfoUseCase,
    private val purchaseEmoticonPackUseCase: PurchaseEmoticonPackUseCase,
    private val downloadEmoticonPackUseCase: DownloadEmoticonPackUseCase,
    private val getDownloadPackInfoUseCase: GetDownloadPackInfoUseCase,
    private val updateDownloadedStatusUseCase: UpdateDownloadedStatusUseCase,
    private val reportPackUseCase: ReportPackUseCase,
    userSession: UserSession
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<EmoticonPackDetail>>(UiState.Loading)
    val uiState: StateFlow<UiState<EmoticonPackDetail>> = _uiState

    private val _purchaseState = MutableStateFlow<UiState<PurchaseInfo>>(UiState.Loading)
    val purchaseState: StateFlow<UiState<PurchaseInfo>> = _purchaseState

    val isLoggedIn: StateFlow<Boolean> = userSession.isLoggedIn
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    private val _isDownloading = MutableStateFlow(false)
    val isDownloading: StateFlow<Boolean> = _isDownloading

    private val _totalEmoticonCount = MutableStateFlow(9999)
    val totalEmoticonCount: StateFlow<Int> = _totalEmoticonCount

    private val _downloadedEmoticonCount = MutableStateFlow(0)
    val downloadedEmoticonCount: StateFlow<Int> = _downloadedEmoticonCount

    fun fetchEmoticonPackDetail(emoticonPackUuid: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = getPublicPackDetailUseCase(emoticonPackUuid)
            if (result.isSuccess) {
                _uiState.value = UiState.Success(result.getOrThrow())
            } else {
                _uiState.value =
                    UiState.Error(result.exceptionOrNull() ?: Exception("Unknown error"))
            }
        }
    }

    fun fetchPurchaseInfo(packId: Int) {
        viewModelScope.launch {
            _purchaseState.value = UiState.Loading
            val result = getPurchaseInfoUseCase(packId)
            if (result.isSuccess) {
                _purchaseState.value = UiState.Success(result.getOrThrow())
            } else {
                _purchaseState.value =
                    UiState.Error(result.exceptionOrNull() ?: Exception("Unknown error"))
            }
        }
    }

    fun purchaseEmoticonPack(packId: Int) {
        viewModelScope.launch {
            _purchaseState.value = UiState.Loading
            val result = purchaseEmoticonPackUseCase(packId)
            result.onSuccess {
                _toastEvent.emit(it)
                fetchPurchaseInfo(packId)
            }.onFailure {
                _toastEvent.emit(it.message ?: "Unknown error")
                fetchPurchaseInfo(packId)
            }
        }
    }

    fun downloadEmoticonPack(packId: Int, uuid: String) {
        viewModelScope.launch {
            _isDownloading.value = true
            val packInfo = getDownloadPackInfoUseCase(uuid)
            _totalEmoticonCount.value = packInfo.emoticonUrls.size
            _downloadedEmoticonCount.value = 0
            packInfo.emoticonUrls.forEachIndexed { index, url ->
                val result = downloadEmoticonPackUseCase(index, packId, url)
                result
                    .onSuccess {
                        _downloadedEmoticonCount.value++
                    }
                    .onFailure {
                        _toastEvent.emit("다운로드 실패")
                        _isDownloading.value = false
                        return@forEachIndexed
                    }
            }
            updateDownloadedStatusUseCase(packId, true)
            _toastEvent.emit("다운로드 완료")
            _isDownloading.value = false
            fetchPurchaseInfo(packId)
        }
    }

    fun reportPack(packUUid: String, reason: String) {
        viewModelScope.launch {
            val result = reportPackUseCase(packUUid, reason)
            result.onSuccess {
                _toastEvent.emit("신고가 접수되었습니다.")
            }.onFailure {
                _toastEvent.emit(it.message ?: "Unknown error")
            }
        }
    }

    sealed class UiState<out T> {
        data object Loading : UiState<Nothing>()
        data class Success<T>(val data: T) : UiState<T>()
        data class Error(val exception: Throwable) : UiState<Nothing>()
    }
}
