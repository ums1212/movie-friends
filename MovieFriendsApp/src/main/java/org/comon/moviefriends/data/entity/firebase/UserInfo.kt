package org.comon.moviefriends.data.entity.firebase

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class UserInfo(
    val id: String = "",
    val joinType: String = "",
    val nickName: String = "",
    val gender: String = "",
    val ageRange: Int = -1,
    val createdDate: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
    val profileImage: String = "",
    val fcmToken: String = "",
    val sendBirdId: String = "",
    val sendBirdToken: String = "",
    val status: Boolean = true,
)
