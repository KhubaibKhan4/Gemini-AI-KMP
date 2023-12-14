package org.gemini.app.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Candidate(
    @SerialName("content")
    val content: Content? = null,
    @SerialName("finishReason")
    val finishReason: String? = null,
    @SerialName("index")
    val index: Int,
    @SerialName("safetyRatings")
    val safetyRatings: List<SafetyRating>
)