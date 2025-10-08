package com.maulik.android_boilerplate.common

import android.content.Context
import com.squareup.moshi.Moshi
import kotlinx.coroutines.delay
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

object MockUtil {

    // Static file names for mock responses
    const val WORD_DEFINITION_MOCK_FILE = "word_definition.json"

    // Default network delay in milliseconds
    const val DEFAULT_DELAY_MS = 1000L

    /**
     * Generic method to get mock response
     * Use this in your specific mock functions with the type you need
     *
     * Example:
     *   suspend fun getUserMock(context: Context, moshi: Moshi): UserProfile {
     *       return MockUtil.getMockResponse<UserProfile>(
     *           context = context,
     *           moshi = moshi,
     *      )
     *   }
     */
    suspend inline fun <reified T> getMockResponse(
        context: Context,
        moshi: Moshi,
        delayMs: Long = DEFAULT_DELAY_MS
    ): T {
        // Simulate network delay
        if (delayMs > 0) {
            delay(delayMs)
        }

        val jsonString = context.assets.open("mock/$WORD_DEFINITION_MOCK_FILE").bufferedReader().use {
            it.readText()
        }

        // Get the actual type including generic parameters
        val type = object : TypeToken<T>() {}.type
        val adapter = moshi.adapter<T>(type)
        return adapter.fromJson(jsonString) ?: throw Exception("Failed to parse JSON from $WORD_DEFINITION_MOCK_FILE")
    }

    /**
     * Helper class to capture generic type at runtime
     */
    abstract class TypeToken<T> {
        val type: Type
            get() = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
    }
}
