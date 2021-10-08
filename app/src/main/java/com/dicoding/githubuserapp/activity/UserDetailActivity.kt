package com.dicoding.githubuserapp.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.data.User
import com.dicoding.githubuserapp.databinding.ActivityUserDetailBinding

class UserDetailActivity : AppCompatActivity(){
    private lateinit var binding: ActivityUserDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get user data from intent
        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User

        // Set action bar
        supportActionBar?.title = user.name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Bind user data to components
        binding.detailUsername.text = getString(R.string.username, user.username)
        binding.detailName.text = user.name
        binding.detailAvatar.setImageResource(user.avatar)
        binding.detailCompany.text = user.company
        binding.detailLocation.text = user.location
        binding.detailRepository.text = user.repository
        binding.detailFollower.text = user.follower
        binding.detailFollowing.text = user.following

        // Set click listener to share detail button
        binding.detailShareBtn.setOnClickListener {
            val message = "Hi! I'm ${user.name}. Follow my Github Account: @${user.username}\nhttps://www.github.com/${user.username}\n(Total Repository: ${user.repository})"

            // Share message as text
            val shareProfileIntent = Intent(Intent.ACTION_SEND).apply{
                putExtra(Intent.EXTRA_TEXT, message)
                type = "text/plain"
            }

            startActivity(Intent.createChooser(shareProfileIntent, null))
        }
    }

    companion object {
        const val EXTRA_USER = "extra_user"
    }
}