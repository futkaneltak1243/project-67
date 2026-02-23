package com.ghostbuilder.domain.models

import com.google.gson.annotations.SerializedName

data class ProjectState(
    @SerializedName("schema_version") val schemaVersion: String,
    @SerializedName("project_id") val projectId: String,
    val repo: RepoInfo,
    @SerializedName("account_binding") val accountBinding: Any?,
    val seeds: SeedsState,
    val stages: StagesState,
    val files: Map<String, FileState>
)

data class RepoInfo(
    val owner: String,
    val name: String,
    @SerializedName("default_branch") val defaultBranch: String
)

data class SeedsState(
    val verified: Boolean,
    @SerializedName("last_verified_at") val lastVerifiedAt: String?
)

data class StagesState(
    val walkthrough: StageCompletion,
    val brain: StageCompletion,
    val code: StageCompletion
)

data class StageCompletion(
    val complete: Boolean,
    @SerializedName("completed_at") val completedAt: String?
)

data class FileState(
    val kind: String,
    val stage: String,
    val lifecycle: FileLifecycle
)

data class FileLifecycle(
    val status: FileLifecycleStatus
)
