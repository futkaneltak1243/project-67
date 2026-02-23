package com.ghostbuilder.data.remote

import com.ghostbuilder.data.remote.model.CreateBlobRequest
import com.ghostbuilder.data.remote.model.CreateBlobResponse
import com.ghostbuilder.data.remote.model.CreateCommitRequest
import com.ghostbuilder.data.remote.model.CreateCommitResponse
import com.ghostbuilder.data.remote.model.CreateRepoRequest
import com.ghostbuilder.data.remote.model.CreateRepoResponse
import com.ghostbuilder.data.remote.model.CreateTreeRequest
import com.ghostbuilder.data.remote.model.CreateTreeResponse
import com.ghostbuilder.data.remote.model.UpdateRefRequest
import com.ghostbuilder.data.remote.model.UpdateRefResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface GitHubApiService {

    @GET("repos/{owner}/{repo}")
    suspend fun checkRepositoryExists(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<Unit>

    @POST("user/repos")
    suspend fun createRepository(
        @Body body: CreateRepoRequest
    ): CreateRepoResponse

    @POST("repos/{owner}/{repo}/git/blobs")
    suspend fun createBlob(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body body: CreateBlobRequest
    ): CreateBlobResponse

    @POST("repos/{owner}/{repo}/git/trees")
    suspend fun createTree(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body body: CreateTreeRequest
    ): CreateTreeResponse

    @POST("repos/{owner}/{repo}/git/commits")
    suspend fun createCommit(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body body: CreateCommitRequest
    ): CreateCommitResponse

    @PATCH("repos/{owner}/{repo}/git/refs/{ref}")
    suspend fun updateReference(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("ref") ref: String,
        @Body body: UpdateRefRequest
    ): UpdateRefResponse
}