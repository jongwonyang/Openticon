package io.ssafy.openticon.data.model

import androidx.room.PrimaryKey
import androidx.room.Entity;
import kotlinx.serialization.Serializable;

@Serializable
@Entity(tableName = "like_emoticons")
data class LikeEmoticon(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // id는 자동 증가됨
    val filePath: String,
    val title: String,
    val packId: Int
)
