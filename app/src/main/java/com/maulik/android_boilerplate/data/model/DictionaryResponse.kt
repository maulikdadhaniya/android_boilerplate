package com.maulik.android_boilerplate.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class DictionaryResponse(
    @Json(name = "word") val word: String,
    @Json(name = "phonetic") val phonetic: String?,
    @Json(name = "phonetics") val phonetics: List<Phonetic>,
    @Json(name = "meanings") val meanings: List<Meaning>,
    @Json(name = "license") val license: License,
    @Json(name = "sourceUrls") val sourceUrls: List<String>
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Phonetic(
    @Json(name = "text") val text: String?,
    @Json(name = "audio") val audio: String?,
    @Json(name = "sourceUrl") val sourceUrl: String?,
    @Json(name = "license") val license: License?
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Meaning(
    @Json(name = "partOfSpeech") val partOfSpeech: String,
    @Json(name = "definitions") val definitions: List<Definition>,
    @Json(name = "synonyms") val synonyms: List<String>,
    @Json(name = "antonyms") val antonyms: List<String>
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Definition(
    @Json(name = "definition") val definition: String,
    @Json(name = "synonyms") val synonyms: List<String>,
    @Json(name = "antonyms") val antonyms: List<String>,
    @Json(name = "example") val example: String?
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class License(
    @Json(name = "name") val name: String,
    @Json(name = "url") val url: String
) : Parcelable
