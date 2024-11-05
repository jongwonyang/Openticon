package io.ssafy.openticon.domain.repository

interface PointsRepository {
    suspend fun purchasePack(packId: Int): Result<String>
}