package com.ghostbuilder.di

import com.ghostbuilder.domain.repositories.ProjectRepository
import com.ghostbuilder.domain.usecases.DeleteProjectUseCase
import com.ghostbuilder.domain.usecases.GetProjectByIdUseCase
import com.ghostbuilder.domain.usecases.GetProjectsUseCase
import com.ghostbuilder.domain.usecases.UpdateProjectStageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    fun provideGetProjectsUseCase(projectRepository: ProjectRepository): GetProjectsUseCase {
        return GetProjectsUseCase(projectRepository)
    }

    @Provides
    fun provideDeleteProjectUseCase(projectRepository: ProjectRepository): DeleteProjectUseCase {
        return DeleteProjectUseCase(projectRepository)
    }

    @Provides
    fun provideGetProjectByIdUseCase(projectRepository: ProjectRepository): GetProjectByIdUseCase {
        return GetProjectByIdUseCase(projectRepository)
    }

    @Provides
    fun provideUpdateProjectStageUseCase(projectRepository: ProjectRepository): UpdateProjectStageUseCase {
        return UpdateProjectStageUseCase(projectRepository)
    }
}
