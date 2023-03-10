package com.example.android_mvvm_gitgubapi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android_mvvm_gitgubapi.R
import com.example.android_mvvm_gitgubapi.adapter.listener.MainListener
import com.example.android_mvvm_gitgubapi.databinding.RepositoriesItemBinding
import com.example.android_mvvm_gitgubapi.model.RepositoriesItem
import com.example.android_mvvm_gitgubapi.model.UserRepositories
import com.example.android_mvvm_gitgubapi.ui.RepositoriesItemUiState
import com.example.android_mvvm_gitgubapi.ui.RepositoriesUiState

// UserRepositories
// RepositoriesUiState
class MainAdapter : ListAdapter<RepositoriesItemUiState, MainAdapter.MainViewHolder>(MainDiffCallback()) {

    lateinit var clickListener: MainListener

    fun setOnItemClickListener(listener : MainListener) {
        this.clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.repositories_item, parent, false)
        val binding = RepositoriesItemBinding.bind(view)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    //
    // RepositoriesItemUiState
   inner class MainViewHolder(private val binding: RepositoriesItemBinding) : RecyclerView.ViewHolder(binding.root) {
       fun bind(userRepositories: RepositoriesItemUiState) {
           binding.apply {
               repositoryName.text = userRepositories.name
               repositoryDes.text = userRepositories.description
               stars.text = "${userRepositories.stargazers_count} Stars"
               language.text = userRepositories.language
               itemView.setOnClickListener {
                   clickListener.onClick(it, adapterPosition)
               }
           }
       }
   }
}

class MainDiffCallback : DiffUtil.ItemCallback<RepositoriesItemUiState>() {
    override fun areItemsTheSame(oldItem: RepositoriesItemUiState, newItem: RepositoriesItemUiState): Boolean {
        // ?????? ???????????? ???????????? ?????? ????????????, ????????? ???????????? ????????? ??????(??????????????? ??????)
        // ????????? ?????????????????? url??? ???????????????.
        return oldItem.html_url == newItem.html_url
    }

    override fun areContentsTheSame(oldItem: RepositoriesItemUiState, newItem: RepositoriesItemUiState): Boolean {
        // ?????? ???????????? ???????????? ?????? ????????????, ????????? ???????????? equals ??????
        // areItemsTheSame?????? true??? ???????????? ??????????????? ??????
        return oldItem == newItem
    }
}

