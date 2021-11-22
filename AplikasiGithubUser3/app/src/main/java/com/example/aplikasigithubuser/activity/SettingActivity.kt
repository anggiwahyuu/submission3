package com.example.aplikasigithubuser.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.helper.PrefHelper
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {
    private val pref by lazy { PrefHelper(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        supportActionBar?.title = "Setting"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        switch_theme.isChecked = pref.getBoolean("pref_is_dark")

        switch_theme.setOnCheckedChangeListener { _, isChecked ->
            when(isChecked){
                true -> {
                    pref.put("pref_is_dark", true)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                false ->{
                    pref.put("pref_is_dark", false)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}