package org.comon.moviefriends.data.entity.ai


import com.google.gson.annotations.SerializedName

data class RequestAnthropicBodyDto(
    @SerializedName("model")
    val model: String = "claude-3-5-sonnet-20241022",
    @SerializedName("max_tokens")
    val maxTokens: Int = 1024,
    @SerializedName("messages")
    val messages: List<Message>,
) {
    data class Message(
        @SerializedName("role")
        val role: String = "user",
        @SerializedName("content")
        val content: String = "hello",
    )
}