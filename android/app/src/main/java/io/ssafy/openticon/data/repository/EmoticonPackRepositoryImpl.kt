package io.ssafy.openticon.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ssafy.openticon.data.local.EmoticonDao
import io.ssafy.openticon.data.model.Emoticon
import io.ssafy.openticon.data.model.EmoticonPackOrder
import io.ssafy.openticon.data.model.ErrorResponse
import io.ssafy.openticon.data.model.ReportPackRequestDto
import io.ssafy.openticon.data.model.toEmoticonPack
import io.ssafy.openticon.data.model.toEmoticonPackEntity
import io.ssafy.openticon.data.remote.EmoticonPacksApi
import io.ssafy.openticon.domain.model.EmoticonPack
import io.ssafy.openticon.domain.repository.EmoticonPackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
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
        if(entity.downloaded){
            emoticonDao.insertPackOrder(EmoticonPackOrder(
                packId = entity.id
            ))
        }
    }

    override suspend fun downloadAndSavePublicEmoticonPack(packId: Int, emoticonUrls: List<String>) {
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
    }

    override suspend fun downloadAndSaveEmoticonPack(idx: Int, packId: Int, url: String) {
        val fileName = "emoticon_$idx.${url.substringAfterLast(".")}"
        val filePath = downloadAndSaveEmoticonFile(url, packId, fileName)

        if (filePath != null) {
            val emoticon = Emoticon(
                id = "$packId-$idx",
                packId = packId,
                filePath = filePath
            )
            emoticonDao.insertEmoticon(emoticon)
        } else {
            throw Exception("Failed to download emoticon from $url")
        }
    }

    override suspend fun reportPack(packUuid: String, reason: String): Result<String> {
        val response = emoticonPacksApi.reportPack(
            ReportPackRequestDto(
                uuid = packUuid,
                description = reason
            )
        )
        return if (response.isSuccessful) {
            Result.success("신고가 접수되었습니다.")
        } else {
            val errorResponse: ErrorResponse? = response.errorBody()?.let { errorBody ->
                try {
                    val gson = Gson()
                    gson.fromJson(errorBody.charStream(), ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
            }
            Result.failure(Exception(errorResponse?.message ?: "Unknown error"))
        }
    }

    override suspend fun updateDownloadedStatus(packId: Int, isDownloaded: Boolean) {
        withContext(Dispatchers.IO) {
            emoticonDao.updateEmoticonPackDownloaded(packId, isDownloaded)
            if(isDownloaded){
                emoticonDao.insertPackOrder(
                    EmoticonPackOrder(packId = packId)
                )
            }
        }
    }

    override suspend fun getDownloadPackInfo(uuid: String): EmoticonPack {
        val packInfo = emoticonPacksApi.getDownloadPackInfo(uuid)
        return packInfo.toEmoticonPack()
    }

    private suspend fun downloadAndSaveEmoticonFile(emoticonUrl: String, packId: Int, fileName: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val responseBody = emoticonPacksApi.downloadEmoticon(emoticonUrl)
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

    override suspend fun deleteFilesFromId(packId: Int, emoticons: List<Emoticon>){
        emoticons.map { emoticon ->
            deleteFileFromLocal(emoticon.packId, emoticon.filePath)
        }
    }

    private fun deleteFileFromLocal(packId: Int, filePath: String): Boolean {
        return try {
            Log.e("deleteFile", filePath)
//            val packDir = File(context.filesDir, "emoticon_packs/$packId")
//            val file = File(packDir, fileName)
            val file = File(filePath)
            if (file.exists()) {
                file.delete()  // 파일 삭제
            } else {
                false  // 파일이 존재하지 않음
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}