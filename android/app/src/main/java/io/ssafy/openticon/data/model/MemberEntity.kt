package io.ssafy.openticon.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "member")
data class MemberEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,  // ID

    val email: String = "",  // Email

    val nickname: String = "",  // Unique Nickname

    val point: Long = 0L,  // Points

    val createdAt: String = "",  // Creation Time

    var updatedAt: String = "",  // Updated Time

    val manager: Boolean = false,  // Is Manager

    val isResigned: Boolean = false,  // Is Resigned

    val mobileFcm: String = "",  // Mobile FCM Token

    val webFcm: String = "",  // Web FCM Token

    val profile_image: String = "",  // Profile Image

    val bio : String = "" //삳태 메세지
)

