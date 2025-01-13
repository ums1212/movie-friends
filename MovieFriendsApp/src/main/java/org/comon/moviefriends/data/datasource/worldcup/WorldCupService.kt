package org.comon.moviefriends.data.datasource.worldcup

import org.comon.moviefriends.data.entity.worldcup.ResponseMovieWorldCupInfoDto
import org.comon.moviefriends.data.entity.worldcup.ResponseMovieWorldCupItemListDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WorldCupService {

    // 월드컵 게임 정보
    @GET("monthlyWorldCupInfo")
    suspend fun requestMonthlyWorldCupInfo(
        @Query("year") year: Int,
        @Query("month") month: Int,
    ): Response<ResponseMovieWorldCupInfoDto>

    // 월드컵 게임 항목
    @GET("monthlyWorldCupItems/{worldCupId}")
    suspend fun requestMonthlyWorldCupItems(
        @Path("worldCupId") worldCupId: Int,
    ): Response<ResponseMovieWorldCupItemListDto>

}