package org.comon.moviefriends.data.model.tmdb

import com.google.gson.annotations.SerializedName

data class ResponseMovieDetailDto(
    val id: Int,
    @SerializedName("poster_path")
    val posterPath: String,
    val title: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val genres: List<TMDBMovieDetailGenre>,
    val runtime: Int,
    val overview: String,
){
    data class TMDBMovieDetailGenre(
        val id: Int,
        val name: String
    )
}