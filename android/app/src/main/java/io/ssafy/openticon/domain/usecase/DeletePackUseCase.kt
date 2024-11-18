package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.data.model.Emoticon
import io.ssafy.openticon.data.repository.EmoticonPacksRepository
import io.ssafy.openticon.domain.repository.EmoticonPackRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeletePackUseCase @Inject constructor(
    private val repository: EmoticonPackRepository,
    private val db: EmoticonPacksRepository
) {
    suspend operator fun invoke(packId: Int): Result<Unit> {
        try {
            // 서버에서 팩 정보 가져오기
            val pack = db.getEmotionsByPackId(packId).first()
            //repository.
            // DB 업데이트
            repository.deleteFilesFromId(packId, pack)
            return Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e)
        }
    }
}