package org.comon.moviefriends.model

data class UserInfo(
    val id: Int = 0,
    val joinType: String = "",
    val nickName: String = "",
    val createdDate: String = "",
    val profileImage: String = "",
    val status: Boolean = true,
)
