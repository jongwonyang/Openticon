package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.data.repository.EmoticonPacksRepository
import javax.inject.Inject

class DownloadEmoticonPackUseCase @Inject constructor(
    private val repository: EmoticonPacksRepository
) {
    suspend operator fun invoke(packId: Int) {
        // 서버에서 팩 정보 가져오기

        // 팩 정보에 있는 url로 부터 다운로드
    }
}