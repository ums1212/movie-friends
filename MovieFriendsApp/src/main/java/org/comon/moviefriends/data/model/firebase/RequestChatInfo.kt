package org.comon.moviefriends.data.model.firebase

import com.google.firebase.Timestamp
import java.util.UUID

data class RequestChatInfo(
    val id: String = UUID.randomUUID().toString(),
    val movieId: Int = 0,
    val moviePosterPath: String = "",
    val sendUser: UserInfo = UserInfo(),
    val receiveUser: UserInfo = UserInfo(),
    val receiveUserRegion: String = "위치 없음",
    val proposalFlag: String = ProposalFlag.WAITING.str,
    val createdDate: Timestamp = Timestamp.now(),
    val channelUrl: String = "",
    val status: Boolean = true,
)

enum class ProposalFlag(val str: String) {
    DENIED("거절 됨"),
    WAITING("대기 중"),
    CONFIRMED("승인 됨")
}