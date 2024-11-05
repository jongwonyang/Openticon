package io.ssafy.openticon.data.repository

import io.ssafy.openticon.data.local.EmoticonDao
import io.ssafy.openticon.data.model.toEmoticonPack
import io.ssafy.openticon.data.model.toEmoticonPackEntity
import io.ssafy.openticon.data.remote.EmoticonPacksApi
import io.ssafy.openticon.domain.model.EmoticonPack
import io.ssafy.openticon.domain.repository.EmoticonPackRepository
import javax.inject.Inject

class EmoticonPackRepositoryImpl @Inject constructor(
    private val emoticonPacksApi: EmoticonPacksApi,
    private val emoticonDao: EmoticonDao
) : EmoticonPackRepository {
    override suspend fun getPublicPackInfo(packId: Int): EmoticonPack {
        val packInfo = emoticonPacksApi.getPublicPackInfo(packId)
        return packInfo.toEmoticonPack()
    }

    override suspend fun savePackInfo(emoticonPack: EmoticonPack) {
        val entity = emoticonPack.toEmoticonPackEntity()
        emoticonDao.insertEmoticonPack(entity)
    }
}