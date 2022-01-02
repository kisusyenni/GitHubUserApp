package com.dicoding.githubuserapp.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.githubuserapp.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashIntent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(splashIntent)
        finish()
    }
}