package org.comon.moviefriends.data.repo

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.comon.moviefriends.data.datasource.firebase.ChatDataSource
import org.comon.moviefriends.data.datasource.firebase.MovieDataSource
import org.comon.moviefriends.data.datasource.lbs.MFLocationService
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.datasource.tmdb.TMDBService
import org.comon.moviefriends.data.entity.tmdb.ResponseMovieDetailDto
import org.comon.moviefriends.data.entity.firebase.UserInfo
import org.comon.moviefriends.data.entity.firebase.UserRate
import org.comon.moviefriends.data.entity.firebase.UserReview
import org.comon.moviefriends.data.entity.firebase.UserWantMovieInfo
import org.comon.moviefriends.domain.repo.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor (
    private val movieDataSource: MovieDataSource,
    private val chatDataSource: ChatDataSource,
    private val location: MFLocationService,
    private val rest: TMDBService,
): MovieRepository {

    /**
     * TMDB API로 현재 상영중인 영화 목록을 불러옵니다.
     */
    override fun getNowPlaying() = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(rest.getNowPlaying()))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    /**
     * TMDB API로 인기 영화 목록을 불러옵니다.
     */
    override fun getPopular() = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(rest.getPopular()))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    /**
     * TMDB API로 트렌드 영화 목록을 불러옵니다.
     */
    override fun getTrending() = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(rest.getTrending()))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    /**
     * TMDB API로 개봉 예정 영화 목록을 불러옵니다.
     */
    override fun getUpcoming() = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(rest.getUpcoming()))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    /**
     * TMDB API로 영화 상세 정보를 불러옵니다.
     * @param movieId 영화 ID
     */
    override fun getMovieDetail(movieId: Int) = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(rest.getMovieDetail(movieId)))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    /** 
     * TMDB API로 영화 출연진 목록을 불러옵니다.
     * @param movieId 영화 ID
     */
    override fun getMovieCredit(movieId: Int) = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(rest.getMovieCredit(movieId)))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    /** 
     * TMDB API로 영화 관련 유튜브 링크 목록을 불러옵니다.
     * @param movieId 영화 ID
     */
    override fun getMovieVideo(movieId: Int) = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(rest.getMovieVideo(movieId)))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    override fun searchMovieByTitle(
        movieTitle: String,
        releaseYear: String
    ) = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(rest.searchMovieByTitle(movieTitle = movieTitle, releaseYear = releaseYear)))
    }.catch {
        error -> emit(APIResult.NetworkError(error))
    }

    /** 
     * 해당 영화의 "보고 싶다" 체크 여부를 반환합니다.  
     * @param movieId 영화 ID
     * @param userInfo 유저 정보
     */
    override suspend fun getStateWantThisMovie(movieId: Int, userInfo: UserInfo) = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(movieDataSource.getStateWantThisMovie(movieId, userInfo)))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    /** 
     * 유저의 해당 영화 "보고 싶다" 상태값을 변경합니다.  
     * @param movieInfo 영화 정보
     * @param userInfo 유저 정보
     * @param nowLocation 현재 위치
     */
    override suspend fun changeStateWantThisMovie(
        movieInfo: ResponseMovieDetailDto,
        userInfo: UserInfo,
        nowLocation: List<Double>
    ) = flow {
        emit(APIResult.Loading)

        if(movieDataSource.getStateWantThisMovie(movieInfo.id, userInfo)){
            // 기존 데이터가 있다면 삭제
            movieDataSource.deleteUserWantMovieInfo(movieInfo.id, userInfo)
                .onSuccess {
                    emit(APIResult.Success(false))
                }
                .onFailure {
                    emit(APIResult.NetworkError(it))
                }
        }else{
            // 기존 데이터가 없다면 추가

            // 현재 위치 정보 가져오기
            val region = location
                .getCurrentRegion(nowLocation[1], nowLocation[0])
                .body()?.documents?.find {
                    it.regionType == "B"
                }?.region3depthName

            val wantMovieInfo = UserWantMovieInfo(
                movieId = movieInfo.id,
                moviePosterPath = movieInfo.posterPath,
                userInfo = userInfo,
                userLocation = region ?: "위치 없음",
            )
            movieDataSource.addUserWantMovieInfo(wantMovieInfo)
                .onSuccess {
                    emit(APIResult.Success(true))
                }
                .onFailure {
                    emit(APIResult.NetworkError(it))
                }
        }
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    /** 
     * 해당 영화를 보고 싶다고 체크한 유저 목록을 불러옵니다.
     * (본인 및 요청 내역이 있는 경우는 제외)
     * @param movieId 영화 ID
     * @param userId 유저 ID
     */
    override suspend fun getUserWantList(movieId: Int, userId: String) = flow {
        emit(APIResult.Loading)

        val userWantList = movieDataSource.getUserWantMovieListExceptMe(movieId, userId).getOrThrow()
        val requestList = chatDataSource.getConfirmedSendRequestList(movieId, userId).getOrThrow()
        val receiveList = chatDataSource.getMyReceiveRequestList(userId).getOrThrow()

        val filteredList = userWantList.filter { want ->
            // 이미 요청 내역에 있는 경우 제외
            requestList.find { request ->
                request.receiveUser.id == want.userInfo.id
                        && request.sendUser.id == userId
            } == null
        }.filter { want ->
            // 이미 받은 내역에 있는 경우 제외
            receiveList.find { receive ->
                receive.sendUser.id == want.userInfo.id
                        && receive.movieId == want.movieId
            } == null
        }
        emit(APIResult.Success(filteredList))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    /** 
     * 모든 영화에 대해 보고 싶다고 체크한 유저 목록을 불러옵니다.
     * (본인 및 요청 내역이 있는 경우는 제외)
     * @param userId 유저 ID
     */
    override suspend fun getAllUserWantListExceptMe(userId: String) = flow {
        emit(APIResult.Loading)

        val wantList = movieDataSource.getAllUserWantListExceptMe(userId).getOrThrow()
        val requestList = chatDataSource.getRequestChatList(userId).getOrThrow()
        val receiveList = chatDataSource.getReceiveChatList(userId).getOrThrow()

        val filteredList = wantList.filter { want ->
            // 이미 요청 내역에 있는 경우 제외
            requestList.find { request ->
                request.receiveUser.id == want.userInfo.id
                        && request.movieId == want.movieId
            } == null
        }.filter { want ->
            // 이미 받은 내역에 있는 경우 제외
            receiveList.find { receive ->
                receive.sendUser.id == want.userInfo.id
                        && receive.movieId == want.movieId
            } == null
        }
        emit(APIResult.Success(filteredList))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    /** 
     * 해당 영화에 평점 주기
     * @param movieId 영화 ID
     * @param userInfo 유저 정보
     * @param rating 평점
     */
    override suspend fun voteUserMovieRating(
        movieId: Int,
        userInfo: UserInfo,
        rating: Int
    ) = flow {
        emit(APIResult.Loading)
        val userRate = UserRate(
            movieId = movieId,
            user = userInfo,
            rate = rating
        )
        movieDataSource.voteUserMovieRating(userRate)
            .onSuccess { emit(APIResult.Success(it)) }
            .onFailure { emit(APIResult.NetworkError(it)) }
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    /** 
     * 해당 영화의 평균 평점 불러옵니다.
     * @param movieId 영화 ID
     */
    override suspend fun getAllUserMovieRating(movieId: Int) = flow {
        emit(APIResult.Loading)
        val rateList = movieDataSource.getAllUserMovieRating(movieId).getOrThrow()
        emit(APIResult.Success(rateList.sumOf { it.rate } / rateList.size))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    /** 
     * 해당 영화의 리뷰를 남깁니다.
     * @param movieId 영화 ID
     * @param userInfo 유저 정보
     * @param review 리뷰 내용
     */
    override suspend fun insertUserReview(
        movieId: Int,
        userInfo: UserInfo,
        review: String
    ) = flow {
        emit(APIResult.Loading)
        val userReview = UserReview(
            movieId = movieId,
            user = userInfo,
            content = review
        )
        movieDataSource.insertUserReview(userReview)
            .onSuccess { emit(APIResult.Success(it)) }
            .onFailure { emit(APIResult.NetworkError(it)) }
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    /**
     * 해당 영화의 리뷰를 삭제합니다.
     * @param reviewId 리뷰 ID
     */
    override suspend fun deleteUserReview(reviewId: String) = flow {
        emit(APIResult.Loading)
        movieDataSource.deleteUserReview(reviewId)
            .onSuccess { emit(APIResult.Success(it)) }
            .onFailure { emit(APIResult.NetworkError(it)) }
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    /**
     * 해당 영화의 리뷰 목록을 불러옵니다.
     * @param movieId 영화 ID
     * @param userId 유저 ID
     */
    override suspend fun getUserReview(movieId: Int, userId: String) = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(movieDataSource.getUserReview(movieId, userId).getOrThrow()))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

}