package io.ssafy.openticon.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.ssafy.openticon.domain.model.EmoticonPack
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "emoticon_packs")
data class EmoticonPackEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val thumbnail: String,
    val listImg: String,
    val isPublic: Boolean,
    val downloaded: Boolean
)

fun EmoticonPack.toEmoticonPackEntity(
    thumbnailFilePath: String,
    listImgFilePath: String
): EmoticonPackEntity {
    return EmoticonPackEntity(
        id = id,
        title = title,
        thumbnail = thumbnailFilePath,
        listImg = listImgFilePath,
        isPublic = isPublic,
        downloaded = false
    )
}