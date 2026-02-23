package com.ghostbuilder.app.di

import com.ghostbuilder.domain.PipelineManager
import com.ghostbuilder.domain.PipelineManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindPipelineManager(impl: PipelineManagerImpl): PipelineManager
}
