package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.domain.repository.EmoticonPackRepository
import javax.inject.Inject

class ReportPackUseCase @Inject constructor(
    private val emoticonPackRepository: EmoticonPackRepository
) {
    suspend operator fun invoke(packUuid: String, reason: String): Result<String> {
        return emoticonPackRepository.reportPack(packUuid, reason)
    }
}