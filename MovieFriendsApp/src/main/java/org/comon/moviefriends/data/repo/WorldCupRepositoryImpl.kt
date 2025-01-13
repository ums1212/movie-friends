package org.comon.moviefriends.data.repo

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.datasource.worldcup.WorldCupService
import org.comon.moviefriends.domain.repo.WorldCupRepository
import javax.inject.Inject

class WorldCupRepositoryImpl @Inject constructor(
    private val worldCupService: WorldCupService
): WorldCupRepository {

    override suspend fun getMonthlyWorldCupInfo(month: Int, year: Int) = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(worldCupService.requestMonthlyWorldCupInfo(month, year)))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    override suspend fun getMonthlyWorldCupItemList(worldCupId: Int) = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(worldCupService.requestMonthlyWorldCupItems(worldCupId)))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

}