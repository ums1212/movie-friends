package org.comon.moviefriends.data.model

data class UserWantMovieInfo (
    val id: Int = 0,
    val movieId: Int = 0,
    val moviePosterPath: String = "",
    val userInfo: UserInfo = UserInfo(),
    val userLocation: String = "위치 없음",
    val createdDate: String = "",
)