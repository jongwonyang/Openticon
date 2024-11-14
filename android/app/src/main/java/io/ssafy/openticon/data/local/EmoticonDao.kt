package io.ssafy.openticon.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.ssafy.openticon.data.model.Emoticon
import io.ssafy.openticon.data.model.EmoticonPackEntity
import io.ssafy.openticon.data.model.EmoticonPackOrder
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPackOrder(emoticon: EmoticonPackOrder)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLikeEmoticon(likeEmoticon: LikeEmoticon)

    @Query("SELECT * FROM emoticon_packs WHERE id = :packId")
    fun getEmoticonPack(packId: Int): Flow<EmoticonPackEntity?>

    @Query("SELECT * FROM emoticons WHERE packId = :packId")
    fun getEmoticonsByPack(packId: Int): Flow<List<Emoticon>>

    @Query("""
        SELECT e.* FROM emoticon_packs AS e
        LEFT JOIN emoticon_pack_orders AS o ON e.id = o.packId
        ORDER BY o.`order` IS NULL, o.`order`
    """)
    fun getAllEmoticonPacks(): Flow<List<EmoticonPackEntity>>


    @Query("""
        SELECT e.* FROM emoticon_packs AS e
        LEFT JOIN emoticon_pack_orders AS o ON e.id = o.packId
        WHERE e.downloaded = 1
        ORDER BY o.`order` IS NULL, o.`order`
    """)
    fun getAllDownloadedEmoticonPacks(): Flow<List<EmoticonPackEntity>>

    @Query("UPDATE emoticon_packs SET downloaded = :b WHERE id = :packId")
    fun updateEmoticonPackDownloaded(packId: Int, b: Boolean)

    @Query("SELECT * FROM like_emoticons")
    fun getLikeEmoticonPack(): Flow<List<LikeEmoticon>>

    @Query("Delete From emoticons where packId = :packId")
    fun deleteEmoticonsByPackId(packId: Int)

    @Query("Delete From emoticon_pack_orders where packId = :packId")
    fun deleteFromOrder(packId: Int)

    @Query("Delete From like_emoticons where packId = :packId")
    fun deleteLikeEmoticonsByPackId(packId: Int)

    @Query("Delete From like_emoticons where id = :id")
    fun deleteLikeEmoticonsId(id: Int)

    @Query("DELETE FROM emoticon_pack_orders")
    suspend fun deleteAllEmoticonPacksOrder()


    @Update
    suspend fun updateEmoticonPack(emoticonPack: EmoticonPackEntity)
}