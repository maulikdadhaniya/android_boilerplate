package com.maulik.android_boilerplate.domain.usecase

import com.maulik.android_boilerplate.data.model.DictionaryResponse
import com.maulik.android_boilerplate.data.util.Result
import com.maulik.android_boilerplate.domain.repository.DictionaryRepository
import javax.inject.Inject

class GetWordDefinitionUseCase @Inject constructor(
    private val repository: DictionaryRepository
) {
    suspend operator fun invoke(word: String): Result<List<DictionaryResponse>> {
        if (word.isBlank()) {
            return Result.Error("Word cannot be empty")
        }
        return repository.getWordDefinition(word.trim())
    }
}
