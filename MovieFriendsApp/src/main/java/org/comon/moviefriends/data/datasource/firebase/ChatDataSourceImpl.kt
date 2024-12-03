package org.comon.moviefriends.data.datasource.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import org.comon.moviefriends.data.model.firebase.ProposalFlag
import org.comon.moviefriends.data.model.firebase.RequestChatInfo
import javax.inject.Inject

class ChatDataSourceImpl @Inject constructor (
    private val fs: FirebaseFirestore,
): ChatDataSource {

    override suspend fun getConfirmedSendRequestList(movieId: Int, userId: String): Result<List<RequestChatInfo>>
        = runCatching {
            val requestQuerySnapshot = fs.collection("request_chat")
                .whereEqualTo("sendUser.id", userId)
                .whereEqualTo("movieId", movieId)
                .whereNotEqualTo("proposalFlag", ProposalFlag.WAITING.str)
                .get().await()
            requestQuerySnapshot.toObjects(RequestChatInfo::class.java)
        }

    override suspend fun getConfirmedReceiveRequestList(movieId: Int, userId: String): Result<List<RequestChatInfo>>
        = runCatching {
            val receiveQuerySnapshot = fs.collection("request_chat")
                .whereEqualTo("receiveUser.id", userId)
                .whereEqualTo("movieId", movieId)
                .get().await()
            receiveQuerySnapshot.toObjects(RequestChatInfo::class.java)
        }

    override suspend fun getRequestChatList(userId: String): Result<List<RequestChatInfo>>
        = runCatching {
            val requestQuerySnapshot = fs.collection("request_chat")
                .whereEqualTo("sendUser.id", userId)
                .get().await()
            requestQuerySnapshot.toObjects(RequestChatInfo::class.java)
        }

    override suspend fun getReceiveChatList(userId: String): Result<List<RequestChatInfo>>
        = runCatching {
            val receiveQuerySnapshot = fs.collection("request_chat")
                .whereEqualTo("receiveUser.id", userId)
                .get().await()
            receiveQuerySnapshot.toObjects(RequestChatInfo::class.java)
        }

    override suspend fun getMySendRequestList(userId: String): Result<List<RequestChatInfo>>
        = runCatching {
            val querySnapshot = fs.collection("request_chat")
                .whereEqualTo("sendUser.id", userId)
                .whereEqualTo("status", true)
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .get().await()
            querySnapshot.toObjects(RequestChatInfo::class.java)
        }

    override suspend fun getMyReceiveRequestList(userId: String): Result<List<RequestChatInfo>>
        = runCatching {
            val querySnapshot = fs.collection("request_chat")
                .whereEqualTo("receiveUser.id", userId)
                .whereEqualTo("status", true)
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .get().await()
            querySnapshot.toObjects(RequestChatInfo::class.java)
        }

    override suspend fun getMyConfirmedReceiveRequestList(userId: String): Result<List<RequestChatInfo>>
        = runCatching {
            val querySnapshot = fs.collection("request_chat")
                .whereEqualTo("receiveUser.id", userId)
                .whereEqualTo("status", true)
                .whereEqualTo("proposalFlag", ProposalFlag.CONFIRMED.str)
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .get().await()
            querySnapshot.toObjects(RequestChatInfo::class.java)
        }

    override suspend fun getMyConfirmedSendRequestList(userId: String): Result<List<RequestChatInfo>>
        = runCatching {
            val querySnapshot = fs.collection("request_chat")
                .whereEqualTo("sendUser.id", userId)
                .whereEqualTo("status", true)
                .whereEqualTo("proposalFlag", ProposalFlag.CONFIRMED.str)
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .get().await()
            querySnapshot.toObjects(RequestChatInfo::class.java)
        }

    override suspend fun getRequestAlreadyExist(requestChatInfo: RequestChatInfo): Result<RequestChatInfo>
        = runCatching {
            val querySnapshot = fs.collection("request_chat")
                .whereEqualTo("movieId", requestChatInfo.movieId)
                .whereEqualTo("sendUser.id", requestChatInfo.sendUser.id)
                .whereEqualTo("receiveUser.id", requestChatInfo.receiveUser.id).get().await()
            querySnapshot.first().toObject(RequestChatInfo::class.java)
        }

    override suspend fun insertRequestChatInfo(requestChatInfo: RequestChatInfo): Result<Boolean>
        = runCatching {
            fs.collection("request_chat").add(requestChatInfo).await()
            true
        }

    override suspend fun deleteRequestChatInfo(requestChatInfo: RequestChatInfo): Result<Boolean>
        = runCatching {
            fs.collection("request_chat").whereEqualTo("id", requestChatInfo.id)
                .get().await()
                .first().reference.delete().await()
            true
        }

    override suspend fun getWaitingSendRequestCount(userId: String): Result<Int>
        = runCatching {
            fs.collection("request_chat")
                .whereEqualTo("sendUser.id", userId)
                .whereEqualTo("proposalFlag", ProposalFlag.WAITING.str)
                .get().await()
                .documents.size
        }

    override suspend fun getWaitingReceiveRequestCount(userId: String): Result<Int>
        = runCatching {
            fs.collection("request_chat")
                .whereEqualTo("receiveUser.id", userId)
                .whereEqualTo("proposalFlag", ProposalFlag.WAITING.str)
                .get().await()
                .documents.size
        }

    override suspend fun updateRequestConfirmed(requestId: String, channelUrl: String): Result<Boolean>
        = runCatching {
            // 요청 승인으로 업데이트
            fs.collection("request_chat").whereEqualTo("id", requestId)
                .get().await()
                .documents.first().reference.update(
                    "proposalFlag", ProposalFlag.CONFIRMED.str,
                    "channelUrl", channelUrl
                ).await()
            true
        }

    override suspend fun updateRequestDenied(requestId: String): Result<Boolean>
        = runCatching {
            // 요청 거절로 업데이트
            fs.collection("request_chat").whereEqualTo("id", requestId)
                .get().await()
                .documents.first().reference.update(
                    "proposalFlag", ProposalFlag.DENIED.str,
                ).await()
            true
        }
}