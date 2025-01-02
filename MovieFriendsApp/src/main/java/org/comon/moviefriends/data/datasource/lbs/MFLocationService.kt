package org.comon.moviefriends.data.datasource.lbs

import org.comon.moviefriends.data.entity.tmdb.ResponseRegionDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MFLocationService {

    @GET("v2/local/geo/coord2regioncode")
    suspend fun getCurrentRegion(
        @Query("x") longitude:Double,
        @Query("y") latitude:Double,
    ): Response<ResponseRegionDto>

}