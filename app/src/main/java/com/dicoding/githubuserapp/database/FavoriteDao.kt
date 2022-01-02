package com.dicoding.githubuserapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favorite: Favorite)

    @Query("DELETE FROM favorite WHERE favorite.id = :id")
    fun delete(id: Int): Int

    @Query("SELECT * from favorite ORDER BY id ASC")
    fun getFavoriteUsers(): LiveData<List<Favorite>>

    @Query("SELECT count(*) FROM favorite WHERE favorite.id = :id")
    fun check(id: Int): Int
}