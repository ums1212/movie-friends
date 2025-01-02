package org.comon.moviefriends.data.entity.firebase

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

data class UserWantMovieInfo (
    val id: String = UUID.randomUUID().toString(),
    val movieId: Int = 0,
    val moviePosterPath: String = "",
    val userInfo: UserInfo = UserInfo(),
    val userLocation: String = "위치 없음",
    val createdDate: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
)