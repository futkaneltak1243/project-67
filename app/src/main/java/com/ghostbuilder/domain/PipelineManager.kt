package com.ghostbuilder.domain

import com.ghostbuilder.domain.models.ProjectStage
import com.ghostbuilder.domain.models.NextStep

interface PipelineManager {
    suspend fun getCurrentStage(): Result<ProjectStage>
    suspend fun getNextStep(): Result<NextStep>
}
