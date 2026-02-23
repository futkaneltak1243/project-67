package com.ghostbuilder.data.remote

import com.ghostbuilder.data.remote.dto.DogImageDto
import retrofit2.http.GET

interface DogApiService {
    @GET("api/breeds/image/random")
    suspend fun getRandomDogImage(): DogImageDto
}
