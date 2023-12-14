package org.gemini.app.domain.repository

import org.gemini.app.data.model.Gemini
import org.gemini.app.data.remote.GeminiClientApi
import org.gemini.app.data.repository.Plugin

class Repository: Plugin {
    override suspend fun generateContent( content: String): Gemini {
        return GeminiClientApi.generateContent(content)
    }
}