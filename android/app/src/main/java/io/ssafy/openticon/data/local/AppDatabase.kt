package io.ssafy.openticon.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import io.ssafy.openticon.data.model.Emoticon
import io.ssafy.openticon.data.model.EmoticonPackEntity
import io.ssafy.openticon.data.model.EmoticonPackOrder
import io.ssafy.openticon.data.model.LikeEmoticon

@Database(entities = [Emoticon::class, EmoticonPackEntity::class, LikeEmoticon::class, EmoticonPackOrder::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun emoticonDao(): EmoticonDao
}