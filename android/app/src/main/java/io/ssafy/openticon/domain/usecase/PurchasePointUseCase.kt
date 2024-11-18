package io.ssafy.openticon.domain.usecase

import com.google.gson.Gson
import io.ssafy.openticon.data.model.ErrorResponse
import io.ssafy.openticon.data.repository.PointsRepository
import javax.inject.Inject

class PurchasePointUseCase @Inject constructor(
    private val repository: PointsRepository
) {
    suspend operator fun invoke(amount: Int, impUid: String): Result<String> {

        val response = repository.purchasePoint(amount, impUid)
        return if(response.isSuccessful){
            Result.success(response.body()?.message ?: "충전 성공")
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = errorBody?.let {
                try {
                    Gson().fromJson(it, ErrorResponse::class.java).message
                } catch (e: Exception) {
                    e.printStackTrace()
                    "알 수 없는 오류가 발생했습니다."
                }
            }
            Result.failure(Exception(errorMessage))
        }
    }
}
