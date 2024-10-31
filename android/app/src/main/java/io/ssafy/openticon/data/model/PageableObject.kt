package io.ssafy.openticon.data.model

data class PageableObject(
    val paged: Boolean,
    val pageNumber: Int,
    val pageSize: Int,
    val offset: Int,
    val sort: SortObject,
    val unpaged: Boolean
)
