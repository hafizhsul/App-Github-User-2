package com.example.appgithubuser.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.appgithubuser.data.remote.response.ItemsItem
import com.example.appgithubuser.databinding.ListItemDataBinding

class UserAdapter(private val onItemClick: (ItemsItem) -> Unit) : ListAdapter<ItemsItem, UserAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    inner class MyViewHolder(val binding: ListItemDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem, position: Int) {
            binding.tvItemUser.text = user.login
            binding.tvItemUrl.text = user.htmlUrl
            Glide.with(itemView)
                .load(user.avatarUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .circleCrop()
                .into(binding.imgItemPhoto)

            itemView.setOnClickListener {
                onItemClick.invoke(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListItemDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user, position)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}