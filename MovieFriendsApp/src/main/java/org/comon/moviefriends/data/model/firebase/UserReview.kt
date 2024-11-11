package org.comon.moviefriends.data.model.firebase

import java.time.LocalDateTime
import java.util.UUID

data class UserReview(
    val id: String = UUID.randomUUID().toString(),
    val movieId: Int = 0,
    val content: String = "",
    val user: UserInfo = UserInfo(),
    val createdDate: LocalDateTime = LocalDateTime.now(),
    val status: Boolean = true,
)