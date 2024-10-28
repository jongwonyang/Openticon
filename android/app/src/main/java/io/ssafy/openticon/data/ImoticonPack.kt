package io.ssafy.openticon.data

data class ImoticonPack(
    val name: String,
    val representativeImage: Int,  // 대표 이미지 리소스 ID
    val imageResources: List<Int>  // 이미지 리스트 리소스 ID들
)
