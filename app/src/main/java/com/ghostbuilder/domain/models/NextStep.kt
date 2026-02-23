package com.ghostbuilder.domain.models

import com.ghostbuilder.domain.models.ProjectStage

sealed class NextStep {
    data class WorkOnDraftFile(val filePath: String) : NextStep()
    data class ProposeNewFile(val directory: String) : NextStep()
    data class PromptToCompleteStage(val stage: ProjectStage) : NextStep()
    object AllStagesComplete : NextStep()
}
