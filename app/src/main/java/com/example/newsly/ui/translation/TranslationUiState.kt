package com.example.newsly.ui.translation

import com.example.newsly.data.model.TranslationResult

sealed interface TranslationUiState {
    data object Idle : TranslationUiState
    data object Loading : TranslationUiState
    data class Success(
        val result: TranslationResult,
        val showOriginal: Boolean = false
    ) : TranslationUiState
    data class Error(
        val message: String
    ) : TranslationUiState
}
