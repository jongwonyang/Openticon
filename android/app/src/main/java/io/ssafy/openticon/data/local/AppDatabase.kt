package io.ssafy.openticon.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import io.ssafy.openticon.data.model.Emoticon
import io.ssafy.openticon.data.model.EmoticonPackEntity

@Database(entities = [Emoticon::class, EmoticonPackEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun emoticonDao(): EmoticonDao
}