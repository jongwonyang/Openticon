package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.data.repository.EmoticonPacksRepository
import io.ssafy.openticon.domain.model.PurchaseInfo
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetPurchaseInfoUseCase @Inject constructor(
    private val repository: EmoticonPacksRepository
) {
    suspend operator fun invoke(packId: Int): Result<PurchaseInfo> {
        val purchaseInfo = repository.getPurchasedPackInfo(packId).first()

        return if (purchaseInfo != null) {
            Result.success(PurchaseInfo(
                packId = packId,
                purchased = true,
                downloaded = purchaseInfo.downloaded,
                uuid = purchaseInfo.uuid
            ))
        } else {
            Result.success(PurchaseInfo(
                packId = packId,
                purchased = false,
                downloaded = false,
                uuid = ""
            ))
        }
    }
}