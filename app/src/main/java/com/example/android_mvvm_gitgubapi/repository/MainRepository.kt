package com.example.android_mvvm_gitgubapi.repository

import android.util.Log
import com.example.android_mvvm_gitgubapi.model.Repositories
import com.example.android_mvvm_gitgubapi.model.UserInfo
import com.example.android_mvvm_gitgubapi.model.UserRepositories
import com.example.android_mvvm_gitgubapi.resource.RepositoryInterface
import com.example.android_mvvm_gitgubapi.resource.Retrofit

class MainRepository {
    private val repository: RepositoryInterface = Retrofit.getInstance().create(RepositoryInterface::class.java)

    suspend fun getUserRepositories(username: String): UserInfo? {
        val response = repository.getRepository(username)
        return if(response.isSuccessful && response.body()!!.isNotEmpty()) {
            val user = UserInfo(
                avatar_url = response.body()!![0].owner.avatar_url,
                url = response.body()!![0].owner.html_url,
                login = username,
                repositories = arrayListOf()
            )
            if(response.body()!!.isNotEmpty()) {
                for(i in response.body()!!) {
                    user.repositories.add(
                        UserRepositories(
                            name = i.name,
                            html_url = i.html_url,
                            description = i.description ?: "",
                            stargazers_count = i.stargazers_count,
                            language = i.language ?: ""
                        )
                    )
                }
            }
            user
        } else null
    }
}