package org.comon.moviefriends.model

data class PostInfo(
    val id: Int,
    val user: UserInfo,
    val title: String,
    val content: String,
    val imageLink: List<String>,
    val viewCount: Int,
    val likes: List<UserInfo>,
    val reply: List<ReplyInfo>,
    val createdDate: String,
    val status: Boolean,
)