package org.comon.moviefriends.data.datasource.firebase

import org.comon.moviefriends.data.model.firebase.RequestChatInfo

interface ChatDataSource {
    // 해당 영화의 요청 승인한 대화 요청한 리스트 불러오기
    suspend fun getConfirmedSendRequestList(movieId: Int, userId: String): Result<List<RequestChatInfo>>
    // 해당 영화의 요청 승인한 대화 요청 받은 리스트 불러오기
    suspend fun getConfirmedReceiveRequestList(movieId: Int, userId: String): Result<List<RequestChatInfo>>
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
    // 승인 대기중인 요청한 채팅 건 수 불러오기
    suspend fun getWaitingSendRequestCount(userId: String): Result<Int>
    // 승인 대기중인 받은 채팅 건 수 불러오기
    suspend fun getWaitingReceiveRequestCount(userId: String): Result<Int>
    // 요청 받은 채팅 상태 승인으로 데이터 업데이트
    suspend fun updateRequestConfirmed(requestId: String, channelUrl: String): Result<Boolean>
    // 요청 받은 채팅 상태 거절로 데이터 업데이트
    suspend fun updateRequestDenied(requestId: String): Result<Boolean>
}