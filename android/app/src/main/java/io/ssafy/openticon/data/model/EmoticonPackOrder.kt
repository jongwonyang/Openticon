package io.ssafy.openticon.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emoticon_pack_orders")
data class EmoticonPackOrder(
    @PrimaryKey(autoGenerate = true) val order: Int = 0,
    val packId: Int,
)
