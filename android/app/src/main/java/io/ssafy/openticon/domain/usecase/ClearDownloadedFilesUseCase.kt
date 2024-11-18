package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.domain.repository.EmoticonRepository
import java.io.File
import javax.inject.Inject

class ClearDownloadedFilesUseCase @Inject constructor(
    private val emoticonRepository: EmoticonRepository
) {
    suspend operator fun invoke() {
        val emoticons = emoticonRepository.getAllEmoticons()
        emoticons.forEach { emoticon ->
            val file = File(emoticon.filePath)
            if (file.exists()) {
                file.delete()
            }
        }
    }
}