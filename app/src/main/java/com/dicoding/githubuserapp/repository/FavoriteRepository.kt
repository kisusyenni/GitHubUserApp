package com.dicoding.githubuserapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.githubuserapp.database.Favorite
import com.dicoding.githubuserapp.database.FavoriteDao
import com.dicoding.githubuserapp.database.FavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoritesDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavoritesDao = db.favoriteDao()
    }

    fun getFavoriteUsers(): LiveData<List<Favorite>> = mFavoritesDao.getFavoriteUsers()

    fun insert(favorite: Favorite) {
        executorService.execute { mFavoritesDao.insert(favorite) }
    }

    fun delete(username: String) {
        executorService.execute { mFavoritesDao.delete(username) }
    }

    fun check(username: String) = mFavoritesDao.check(username)
}