package com.maulik.android_boilerplate.presentation.state

import com.maulik.android_boilerplate.data.model.DictionaryResponse

sealed class WordDefinitionUiState {
    data object Init : WordDefinitionUiState()
    data object Empty : WordDefinitionUiState()
    data object Loading : WordDefinitionUiState()
    data class Success(val definitions: List<DictionaryResponse>) : WordDefinitionUiState()
    data class Error(val message: String) : WordDefinitionUiState()
}