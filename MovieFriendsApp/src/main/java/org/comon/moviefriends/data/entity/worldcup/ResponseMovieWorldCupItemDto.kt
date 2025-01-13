package org.comon.moviefriends.data.entity.worldcup

data class ResponseMovieWorldCupItemListDto(
    val itemList: List<ResponseMovieWorldCupItemListDto>
) {
    data class ResponseMovieWorldCupItemListDto(
        val worldCupId: Int,
        val itemId: Int,
        val image: String,
        val description: String,
    )
}