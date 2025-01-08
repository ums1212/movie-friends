package org.comon.moviefriends.data.entity.ai


import com.google.gson.annotations.SerializedName

data class ResponseAnthropicDto(
    @SerializedName("content")
    val content: List<Content>,
    @SerializedName("id")
    val id: String,
    @SerializedName("model")
    val model: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("stop_reason")
    val stopReason: String,
    @SerializedName("stop_sequence")
    val stopSequence: Any?,
    @SerializedName("type")
    val type: String,
    @SerializedName("usage")
    val usage: Usage
) {
    data class Content(
        @SerializedName("text")
        val text: String,
        @SerializedName("type")
        val type: String
    )

    data class Usage(
        @SerializedName("input_tokens")
        val inputTokens: Int,
        @SerializedName("output_tokens")
        val outputTokens: Int
    )
}