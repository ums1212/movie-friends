package org.comon.moviefriends.data.model

data class UserWantMovieInfo (
    val id: Int = 0,
    val movieId: Int = 0,
    val moviePosterPath: String = "",
    val userInfo: UserInfo = UserInfo(),
    val userDistance: Int = 0,
    val createdDate: String = "",
)