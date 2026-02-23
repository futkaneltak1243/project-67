package com.ghosts.of.history.domain.models

import com.google.gson.annotations.SerializedName

data class ProjectState(
    @SerializedName("project_name") val projectName: String,
    @SerializedName("seeds") val seeds: SeedsState,
    @SerializedName("walkthrough") val walkthrough: StageState,
    @SerializedName("brain") val brain: StageState,
    @SerializedName("code") val code: StageState,
    @SerializedName("files") val files: Map<String, FileState>
)

data class SeedsState(
    @SerializedName("verified") val verified: Boolean,
    @SerializedName("last_verified_at") val lastVerifiedAt: String,
    @SerializedName("verified_hash") val verifiedHash: String
)

data class StageState(
    @SerializedName("complete") val complete: Boolean
)

data class FileState(
    @SerializedName("lifecycle") val lifecycle: LifecycleState
)

data class LifecycleState(
    @SerializedName("status") val status: String
)
