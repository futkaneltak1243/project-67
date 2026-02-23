package com.ghostbuilder.data.remote.model

import com.google.gson.annotations.SerializedName

data class CreateRepoRequest(val name: String, val private: Boolean)

data class CreateBlobRequest(val content: String, val encoding: String = "utf-8")

data class CreateBlobResponse(@SerializedName("sha") val sha: String)

data class TreeEntry(val path: String, val mode: String = "100644", val type: String = "blob", val sha: String)

data class CreateTreeRequest(@SerializedName("tree") val tree: List<TreeEntry>)

data class CreateTreeResponse(@SerializedName("sha") val sha: String)

data class CreateCommitRequest(val message: String, val tree: String, val parents: List<String> = emptyList())

data class CreateCommitResponse(@SerializedName("sha") val sha: String)

data class UpdateRefRequest(val sha: String)
