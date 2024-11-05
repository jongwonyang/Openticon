package io.ssafy.openticon.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ssafy.openticon.data.local.EmoticonDao
import io.ssafy.openticon.data.model.toEmoticonPack
import io.ssafy.openticon.data.model.toEmoticonPackEntity
import io.ssafy.openticon.data.remote.EmoticonPacksApi
import io.ssafy.openticon.domain.model.EmoticonPack
import io.ssafy.openticon.domain.repository.EmoticonPackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class EmoticonPackRepositoryImpl @Inject constructor(
    private val emoticonPacksApi: EmoticonPacksApi,
    private val emoticonDao: EmoticonDao,
    @ApplicationContext private val context: Context
) : EmoticonPackRepository {
    override suspend fun getPublicPackInfo(packId: Int): EmoticonPack {
        val packInfo = emoticonPacksApi.getPublicPackInfo(packId)
        return packInfo.toEmoticonPack()
    }

    override suspend fun savePackInfo(emoticonPack: EmoticonPack) {
        val thumbnailBody = emoticonPacksApi.downloadEmoticon(emoticonPack.thumbnail)
        val listImgBody = emoticonPacksApi.downloadEmoticon(emoticonPack.listImg)

        val packDir = File(context.filesDir, "emoticon_packs/${emoticonPack.id}")
        if (!packDir.exists()) packDir.mkdirs()

        val thumbnailFileName = "thumbnail.${emoticonPack.thumbnail.substringAfterLast(".")}"
        val listImgFileName = "listImg.${emoticonPack.listImg.substringAfterLast(".")}"

        val thumbnailFile = File(packDir, thumbnailFileName)
        val listImgFile = File(packDir, listImgFileName)

        val tis = thumbnailBody.byteStream()
        val tos = withContext(Dispatchers.IO) {
            FileOutputStream(thumbnailFile)
        }
        tis.use { input ->
            tos.use { output ->
                input.copyTo(output)
            }
        }
        val thumbnailFilePath = thumbnailFile.absolutePath

        val lis = listImgBody.byteStream()
        val los = withContext(Dispatchers.IO) {
            FileOutputStream(listImgFile)
        }
        lis.use { input ->
            los.use { output ->
                input.copyTo(output)
            }
        }
        val listImgFilePath = listImgFile.absolutePath

        val entity = emoticonPack.toEmoticonPackEntity(
            thumbnailFilePath = thumbnailFilePath,
            listImgFilePath = listImgFilePath
        )

        emoticonDao.insertEmoticonPack(entity)
    }
}