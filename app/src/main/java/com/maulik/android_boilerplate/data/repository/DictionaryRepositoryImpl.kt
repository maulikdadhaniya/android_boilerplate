package com.maulik.android_boilerplate.data.repository

import com.maulik.android_boilerplate.BuildConfig
import com.maulik.android_boilerplate.data.api.DictionaryApi
import com.maulik.android_boilerplate.data.model.DictionaryResponse
import com.maulik.android_boilerplate.data.model.ErrorResponse
import com.maulik.android_boilerplate.common.MockUtil
import com.maulik.android_boilerplate.data.util.Result
import com.maulik.android_boilerplate.data.util.safeApiCall
import com.maulik.android_boilerplate.domain.repository.DictionaryRepository
import com.squareup.moshi.Moshi
import javax.inject.Inject

class DictionaryRepositoryImpl @Inject constructor(
    private val api: DictionaryApi,
    private val moshi: Moshi,
    private val mockUtil: MockUtil
) : DictionaryRepository {

    override suspend fun getWordDefinition(word: String): Result<List<DictionaryResponse>> {
        // Check if mock flavor is enabled
        return if (BuildConfig.IS_MOCK) {
            // Return mock response from JSON file
            safeApiCall {
                mockUtil.getWordDefinitionMock(word)
            }
        } else {
            // Make real API call
            safeApiCall {
                val response = api.getWordDefinition(word)
                if (response.isSuccessful) {
                    response.body() ?: throw Exception("Response body is null")
                } else {
                    val errorBody = response.errorBody()?.string()
                    if (errorBody != null) {
                        val errorResponse = moshi.adapter(ErrorResponse::class.java).fromJson(errorBody)
                        throw Exception(errorResponse?.message ?: "Unknown error occurred")
                    } else {
                        throw Exception("Unknown error occurred")
                    }
                }
            }
        }
    }
}
