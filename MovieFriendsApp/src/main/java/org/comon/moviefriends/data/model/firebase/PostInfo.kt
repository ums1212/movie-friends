package org.comon.moviefriends.data.model.firebase

import com.google.firebase.Timestamp
import java.util.UUID

data class PostInfo(
    val id: String = UUID.randomUUID().toString(),
    val user: UserInfo = UserInfo(),
    val title: String = "",
    val content: String = "",
    val imageLink: String = "",
    val viewCount: Int = 0,
    val likes: List<UserInfo> = emptyList(),
    val reply: List<ReplyInfo> = emptyList(),
    val createdDate: Timestamp = Timestamp.now(),
    val status: Boolean = true,
)