package io.ssafy.openticon.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.ssafy.openticon.data.model.Emoticon
import io.ssafy.openticon.data.model.EmoticonPackEntity
import io.ssafy.openticon.data.model.LikeEmoticon
import kotlinx.coroutines.flow.Flow

@Dao
interface EmoticonDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEmoticonPack(emoticonPackEntity: EmoticonPackEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmoticons(emoticons: List<Emoticon>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmoticon(emoticon: Emoticon)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLikeEmoticon(likeEmoticon: LikeEmoticon)

    @Query("SELECT * FROM emoticon_packs WHERE id = :packId")
    fun getEmoticonPack(packId: Int): Flow<EmoticonPackEntity?>

    @Query("SELECT * FROM emoticons WHERE packId = :packId")
    fun getEmoticonsByPack(packId: Int): Flow<List<Emoticon>>

    @Query("SELECT * FROM emoticon_packs")
    fun getAllEmoticonPacks(): Flow<List<EmoticonPackEntity>>

    @Query("SELECT * FROM emoticon_packs WHERE downloaded = 1")
    fun getAllDownloadedEmoticonPacks(): Flow<List<EmoticonPackEntity>>

    @Query("UPDATE emoticon_packs SET downloaded = :b WHERE id = :packId")
    fun updateEmoticonPackDownloaded(packId: Int, b: Boolean)

    @Query("SELECT * FROM like_emoticons")
    fun getLikeEmoticonPack(): Flow<List<LikeEmoticon>>

    @Query("Delete From emoticons where packId = :packId")
    fun deleteEmoticonsByPackId(packId: Int)

    @Query("Delete From like_emoticons where packId = :packId")
    fun deleteLikeEmoticonsByPackId(packId: Int)

    @Update
    suspend fun updateEmoticonPack(emoticonPack: EmoticonPackEntity)
}