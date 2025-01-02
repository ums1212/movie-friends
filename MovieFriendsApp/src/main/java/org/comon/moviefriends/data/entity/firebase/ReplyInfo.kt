package org.comon.moviefriends.data.entity.firebase

import com.google.firebase.Timestamp
import java.util.UUID

data class ReplyInfo(
    val id: String = UUID.randomUUID().toString(),
    val postId: String = "",
    val user: UserInfo = UserInfo(),
    val content: String = "",
    val createdDate: Timestamp = Timestamp.now(),
    val status: Boolean = true,
)
