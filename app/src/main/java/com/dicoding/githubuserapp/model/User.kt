package com.dicoding.githubuserapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val username: String,
//    val name: String,
    val avatar: String?,
//    val company: String,
//    val location: String,
//    val repository: String,
//    val follower: String,
//    val following: String
) : Parcelable