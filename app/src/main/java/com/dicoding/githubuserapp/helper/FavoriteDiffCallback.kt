package com.dicoding.githubuserapp.helper

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.githubuserapp.database.Favorite

class FavoriteDiffCallback(private val mOldFavoriteList: List<Favorite>, private val mNewFavoriteList: List<Favorite>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldFavoriteList.size
    }
    override fun getNewListSize(): Int {
        return mNewFavoriteList.size
    }
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldFavoriteList[oldItemPosition].id == mNewFavoriteList[newItemPosition].id
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFavorite = mOldFavoriteList[oldItemPosition]
        val newFavorite = mNewFavoriteList[newItemPosition]
        return oldFavorite.username == newFavorite.username && oldFavorite.avatar == newFavorite.avatar
    }
}