package com.ghostbuilder.data.repository

import com.ghostbuilder.data.remote.DogApiService
import com.ghostbuilder.domain.repository.DogImageRepository
import javax.inject.Inject

class DogImageRepositoryImpl @Inject constructor(
    private val dogApiService: DogApiService
) : DogImageRepository {

    override suspend fun getRandomDogImage(): String {
        return dogApiService.getRandomDogImage().message
    }
}
