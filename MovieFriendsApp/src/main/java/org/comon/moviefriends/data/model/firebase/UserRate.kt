package org.comon.moviefriends.data.model.firebase

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

data class UserRate(
    val id: String = UUID.randomUUID().toString(),
    val movieId: Int = 0,
    val rate: Int = 0,
    val user: UserInfo = UserInfo(),
    val createdDate: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
)
