package com.dicoding.githubuserapp.ui.favorite

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.database.Favorite
import com.dicoding.githubuserapp.databinding.ActivityFavoriteBinding
import com.dicoding.githubuserapp.helper.ViewModelFactory
import com.dicoding.githubuserapp.ui.adapter.FavoriteAdapter
import com.dicoding.githubuserapp.ui.detail.UserDetailActivity

class FavoriteActivity : AppCompatActivity() {

    private var activityFavoriteBinding: ActivityFavoriteBinding? = null
    private var rvFavorite: RecyclerView? = null
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
                showEmpty(false)
                favoriteAdapter.setListFavorites(favoriteList)

                favoriteAdapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: Favorite) {
                        val userDetailIntent = Intent(this@FavoriteActivity, UserDetailActivity::class.java)
                        userDetailIntent.putExtra(UserDetailActivity.EXTRA_USER, data.username)
                        userDetailIntent.putExtra(UserDetailActivity.EXTRA_AVATAR, data.avatar)
                        userDetailIntent.putExtra(UserDetailActivity.EXTRA_ID, data.id)
                        startActivity(userDetailIntent)
                    }
                })
            } else {
                showEmpty(true)
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
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    private fun showEmpty(state: Boolean) {
        if (state) {
            activityFavoriteBinding?.favoriteMessage?.text = resources.getString(R.string.favorite_empty)
            activityFavoriteBinding?.favoriteMessage?.visibility = View.VISIBLE
            activityFavoriteBinding?.rvFavoriteUsers?.visibility = View.INVISIBLE

        } else {
            activityFavoriteBinding?.favoriteMessage?.visibility = View.INVISIBLE
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        activityFavoriteBinding = null
    }
}