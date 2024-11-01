package io.ssafy.openticon.data.repository

import io.ssafy.openticon.data.model.PackInfoResponseDto
import io.ssafy.openticon.data.model.PageEmoticonPackResponseDto
import io.ssafy.openticon.data.remote.EmoticonPacksApi
import javax.inject.Inject

class EmoticonPacksRepository @Inject constructor(
    private val api: EmoticonPacksApi
) {
    suspend fun searchEmoticonPacks(
        query: String,
        type: String,
        sort: String,
        size: Int,
        page: Int
    ): PageEmoticonPackResponseDto {
        return api.searchEmoticonPacks(
            query = query,
            type = type,
            sort = sort,
            size = size,
            page = page
        )
    }

    suspend fun getPublicPackInfo(emoticonPackId: Int): PackInfoResponseDto {
        return api.getPublicPackInfo(emoticonPackId)
    }
}