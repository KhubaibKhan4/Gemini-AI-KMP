package org.gemini.app.data.repository

import org.gemini.app.data.model.Gemini

interface Plugin {
    suspend fun generateContent( content: String): Gemini
}