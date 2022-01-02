package com.dicoding.githubuserapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuserapp.database.Favorite
import com.dicoding.githubuserapp.repository.FavoriteRepository

class FavoriteViewModel(application: Application): ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)
    fun getFavoriteUsers(): LiveData<List<Favorite>> = mFavoriteRepository.getFavoriteUsers()
}