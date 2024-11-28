package org.comon.moviefriends.data.datasource.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import org.comon.moviefriends.data.model.firebase.ProposalFlag
import org.comon.moviefriends.data.model.firebase.RequestChatInfo
import org.comon.moviefriends.data.model.firebase.UserInfo
import org.comon.moviefriends.data.model.firebase.UserRate
import org.comon.moviefriends.data.model.firebase.UserReview
import org.comon.moviefriends.data.model.firebase.UserWantMovieInfo
import javax.inject.Inject

class MovieDetailDataSourceImpl @Inject constructor (
    private val db: FirebaseFirestore,
): MovieDetailDataSource {

    override suspend fun getWantThisMovieInfo(movieId: Int, userInfo: UserInfo): Result<UserWantMovieInfo>
        = runCatching {
            val querySnapshot = db.collection("want_movie")
                .whereEqualTo("movieId", movieId)
                .whereEqualTo("userInfo.id", userInfo.id)
                .get()
                .await()
            // first()는 해당 querySnapshot 결과가 비어있을 경우 NoSuchElementException를 던짐
            querySnapshot.first().toObject(UserWantMovieInfo::class.java)
        }

    override suspend fun getStateWantThisMovie(movieId: Int, userInfo: UserInfo): Boolean {
        val result = getWantThisMovieInfo(movieId, userInfo)
        try {
            // 해당 객체가 있으면 true를 반환하고 없다면 NoSuchElementException를 받기 때문에 try-catch로 감싸준다.
            result.getOrThrow()
            return true
        }catch (e: NoSuchElementException) {
            return false
        }
    }

    override suspend fun addUserWantMovieInfo(userWantMovieInfo: UserWantMovieInfo): Result<Boolean>
        = runCatching {
            db.collection("want_movie").add(userWantMovieInfo).await()
            true
        }

    override suspend fun deleteUserWantMovieInfo(movieId: Int, userInfo: UserInfo): Result<Boolean>
        = runCatching {
            db.collection("want_movie")
                .whereEqualTo("movieId", movieId)
                .whereEqualTo("userInfo.id", userInfo.id)
                .get().await()
                .documents.first().reference.delete().await()
            true
        }

//    override suspend fun changeStateWantThisMovie(
//        movieDetail: ResponseMovieDetailDto,
//        userInfo: UserInfo,
//        nowLocation: List<Double>
//    ) = flow {
//        emit(APIResult.Loading)
//        // 위치 정보 가져오기
//        val region = mfLocationService
//            .getCurrentRegion(nowLocation[1], nowLocation[0])
//            .body()?.documents?.find {
//                it.regionType == "B"
//            }?.region3depthName
//
//        // 기존 데이터가 있는지 확인하기
//        val querySnapshot = getWantThisMovieInfo(movieDetail.id, userInfo)
//        if(querySnapshot.documents.isEmpty()){
//            val wantMovieInfo = UserWantMovieInfo(
//                movieId = movieDetail.id,
//                moviePosterPath = movieDetail.posterPath,
//                userInfo = userInfo,
//                userLocation = region ?: "위치 없음",
//            )
//            db.collection("want_movie").add(wantMovieInfo).await()
//            emit(APIResult.Success(true))
//        }else{
//            // 기존 데이터가 있다면 삭제
//            querySnapshot.first().reference.delete().await()
//            emit(APIResult.Success(false))
//        }
//    }.catch{
//        error -> emit(APIResult.NetworkError(error))
//    }

    override suspend fun getConfirmedSendRequestList(movieId: Int, userId: String): Result<List<RequestChatInfo>>
        = runCatching {
            val requestQuerySnapshot = db.collection("request_chat")
                .whereEqualTo("sendUser.id", userId)
                .whereEqualTo("movieId", movieId)
                .whereNotEqualTo("proposalFlag", ProposalFlag.WAITING.str)
                .get().await()
            requestQuerySnapshot.toObjects(RequestChatInfo::class.java)
        }

    override suspend fun getConfirmedReceiveRequestList(movieId: Int, userId: String): Result<List<RequestChatInfo>>
        = runCatching {
            val receiveQuerySnapshot = db.collection("request_chat")
                .whereEqualTo("receiveUser.id", userId)
                .whereEqualTo("movieId", movieId)
                .get().await()
            receiveQuerySnapshot.toObjects(RequestChatInfo::class.java)
        }

    override suspend fun getUserWantMovieListExceptMe(movieId: Int, userId: String): Result<List<UserWantMovieInfo>>
        = runCatching {
            val querySnapshot = db.collection("want_movie")
                .whereEqualTo("movieId", movieId)
                .whereNotEqualTo("userInfo.id", userId)
                .get().await()
            querySnapshot.toObjects(UserWantMovieInfo::class.java)
        }

//        emit(APIResult.Loading)
//
//        val querySnapshot = db.collection("want_movie")
//            .whereEqualTo("movieId", movieId)
//            .whereNotEqualTo("userInfo.id", userId)
//            .get().await()
//        val userWantList = querySnapshot.toObjects(UserWantMovieInfo::class.java)
//
//        val requestQuerySnapshot = db.collection("request_chat")
//            .whereEqualTo("sendUser.id", userId)
//            .whereEqualTo("movieId", movieId)
//            .whereNotEqualTo("proposalFlag", ProposalFlag.WAITING.str)
//            .get().await()
//
//        val receiveQuerySnapshot = db.collection("request_chat")
//            .whereEqualTo("receiveUser.id", userId)
//            .whereEqualTo("movieId", movieId)
//            .get().await()
//
//        val myRequestList = requestQuerySnapshot.toObjects(RequestChatInfo::class.java)
//        val myReceiveList = receiveQuerySnapshot.toObjects(RequestChatInfo::class.java)
//        val filteredList = userWantList.filter { want ->
//            // 이미 요청 내역에 있는 경우 제외
//            myRequestList.find { request ->
//                request.receiveUser.id == want.userInfo.id
//                        && request.movieId == want.movieId
//            } == null
//        }.filter { want ->
//            // 이미 받은 내역에 있는 경우 제외
//            myReceiveList.find { receive ->
//                receive.sendUser.id == want.userInfo.id
//                        && receive.movieId == want.movieId
//            } == null
//        }
//
//        emit(APIResult.Success(filteredList))
//    }.catch {
//        emit(APIResult.NetworkError(it))
//    }

    override suspend fun getAllUserWantListExceptMe(userId: String): Result<List<UserWantMovieInfo>>
        = runCatching {
            val wantQuerySnapshot = db.collection("want_movie")
                .whereNotEqualTo("userInfo.id", userId)
                .get().await()
            wantQuerySnapshot.toObjects(UserWantMovieInfo::class.java)
        }

    override suspend fun getRequestChatList(userId: String): Result<List<RequestChatInfo>>
        = runCatching {
            val requestQuerySnapshot = db.collection("request_chat")
                .whereEqualTo("sendUser.id", userId)
                .get().await()
            requestQuerySnapshot.toObjects(RequestChatInfo::class.java)
        }

    override suspend fun getReceiveChatList(userId: String): Result<List<RequestChatInfo>>
        = runCatching {
            val receiveQuerySnapshot = db.collection("request_chat")
                .whereEqualTo("receiveUser.id", userId)
                .get().await()
            receiveQuerySnapshot.toObjects(RequestChatInfo::class.java)
        }

//    override suspend fun getAllUserWantList(userId: String) = flow {
//        emit(APIResult.Loading)
//        val wantQuerySnapshot = db.collection("want_movie")
//            .whereNotEqualTo("userInfo.id", userId)
//            .get().await()
//        // 요청 내역도 같이 불러오기
//        val requestQuerySnapshot = db.collection("request_chat")
//            .whereEqualTo("sendUser.id", userId)
//            .get().await()
//        // 받은 내역도 같이 불러오기
//        val receiveQuerySnapshot = db.collection("request_chat")
//            .whereEqualTo("receiveUser.id", userId)
//            .get().await()
//        val wantList = wantQuerySnapshot.toObjects(UserWantMovieInfo::class.java)
//        val myRequestList = requestQuerySnapshot.toObjects(RequestChatInfo::class.java)
//        val myReceiveList = receiveQuerySnapshot.toObjects(RequestChatInfo::class.java)
//        val filteredList = wantList.filter { want ->
//            // 이미 요청 내역에 있는 경우 제외
//            myRequestList.find { request ->
//                request.receiveUser.id == want.userInfo.id
//                        && request.movieId == want.movieId
//            } == null
//        }.filter { want ->
//            // 이미 받은 내역에 있는 경우 제외
//            myReceiveList.find { receive ->
//                receive.sendUser.id == want.userInfo.id
//                        && receive.movieId == want.movieId
//            } == null
//        }
//        emit(APIResult.Success(filteredList))
//    }.catch {
//        emit(APIResult.NetworkError(it))
//    }

    override suspend fun getMySendRequestList(userId: String): Result<List<RequestChatInfo>>
        = runCatching {
            val querySnapshot = db.collection("request_chat")
                .whereEqualTo("sendUser.id", userId)
                .whereEqualTo("status", true)
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .get().await()
            querySnapshot.toObjects(RequestChatInfo::class.java)
        }

    override suspend fun getMyReceiveRequestList(userId: String): Result<List<RequestChatInfo>>
        = runCatching {
            val querySnapshot = db.collection("request_chat")
                .whereEqualTo("receiveUser.id", userId)
                .whereEqualTo("status", true)
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .get().await()
            querySnapshot.toObjects(RequestChatInfo::class.java)
        }

    override suspend fun getMyConfirmedReceiveRequestList(userId: String): Result<List<RequestChatInfo>>
        = runCatching {
            val querySnapshot = db.collection("request_chat")
                .whereEqualTo("receiveUser.id", userId)
                .whereEqualTo("status", true)
                .whereEqualTo("proposalFlag", ProposalFlag.CONFIRMED.str)
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .get().await()
            querySnapshot.toObjects(RequestChatInfo::class.java)
        }

    override suspend fun getMyConfirmedSendRequestList(userId: String): Result<List<RequestChatInfo>>
        = runCatching {
            val querySnapshot = db.collection("request_chat")
                .whereEqualTo("sendUser.id", userId)
                .whereEqualTo("status", true)
                .whereEqualTo("proposalFlag", ProposalFlag.CONFIRMED.str)
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .get().await()
            querySnapshot.toObjects(RequestChatInfo::class.java)
        }

//    override suspend fun getConfirmedList(userId: String) = flow {
//        emit(APIResult.Loading)
//
//        val querySnapshot1 = db.collection("request_chat")
//            .whereEqualTo("receiveUser.id", userId)
//            .whereEqualTo("status", true)
//            .whereEqualTo("proposalFlag", ProposalFlag.CONFIRMED.str)
//            .orderBy("createdDate", Query.Direction.DESCENDING)
//            .get().await()
//        val requestList1 = querySnapshot1.toObjects(RequestChatInfo::class.java)
//
//        val querySnapshot2 = db.collection("request_chat")
//            .whereEqualTo("sendUser.id", userId)
//            .whereEqualTo("status", true)
//            .whereEqualTo("proposalFlag", ProposalFlag.CONFIRMED.str)
//            .orderBy("createdDate", Query.Direction.DESCENDING)
//            .get().await()
//        val requestList2 = querySnapshot2.toObjects(RequestChatInfo::class.java)
//
//        requestList1.addAll(requestList2)
//
//        emit(APIResult.Success(requestList1))
//    }.catch {
//        emit(APIResult.NetworkError(it))
//    }

    override suspend fun getRequestAlreadyExist(requestChatInfo: RequestChatInfo): Result<RequestChatInfo>
        = runCatching {
            val querySnapshot = db.collection("request_chat")
                .whereEqualTo("movieId", requestChatInfo.movieId)
                .whereEqualTo("sendUser.id", requestChatInfo.sendUser.id)
                .whereEqualTo("receiveUser.id", requestChatInfo.receiveUser.id).get().await()
            querySnapshot.first().toObject(RequestChatInfo::class.java)
        }

    override suspend fun insertRequestChatInfo(requestChatInfo: RequestChatInfo): Result<Boolean>
        = runCatching {
            db.collection("request_chat").add(requestChatInfo).await()
            true
        }

    override suspend fun deleteRequestChatInfo(requestChatInfo: RequestChatInfo): Result<Boolean>
        = runCatching {
            val querySnapshot = db.collection("request_chat").whereEqualTo("id", requestChatInfo.id).get().await()
            querySnapshot.first().reference.delete().await()
            true
        }

//    override fun requestWatchTogether(
//        requestChatInfo: RequestChatInfo,
//    ) = runCatching {
//        db.collection("request_chat")
//            .whereEqualTo("movieId", requestChatInfo.movieId)
//            .whereEqualTo("sendUser.id", requestChatInfo.sendUser.id)
//            .whereEqualTo("receiveUser.id", requestChatInfo.receiveUser.id).get()
//            .addOnSuccessListener { result ->
//                if(result.isEmpty){
//                    db.collection("request_chat").add(requestChatInfo)
//                    CoroutineScope(Dispatchers.IO).launch {
//                        FCMSendService.sendNotificationToToken(
//                            requestChatInfo.receiveUser.fcmToken,
//                            "영화 같이 보기 요청",
//                            "${requestChatInfo.sendUser.nickName}님이 같이 영화를 보고 싶어 합니다."
//                        )
//                    }
//                }else{
//                    result.documents.first().reference.delete()
//                }
//            }
//            .addOnFailureListener {
//                Log.e("requestWatchTogether", "$it")
//            }
//    }


    override suspend fun voteUserMovieRating(userRate: UserRate): Result<Boolean>
        = runCatching {
            db.collection("user_rate").document("${userRate.movieId}_${userRate.user.id}").set(userRate).await()
            true
        }

    override suspend fun getAllUserMovieRating(movieId: Int): Result<List<UserRate>>
        = runCatching {
            val querySnapshot = db.collection("user_rate")
                .whereEqualTo("movieId", movieId).get().await()
            querySnapshot.toObjects(UserRate::class.java)
        }

    override suspend fun getUserMovieRating(
        movieId: Int,
        userInfo: UserInfo
    ): Result<UserRate>
        = runCatching {
            val querySnapshot = db.collection("user_rate")
                .whereEqualTo("movieId", movieId)
                .whereEqualTo("user.id", userInfo.id)
                .get().await()
            querySnapshot.first().toObject(UserRate::class.java)
        }

    override suspend fun insertUserReview(userReview: UserReview): Result<Boolean>
        = runCatching {
            db.collection("user_review").add(userReview).await()
            true
        }

    override suspend fun deleteUserReview(reviewId: String): Result<Boolean>
        = runCatching {
            db.collection("user_review")
                .whereEqualTo("id", reviewId)
                .get().await()
                .first().reference
                .delete().await()
            true
        }

    override suspend fun getUserReview(movieId: Int, userId: String): Result<List<UserReview>>
        = runCatching {
            val querySnapshot = db.collection("user_review")
                .whereEqualTo("movieId", movieId)
                .orderBy("createdDate", Query.Direction.ASCENDING)
                .get().await()
            querySnapshot.toObjects(UserReview::class.java)
        }

    override suspend fun getWaitingSendRequestCount(userId: String): Result<Int>
        = runCatching {
            db.collection("request_chat")
                .whereEqualTo("sendUser.id", userId)
                .whereEqualTo("proposalFlag", ProposalFlag.WAITING.str)
                .get().await()
                .documents.size
        }

    override suspend fun getWaitingReceiveRequestCount(userId: String): Result<Int>
        = runCatching {
            db.collection("request_chat")
                .whereEqualTo("receiveUser.id", userId)
                .whereEqualTo("proposalFlag", ProposalFlag.WAITING.str)
                .get().await()
                .documents.size
        }

    override suspend fun updateRequestConfirmed(requestId: String, channelUrl: String): Result<Boolean>
        = runCatching {
            // 요청 승인으로 업데이트
            db.collection("request_chat").whereEqualTo("id", requestId)
                .get().await()
                .documents.first().reference.update(
                    "proposalFlag", ProposalFlag.CONFIRMED.str,
                    "channelUrl", channelUrl
                ).await()
            true
        }

    override suspend fun updateRequestDenied(requestId: String): Result<Boolean>
        = runCatching {
            // 요청 승인으로 업데이트
            db.collection("request_chat").whereEqualTo("id", requestId)
                .get().await()
                .documents.first().reference.update(
                    "proposalFlag", ProposalFlag.DENIED.str,
                ).await()
            true
        }

//    override suspend fun confirmRequest(userInfo: UserInfo, requestChatInfo: RequestChatInfo) = flow {
//        emit(APIResult.Loading)
//
//        // 채널 생성
//        val params = GroupChannelCreateParams().apply {
//            name = "${requestChatInfo.sendUser.nickName} 님과 ${requestChatInfo.receiveUser.nickName} 님의 대화"
//            coverUrl = requestChatInfo.moviePosterPath
//            operatorUserIds = listOf(requestChatInfo.sendUser.sendBirdId, requestChatInfo.receiveUser.sendBirdId)         // Or operators = listOfOperators
//        }
//        val createdChannel = GroupChannel.awaitCreateChannel(params)
//        createdChannel.awaitInvite(listOf(requestChatInfo.sendUser.sendBirdId))
//        val channelUrl = createdChannel.url
//
//        // 요청 승인으로 업데이트
//        db.collection("request_chat").whereEqualTo("id", requestChatInfo.id).get().await()
//            .documents.first().reference.update("proposalFlag", ProposalFlag.CONFIRMED.str, "channelUrl", channelUrl).await()
//
//        // 승인 앱 푸시
//        FCMSendService.sendNotificationToToken(requestChatInfo.sendUser.fcmToken, "영화 같이 보기 요청 승인", "${requestChatInfo.receiveUser.nickName}님이 같이 보기 요청을 승인하셨습니다.")
//
//        emit(APIResult.Success(true))
//    }.catch {
//        emit(APIResult.NetworkError(it))
//    }
//
//    override suspend fun denyRequest(requestChatInfo: RequestChatInfo) = flow {
//        emit(APIResult.Loading)
//
//        // 요청 승인으로 업데이트
//        db.collection("request_chat").whereEqualTo("id", requestChatInfo.id).get().await()
//            .documents.first().reference.update("proposalFlag", ProposalFlag.DENIED.str).await()
//
//        // 거절 앱 푸시
//        FCMSendService.sendNotificationToToken(requestChatInfo.sendUser.fcmToken, "영화 같이 보기 요청 거절", "${requestChatInfo.receiveUser.nickName}님이 같이 보기 요청을 거절하셨습니다.")
//
//        emit(APIResult.Success(true))
//    }.catch {
//        emit(APIResult.NetworkError(it))
//    }

}