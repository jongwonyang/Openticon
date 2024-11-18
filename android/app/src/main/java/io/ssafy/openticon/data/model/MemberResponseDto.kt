package io.ssafy.openticon.data.model


import androidx.room.Entity

@Entity(tableName = "member")
data class MemberResponseDto(
    val stateCode : Int,
    val message : String,
    val memberEntity: MemberEntity
)

