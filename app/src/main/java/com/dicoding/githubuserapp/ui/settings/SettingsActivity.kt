package com.dicoding.githubuserapp.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.databinding.ActivitySettingsBinding
import com.dicoding.githubuserapp.helper.SettingsViewModelFactory
import com.dicoding.githubuserapp.ui.favorite.FavoriteActivity

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsActivity : AppCompatActivity() {
    private lateinit var activitySettingsBinding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activitySettingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(activitySettingsBinding.root)

        val switchTheme = activitySettingsBinding.switchTheme
        val pref = SettingsPreferences.getInstance(dataStore)
        val settingsViewModel =
            ViewModelProvider(this, SettingsViewModelFactory(pref))[SettingsViewModel::class.java]

        settingsViewModel.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    switchTheme.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    switchTheme.isChecked = false
                }
            })

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingsViewModel.saveThemeSetting(isChecked)
        }

        activitySettingsBinding.favoriteIntentBtn.setOnClickListener { view ->
            if (view.id == R.id.favorite_intent_btn) {
                val intent = Intent(this@SettingsActivity, FavoriteActivity::class.java)
                startActivity(intent)
            }
        }

    }
}