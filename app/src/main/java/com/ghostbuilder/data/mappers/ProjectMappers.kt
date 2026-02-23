package com.ghostbuilder.data.mappers

import com.ghostbuilder.data.Project
import com.ghostbuilder.domain.models.Project as DomainProject
import com.ghostbuilder.domain.models.ProjectStage

/**
 * Extension function to convert a data layer [Project] to a domain layer [DomainProject].
 */
fun Project.toDomain(): DomainProject {
    return DomainProject(
        id = this.id,
        name = this.name,
        description = this.description,
        stage = ProjectStage.valueOf(this.stage)
    )
}

/**
 * Extension function to convert a domain layer [DomainProject] to a data layer [Project].
 */
fun DomainProject.toEntity(): Project {
    return Project(
        id = this.id,
        name = this.name,
        description = this.description,
        stage = this.stage.name
    )
}
