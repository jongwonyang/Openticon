package io.ssafy.openticon.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emoticon_packs")
data class EmoticonPack(
    @PrimaryKey val id: Int,
    val title: String,
    val downloaded: Boolean
)