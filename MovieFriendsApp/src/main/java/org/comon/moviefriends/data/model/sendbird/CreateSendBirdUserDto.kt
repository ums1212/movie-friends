package org.comon.moviefriends.data.model.sendbird

import com.google.gson.annotations.SerializedName

data class CreateSendBirdUserDto(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("profile_url")
    val profileUrl: String,
    @SerializedName("issue_access_token")
    val issueAccessToken: Boolean,
    @SerializedName("session_token_expires_at")
    val sessionTokenExpiresAt: Long,
    @SerializedName("metadata")
    val metadata: Metadata,
){
    data class Metadata(
        @SerializedName("font_preference")
        val fontPreference: String,
        @SerializedName("font_color")
        val fontColor: String,
    )
}
