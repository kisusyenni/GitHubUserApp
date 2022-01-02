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
import com.dicoding.githubuserapp.ui.adapter.DetailPagerAdapter
import com.dicoding.githubuserapp.database.Favorite
import com.dicoding.githubuserapp.databinding.ActivityUserDetailBinding
import com.dicoding.githubuserapp.helper.ViewModelFactory
import com.dicoding.githubuserapp.model.UserDetailResponse
import com.dicoding.githubuserapp.ui.favorite.FavoriteAddUpdateViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var username: String
    private var favorite: Favorite? = null

    private lateinit var favoriteAddUpdateViewModel: FavoriteAddUpdateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get username from intent
        username = intent.getStringExtra(EXTRA_USER).toString()

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
        val userDetailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[UserDetailViewModel::class.java]
        userDetailViewModel.sendUsername(username)
        userDetailViewModel.userDetail.observe(this, { data ->
            setUserDetail(data)

            favorite.let { favorite ->
                favorite?.username = username
                favorite?.avatar = data.avatarUrl
            }
        })
        userDetailViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        favoriteAddUpdateViewModel = obtainViewModel(this@UserDetailActivity)

        binding.favoriteCheckBox.setOnClickListener {
            setFavoriteListener()
        }

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

    // set favorite button onclick
    private fun setFavoriteListener() {
        val isChecked = binding.favoriteCheckBox.isChecked
        if (favorite != null) {
            if (isChecked) {
                favoriteAddUpdateViewModel.insert(favorite!!)
                showToast(getString(R.string.add_to_favorite))
            } else {
                favoriteAddUpdateViewModel.delete(favorite!!)
                showToast(getString(R.string.remove_from_favorite))
            }
        }

        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteAddUpdateViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteAddUpdateViewModel::class.java]
    }

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_followers,
            R.string.tab_text_following
        )
    }

}