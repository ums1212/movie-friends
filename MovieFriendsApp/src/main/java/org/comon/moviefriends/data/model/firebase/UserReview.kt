package org.comon.moviefriends.data.model.firebase

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

data class UserReview(
    val id: String = UUID.randomUUID().toString(),
    val movieId: Int,
    val content: String,
    val user: UserInfo,
    val createdDate: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
    val status: Boolean = true,
)