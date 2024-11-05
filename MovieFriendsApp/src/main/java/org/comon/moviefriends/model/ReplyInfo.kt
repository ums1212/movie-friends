package org.comon.moviefriends.model

data class ReplyInfo(
    val id: Int,
    val user: UserInfo,
    val content: String,
    val createdDate: String,
    val status: Boolean,
)
