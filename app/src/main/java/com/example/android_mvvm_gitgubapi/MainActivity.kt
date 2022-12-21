package com.example.android_mvvm_gitgubapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.android_mvvm_gitgubapi.adapter.MainAdapter
import com.example.android_mvvm_gitgubapi.adapter.listener.MainListener
import com.example.android_mvvm_gitgubapi.databinding.ActivityMainBinding
import com.example.android_mvvm_gitgubapi.model.UserRepositories
import com.example.android_mvvm_gitgubapi.repository.MainRepository
import com.example.android_mvvm_gitgubapi.viewmodel.MainViewModel
import com.example.android_mvvm_gitgubapi.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, MainViewModelFactory(MainRepository()))[MainViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /** 검색 클릭 **/
        binding.searchButton.setOnClickListener {
            viewModel.getRepositories(binding.userNameEditText.text.toString())
        }

        viewModel.repositoriesResult.observe(this) {
            when (it) {
                null -> {
                    hideUiLayoutOnOff(8) // GONE
                    Toast.makeText(this, "존재하지않는 아이디거나 레포지토리가 없습니다..", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    hideUiLayoutOnOff(0) // VISIBLE
                    setData(it.avatar_url, it.login, it.url, it.repositories)
                }
            }
        }
    }

    private fun hideUiLayoutOnOff(stateNumber: Int) {
        binding.repoReclerView.visibility = stateNumber
        binding.profileLayout.visibility = stateNumber
    }

    private fun setData(avatar_url: String, login: String, url: String, repositories: ArrayList<UserRepositories>) {
        Glide.with(this).load(avatar_url).into(binding.profileImageView)
        binding.profileUsernameTextView.text = login
        binding.projectGitUrlTextView.text = url

        val mainAdapter = MainAdapter()
        mainAdapter.apply {
            submitList(repositories)
            setOnItemClickListener(object: MainListener{
                override fun onClick(view: View, position: Int) {
                    val intent = Intent(Intent.ACTION_VIEW, repositories[position].html_url.toUri())
                    startActivity(intent)
                }
            })
        }
        binding.repoReclerView.adapter = mainAdapter
    }
}