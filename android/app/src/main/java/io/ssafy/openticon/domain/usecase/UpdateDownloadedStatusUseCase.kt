package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.domain.repository.EmoticonPackRepository
import javax.inject.Inject

class UpdateDownloadedStatusUseCase @Inject constructor(
    private val repository: EmoticonPackRepository
) {
    suspend operator fun invoke(packId: Int, isDownloaded: Boolean) {
        repository.updateDownloadedStatus(packId, isDownloaded)
    }
}