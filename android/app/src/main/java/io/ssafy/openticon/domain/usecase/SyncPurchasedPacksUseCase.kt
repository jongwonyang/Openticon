package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.domain.repository.PurchaseRepository
import javax.inject.Inject

class SyncPurchasedPacksUseCase @Inject constructor(
    private val purchaseRepository: PurchaseRepository
) {
    suspend operator fun invoke() {
        val purchasedPacks = purchaseRepository.fetchPurchasedPacksFromServer()
        purchaseRepository.syncPurchasedPacks(purchasedPacks)
    }
}