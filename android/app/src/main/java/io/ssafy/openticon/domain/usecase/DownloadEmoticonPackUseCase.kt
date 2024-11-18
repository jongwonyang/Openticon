package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.domain.repository.EmoticonPackRepository
import javax.inject.Inject

class DownloadEmoticonPackUseCase @Inject constructor(
    private val repository: EmoticonPackRepository
) {
    suspend operator fun invoke(idx: Int, packId: Int, url: String): Result<Unit> {
        try {
            // 다운로드
            repository.downloadAndSaveEmoticonPack(idx, packId, url)

            return Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e)
        }
    }
}