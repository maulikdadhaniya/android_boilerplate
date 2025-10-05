package com.maulik.android_boilerplate.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.maulik.android_boilerplate.databinding.FragmentWordSearchBinding
import com.maulik.android_boilerplate.presentation.state.WordDefinitionUiState
import com.maulik.android_boilerplate.presentation.ui.adapter.WordListAdapter
import com.maulik.android_boilerplate.presentation.viewmodel.WordSearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WordSearchFragment : Fragment() {

    private var _binding: FragmentWordSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WordSearchViewModel by viewModels()
    private lateinit var wordListAdapter: WordListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWordSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchBar()
        observeUiState()
    }

    private fun setupRecyclerView() {
        wordListAdapter = WordListAdapter(
            onItemClick = { wordDefinition ->
                val action = WordSearchFragmentDirections
                    .actionWordSearchFragmentToWordDetailFragment(wordDefinition)
                findNavController().navigate(action)
            }
        )
        binding.recyclerView.apply {
            adapter = wordListAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupSearchBar() {
        binding.searchBar.addTextChangedListener { text ->
            viewModel.searchWord(text?.toString() ?: "")
        }

        binding.errorState.retryButton.setOnClickListener {
            binding.searchBar.text?.toString()?.let { query ->
                viewModel.searchWord(query)
            }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is WordDefinitionUiState.Init, is WordDefinitionUiState.Empty -> {
                            binding.progressBar.visibility = View.GONE
                            binding.recyclerView.visibility = View.GONE
                            binding.emptyState.root.visibility = View.VISIBLE
                            binding.errorState.root.visibility = View.GONE
                        }
                        is WordDefinitionUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.recyclerView.visibility = View.GONE
                            binding.emptyState.root.visibility = View.GONE
                            binding.errorState.root.visibility = View.GONE
                        }
                        is WordDefinitionUiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.recyclerView.visibility = View.VISIBLE
                            binding.emptyState.root.visibility = View.GONE
                            binding.errorState.root.visibility = View.GONE
                            wordListAdapter.submitList(state.definitions)
                        }
                        is WordDefinitionUiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.recyclerView.visibility = View.GONE
                            binding.emptyState.root.visibility = View.GONE
                            binding.errorState.apply {
                                root.visibility = View.VISIBLE
                                errorMessage.text = state.message
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
