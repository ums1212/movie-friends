package org.comon.moviefriends.data.model.firebase

data class LikeInfo(
    val postId: String = "",
    val userId: String = "",
    val likeCount: Int = 0,
    val isLiked: Boolean = false
)
