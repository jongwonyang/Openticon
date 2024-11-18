package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.domain.repository.EmoticonPackRepository
import io.ssafy.openticon.domain.repository.PointsRepository
import javax.inject.Inject

class PurchaseEmoticonPackUseCase @Inject constructor(
    private val pointsRepository: PointsRepository,
    private val emoticonPackRepository: EmoticonPackRepository
) {
    suspend operator fun invoke(packId: Int): Result<String> {
        // 서버에 구매 요청
        val result = pointsRepository.purchasePack(packId)

        if (result.isSuccess) {
            // DB에 구매한 팩 정보 저장
            val purchasedPack = emoticonPackRepository.getPublicPackInfo(packId)
            emoticonPackRepository.savePackInfo(purchasedPack)
        }

        return result
    }
}