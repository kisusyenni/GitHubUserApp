package com.dicoding.githubuserapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.databinding.ItemRowUserBinding
import com.dicoding.githubuserapp.model.User

class ListUserAdapter(private val listUser: List<User>) : RecyclerView.Adapter<ListUserAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    // Declare inner class for declare component binding
    inner class ViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    // Create view holder layout
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    // Bind user holder data with components
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val userData = listUser[position]
        Glide.with(viewHolder.itemView.context)
            .load(userData.avatar)
            .into(viewHolder.binding.userPhoto)
        viewHolder.binding.tvUserUsername.text = viewHolder.itemView.context.getString(R.string.username, userData.username)

        // On user item click, send callback to intent to detail page
        viewHolder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(userData) }
    }

    override fun getItemCount(): Int = listUser.size

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}