package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.R
import io.ssafy.openticon.data.model.LikeEmoticon
import io.ssafy.openticon.data.model.LikeEmoticonPack
import io.ssafy.openticon.data.repository.EmoticonPacksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLikeEmoticonPack @Inject constructor(
    private val emoticonRepository: EmoticonPacksRepository
) {
    suspend fun execute(): Flow<LikeEmoticonPack> = flow {
        // 모든 EmoticonPack을 가져옴
        val emoticons = emoticonRepository.getLikeEmoticonPack().first()

        val likeEmoticonPack = LikeEmoticonPack(emoticons = emoticons, filePath = R.drawable.sin, name = "즐겨 찾기")


        // Flow로 변환된 데이터를 emit
        emit(likeEmoticonPack)
    }

    suspend fun insertLike(likeEmoticon: LikeEmoticon){
        emoticonRepository.insertLikeEmoticons(likeEmoticon)
    }
}