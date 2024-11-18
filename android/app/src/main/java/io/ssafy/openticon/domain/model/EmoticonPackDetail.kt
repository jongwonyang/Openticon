package io.ssafy.openticon.domain.model

import io.ssafy.openticon.data.model.PackInfoResponseDto
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class EmoticonPackDetail(
    val id: Int,
    val thumbnail: String,
    val title: String,
    val price: Int,
    val description: String,
    val items: List<String>,
    val createdAt: String,
    val authorId: Int,
    val authorNickname: String,
    val authorProfilePic: String,
    val authorBio : String,
)

fun PackInfoResponseDto.toEmoticonPackDetail(): EmoticonPackDetail {
    val source = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SS")
    val target = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formatted = LocalDateTime
        .parse(createdAt, source)
        .format(target)

    return EmoticonPackDetail(
        id = id,
        thumbnail = thumbnailImg,
        title = title,
        price = price,
        description = description,
        items = emoticons,
        createdAt = formatted,
        authorId = author.id,
        authorNickname = author.nickname,
        authorProfilePic = author.profile,
        authorBio = author.bio,
    )
}