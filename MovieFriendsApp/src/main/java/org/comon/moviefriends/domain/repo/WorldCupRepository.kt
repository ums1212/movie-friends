package org.comon.moviefriends.domain.repo

import kotlinx.coroutines.flow.Flow
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.entity.worldcup.ResponseMovieWorldCupInfoListDto
import org.comon.moviefriends.data.entity.worldcup.ResponseMovieWorldCupItemListDto
import retrofit2.Response

interface WorldCupRepository {

    suspend fun getMonthlyWorldCupInfo(month: Int, year: Int): Flow<APIResult<Response<ResponseMovieWorldCupInfoListDto>>>
    suspend fun getMonthlyWorldCupItemList(worldCupId: Int): Flow<APIResult<Response<ResponseMovieWorldCupItemListDto>>>
}