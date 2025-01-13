package org.comon.moviefriends.data.entity.worldcup

data class ResponseMovieWorldCupInfoDto(
    val worldCupId: Int,
    val title: String,
    val description: String,
    val totalRound: Int,
    val image: String,
)
