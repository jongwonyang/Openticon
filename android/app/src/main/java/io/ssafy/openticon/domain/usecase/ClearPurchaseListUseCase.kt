package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.domain.repository.EmoticonPackRepository
import javax.inject.Inject

class ClearPurchaseListUseCase @Inject constructor(
    private val repository: EmoticonPackRepository
) {
    suspend operator fun invoke() {
        repository.clearPurchaseList()
    }
}