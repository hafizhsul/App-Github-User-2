package com.example.appgithubuser.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.appgithubuser.data.local.AppDatabase
import com.example.appgithubuser.data.local.FavoriteUser
import com.example.appgithubuser.data.local.FavoriteUserDao

class FavoriteViewModel(application: Application): AndroidViewModel(application) {

    private var favoriteUserDao: FavoriteUserDao? = null
    private var appDatabase: AppDatabase? = null

    init {
        appDatabase = AppDatabase.getDatabase(application)
        favoriteUserDao = appDatabase?.FavoriteUserDao()
    }

    fun getFavoriteUser(): LiveData<List<FavoriteUser>>? {
        return favoriteUserDao?.loadFavoriteUser()
    }
}