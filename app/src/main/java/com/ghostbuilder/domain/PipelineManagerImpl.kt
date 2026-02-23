package com.ghostbuilder.domain

import com.ghostbuilder.domain.models.ProjectStage
import com.ghostbuilder.domain.repositories.ProjectStateRepository
import com.ghostbuilder.domain.models.FileLifecycleStatus
import com.ghostbuilder.domain.models.NextStep
import com.ghostbuilder.domain.models.ProjectState
import javax.inject.Inject
import kotlin.Result
import com.ghostbuilder.util.map

class PipelineManagerImpl @Inject constructor(
    private val projectStateRepository: ProjectStateRepository
) : PipelineManager {

    override suspend fun getCurrentStage(): Result<ProjectStage> {
        return projectStateRepository.getProjectState().map {
            when {
                !it.seeds.verified -> ProjectStage.SeedsUnverified
                !it.stages.walkthrough.complete -> ProjectStage.WalkthroughInProgress
                !it.stages.brain.complete -> ProjectStage.BrainInProgress
                !it.stages.code.complete -> ProjectStage.CodeInProgress
                else -> ProjectStage.CodeComplete
            }
        }
    }

    override suspend fun getNextStep(): Result<NextStep> {
        return projectStateRepository.getProjectState().map { projectState ->
            val currentStage = when {
                !projectState.seeds.verified -> ProjectStage.SeedsUnverified
                !projectState.stages.walkthrough.complete -> ProjectStage.WalkthroughInProgress
                !projectState.stages.brain.complete -> ProjectStage.BrainInProgress
                !projectState.stages.code.complete -> ProjectStage.CodeInProgress
                else -> ProjectStage.CodeComplete
            }

            when (currentStage) {
                ProjectStage.WalkthroughInProgress -> {
                    val walkthroughFiles = projectState.files.filter { it.key.startsWith("walkthrough/") }
                    val draftFile = walkthroughFiles.entries.find { it.value.lifecycle.status == FileLifecycleStatus.Draft }
                    if (draftFile != null) {
                        NextStep.WorkOnDraftFile(draftFile.key)
                    } else {
                        val allApproved = walkthroughFiles.isNotEmpty() && walkthroughFiles.entries.all { it.value.lifecycle.status == FileLifecycleStatus.Approved }
                        if (allApproved) {
                            NextStep.PromptToCompleteStage(ProjectStage.WalkthroughInProgress)
                        } else {
                            NextStep.ProposeNewFile("walkthrough/")
                        }
                    }
                }
                ProjectStage.BrainInProgress -> {
                    val brainFiles = projectState.files.filter { it.key.startsWith("brain/") }
                    val draftFile = brainFiles.entries.find { it.value.lifecycle.status == FileLifecycleStatus.Draft }
                    if (draftFile != null) {
                        NextStep.WorkOnDraftFile(draftFile.key)
                    } else {
                        val allApproved = brainFiles.isNotEmpty() && brainFiles.entries.all { it.value.lifecycle.status == FileLifecycleStatus.Approved }
                        if (allApproved) {
                            NextStep.PromptToCompleteStage(ProjectStage.BrainInProgress)
                        } else {
                            NextStep.ProposeNewFile("brain/")
                        }
                    }
                }
                ProjectStage.CodeInProgress -> {
                    val appFiles = projectState.files.filter { it.key.startsWith("app/") }
                    val draftFile = appFiles.entries.find { it.value.lifecycle.status == FileLifecycleStatus.Draft }
                    if (draftFile != null) {
                        NextStep.WorkOnDraftFile(draftFile.key)
                    } else {
                        val allApproved = appFiles.isNotEmpty() && appFiles.entries.all { it.value.lifecycle.status == FileLifecycleStatus.Approved }
                        if (allApproved) {
                            NextStep.PromptToCompleteStage(ProjectStage.CodeInProgress)
                        } else {
                            NextStep.ProposeNewFile("app/")
                        }
                    }
                }
                ProjectStage.CodeComplete -> NextStep.AllStagesComplete
                else -> NextStep.AllStagesComplete
            }
        }
    }
}
