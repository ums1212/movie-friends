package org.comon.moviefriends.data.entity.worldcup

import com.google.gson.annotations.SerializedName

class ResponseMovieWorldCupItemListDto: ArrayList<ResponseMovieWorldCupItemListDto.ResponseMovieWorldCupItemDto>() {
    data class ResponseMovieWorldCupItemDto(
        @SerializedName("worldCupId")
        val worldCupId: Int,
        @SerializedName("itemId")
        val itemId: Int,
        @SerializedName("itemImage")
        val itemImage: String,
        @SerializedName("description")
        val description: String,
    )
}