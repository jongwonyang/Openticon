package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.data.repository.EmoticonPacksRepository
import io.ssafy.openticon.data.repository.MemberRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeleteAllOrderUseCase @Inject constructor(
    private val repository: EmoticonPacksRepository
){
    suspend operator fun invoke(): Result<Unit> {
        try {
            repository.deleteAllEmoticonPacksOrder()
            return Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e)
        }
    }
}