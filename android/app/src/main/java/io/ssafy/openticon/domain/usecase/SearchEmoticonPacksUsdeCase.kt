package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.data.model.TagListResponseDto
import io.ssafy.openticon.data.repository.EmoticonPacksRepository
import io.ssafy.openticon.domain.model.SearchEmoticonPacksListItem
import io.ssafy.openticon.domain.model.toSearchEmoticonPacksList
import javax.inject.Inject

class SearchTagsUseCase @Inject constructor(
    private val repository: EmoticonPacksRepository
) {
    suspend operator fun invoke(
    ): TagListResponseDto {
        return repository.searchEmoticonTags()
    }
}