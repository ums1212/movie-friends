package org.comon.moviefriends.data.datasource.firebase

import org.comon.moviefriends.data.model.firebase.RequestChatInfo
import org.comon.moviefriends.data.model.firebase.UserInfo
import org.comon.moviefriends.data.model.firebase.UserRate
import org.comon.moviefriends.data.model.firebase.UserReview
import org.comon.moviefriends.data.model.firebase.UserWantMovieInfo

interface MovieDetailDataSource {
    // 해당 유저의 "이 영화를 보고 싶다" 데이터 가져오기
    suspend fun getWantThisMovieInfo(movieId: Int, userInfo: UserInfo): Result<UserWantMovieInfo>
    // 해당 유저의 "이 영화를 보고 싶다" 체크 여부 확인
    suspend fun getStateWantThisMovie(movieId: Int, userInfo: UserInfo): Boolean
    // 해당 유저의 "이 영화를 보고 싶다" 체크 시 데이터 입력
    suspend fun addUserWantMovieInfo(userWantMovieInfo: UserWantMovieInfo): Result<Boolean>
    // 해당 유저의 "이 영화를 보고 싶다" 체크 해제 시 데이터 삭제
    suspend fun deleteUserWantMovieInfo(movieId: Int, userInfo: UserInfo): Result<Boolean>
    // 해당 영화의 요청 승인한 대화 요청한 리스트 불러오기
    suspend fun getConfirmedSendRequestList(movieId: Int, userId: String): Result<List<RequestChatInfo>>
    // 해당 영화의 요청 승인한 대화 요청 받은 리스트 불러오기
    suspend fun getConfirmedReceiveRequestList(movieId: Int, userId: String): Result<List<RequestChatInfo>>
    // 본인을 제외하고 해당 영화를 "이 영화를 보고 싶다" 체크한 유저 리스트 불러오기
    suspend fun getUserWantMovieListExceptMe(movieId: Int, userId: String): Result<List<UserWantMovieInfo>>
    // 본인을 제외하고 모든 영화를 "이 영화를 보고 싶다" 체크한 유저 리스트 불러오기
    suspend fun getAllUserWantListExceptMe(userId: String): Result<List<UserWantMovieInfo>>
    // 본인이 요청한 채팅 요청 리스트 불러오기
    suspend fun getRequestChatList(userId: String): Result<List<RequestChatInfo>>
    // 본인이 받은 채팅 요청 리스트 불러오기
    suspend fun getReceiveChatList(userId: String): Result<List<RequestChatInfo>>
    // 본인이 요청한 채팅 요청 리스트 내림차 순으로 불러오기
    suspend fun getMySendRequestList(userId: String): Result<List<RequestChatInfo>>
    // 본인이 받은 채팅 요청 리스트 내림차 순으로 불러오기
    suspend fun getMyReceiveRequestList(userId: String): Result<List<RequestChatInfo>>
    // 본인이 승인한 채팅 요청 리스트 내림차 순으로 불러오기
    suspend fun getMyConfirmedReceiveRequestList(userId: String): Result<List<RequestChatInfo>>
    // 상대가 승인한 채팅 요청 리스트 내림차 순으로 불러오기
    suspend fun getMyConfirmedSendRequestList(userId: String): Result<List<RequestChatInfo>>
    // 해당 채팅 요청 불러오기
    suspend fun getRequestAlreadyExist(requestChatInfo: RequestChatInfo): Result<RequestChatInfo>
    // 채팅 요청 데이터 저장
    suspend fun insertRequestChatInfo(requestChatInfo: RequestChatInfo): Result<Boolean>
    // 채팅 요청 데이터 삭제
    suspend fun deleteRequestChatInfo(requestChatInfo: RequestChatInfo): Result<Boolean>
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
    // 승인 대기중인 요청한 채팅 건 수 불러오기
    suspend fun getWaitingSendRequestCount(userId: String): Result<Int>
    // 승인 대기중인 받은 채팅 건 수 불러오기
    suspend fun getWaitingReceiveRequestCount(userId: String): Result<Int>
    // 요청 받은 채팅 상태 승인으로 데이터 업데이트
    suspend fun updateRequestConfirmed(requestId: String, channelUrl: String): Result<Boolean>
    // 요청 받은 채팅 상태 거절로 데이터 업데이트
    suspend fun updateRequestDenied(requestId: String): Result<Boolean>
}