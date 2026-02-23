package com.ghostbuilder.di

import android.app.Application
import com.ghostbuilder.data.repository.ProjectStateRepositoryImpl
import com.ghostbuilder.domain.repository.ProjectStateRepository
import com.ghostbuilder.data.repository.ProjectFilesRepositoryImpl
import com.ghostbuilder.domain.repository.ProjectFilesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        }
    }

    @Provides
    @Singleton
    fun provideProjectStateRepository(
        application: Application,
        json: Json
    ): ProjectStateRepository {
        return ProjectStateRepositoryImpl(application, json)
    }

    @Binds
    @Singleton
    abstract fun bindProjectFilesRepository(impl: ProjectFilesRepositoryImpl): ProjectFilesRepository
}
