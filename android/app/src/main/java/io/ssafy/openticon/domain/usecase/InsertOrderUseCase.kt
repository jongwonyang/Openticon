package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.data.repository.EmoticonPacksRepository
import io.ssafy.openticon.data.repository.MemberRepository
import javax.inject.Inject

class InsertOrderUseCase @Inject constructor(
    private val repository: EmoticonPacksRepository
){
    suspend operator fun invoke(packId: Int): Result<Unit> {
        try {
            val response = repository.insertOrder(packId)
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}