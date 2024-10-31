package io.ssafy.openticon.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "member")
data class MemberResponseDTO(
    val stateCode : Int,
    val message : String,
    val memberEntity: MemberEntity
)

