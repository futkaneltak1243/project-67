package com.ghostbuilder.domain.repository

import com.ghostbuilder.data.service.DogApiService
import com.ghostbuilder.data.model.DogImageResponse

/**
 * Repository interface for fetching dog images.
 */
interface DogImageRepository {
    /**
     * Retrieves a random dog image URL.
     * @return A [Result] containing the dog image URL on success, or an exception on failure.
     */
    suspend fun getRandomDogImage(): Result<String>
}

/**
 * Implementation of [DogImageRepository] that fetches data from [DogApiService].
 *
 * @param dogApiService The API service used to fetch dog image data.
 */
class DogImageRepositoryImpl(
    private val dogApiService: DogApiService
) : DogImageRepository {
    override suspend fun getRandomDogImage(): Result<String> {
        return try {
            val response: DogImageResponse = dogApiService.getRandomDogImage()
            if (response.status == "success") {
                Result.success(response.message)
            } else {
                // If the API indicates a failure status but doesn't throw an exception
                Result.failure(RuntimeException("Dog API returned status: ${response.status}"))
            }
        } catch (e: Exception) {
            // Catch network errors, JSON parsing errors, etc.
            Result.failure(e)
        }
    }
}
