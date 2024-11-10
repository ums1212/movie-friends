package org.comon.moviefriends.data.model.firebase

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

data class ReplyInfo(
    val id: String = UUID.randomUUID().toString(),
    val user: UserInfo = UserInfo(),
    val content: String = "",
    val createdDate: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
    val status: Boolean = true,
)
