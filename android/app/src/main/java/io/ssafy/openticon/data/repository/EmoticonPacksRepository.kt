package io.ssafy.openticon.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ssafy.openticon.data.local.EmoticonDao
import io.ssafy.openticon.data.model.Emoticon
import io.ssafy.openticon.data.model.EmoticonPack
import io.ssafy.openticon.data.model.PackInfoResponseDto
import io.ssafy.openticon.data.model.PageEmoticonPackResponseDto
import io.ssafy.openticon.data.remote.EmoticonPacksApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class EmoticonPacksRepository @Inject constructor(
    private val api: EmoticonPacksApi,
    private val emoticonDao: EmoticonDao,
    @ApplicationContext private val context: Context
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

    suspend fun savedEmoticonPack(emoticonPack: EmoticonPack) {
        emoticonDao.insertEmoticonPack(emoticonPack)
    }

    suspend fun downloadAndSavePublicEmoticonPack(packId: Int, emoticonUrls: List<String>) {
        for ((index, url) in emoticonUrls.withIndex()) {
            val fileName = "emoticon_$index.${url.substringAfterLast(".")}"
            val filePath = downloadAndSaveEmoticonFile(url, packId, fileName)

            if (filePath != null) {
                val emoticon = Emoticon(
                    id = "$packId-$index",
                    packId = packId,
                    filePath = filePath
                )
                emoticonDao.insertEmoticon(emoticon)
            } else {
                throw Exception("Failed to download emoticon from $url")
            }
        }
        emoticonDao.updateEmoticonPackDownloaded(packId, true)
    }

    private suspend fun downloadAndSaveEmoticonFile(emoticonUrl: String, packId: Int, fileName: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val responseBody = api.downloadEmoticon(emoticonUrl)
                saveFileToLocal(responseBody, packId, fileName)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun saveFileToLocal(body: ResponseBody, packId: Int, fileName: String): String? {
        return try {
            val packDir = File(context.filesDir, "emoticon_packs/$packId")
            if (!packDir.exists()) packDir.mkdirs()

            val file = File(packDir, fileName)
            val inputStream = body.byteStream()
            val outputStream = FileOutputStream(file)

            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getPurchasedPackInfo(packId: Int): Flow<EmoticonPack?> {
        return emoticonDao.getEmoticonPack(packId)
    }
}