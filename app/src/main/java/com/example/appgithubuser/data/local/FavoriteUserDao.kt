package com.example.appgithubuser.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteUserDao {
    @Insert()
    fun addFavoriteUser(user: FavoriteUser)

    @Query("SELECT * FROM favorite_users")
    fun loadFavoriteUser(): LiveData<List<FavoriteUser>>
    
    @Query("SELECT count(*) FROM favorite_users WHERE id = :id")
    fun checkUser(id: Int): Int

    @Query("DELETE FROM favorite_users WHERE id = :id")
    fun deleteFavoriteUser(id: Int)
}