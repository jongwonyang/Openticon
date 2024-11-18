package io.ssafy.openticon.domain.repository

import io.ssafy.openticon.domain.model.EmoticonPack

interface PurchaseRepository {
    suspend fun fetchPurchasedPacksFromServer(): List<EmoticonPack>
    suspend fun syncPurchasedPacks(purchasedPacks: List<EmoticonPack>)
}