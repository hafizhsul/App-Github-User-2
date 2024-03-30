package com.example.appgithubuser.ui.detailuser

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.appgithubuser.R
import com.example.appgithubuser.data.remote.response.DetailUserResponse
import com.example.appgithubuser.data.remote.response.ItemsItem
import com.example.appgithubuser.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private val viewModel by viewModels<DetailUserViewModel>()
    private lateinit var adapter: SectionsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val item = intent.getParcelableExtra<ItemsItem>("item")
        val username = item?.login ?: ""
        val id = item?.id ?: 0
        val avatarUrl = item?.avatarUrl ?: ""
        val htmlUrl = item?.htmlUrl ?: ""

        val mBundle = Bundle()
        mBundle.putString("username", username)

        adapter = SectionsPagerAdapter(this)
        adapter.username = username

        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabsLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Followers"
                1 -> "Following"
                else -> "Unknown"
            }
        }.attach()

        viewModel.getUserDetail(username)
        viewModel.detailuser.observe(this) { detailUser ->
            if (detailUser != null) {
                setUserData(detailUser)
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
                        _isChecked = true
                    } else {
                        binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border)
                        _isChecked = false
                    }
                }
            }
        }

        binding.fabFavorite.setOnClickListener {
            _isChecked = !_isChecked
            if (_isChecked) {
                viewModel.addFavoriteUser(id, username, avatarUrl, htmlUrl)
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
            } else {
                viewModel.removeFavoriteUser(id)
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border)
            }
        }
    }

    private fun setUserData(detailUser: DetailUserResponse) {
        Glide.with(this@DetailUserActivity)
            .load(detailUser.avatarUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .centerCrop()
            .circleCrop()
            .into(binding.imgDetailUser)
        if (detailUser.name != null) {
            binding.tvName.text = detailUser.name
        } else {
            binding.tvName.text = detailUser.login
        }
        binding.tvUsername.text = detailUser.login
        binding.tvFollowers.text = "${detailUser.followers} Followers"
        binding.tvFollowing.text = "${detailUser.following} Following"
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}