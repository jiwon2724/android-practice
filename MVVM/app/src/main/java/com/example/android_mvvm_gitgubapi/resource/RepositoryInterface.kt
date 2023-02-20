package com.example.android_mvvm_gitgubapi.resource

import com.example.android_mvvm_gitgubapi.model.Repositories
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RepositoryInterface {
    @GET("/users/{username}/repos")
    suspend fun getRepository(
        @Path("username") username: String
    ) : Response<Repositories>
}