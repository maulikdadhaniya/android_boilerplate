package com.maulik.android_boilerplate.data.api

import com.maulik.android_boilerplate.data.model.DictionaryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {
    @GET("api/v2/entries/en/{word}")
    suspend fun getWordDefinition(
        @Path("word") word: String
    ): Response<List<DictionaryResponse>>
}
