package org.comon.moviefriends.data.repo

import com.sendbird.android.channel.GroupChannel
import com.sendbird.android.ktx.extension.channel.awaitCreateChannel
import com.sendbird.android.ktx.extension.channel.awaitInvite
import com.sendbird.android.params.GroupChannelCreateParams
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.comon.moviefriends.data.datasource.firebase.ChatDataSource
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.firebase.RequestChatInfo
import org.comon.moviefriends.domain.repo.ChatRepository
import org.comon.moviefriends.presenter.service.FCMSendService
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatDataSource: ChatDataSource,
): ChatRepository {

    /**
     * 현재 대화중인 채팅 리스트를 불러옵니다.
     * @param userId 유저 아이디
     */
    override suspend fun loadChatList(userId: String) = flow {
        emit(APIResult.Loading)
        val confirmedReceiveList = chatDataSource.getMyConfirmedReceiveRequestList(userId).getOrThrow()
        val confirmedSendList = chatDataSource.getMyConfirmedSendRequestList(userId).getOrThrow()
        emit(APIResult.Success(confirmedReceiveList + confirmedSendList))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    /**
     * 같이보기 요청 내역을 불러옵니다.
     * @param userId 유저 아이디
     */
    override suspend fun getRequestChatList(userId: String) = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(chatDataSource.getRequestChatList(userId).getOrThrow()))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    /**
     * 같이보기 받은 내역을 불러옵니다.
     * @param userId 유저 아이디
     */
    override suspend fun getReceiveChatList(userId: String) = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(chatDataSource.getReceiveChatList(userId).getOrThrow()))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    /**
     * 같이보기 요청하기
     * (이미 요청을 한 경우에는 요청이 취소됩니다.)
     * @param requestChatInfo 같이 보기 요청 정보
     */
    override suspend fun requestWatchTogether(
        requestChatInfo: RequestChatInfo
    ) = flow {
        emit(APIResult.Loading)
        // 기존 요청 데이터가 있는지 확인
        chatDataSource.getRequestAlreadyExist(requestChatInfo)
            .onSuccess {
                // 기존 요청 데이터가 있는 경우 삭제
                chatDataSource.deleteRequestChatInfo(requestChatInfo.id).getOrThrow()
                emit(APIResult.Success(false))
            }
            .onFailure {
                when(it){
                    is NoSuchElementException -> {
                        // 요청이 없는 경우 요청 데이터 생성
                        chatDataSource.insertRequestChatInfo(requestChatInfo).getOrThrow()
                        // FCM 알림 보내기
                        FCMSendService.sendNotificationToToken(
                            token = requestChatInfo.receiveUser.fcmToken,
                            title = "영화 같이 보기 요청",
                            body = "${requestChatInfo.sendUser.nickName}님이 같이 영화를 보고 싶어 합니다."
                        )
                        emit(APIResult.Success(true))
                    }
                    else -> {
                        emit(APIResult.NetworkError(it))
                    }
                }
            }
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    /**
     * 같이보기 요청 갯수를 불러옵니다.(요청개수, 받은개수)
     * @param userId 유저 아이디
     */
    override suspend fun getAllChatRequestCount(userId: String) = flow {
        emit(APIResult.Loading)
        val sendCount = chatDataSource.getWaitingSendRequestCount(userId).getOrThrow()
        val receiveCount = chatDataSource.getWaitingReceiveRequestCount(userId).getOrThrow()
        emit(APIResult.Success(mapOf("sendCount" to sendCount, "receiveCount" to receiveCount)))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    /**
     * 같이보기 요청을 승인합니다.
     * @param requestChatInfo 요청 정보
     */
    override suspend fun confirmRequest(
        requestChatInfo: RequestChatInfo
    ) = flow {
        emit(APIResult.Loading)

        // 채널 생성
        val params = GroupChannelCreateParams().apply {
            name = "${requestChatInfo.sendUser.nickName} 님과 ${requestChatInfo.receiveUser.nickName} 님의 대화"
            coverUrl = requestChatInfo.moviePosterPath
            operatorUserIds = listOf(requestChatInfo.sendUser.sendBirdId, requestChatInfo.receiveUser.sendBirdId)
        }
        val createdChannel = GroupChannel.awaitCreateChannel(params)
        createdChannel.awaitInvite(listOf(requestChatInfo.sendUser.sendBirdId))
        val channelUrl = createdChannel.url

        // 요청 승인으로 업데이트
        chatDataSource.updateRequestConfirmed(requestChatInfo.id, channelUrl).getOrThrow()

        // 승인 앱 푸시
        FCMSendService.sendNotificationToToken(
            token = requestChatInfo.sendUser.fcmToken,
            title = "영화 같이 보기 요청 승인",
            body = "${requestChatInfo.receiveUser.nickName}님이 같이 보기 요청을 승인하셨습니다."
        )

        emit(APIResult.Success(true))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    /**
     * 같이보기 요청을 거절합니다.
     * @param requestChatInfo 요청 정보
     */
    override suspend fun denyRequest(requestChatInfo: RequestChatInfo) = flow {
        emit(APIResult.Loading)

        // 요청 거절로 업데이트
        chatDataSource.updateRequestDenied(requestChatInfo.id).getOrThrow()

        // 거절 앱 푸시
        FCMSendService.sendNotificationToToken(
            token = requestChatInfo.sendUser.fcmToken,
            title = "영화 같이 보기 요청 거절",
            body = "${requestChatInfo.receiveUser.nickName}님이 같이 보기 요청을 거절하셨습니다."
        )

        emit(APIResult.Success(true))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

}