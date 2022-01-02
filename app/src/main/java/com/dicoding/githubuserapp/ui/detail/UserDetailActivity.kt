package com.dicoding.githubuserapp.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.databinding.ActivityUserDetailBinding
import com.dicoding.githubuserapp.helper.ViewModelFactory
import com.dicoding.githubuserapp.model.UserDetailResponse
import com.dicoding.githubuserapp.ui.adapter.DetailPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var username: String
    private lateinit var avatar: String
    private var id: Int = 0
//    private var favorite: Favorite? = null

    private lateinit var userDetailViewModel: UserDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get username from intent
        username = intent.getStringExtra(EXTRA_USER).toString()
        avatar = intent.getStringExtra(EXTRA_AVATAR).toString()
        id = intent.getIntExtra(EXTRA_ID, 0)

        // attach fragment to activity
        val detailPagerAdapter = DetailPagerAdapter(this, username)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = detailPagerAdapter

        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        // Set action bar
        supportActionBar?.elevation = 0f
        supportActionBar?.title = username
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // get user detail data from Live Data
        userDetailViewModel = obtainViewModel(this@UserDetailActivity)
        userDetailViewModel.sendUsername(username)
        userDetailViewModel.userDetail.observe(this, { data ->
            setUserDetail(data)

            var isFavorite = false
            CoroutineScope(Dispatchers.IO).launch {
                val count = userDetailViewModel.checkUser(username)
                withContext(Dispatchers.Main) {
                    if (count > 0) {
                        setStatusFavorite(true)
                        isFavorite = true
                    } else {
                        setStatusFavorite(false)
                    }
                }
            }

            binding.fabFavorite.setOnClickListener {
                isFavorite = !isFavorite

                if (isFavorite) {
                    userDetailViewModel.addToFavorite(username, id, avatar)
                    Toast.makeText(
                        this@UserDetailActivity, resources.getString(R.string.add_to_favorite),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    userDetailViewModel.removeFromFavorite(username)
                    Toast.makeText(
                        this@UserDetailActivity, resources.getString(R.string.remove_from_favorite),
                        Toast.LENGTH_LONG
                    )
                        .show()
                }

                setStatusFavorite(isFavorite)
            }


        })
        userDetailViewModel.isLoading.observe(this, {
            showLoading(it)
        })

    }

    private fun setUserDetail(user: UserDetailResponse) {
        // Bind user data to components
        binding.detailUsername.text = getString(R.string.username, username)
        binding.detailName.text = user.name
        Glide.with(this@UserDetailActivity)
            .load(user.avatarUrl)
            .into(binding.detailAvatar)
        if (user.company == null) {
            binding.detailCompany.isVisible = false
        } else {
            binding.detailCompany.text = user.company
        }

        if (user.location == null || user.location == "") {
            binding.detailLocation.isVisible = false
        } else {
            binding.detailLocation.text = user.location
        }

        binding.detailRepository.text =
            getString(R.string.repositoryTotal, user.publicRepos.toString())

    }

    private fun showLoading(isLoading: Boolean) {
        binding.detailProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    private fun setStatusFavorite(isFavorite: Boolean) {
        if (isFavorite) {
            binding.fabFavorite.setImageResource(R.drawable.ic_favorite_24)
        } else {
            binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border_24)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): UserDetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[UserDetailViewModel::class.java]
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVATAR = "extra_avatar"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_followers,
            R.string.tab_text_following
        )
    }

}