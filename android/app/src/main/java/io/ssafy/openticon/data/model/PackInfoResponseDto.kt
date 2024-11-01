package io.ssafy.openticon.data.model

data class PackInfoResponseDto(
    val id: Int,
    val title: String,
    val author: AuthorResponseDto,
    val price: Int,
    val view: Int,
    val category: String,
    val thumbnailImg: String,
    val listImg: String,
    val emoticons: List<String>,
    val description: String,
    val createdAt: String,
    val aigenerated: Boolean
)
