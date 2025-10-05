package com.maulik.android_boilerplate.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.maulik.android_boilerplate.databinding.FragmentWordDetailBinding
import com.maulik.android_boilerplate.presentation.viewmodel.WordDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WordDetailFragment : Fragment() {

    private var _binding: FragmentWordDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WordDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWordDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWordDetails()
        observeAudioState()
    }

    private fun setupWordDetails() {
        viewModel.wordDefinition?.let { definition ->
            binding.apply {
                wordTitle.text = definition.word
                phonetic.text = definition.phonetic ?: ""

                // Setup audio button
                val audioUrl = definition.phonetics.firstOrNull { !it.audio.isNullOrBlank() }?.audio
                audioButton.visibility = if (audioUrl != null) View.VISIBLE else View.GONE
                audioButton.setOnClickListener {
                    audioUrl?.let { url -> viewModel.playAudio(url) }
                }

                val meaningsText = definition.meanings.joinToString("\n\n") { meaning ->
                    "Part of Speech: ${meaning.partOfSpeech}\n" +
                    meaning.definitions.joinToString("\n") { def ->
                        "• ${def.definition}" + (def.example?.let { "\nExample: $it" } ?: "")
                    }
                }
                meanings.text = meaningsText

                if (definition.phonetics.isNotEmpty()) {
                    val phoneticText = definition.phonetics.joinToString("\n") { phonetic ->
                        "${phonetic.text ?: ""}"
                    }
                    phonetics.text = phoneticText
                }
            }
        }
    }

    private fun observeAudioState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.audioState.collect { state ->
                    binding.audioButton.apply {
                        when (state) {
                            is WordDetailViewModel.AudioState.Initial -> {
                                isEnabled = true
                            }
                            is WordDetailViewModel.AudioState.Loading -> {
                                isEnabled = false
                            }
                            is WordDetailViewModel.AudioState.Playing -> {
                                isEnabled = false
                            }
                            is WordDetailViewModel.AudioState.Error -> {
                                isEnabled = true
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
