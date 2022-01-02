package com.dicoding.githubuserapp.ui.favorite

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.adapter.FavoriteAdapter
import com.dicoding.githubuserapp.databinding.ActivityFavoriteBinding
import com.dicoding.githubuserapp.helper.ViewModelFactory
import com.dicoding.githubuserapp.model.User

class FavoriteActivity : AppCompatActivity() {

    private var activityFavoriteBinding: ActivityFavoriteBinding? = null
    private var rvFavorite: RecyclerView? = null
    private val favoriteDataList = ArrayList<User>()
    private var errorMessage: TextView? = null
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityFavoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(activityFavoriteBinding?.root)

        supportActionBar?.elevation = 0f
        supportActionBar?.title = resources.getString(R.string.my_favorite)

        favoriteAdapter = FavoriteAdapter()

        val mainViewModel = obtainViewModel(this@FavoriteActivity)
        mainViewModel.getFavoriteUsers().observe(this, { favoriteList ->
            if (favoriteList != null) {
                favoriteAdapter.setListFavorites(favoriteList)
            }
        })

        rvFavorite = activityFavoriteBinding?.rvFavoriteUsers
        rvFavorite?.adapter = favoriteAdapter
        rvFavorite?.setHasFixedSize(true)

        // Config layout by screen orientation
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvFavorite?.layoutManager = GridLayoutManager(this, 3)
        } else {
            rvFavorite?.layoutManager = GridLayoutManager(this, 2)
        }

        // Bind message text view
        errorMessage = activityFavoriteBinding?.favoriteMessage
        errorMessage?.visibility = View.GONE
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        activityFavoriteBinding = null
    }
}