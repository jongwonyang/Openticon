package io.ssafy.openticon.data.repository

import io.ssafy.openticon.data.local.EmoticonDao
import io.ssafy.openticon.domain.model.Emoticon
import io.ssafy.openticon.domain.repository.EmoticonRepository
import javax.inject.Inject

class EmoticonRepositoryImpl @Inject constructor(
    private val emoticonDao: EmoticonDao
) : EmoticonRepository {
    override suspend fun getAllEmoticons(): List<Emoticon> {
        return emoticonDao.getAllEmoticons().map {
            Emoticon(
                id = it.id,
                packId = it.packId,
                filePath = it.filePath
            )
        }
    }

    override suspend fun deleteAllEmoticons() {
        emoticonDao.deleteAllEmoticons()
    }

    override suspend fun deleteAllEmoticonPackOrders() {
        emoticonDao.deleteAllEmoticonPacksOrder()
    }

    override suspend fun deleteAllLikeEmoticons() {
        emoticonDao.deleteAllLikeEmoticons()
    }
}