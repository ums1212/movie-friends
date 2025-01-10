package org.comon.moviefriends.data.entity.ai


import com.google.gson.annotations.SerializedName

data class RequestRecommendationApiDto(
    @SerializedName("viewing_history")
    val viewingHistory: String
)