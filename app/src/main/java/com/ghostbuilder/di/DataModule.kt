package com.ghostbuilder.di

import com.ghostbuilder.data.FileSystemManager
import com.ghostbuilder.data.ProjectRepository
import com.ghostbuilder.domain.interfaces.IFileSystemManager
import com.ghostbuilder.domain.interfaces.IProjectRepository
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindFileSystemManager(fileSystemManager: FileSystemManager): IFileSystemManager

    @Binds
    @Singleton
    abstract fun bindProjectRepository(projectRepository: ProjectRepository): IProjectRepository

    companion object {
        @Provides
        @Singleton
        fun provideGson(): Gson {
            return Gson()
        }
    }
}
