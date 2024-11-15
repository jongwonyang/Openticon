package io.ssafy.openticon.domain.repository

import io.ssafy.openticon.domain.model.Emoticon

interface EmoticonRepository {
    suspend fun getAllEmoticons(): List<Emoticon>
    suspend fun deleteAllEmoticons()
    suspend fun deleteAllEmoticonPackOrders()
    suspend fun deleteAllLikeEmoticons()
}