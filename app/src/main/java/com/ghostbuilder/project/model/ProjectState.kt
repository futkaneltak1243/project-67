package com.ghostbuilder.project.model

import kotlinx.serialization.Serializable

/**
 * Represents the different stages a project can be in within the pipeline.
 */
enum class PipelineStage {
    SEED,
    WALKTHROUGH,
    BRAIN,
    CODE_GENERATION,
    DONE
}

/**
 * Represents the status of a file within the project.
 */
enum class FileStatus {
    DRAFT,
    PENDING_REVIEW,
    APPROVED,
    IMPLEMENTED
}

/**
 * Represents an event in the project's timeline.
 *
 * @property timestamp The time the event occurred, in milliseconds since the epoch.
 * @property description A description of the event.
 */
@Serializable
data class TimelineEvent(
    val timestamp: Long,
    val description: String
)

/**
 * Represents the overall state of a project.
 *
 * @property projectName The name of the project.
 * @property currentStage The current stage of the project in the pipeline.
 * @property fileStatuses A map of file paths to their current status.
 * @property timeline A chronological list of events that have occurred in the project.
 */
@Serializable
data class ProjectState(
    val projectName: String,
    val currentStage: PipelineStage,
    val fileStatuses: Map<String, FileStatus>,
    val timeline: List<TimelineEvent>
)
