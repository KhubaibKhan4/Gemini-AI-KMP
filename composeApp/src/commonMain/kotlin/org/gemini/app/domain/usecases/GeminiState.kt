package org.gemini.app.domain.usecases

import org.gemini.app.data.model.Gemini

sealed class GeminiState {
    object LOADING: GeminiState()
    data class SUCCESS(val content: Gemini): GeminiState()
    data class ERROR(val error: String): GeminiState()
}