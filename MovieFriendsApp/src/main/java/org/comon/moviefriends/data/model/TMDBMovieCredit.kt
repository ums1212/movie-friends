package org.comon.moviefriends.data.model

import com.google.gson.annotations.SerializedName

data class TMDBMovieCredit(
    val id: Int,
    val cast: List<TMDBCastInfo>,
    val crew: List<Crew>
) {
    data class TMDBCastInfo (
        val adult: Boolean,
        val gender: Int,
        val id: Int,
        @SerializedName("known_for_department")
        val knownForDepartment: String,
        val name: String,
        @SerializedName("original_name")
        val originalName: String,
        val popularity: Float,
        @SerializedName("profile_path")
        val profilePath: String,
        @SerializedName("cast_id")
        val castId: Int,
        val character: String,
        @SerializedName("credit_id")
        val creditId: String,
        val order: Int
    )
    data class Crew(
        @SerializedName("adult")
        val adult: Boolean,
        @SerializedName("credit_id")
        val creditId: String,
        @SerializedName("department")
        val department: String,
        @SerializedName("gender")
        val gender: Int,
        @SerializedName("id")
        val id: Int,
        @SerializedName("job")
        val job: String,
        @SerializedName("known_for_department")
        val knownForDepartment: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("original_name")
        val originalName: String,
        @SerializedName("popularity")
        val popularity: Double,
        @SerializedName("profile_path")
        val profilePath: String?
    )
}