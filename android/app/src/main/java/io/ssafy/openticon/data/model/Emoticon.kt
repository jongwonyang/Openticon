package io.ssafy.openticon.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "emoticons")
data class Emoticon(
    @PrimaryKey val id: String,
    val packId: Int,
    val filePath: String
)
