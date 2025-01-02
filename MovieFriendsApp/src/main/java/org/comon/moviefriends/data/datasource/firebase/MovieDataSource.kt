package org.comon.moviefriends.data.datasource.firebase

import org.comon.moviefriends.data.entity.firebase.UserInfo
import org.comon.moviefriends.data.entity.firebase.UserRate
import org.comon.moviefriends.data.entity.firebase.UserReview
import org.comon.moviefriends.data.entity.firebase.UserWantMovieInfo

interface MovieDataSource {
    // 해당 유저의 "이 영화를 보고 싶다" 데이터 가져오기
    suspend fun getWantThisMovieInfo(movieId: Int, userInfo: UserInfo): Result<UserWantMovieInfo>
    // 해당 유저의 "이 영화를 보고 싶다" 체크 여부 확인
    suspend fun getStateWantThisMovie(movieId: Int, userInfo: UserInfo): Boolean
    // 해당 유저의 "이 영화를 보고 싶다" 체크 시 데이터 입력
    suspend fun addUserWantMovieInfo(userWantMovieInfo: UserWantMovieInfo): Result<Boolean>
    // 해당 유저의 "이 영화를 보고 싶다" 체크 해제 시 데이터 삭제
    suspend fun deleteUserWantMovieInfo(movieId: Int, userInfo: UserInfo): Result<Boolean>
    // 본인을 제외하고 해당 영화를 "이 영화를 보고 싶다" 체크한 유저 리스트 불러오기
    suspend fun getUserWantMovieListExceptMe(movieId: Int, userId: String): Result<List<UserWantMovieInfo>>
    // 본인을 제외하고 모든 영화를 "이 영화를 보고 싶다" 체크한 유저 리스트 불러오기
    suspend fun getAllUserWantListExceptMe(userId: String): Result<List<UserWantMovieInfo>>
    // 해당 영화 평점 데이터 저장
    suspend fun voteUserMovieRating(userRate: UserRate): Result<Boolean>
    // 해당 영화 모든 평점 불러오기
    suspend fun getAllUserMovieRating(movieId: Int): Result<List<UserRate>>
    // 해당 영화의 유저가 남긴 평점 불러오기
    suspend fun getUserMovieRating(movieId: Int, userInfo: UserInfo): Result<UserRate>
    // 영화 리뷰 데이터 저장
    suspend fun insertUserReview(userReview: UserReview): Result<Boolean>
    // 영화 리뷰 데이터 삭제
    suspend fun deleteUserReview(reviewId: String): Result<Boolean>
    // 해당 영화에 대한 리뷰 리스트 불러오기
    suspend fun getUserReview(movieId: Int, userId: String): Result<List<UserReview>>
}