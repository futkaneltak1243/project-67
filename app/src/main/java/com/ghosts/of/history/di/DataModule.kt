package com.ghosts.of.history.di

import com.ghosts.of.history.data.filesystem.FileSystemManager
import com.ghosts.of.history.data.filesystem.FileSystemManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.ghosts.of.history.data.repositories.ProjectStateRepositoryImpl
import com.ghosts.of.history.domain.repositories.ProjectStateRepository
import com.ghosts.of.history.data.repositories.GitHubRepositoryImpl
import com.ghosts.of.history.domain.repositories.GitHubRepository
import com.google.gson.Gson
import dagger.Provides
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindFileSystemManager(fileSystemManagerImpl: FileSystemManagerImpl): FileSystemManager

    @Binds
    abstract fun bindProjectStateRepository(impl: ProjectStateRepositoryImpl): ProjectStateRepository

    @Binds
    abstract fun bindGitHubRepository(gitHubRepositoryImpl: GitHubRepositoryImpl): GitHubRepository
    companion object {
        @Provides
        @Singleton
        fun provideGson(): Gson {
            return Gson()
        }
    }
}
