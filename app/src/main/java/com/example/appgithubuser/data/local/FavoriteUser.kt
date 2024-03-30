package com.example.appgithubuser.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "favorite_users")
@Parcelize
data class FavoriteUser(
    @PrimaryKey var id: Int,
    var username: String?,
    var avatarUrl: String?,
    var htmlUrl: String?,
): Parcelable