package com.dicoding.githubuserapp.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.adapter.DetailPagerAdapter
import com.dicoding.githubuserapp.databinding.ActivityUserDetailBinding
import com.dicoding.githubuserapp.model.UserDetailResponse
import com.dicoding.githubuserapp.network.ApiConfig
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailActivity : AppCompatActivity(){

    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var username: String
    private lateinit var user: UserDetailResponse

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
        supportActionBar?.elevation = 0f

        // Set action bar
        supportActionBar?.title = username
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // get user detail data
        getUserDetail()
    }

    // Get user detail data from Github API
    private fun getUserDetail() {
        showLoading(true)
        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                showLoading(false)
                val responseBody = response.body()
                if (responseBody != null) {
                    user = responseBody
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

                    binding.detailRepository.text = getString(R.string.repositoryTotal, user.publicRepos.toString())

                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        val progressBar: View = binding.detailProgressBar
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_followers,
            R.string.tab_text_following
        )
        const val TAG = "UserDetailActivity"
    }

}