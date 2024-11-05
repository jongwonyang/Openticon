package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.data.model.EmoticonPackEntity
import io.ssafy.openticon.data.model.EmoticonPackWithEmotions
import io.ssafy.openticon.data.repository.EmoticonPacksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetEmoticonPacksUseCase @Inject constructor(
    private val emoticonRepository: EmoticonPacksRepository
) {
    suspend fun execute(): Flow<List<EmoticonPackEntity>> = flow {
        // 모든 EmoticonPack을 가져옴
        val emoticonPacks = emoticonRepository.getLocalEmoticonPacks().first()


        // Flow로 변환된 데이터를 emit
        emit(emoticonPacks)
    }

    suspend fun updateEmoticonPack(emoticonPack: EmoticonPackEntity){
        emoticonRepository.updateEmoticonPack(emoticonPack)
    }
}
