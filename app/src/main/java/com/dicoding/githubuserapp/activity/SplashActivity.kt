package com.dicoding.githubuserapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashIntent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(splashIntent)
        finish()
    }
}