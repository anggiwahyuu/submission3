package com.example.aplikasigithubuser.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aplikasigithubuser.R

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        val background = object : Thread(){
            override fun run(){
                sleep(3000)
                val intent = Intent(baseContext, MainActivity::class.java)
                startActivity(intent)
            }
        }
        background.start()
    }
}