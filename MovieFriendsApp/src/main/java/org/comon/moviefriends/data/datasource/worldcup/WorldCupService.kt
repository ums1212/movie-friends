package org.comon.moviefriends.data.datasource.worldcup

import org.comon.moviefriends.data.entity.worldcup.ResponseMovieWorldCupInfoListDto
import org.comon.moviefriends.data.entity.worldcup.ResponseMovieWorldCupItemListDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface WorldCupService {

    // 월드컵 게임 정보
    @GET("/worldcup/monthlyWorldCupInfo/{year}/{month}")
    suspend fun requestMonthlyWorldCupInfo(
        @Path("year") year: Int,
        @Path("month") month: Int,
    ): Response<ResponseMovieWorldCupInfoListDto>

    // 월드컵 게임 항목
    @GET("/worldcup/monthlyWorldCupItems/{worldCupId}")
    suspend fun requestMonthlyWorldCupItems(
        @Path("worldCupId") worldCupId: Int,
    ): Response<ResponseMovieWorldCupItemListDto>

}