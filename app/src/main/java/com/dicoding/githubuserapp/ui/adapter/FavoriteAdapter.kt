package com.dicoding.githubuserapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.database.Favorite
import com.dicoding.githubuserapp.databinding.ItemRowUserBinding
import com.dicoding.githubuserapp.helper.FavoriteDiffCallback

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private val listFavorite = ArrayList<Favorite>()

    fun setListFavorites(listFavorite: List<Favorite>) {
        val diffCallback = FavoriteDiffCallback(this.listFavorite, listFavorite)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavorite.clear()
        this.listFavorite.addAll(listFavorite)
        diffResult.dispatchUpdatesTo(this)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }
    override fun onBindViewHolder(viewHolder: FavoriteViewHolder, position: Int) {
        val userData = listFavorite[position]
        Glide.with(viewHolder.itemView.context)
            .load(userData.avatar)
            .into(viewHolder.binding.userPhoto)
        viewHolder.binding.tvUserUsername.text = viewHolder.itemView.context.getString(R.string.username, userData.username)

        // On user item click, send callback to intent to detail page
        viewHolder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listFavorite[viewHolder.adapterPosition].username) }
    }
    override fun getItemCount(): Int {
        return listFavorite.size
    }
    inner class FavoriteViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(username: String)
    }
}