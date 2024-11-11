package org.comon.moviefriends.data.model.firebase

import com.google.firebase.Timestamp
import java.util.UUID

data class UserReview(
    val id: String = UUID.randomUUID().toString(),
    val movieId: Int = 0,
    val content: String = "",
    val user: UserInfo = UserInfo(),
    val createdDate: Timestamp = Timestamp.now(),
    val status: Boolean = true,
)