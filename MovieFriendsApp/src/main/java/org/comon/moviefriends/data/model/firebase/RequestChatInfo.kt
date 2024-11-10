package org.comon.moviefriends.data.model.firebase

import java.util.UUID

data class RequestChatInfo(
    val id: String = UUID.randomUUID().toString(),
    val movieId: Int,
    val sendUser: UserInfo,
    val receiveUser: UserInfo,
    val proposalFlag: Boolean,
    val createdDate: String,
    val status: Boolean = true,
)
