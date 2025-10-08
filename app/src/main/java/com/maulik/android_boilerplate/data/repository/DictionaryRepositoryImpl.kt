package com.maulik.android_boilerplate.data.repository

import android.content.Context
import com.maulik.android_boilerplate.BuildConfig
import com.maulik.android_boilerplate.common.MockUtil
import com.maulik.android_boilerplate.data.api.DictionaryApi
import com.maulik.android_boilerplate.data.model.DictionaryResponse
import com.maulik.android_boilerplate.data.model.ErrorResponse
import com.maulik.android_boilerplate.data.util.Result
import com.maulik.android_boilerplate.data.util.safeApiCall
import com.maulik.android_boilerplate.domain.repository.DictionaryRepository
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DictionaryRepositoryImpl @Inject constructor(
    private val api: DictionaryApi,
    private val moshi: Moshi,
    @ApplicationContext private val context: Context
) : DictionaryRepository {

    override suspend fun getWordDefinition(word: String): Result<List<DictionaryResponse>> {
        // Check if mock flavor is enabled
        return if (BuildConfig.IS_MOCK) {
            // Return mock response from JSON file
            safeApiCall {
                MockUtil.getMockResponse<List<DictionaryResponse>>(
                    context = context,
                    moshi = moshi,
                    delayMs = MockUtil.DEFAULT_DELAY_MS
                )
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
                        val errorResponse =
                            moshi.adapter(ErrorResponse::class.java).fromJson(errorBody)
                        throw Exception(errorResponse?.message ?: "Unknown error occurred")
                    } else {
                        throw Exception("Unknown error occurred")
                    }
                }
            }
        }
    }
}
