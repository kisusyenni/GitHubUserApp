package com.dicoding.githubuserapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var activitySettingsBinding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activitySettingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(activitySettingsBinding.root)

        activitySettingsBinding.favoriteIntentBtn.setOnClickListener { view ->
            if (view.id == R.id.favorite_intent_btn) {
                val intent = Intent(this@SettingsActivity, FavoriteActivity::class.java)
                startActivity(intent)
            }
        }
    }
}