package com.maulik.android_boilerplate.common

import android.content.Context
import com.maulik.android_boilerplate.data.model.DictionaryResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockUtil @Inject constructor(
    @ApplicationContext private val context: Context,
    private val moshi: Moshi
) {

    companion object {
        // Static file names for mock responses
        private const val WORD_DEFINITION_MOCK_FILE = "word_definition.json"
    }

    /**
     * Mock API method for getting word definition
     * Returns mock data from JSON file
     */
    suspend fun getWordDefinitionMock(word: String): List<DictionaryResponse> {
        // Simulate network delay
        delay(1000)

        return readJsonFileAsList<DictionaryResponse>(WORD_DEFINITION_MOCK_FILE)
            ?: throw Exception("Mock data not found for word: $word")
    }

    /**
     * Generic method to read a JSON file from assets folder and deserialize it to a list
     */
    private inline fun <reified T> readJsonFileAsList(fileName: String): List<T>? {
        return try {
            val jsonString = context.assets.open("mock/$fileName").bufferedReader().use {
                it.readText()
            }
            val type = Types.newParameterizedType(List::class.java, T::class.java)
            val adapter = moshi.adapter<List<T>>(type)
            adapter.fromJson(jsonString)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}