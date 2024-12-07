package org.comon.moviefriends.data.datasource.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.sendbird.android.channel.GroupChannel
import com.sendbird.android.ktx.extension.channel.awaitCreateChannel
import com.sendbird.android.ktx.extension.channel.awaitInvite
import com.sendbird.android.params.GroupChannelCreateParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.comon.moviefriends.data.datasource.lbs.MFLocationService
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.firebase.ProposalFlag
import org.comon.moviefriends.data.model.firebase.RequestChatInfo
import org.comon.moviefriends.data.model.tmdb.ResponseMovieDetailDto
import org.comon.moviefriends.data.model.firebase.UserInfo
import org.comon.moviefriends.data.model.firebase.UserRate
import org.comon.moviefriends.data.model.firebase.UserReview
import org.comon.moviefriends.data.model.firebase.UserWantMovieInfo
import org.comon.moviefriends.presenter.service.FCMSendService
import javax.inject.Inject

class MovieDetailDataSourceImpl @Inject constructor (
    private val db: FirebaseFirestore,
    private val mfLocationService: MFLocationService,
): MovieDetailDataSource {

    private suspend fun getWantThisMovieInfo(movieId: Int, userInfo: UserInfo)
        = db.collection("want_movie")
            .whereEqualTo("movieId", movieId)
            .whereEqualTo("userInfo.id", userInfo.id)
            .get()
            .await()

    override suspend fun getStateWantThisMovie(movieId: Int, userInfo: UserInfo) = flow {
        emit(APIResult.Loading)
        val querySnapshot = getWantThisMovieInfo(movieId, userInfo)
        if(querySnapshot.documents.isEmpty()){
            emit(APIResult.Success(false))
        }else{
            emit(APIResult.Success(true))
        }
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    override suspend fun changeStateWantThisMovie(
        movieDetail: ResponseMovieDetailDto,
        userInfo: UserInfo,
        nowLocation: List<Double>
    ) = flow {
        emit(APIResult.Loading)
        // 위치 정보 가져오기
        val region = mfLocationService
            .getCurrentRegion(nowLocation[1], nowLocation[0])
            .body()?.documents?.find {
                it.regionType == "B"
            }?.region3depthName

        // 기존 데이터가 있는지 확인하기
        val querySnapshot = getWantThisMovieInfo(movieDetail.id, userInfo)
        if(querySnapshot.documents.isEmpty()){
            val wantMovieInfo = UserWantMovieInfo(
                movieId = movieDetail.id,
                moviePosterPath = movieDetail.posterPath,
                userInfo = userInfo,
                userLocation = region ?: "위치 없음",
            )
            db.collection("want_movie").add(wantMovieInfo).await()
            emit(APIResult.Success(true))
        }else{
            // 기존 데이터가 있다면 삭제
            querySnapshot.first().reference.delete().await()
            emit(APIResult.Success(false))
        }
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    override suspend fun getUserWantList(movieId: Int, userId: String) = flow {
        emit(APIResult.Loading)

        val querySnapshot = db.collection("want_movie")
            .whereEqualTo("movieId", movieId)
            .whereNotEqualTo("userInfo.id", userId)
            .get().await()
        val userWantList = querySnapshot.toObjects(UserWantMovieInfo::class.java)

        val requestQuerySnapshot = db.collection("request_chat")
            .whereEqualTo("sendUser.id", userId)
            .whereEqualTo("movieId", movieId)
            .whereNotEqualTo("proposalFlag", ProposalFlag.WAITING.str)
            .get().await()

        val receiveQuerySnapshot = db.collection("request_chat")
            .whereEqualTo("receiveUser.id", userId)
            .whereEqualTo("movieId", movieId)
            .get().await()

        val myRequestList = requestQuerySnapshot.toObjects(RequestChatInfo::class.java)
        val myReceiveList = receiveQuerySnapshot.toObjects(RequestChatInfo::class.java)
        val filteredList = userWantList.filter { want ->
            // 이미 요청 내역에 있는 경우 제외
            myRequestList.find { request ->
                request.receiveUser.id == want.userInfo.id
                        && request.movieId == want.movieId
            } == null
        }.filter { want ->
            // 이미 받은 내역에 있는 경우 제외
            myReceiveList.find { receive ->
                receive.sendUser.id == want.userInfo.id
                        && receive.movieId == want.movieId
            } == null
        }

        emit(APIResult.Success(filteredList))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    override suspend fun getAllUserWantList(userId: String) = flow {
        emit(APIResult.Loading)
        val wantQuerySnapshot = db.collection("want_movie")
            .whereNotEqualTo("userInfo.id", userId)
            .get().await()
        // 요청 내역도 같이 불러오기
        val requestQuerySnapshot = db.collection("request_chat")
            .whereEqualTo("sendUser.id", userId)
            .get().await()
        // 받은 내역도 같이 불러오기
        val receiveQuerySnapshot = db.collection("request_chat")
            .whereEqualTo("receiveUser.id", userId)
            .get().await()
        val wantList = wantQuerySnapshot.toObjects(UserWantMovieInfo::class.java)
        val myRequestList = requestQuerySnapshot.toObjects(RequestChatInfo::class.java)
        val myReceiveList = receiveQuerySnapshot.toObjects(RequestChatInfo::class.java)
        val filteredList = wantList.filter { want ->
            // 이미 요청 내역에 있는 경우 제외
            myRequestList.find { request ->
                request.receiveUser.id == want.userInfo.id
                        && request.movieId == want.movieId
            } == null
        }.filter { want ->
            // 이미 받은 내역에 있는 경우 제외
            myReceiveList.find { receive ->
                receive.sendUser.id == want.userInfo.id
                        && receive.movieId == want.movieId
            } == null
        }
        emit(APIResult.Success(filteredList))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    override suspend fun getMyRequestList(userId: String) = flow {
        emit(APIResult.Loading)
        val querySnapshot = db.collection("request_chat")
            .whereEqualTo("sendUser.id", userId)
            .whereEqualTo("status", true)
            .orderBy("createdDate", Query.Direction.DESCENDING)
            .get().await()
        val requestList = querySnapshot.toObjects(RequestChatInfo::class.java)
        emit(APIResult.Success(requestList))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    override suspend fun getMyReceiveList(userId: String) = flow {
        emit(APIResult.Loading)
        val querySnapshot = db.collection("request_chat")
            .whereEqualTo("receiveUser.id", userId)
            .whereEqualTo("status", true)
            .orderBy("createdDate", Query.Direction.DESCENDING)
            .get().await()
        val requestList = querySnapshot.toObjects(RequestChatInfo::class.java)
        emit(APIResult.Success(requestList))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    override suspend fun getConfirmedList(userId: String) = flow {
        emit(APIResult.Loading)

        val querySnapshot1 = db.collection("request_chat")
            .whereEqualTo("receiveUser.id", userId)
            .whereEqualTo("status", true)
            .whereEqualTo("proposalFlag", ProposalFlag.CONFIRMED.str)
            .orderBy("createdDate", Query.Direction.DESCENDING)
            .get().await()
        val requestList1 = querySnapshot1.toObjects(RequestChatInfo::class.java)

        val querySnapshot2 = db.collection("request_chat")
            .whereEqualTo("sendUser.id", userId)
            .whereEqualTo("status", true)
            .whereEqualTo("proposalFlag", ProposalFlag.CONFIRMED.str)
            .orderBy("createdDate", Query.Direction.DESCENDING)
            .get().await()
        val requestList2 = querySnapshot2.toObjects(RequestChatInfo::class.java)

        requestList1.addAll(requestList2)

        emit(APIResult.Success(requestList1))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    override fun requestWatchTogether(
        requestChatInfo: RequestChatInfo,
    ) = runCatching {
        db.collection("request_chat")
            .whereEqualTo("movieId", requestChatInfo.movieId)
            .whereEqualTo("sendUser.id", requestChatInfo.sendUser.id)
            .whereEqualTo("receiveUser.id", requestChatInfo.receiveUser.id).get()
            .addOnSuccessListener { result ->
                if(result.isEmpty){
                    db.collection("request_chat").add(requestChatInfo)
                    CoroutineScope(Dispatchers.IO).launch {
                        FCMSendService.sendNotificationToToken(
                            requestChatInfo.receiveUser.fcmToken,
                            "영화 같이 보기 요청",
                            "${requestChatInfo.sendUser.nickName}님이 같이 영화를 보고 싶어 합니다."
                        )
                    }
                }else{
                    result.documents.first().reference.delete()
                }
            }
            .addOnFailureListener {
                Log.e("requestWatchTogether", "$it")
            }
    }

    override suspend fun voteUserMovieRating(
        movieId: Int,
        userInfo: UserInfo,
        rating: Int
    ): Flow<APIResult<Int>> = flow {
        emit(APIResult.Loading)
        val userRate = UserRate(
            movieId = movieId,
            rate = rating,
            user = userInfo,
        )
        db.collection("user_rate").document("${movieId}_${userInfo.id}").set(userRate).await()
        emit(APIResult.Success(rating))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    override suspend fun getAllUserMovieRating(movieId: Int) = flow {
        emit(APIResult.Loading)
        val querySnapshot = db.collection("user_rate")
            .whereEqualTo("movieId", movieId).get().await()
        emit(APIResult.Success(querySnapshot.toObjects(UserRate::class.java)))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    override suspend fun getUserMovieRating(
        movieId: Int,
        userInfo: UserInfo
    ): Flow<APIResult<Int>> = flow {
        emit(APIResult.Loading)
        val querySnapshot = db.collection("user_rate")
            .whereEqualTo("movieId", movieId)
            .whereEqualTo("user.id", userInfo.id)
            .get().await()
        val rate = querySnapshot.documents.first().toObject(UserRate::class.java)?.rate ?: 0
        emit(APIResult.Success(rate))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    override suspend fun insertUserReview(
        movieId: Int,
        userInfo: UserInfo,
        review: String
    ): Flow<APIResult<Boolean>> = flow {
        emit(APIResult.Loading)
        val userReview = UserReview(
            movieId = movieId,
            content = review,
            user = userInfo,
        )
        db.collection("user_review").add(userReview).await()
        emit(APIResult.Success(true))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    override suspend fun deleteUserReview(
        reviewId: String,
    ): Flow<APIResult<Boolean>> = flow {
        emit(APIResult.Loading)
        val querySnapshot = db.collection("user_review").whereEqualTo("id", reviewId).get().await()
        if(querySnapshot.documents.isEmpty()){
            emit(APIResult.Success(false))
        }else{
            querySnapshot.documents.first().reference.delete().await()
            emit(APIResult.Success(true))
        }
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    override suspend fun getUserReview(movieId: Int, userId: String) = flow {
        emit(APIResult.Loading)
        val querySnapshot = db.collection("user_review")
            .whereEqualTo("movieId", movieId)
            .orderBy("createdDate", Query.Direction.ASCENDING)
            .get().await()
        emit(APIResult.Success(querySnapshot.toObjects(UserReview::class.java)))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    override suspend fun getAllChatRequestCount(userId: String) = flow {
        emit(APIResult.Loading)

        val sendCount = db.collection("request_chat")
            .whereEqualTo("sendUser.id", userId)
            .whereEqualTo("proposalFlag", ProposalFlag.WAITING.str)
            .get().await()
            .documents.size

        val receiveCount = db.collection("request_chat")
            .whereEqualTo("receiveUser.id", userId)
            .whereEqualTo("proposalFlag", ProposalFlag.WAITING.str)
            .get().await()
            .documents.size

        val countMap = mutableMapOf(
            "sendCount" to sendCount,
            "receiveCount" to receiveCount,
        )

        emit(APIResult.Success(countMap))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    override suspend fun confirmRequest(userInfo: UserInfo, requestChatInfo: RequestChatInfo) = flow {
        emit(APIResult.Loading)

        // 채널 생성
        val params = GroupChannelCreateParams().apply {
            name = "${requestChatInfo.sendUser.nickName} 님과 ${requestChatInfo.receiveUser.nickName} 님의 대화"
            coverUrl = requestChatInfo.moviePosterPath
            operatorUserIds = listOf(requestChatInfo.sendUser.sendBirdId, requestChatInfo.receiveUser.sendBirdId)         // Or operators = listOfOperators
        }
        val createdChannel = GroupChannel.awaitCreateChannel(params)
        createdChannel.awaitInvite(listOf(requestChatInfo.sendUser.sendBirdId))
        val channelUrl = createdChannel.url

        // 요청 승인으로 업데이트
        db.collection("request_chat").whereEqualTo("id", requestChatInfo.id).get().await()
            .documents.first().reference.update("proposalFlag", ProposalFlag.CONFIRMED.str, "channelUrl", channelUrl).await()

        // 승인 앱 푸시
        FCMSendService.sendNotificationToToken(requestChatInfo.sendUser.fcmToken, "영화 같이 보기 요청 승인", "${requestChatInfo.receiveUser.nickName}님이 같이 보기 요청을 승인하셨습니다.")

        emit(APIResult.Success(true))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    override suspend fun denyRequest(requestChatInfo: RequestChatInfo) = flow {
        emit(APIResult.Loading)

        // 요청 승인으로 업데이트
        db.collection("request_chat").whereEqualTo("id", requestChatInfo.id).get().await()
            .documents.first().reference.update("proposalFlag", ProposalFlag.DENIED.str).await()

        // 거절 앱 푸시
        FCMSendService.sendNotificationToToken(requestChatInfo.sendUser.fcmToken, "영화 같이 보기 요청 거절", "${requestChatInfo.receiveUser.nickName}님이 같이 보기 요청을 거절하셨습니다.")

        emit(APIResult.Success(true))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

}