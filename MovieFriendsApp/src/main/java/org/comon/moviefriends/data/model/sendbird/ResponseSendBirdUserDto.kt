package org.comon.moviefriends.data.model.sendbird

import com.google.gson.annotations.SerializedName

data class ResponseSendBirdUserDto(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("profile_url")
    val profileUrl: String,
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("is_online")
    val isOnline: Boolean,
    @SerializedName("is_active")
    val isActive: Boolean,
    @SerializedName("is_created")
    val isCreated: Boolean,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("require_auth_for_profile_image")
    val requireAuthForProfileImage: Boolean,
    @SerializedName("session_tokens")
    val sessionTokens: List<String>,
    @SerializedName("last_seen_at")
    val lastSeenAt: Int,
    @SerializedName("discovery_keys")
    val discoveryKeys: List<String>,
    @SerializedName("preferred_languages")
    val preferredLanguages: List<String>,
    @SerializedName("has_ever_logged_in")
    val hasEverLoggedIn: Boolean,
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
