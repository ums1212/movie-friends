package org.comon.moviefriends.data.model

import java.util.UUID

data class ReplyInfo(
    val id: String = UUID.randomUUID().toString(),
    val user: UserInfo,
    val content: String,
    val createdDate: String,
    val status: Boolean,
)
