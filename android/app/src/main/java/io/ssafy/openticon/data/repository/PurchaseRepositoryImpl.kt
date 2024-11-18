package io.ssafy.openticon.data.repository

import io.ssafy.openticon.data.model.toEmoticonPack
import io.ssafy.openticon.data.remote.PurchaseApi
import io.ssafy.openticon.domain.model.EmoticonPack
import io.ssafy.openticon.domain.repository.EmoticonPackRepository
import io.ssafy.openticon.domain.repository.PurchaseRepository
import javax.inject.Inject

class PurchaseRepositoryImpl @Inject constructor(
    private val purchaseApi: PurchaseApi,
    private val emoticonPackRepository: EmoticonPackRepository
) : PurchaseRepository {
    override suspend fun fetchPurchasedPacksFromServer(): List<EmoticonPack> {
        return purchaseApi.fetchPurchasedPacks().map { it.toEmoticonPack() }
    }

    override suspend fun syncPurchasedPacks(purchasedPacks: List<EmoticonPack>) {
        purchasedPacks.map {
            emoticonPackRepository.savePackInfo(it)
        }
    }
}