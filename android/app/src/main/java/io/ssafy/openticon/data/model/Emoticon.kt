package io.ssafy.openticon.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emoticons")
data class Emoticon(
    @PrimaryKey val id: String,
    val packId: Int,
    val filePath: String
)
