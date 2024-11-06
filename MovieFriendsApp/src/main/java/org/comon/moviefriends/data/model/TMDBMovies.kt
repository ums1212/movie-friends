package org.comon.moviefriends.data.model

import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

data class TMDBMovies(
    val results: List<MovieInfo>
){
    data class MovieInfo(
        val id: Int,

        @SerializedName("original_title")
        val originalTitle: String,

        val overview: String,

        @SerializedName("poster_path")
        val posterPath: String,

        @SerializedName("release_date")
        val releaseDate: String,

        val title: String,

        @Field("vote_average")
        val voteAverage: Float
    )
}