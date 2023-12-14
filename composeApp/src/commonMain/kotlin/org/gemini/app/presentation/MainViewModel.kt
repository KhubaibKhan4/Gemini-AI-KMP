package org.gemini.app.presentation

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.gemini.app.domain.repository.Repository
import org.gemini.app.domain.usecases.GeminiState

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val _content = MutableStateFlow<GeminiState>(GeminiState.LOADING)
    val content: StateFlow<GeminiState> = _content.asStateFlow()

    fun generateContent(content: String) {
        viewModelScope.launch {
            _content.value = GeminiState.LOADING
            try {
                val response = repository.generateContent(content)
                _content.value = GeminiState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message ?: "Unknown error"
                e.printStackTrace()
                _content.value = GeminiState.ERROR(error)
            }
        }
    }
}