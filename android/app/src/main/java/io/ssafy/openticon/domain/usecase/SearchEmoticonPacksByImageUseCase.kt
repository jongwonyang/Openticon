package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.data.repository.EmoticonPacksRepository
import io.ssafy.openticon.domain.model.SearchEmoticonPacksListItem
import io.ssafy.openticon.domain.model.toSearchEmoticonPacksList
import okhttp3.MultipartBody
import retrofit2.http.Part
import javax.inject.Inject

class SearchEmoticonPacksByImageUseCase  @Inject constructor(
    private val repository: EmoticonPacksRepository)
{
    suspend operator fun invoke(
        size: Int,
        page: Int,
        @Part image: MultipartBody.Part,
    ): Pair<List<SearchEmoticonPacksListItem>, Boolean>{
        return repository.searchEmoticonPackByImage(size = size, page = page, image= image)
            .toSearchEmoticonPacksList();
    }
}