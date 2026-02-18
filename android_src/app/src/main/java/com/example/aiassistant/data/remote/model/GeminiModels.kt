package com.example.aiassistant.data.remote.model

data class GeminiRequest(
    val contents: List<Content>
)

data class Content(
    val parts: List<Part>,
    val role: String? = "user"
)

data class Part(
    val text: String
)

data class GeminiResponse(
    val candidates: List<Candidate>?
)

data class Candidate(
    val content: Content?,
    val finishReason: String?,
    val index: Int?
)
