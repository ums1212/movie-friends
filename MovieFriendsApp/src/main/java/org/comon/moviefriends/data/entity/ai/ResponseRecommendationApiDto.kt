package org.comon.moviefriends.data.entity.ai


import com.google.gson.annotations.SerializedName

data class ResponseRecommendationApiDto(
    @SerializedName("recommendation")
    val recommendation: Recommendation,
    @SerializedName("summary")
    val summary: String
) {
    data class Recommendation(
        @SerializedName("director")
        val director: String,
        @SerializedName("duration")
        val duration: String,
        @SerializedName("genre")
        val genre: List<String>,
        @SerializedName("mainCast")
        val mainCast: List<String>,
        @SerializedName("predictedScore")
        val predictedScore: String,
        @SerializedName("recommendationReason")
        val recommendationReason: String,
        @SerializedName("title")
        val title: Title,
        @SerializedName("year")
        val year: String
    ) {
        data class Title(
            @SerializedName("korean")
            val korean: String,
            @SerializedName("original")
            val original: String
        )
    }
}