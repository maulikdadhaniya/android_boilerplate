package com.maulik.android_boilerplate.domain.repository

import com.maulik.android_boilerplate.data.model.DictionaryResponse
import com.maulik.android_boilerplate.data.util.Result

interface DictionaryRepository {
    suspend fun getWordDefinition(word: String): Result<List<DictionaryResponse>>
}
