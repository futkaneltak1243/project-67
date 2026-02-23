package com.ghostbuilder.domain.model

import com.ghostbuilder.domain.model.FileStatus
import com.ghostbuilder.domain.model.PipelineStage
import com.ghostbuilder.domain.model.TimelineEvent

data class ProjectState(
    val state_version: Int,
    val pipeline_stage: PipelineStage,
    val file_status: Map<String, FileStatus>,
    val timeline: List<TimelineEvent>
)
