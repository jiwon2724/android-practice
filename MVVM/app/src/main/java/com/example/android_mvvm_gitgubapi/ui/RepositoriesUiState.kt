package com.example.android_mvvm_gitgubapi.ui

data class RepositoriesUiState(
    val avatar_url: String? = null,
    val login: String? = null,
    val url: String? = null,
    val repositories: ArrayList<RepositoriesItemUiState> = arrayListOf()
)

data class RepositoriesItemUiState(
    val name: String,
    val html_url: String,
    val description: String,
    val stargazers_count: Int,
    val language: String
)
