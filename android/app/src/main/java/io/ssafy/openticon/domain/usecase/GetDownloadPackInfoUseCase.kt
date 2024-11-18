package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.domain.model.EmoticonPack
import io.ssafy.openticon.domain.repository.EmoticonPackRepository
import javax.inject.Inject

class GetDownloadPackInfoUseCase @Inject constructor(
    private val repository: EmoticonPackRepository
) {
    suspend operator fun invoke(uuid: String): EmoticonPack {
        return repository.getDownloadPackInfo(uuid)
    }
}