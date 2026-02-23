package com.ghostbuilder.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DogImageResponse(
    @Json("message") val message: String,
    val status: String
)
