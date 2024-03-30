package com.example.appgithubuser.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appgithubuser.data.local.FavoriteUser
import com.example.appgithubuser.data.remote.response.ItemsItem
import com.example.appgithubuser.databinding.ActivityFavoriteBinding
import com.example.appgithubuser.ui.detailuser.DetailUserActivity
import com.example.appgithubuser.ui.main.UserAdapter

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val viewModel by viewModels<FavoriteViewModel>()
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.show()

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavoriteUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvFavoriteUser.addItemDecoration(itemDecoration)

        adapter = UserAdapter { user ->
            onItemClick(user)
        }
        binding.rvFavoriteUser.adapter = adapter

        viewModel.getFavoriteUser()?.observe(this) {
            if (it != null) {
                val item = mapList(it)
                adapter.submitList(item)
            }
        }
    }

    private fun mapList(users: List<FavoriteUser>): ArrayList<ItemsItem> {
        val items = arrayListOf<ItemsItem>()
        users.map {
            val item = ItemsItem(
                id = it.id,
                login = it.username.toString(),
                avatarUrl = it.avatarUrl.toString(),
                htmlUrl = it.htmlUrl.toString()
            )
            items.add(item)
        }
        return items
    }

    private fun onItemClick(user: ItemsItem) {
        val intent = Intent(this, DetailUserActivity::class.java)
        intent.putExtra("item", user)
        startActivity(intent)
    }
}