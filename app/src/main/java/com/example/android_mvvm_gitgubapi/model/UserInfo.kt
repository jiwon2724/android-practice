package com.example.android_mvvm_gitgubapi.model

data class UserInfo(
    val avatar_url: String,
    val login: String,
    val url: String,
    val repositories: ArrayList<UserRepositories>
)
