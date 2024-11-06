package org.comon.moviefriends.data.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

data class PostInfo(
    val id: String = UUID.randomUUID().toString(),
    val user: UserInfo,
    val title: String,
    val content: String,
    val imageLink: List<String>,
    val viewCount: Int,
    val likes: List<UserInfo>,
    val reply: List<ReplyInfo>,
    val createdDate: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
    val status: Boolean = true,
)