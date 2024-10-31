package io.ssafy.openticon.data.model

data class EmoticonPackResponseDto(
    val id: Int,
    val title: String,
    val member: PackMemberResponseDto,
    val price: Int,
    val view: Int,
    val category: String,
    val thumbnailImg: String,
    val listImg: String,
    val description: String,
    val examine: String,
    val shareLink: String,
    val createdAt: String,
    val updatedAt: String,
    val public: Boolean,
    val aiGenerated: Boolean,
    val blacklist: Boolean
)
