package com.ghostbuilder.app.di

import com.ghostbuilder.data.repository.AuthoringSessionRepositoryImpl
import com.ghostbuilder.domain.repository.AuthoringSessionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import android.content.Context
import android.content.res.AssetManager
import com.ghostbuilder.data.repositories.ProjectStateRepositoryImpl
import com.ghostbuilder.domain.repositories.ProjectStateRepository
import com.google.gson.Gson
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthoringSessionRepository(repository: AuthoringSessionRepositoryImpl): AuthoringSessionRepository

    @Binds
    @Singleton
    abstract fun bindProjectStateRepository(impl: ProjectStateRepositoryImpl): ProjectStateRepository

    @Provides
    @Singleton
    fun provideAssetManager(@ApplicationContext context: Context): AssetManager {
        return context.assets
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }
}
