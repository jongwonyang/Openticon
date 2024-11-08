package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.data.repository.PointsRepository
import javax.inject.Inject

class PurchasePointUseCase @Inject constructor(
    private val repository: PointsRepository
) {
    suspend operator fun invoke(amount: Int, impUid: String): Result<String> {

        val response = repository.purchasePoint(amount, impUid)
        return if(response.isSuccessful){
            Result.success(response.body() ?: "포인트 충전 성공")
        }else{
            Result.failure(Exception("포인트 충전 실패"))
        }
    }
}
