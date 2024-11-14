package org.comon.moviefriends.data.model.firebase

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class UserInfo(
    val id: String = "",
    val joinType: String = "",
    val nickName: String = "",
    val createdDate: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
    val profileImage: String = "",
    val fcmToken: String = "",
    val sendBirdId: String = "",
    val sendBirdToken: String = "",
    val status: Boolean = true,
)
