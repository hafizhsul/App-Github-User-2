package com.example.appgithubuser.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appgithubuser.R
import com.example.appgithubuser.data.remote.response.ItemsItem
import com.example.appgithubuser.databinding.ActivityMainBinding
import com.example.appgithubuser.ui.detailuser.DetailUserActivity
import com.example.appgithubuser.ui.favorite.FavoriteActivity
import com.example.appgithubuser.ui.utils.SettingActivity
import com.example.appgithubuser.ui.utils.SettingPreferences
import com.example.appgithubuser.ui.utils.SettingViewModel
import com.example.appgithubuser.ui.utils.ViewModelFactory
import com.example.appgithubuser.ui.utils.dataStore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.show()

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    viewModel.getUser(searchView.text.toString())
                    false
                }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.userData.observe(this) {
            if (it != null) {
                setUserData(it)
            }
        }
    }

    private fun setUserData(users: List<ItemsItem>) {
        val adapter = UserAdapter { user ->
            onItemClick(user)
        }
        adapter.submitList(users)
        binding.rvUser.adapter = adapter
    }

    private fun onItemClick(user: ItemsItem) {
        val intent = Intent(this, DetailUserActivity::class.java)
        intent.putExtra("item", user)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val menuItem = menu?.findItem(R.id.action_dark_mode)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                menuItem?.setIcon(R.drawable.ic_light_mode)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                menuItem?.setIcon(R.drawable.ic_dark_mode)
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite_menu -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
            }
            R.id.action_dark_mode -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}