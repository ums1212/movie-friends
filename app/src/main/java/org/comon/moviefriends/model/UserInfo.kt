package org.comon.moviefriends.model

data class UserInfo(
    val id: Int,
    val joinType: String,
    val nickName: String,
    val createdDate: String,
    val status: Boolean,
)
