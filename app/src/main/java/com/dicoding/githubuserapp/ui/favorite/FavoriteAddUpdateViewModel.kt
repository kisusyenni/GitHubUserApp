package com.dicoding.githubuserapp.ui.favorite

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dicoding.githubuserapp.database.Favorite
import com.dicoding.githubuserapp.repository.FavoriteRepository

class FavoriteAddUpdateViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)
    fun insert(favorite: Favorite) {
        mFavoriteRepository.insert(favorite)
    }
    fun update(favorite: Favorite) {
        mFavoriteRepository.update(favorite)
    }
    fun delete(favorite: Favorite) {
        mFavoriteRepository.delete(favorite)
    }


}