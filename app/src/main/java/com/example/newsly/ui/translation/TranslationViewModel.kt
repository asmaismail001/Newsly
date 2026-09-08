package com.example.newsly.ui.translation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.newsly.data.repository.TranslationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TranslationViewModel(
    private val translationRepository: TranslationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<TranslationUiState>(TranslationUiState.Idle)
    val uiState: StateFlow<TranslationUiState> = _uiState.asStateFlow()

    private val _fromLanguage = MutableStateFlow("en") // "en" or "ur"
    val fromLanguage: StateFlow<String> = _fromLanguage.asStateFlow()

    private val _toLanguage = MutableStateFlow("ur") // "ur" or "en"
    val toLanguage: StateFlow<String> = _toLanguage.asStateFlow()

    fun swapLanguages() {
        val currentFrom = _fromLanguage.value
        val currentTo = _toLanguage.value
        _fromLanguage.value = currentTo
        _toLanguage.value = currentFrom

        val current = _uiState.value
        if (current is TranslationUiState.Success) {
            translateText(current.result.translatedText)
        }
    }

    fun translateText(text: String) {
        if (text.isBlank()) return

        viewModelScope.launch {
            _uiState.value = TranslationUiState.Loading
            val result = translationRepository.translateText(
                text = text,
                sourceLang = _fromLanguage.value,
                targetLang = _toLanguage.value
            )

            result.onSuccess { translation ->
                _uiState.value = TranslationUiState.Success(
                    result = translation,
                    showOriginal = false
                )
            }.onFailure { error ->
                _uiState.value = TranslationUiState.Error(
                    message = error.localizedMessage ?: "Translation failed. Please try again."
                )
            }
        }
    }

    fun toggleShowOriginal() {
        val current = _uiState.value
        if (current is TranslationUiState.Success) {
            _uiState.value = current.copy(showOriginal = !current.showOriginal)
        }
    }

    fun reset() {
        _uiState.value = TranslationUiState.Idle
    }

    companion object {
        fun provideFactory(translationRepository: TranslationRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TranslationViewModel(translationRepository) as T
                }
            }
    }
}
