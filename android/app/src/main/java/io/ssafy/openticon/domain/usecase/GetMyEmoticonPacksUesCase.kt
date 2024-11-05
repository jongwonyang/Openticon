package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.data.model.EmoticonPackEntity
import io.ssafy.openticon.data.repository.EmoticonPacksRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMyEmoticonPacksUesCase @Inject constructor(
    private val repository: EmoticonPacksRepository
) {
    suspend operator fun invoke(): Flow<List<EmoticonPackEntity>>? {
        return repository.getLocalEmoticonPacks();
    }
}