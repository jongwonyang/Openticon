package io.ssafy.openticon.data.repository

import io.ssafy.openticon.data.local.EmoticonDao
import io.ssafy.openticon.data.model.EmoticonPack
import io.ssafy.openticon.data.remote.PurchaseApi
import io.ssafy.openticon.domain.repository.PurchaseRepository
import javax.inject.Inject

class PurchaseRepositoryImpl @Inject constructor(
    private val purchaseApi: PurchaseApi,
    private val emoticonDao: EmoticonDao
) : PurchaseRepository {
    override suspend fun fetchPurchasedPacksFromServer(): List<Int> {
        return purchaseApi.fetchPurchasedPacks().map { it.packId }
    }

    override suspend fun syncPurchasedPacks(purchasedPackIds: List<Int>) {
        purchasedPackIds.map {
            val emoticonPack = EmoticonPack(id = it, downloaded = false)
            emoticonDao.insertEmoticonPack(emoticonPack = emoticonPack)
        }
    }
}