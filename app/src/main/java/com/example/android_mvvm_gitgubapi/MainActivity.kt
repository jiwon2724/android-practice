package com.example.android_mvvm_gitgubapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.android_mvvm_gitgubapi.databinding.ActivityMainBinding
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

        binding.searchButton.setOnClickListener {
            viewModel.getRepositories(binding.userNameEditText.text.toString())
        }

        viewModel.repositoriesResult.observe(this) {
            Log.d("repositoriesResult : ", it.toString())
            when (it) {
                null -> {
                    hideUiLayoutOnOff(8) // GONE
                    Toast.makeText(this, "존재하지않는 아이디 입니다.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    hideUiLayoutOnOff(0) // VISIBLE

                }
            }
        }
    }

    private fun hideUiLayoutOnOff(stateNumber: Int) {
        binding.repoReclerView.visibility = stateNumber
        binding.profileLayout.visibility = stateNumber
    }
}