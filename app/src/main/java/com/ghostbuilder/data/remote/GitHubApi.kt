package com.ghostbuilder.data.remote

import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

@Serializable
data class CreateRepoRequest(
    val name: String,
    val description: String,
    val private: Boolean = true
)

@Serializable
data class RepoResponse(
    val name: String,
    val full_name: String,
    val html_url: String
)

@Serializable
data class CreateBlobRequest(
    val content: String,
    val encoding: String = "utf-8"
)

@Serializable
data class BlobResponse(
    val sha: String
)

@Serializable
data class TreeItem(
    val path: String,
    val mode: String,
    val type: String,
    val sha: String
)

@Serializable
data class CreateTreeRequest(
    val base_tree: String? = null,
    val tree: List<TreeItem>
)

@Serializable
data class TreeResponse(
    val sha: String
)

@Serializable
data class CreateCommitRequest(
    val message: String,
    val tree: String,
    val parents: List<String> = emptyList()
)

@Serializable
data class CommitResponse(
    val sha: String
)

@Serializable
data class UpdateRefRequest(
    val sha: String
)

@Serializable
data class RefResponse(
    val ref: String
)

interface GitHubApi {
    @GET("repos/{owner}/{repo}")
    suspend fun getRepo(
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<RepoResponse>

    @POST("user/repos")
    suspend fun createRepo(
        @Header("Authorization") token: String,
        @Body request: CreateRepoRequest
    ): Response<RepoResponse>

    @POST("repos/{owner}/{repo}/git/blobs")
    suspend fun createBlob(
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body request: CreateBlobRequest
    ): Response<BlobResponse>

    @POST("repos/{owner}/{repo}/git/trees")
    suspend fun createTree(
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body request: CreateTreeRequest
    ): Response<TreeResponse>

    @POST("repos/{owner}/{repo}/git/commits")
    suspend fun createCommit(
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body request: CreateCommitRequest
    ): Response<CommitResponse>

    @PATCH("repos/{owner}/{repo}/git/refs/{ref}")
    suspend fun updateRef(
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("ref", encoded = true) ref: String,
        @Body request: UpdateRefRequest
    ): Response<RefResponse>

    @DELETE("repos/{owner}/{repo}")
    suspend fun deleteRepo(
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<Unit>
}
