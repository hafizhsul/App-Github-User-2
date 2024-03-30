package com.example.appgithubuser.ui.detailuser

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.appgithubuser.data.local.AppDatabase
import com.example.appgithubuser.data.local.FavoriteUser
import com.example.appgithubuser.data.local.FavoriteUserDao
import com.example.appgithubuser.data.remote.response.DetailUserResponse
import com.example.appgithubuser.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : AndroidViewModel(application) {
    private val _detailUser = MutableLiveData<DetailUserResponse?>()
    val detailuser: LiveData<DetailUserResponse?> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var favoriteUserDao: FavoriteUserDao? = null
    private var appDatabase: AppDatabase? = null

    init {
        appDatabase = AppDatabase.getDatabase(application)
        favoriteUserDao = appDatabase?.FavoriteUserDao()
    }

    companion object {
        private const val TAG = "DetailUserActivity"
    }

    fun getUserDetail(name: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(name)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _detailUser.value = responseBody
                    }
                } else {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun addFavoriteUser(id: Int, username: String, avatarUrl: String, htmlUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            var user = FavoriteUser(
                username = username,
                avatarUrl = avatarUrl,
                id = id,
                htmlUrl = htmlUrl
            )
            favoriteUserDao?.addFavoriteUser(user)
        }
    }

    fun removeFavoriteUser(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            favoriteUserDao?.deleteFavoriteUser(id)
        }
    }

    suspend fun checkUser(id: Int) = favoriteUserDao?.checkUser(id)
}