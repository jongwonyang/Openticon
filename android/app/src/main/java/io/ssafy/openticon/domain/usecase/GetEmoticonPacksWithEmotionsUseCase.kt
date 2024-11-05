package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.data.model.EmoticonPackWithEmotions
import io.ssafy.openticon.data.repository.EmoticonPacksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class GetEmoticonPacksWithEmotionsUseCase(
    private val emoticonRepository: EmoticonPacksRepository
) {
    suspend fun execute(): Flow<List<EmoticonPackWithEmotions>> = flow {
        // 모든 EmoticonPack을 가져옴
        val emoticonPacks = emoticonRepository.getLocalEmoticonPacks().first()

        // 각 EmoticonPack에 대해 해당 Emoticon 리스트를 가져와서 새로운 데이터 모델에 매핑
        val emoticonPacksWithEmotions = emoticonPacks.map { pack ->
            val emotions = emoticonRepository.getEmotionsByPackId(pack.id).first()
            EmoticonPackWithEmotions(emoticonPackEntity = pack, emotions = emotions)
        }

        // Flow로 변환된 데이터를 emit
        emit(emoticonPacksWithEmotions)
    }
}
