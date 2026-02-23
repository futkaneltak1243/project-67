package com.ghosts.of.history.data.network.models

import com.google.gson.annotations.SerializedName

data class CreateRepoRequest(
    val name: String,
    val description: String,
    val private: Boolean
)

data class GitHubRepo(
    val name: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("html_url") val htmlUrl: String
)

data class CreateBlobRequest(
    val content: String,
    val encoding: String
)

data class CreateBlobResponse(
    val sha: String
)

data class TreeEntry(
    val path: String,
    val mode: String,
    val type: String,
    val sha: String
)

data class CreateTreeRequest(
    @SerializedName("base_tree") val baseTree: String?,
    val tree: List<TreeEntry>
)

data class CreateTreeResponse(
    val sha: String
)

data class CreateCommitRequest(
    val message: String,
    val tree: String,
    val parents: List<String>
)

data class CreateCommitResponse(
    val sha: String
)

data class UpdateRefRequest(
    val sha: String
)
