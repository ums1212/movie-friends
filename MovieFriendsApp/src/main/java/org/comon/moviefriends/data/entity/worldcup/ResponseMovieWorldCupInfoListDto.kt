package org.comon.moviefriends.data.entity.worldcup

import com.google.gson.annotations.SerializedName

class ResponseMovieWorldCupInfoListDto: ArrayList<ResponseMovieWorldCupInfoListDto.ResponseMovieWorldCupInfoDto>() {
    data class ResponseMovieWorldCupInfoDto(
        @SerializedName("worldCupId")
        val worldCupId: Int,
        @SerializedName("title")
        val title: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("totalRound")
        val totalRound: Int,
        @SerializedName("infoImage")
        val image: String,
    )
}
