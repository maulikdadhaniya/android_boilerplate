package com.maulik.android_boilerplate.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    @Json(name = "title") val title: String,
    @Json(name = "message") val message: String,
    @Json(name = "resolution") val resolution: String
)
