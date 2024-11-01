package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.data.repository.EmoticonPacksRepository
import io.ssafy.openticon.domain.model.SearchEmoticonPacksListItem
import io.ssafy.openticon.domain.model.toSearchEmoticonPacksList
import javax.inject.Inject

class SearchEmoticonPacksUseCase @Inject constructor(
    private val repository: EmoticonPacksRepository
) {
    suspend operator fun invoke(
        searchKey: String,
        searchText: String,
        sort: String = "new",
        size: Int = 10,
        page: Int = 0
    ): Pair<List<SearchEmoticonPacksListItem>, Boolean> {

        return repository.searchEmoticonPacks(
            type = searchKey,
            query = searchText,
            sort = sort,
            size = size,
            page = page
        ).toSearchEmoticonPacksList()
    }
}