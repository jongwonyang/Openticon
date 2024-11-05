package io.ssafy.openticon.domain.repository

interface PurchaseRepository {
    suspend fun fetchPurchasedPacksFromServer(): List<Int>
    suspend fun syncPurchasedPacks(purchasedPackIds: List<Int>)
}