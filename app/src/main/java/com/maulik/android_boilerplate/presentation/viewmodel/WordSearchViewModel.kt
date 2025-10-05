package com.maulik.android_boilerplate.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maulik.android_boilerplate.data.util.Result
import com.maulik.android_boilerplate.domain.usecase.GetWordDefinitionUseCase
import com.maulik.android_boilerplate.presentation.state.WordDefinitionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordSearchViewModel @Inject constructor(
    private val getWordDefinitionUseCase: GetWordDefinitionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<WordDefinitionUiState>(WordDefinitionUiState.Init)
    val uiState: StateFlow<WordDefinitionUiState> = _uiState

    private var searchJob: Job? = null

    fun searchWord(word: String) {
        searchJob?.cancel()

        if (word.isBlank()) {
            _uiState.value = WordDefinitionUiState.Empty
            return
        }

        searchJob = viewModelScope.launch {
            _uiState.value = WordDefinitionUiState.Loading
            when (val result = getWordDefinitionUseCase(word.trim())) {
                is Result.Success -> {
                    _uiState.value = WordDefinitionUiState.Success(result.data)
                }
                is Result.Error -> {
                    _uiState.value = WordDefinitionUiState.Error(result.message)
                }
                is Result.Loading -> {
                    _uiState.value = WordDefinitionUiState.Loading
                }
            }
        }
    }

    fun onStateChangeHandled() {
        _uiState.value = WordDefinitionUiState.Init
    }
}
