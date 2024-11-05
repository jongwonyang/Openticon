package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.domain.repository.EmoticonPackRepository
import javax.inject.Inject

class DownloadEmoticonPackUseCase @Inject constructor(
    private val repository: EmoticonPackRepository
) {
    suspend operator fun invoke(packId: Int): Result<Unit> {
        try {
            // 서버에서 팩 정보 가져오기
            val pack = repository.getPublicPackInfo(packId)

            // 팩 정보에 있는 url로 부터 다운로드
            repository.downloadAndSavePublicEmoticonPack(packId, pack.emoticonUrls)

            // DB 업데이트
            repository.updateDownloadedStatus(packId, true)

            return Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e)
        }
    }
}