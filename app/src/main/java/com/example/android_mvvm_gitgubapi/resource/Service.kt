package com.example.android_mvvm_gitgubapi.resource

import com.example.android_mvvm_gitgubapi.model.Repository
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Service {
    @GET("{owner}/{repo}")
    suspend fun getRepository(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ) : Repository
}