package com.maulik.android_boilerplate.presentation.viewmodel

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maulik.android_boilerplate.data.model.DictionaryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val wordDefinition: DictionaryResponse? = savedStateHandle.get<DictionaryResponse>("wordDefinition")
    
    private val _audioState = MutableStateFlow<AudioState>(AudioState.Initial)
    val audioState: StateFlow<AudioState> = _audioState
    
    private var mediaPlayer: MediaPlayer? = null
    
    fun playAudio(url: String) {
        if (_audioState.value is AudioState.Playing) return
        
        viewModelScope.launch {
            _audioState.value = AudioState.Loading
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                try {
                    setDataSource(url)
                    prepareAsync()
                    setOnPreparedListener { mp ->
                        _audioState.value = AudioState.Playing
                        mp.start()
                    }
                    setOnCompletionListener { mp ->
                        mp.release()
                        mediaPlayer = null
                        _audioState.value = AudioState.Initial
                    }
                    setOnErrorListener { mp, _, _ ->
                        mp.release()
                        mediaPlayer = null
                        _audioState.value = AudioState.Error
                        true
                    }
                } catch (e: Exception) {
                    release()
                    mediaPlayer = null
                    _audioState.value = AudioState.Error
                }
            }
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
    }
    
    sealed class AudioState {
        data object Initial : AudioState()
        data object Loading : AudioState()
        data object Playing : AudioState()
        data object Error : AudioState()
    }
}
