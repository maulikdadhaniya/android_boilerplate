package com.maulik.android_boilerplate.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maulik.android_boilerplate.data.model.DictionaryResponse
import com.maulik.android_boilerplate.databinding.ItemWordBinding

class WordListAdapter(
    private val onItemClick: (DictionaryResponse) -> Unit
) : ListAdapter<DictionaryResponse, WordListAdapter.WordViewHolder>(WordDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding = ItemWordBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class WordViewHolder(
        private val binding: ItemWordBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(item: DictionaryResponse) {
            binding.apply {
                wordText.text = item.word
                phoneticText.text = item.phonetic ?: ""
                partOfSpeechChip.text = item.meanings.firstOrNull()?.partOfSpeech ?: ""
                partOfSpeechChip.visibility = if (item.meanings.isNotEmpty()) View.VISIBLE else View.GONE

                audioButton.visibility = View.GONE
            }
        }
    }

    private class WordDiffCallback : DiffUtil.ItemCallback<DictionaryResponse>() {
        override fun areItemsTheSame(oldItem: DictionaryResponse, newItem: DictionaryResponse): Boolean {
            return oldItem.word == newItem.word && 
                   oldItem.phonetic == newItem.phonetic &&
                   oldItem.meanings.firstOrNull()?.partOfSpeech == newItem.meanings.firstOrNull()?.partOfSpeech
        }

        override fun areContentsTheSame(oldItem: DictionaryResponse, newItem: DictionaryResponse): Boolean {
            return oldItem == newItem
        }
    }
}
