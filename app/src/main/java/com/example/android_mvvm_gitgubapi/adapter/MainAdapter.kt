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

class MainAdapter : ListAdapter<UserRepositories, MainAdapter.MainViewHolder>(MainDiffCallback()) {

    lateinit var clickListener: MainListener

    fun setOnItemClickListener(listener : MainListener) {
        this.clickListener = listener
    }

    companion object {
        class MainDiffCallback : DiffUtil.ItemCallback<UserRepositories>() {
            override fun areItemsTheSame(oldItem: UserRepositories, newItem: UserRepositories): Boolean {
                // 현재 리스트의 노출하고 있는 아이템과, 새로운 아이템이 같은지 비교(고유값으로 비교)
                // 여기선 레포지토리의 url을 비교해준다.
                return oldItem.html_url == newItem.html_url
            }

            override fun areContentsTheSame(oldItem: UserRepositories, newItem: UserRepositories): Boolean {
                // 현재 리스트에 노출하고 있는 아이템과, 새로운 아이템의 equals 비교
                // areItemsTheSame에서 true가 나올경우 추가적으로 비교
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.repositories_item, parent, false)
        val binding = RepositoriesItemBinding.bind(view)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


   inner class MainViewHolder(private val binding: RepositoriesItemBinding) : RecyclerView.ViewHolder(binding.root) {
       fun bind(userRepositories: UserRepositories) {
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

