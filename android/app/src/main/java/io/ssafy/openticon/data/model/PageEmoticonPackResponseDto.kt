package io.ssafy.openticon.data.model

data class PageEmoticonPackResponseDto(
    val totalElements: Int,
    val totalPages: Int,
    val pageable: PageableObject,
    val size: Int,
    val content: List<EmoticonPackResponseDto>,
    val number: Int,
    val first: Boolean,
    val last: Boolean,
    val numberOfElements: Int,
    val empty: Boolean
)