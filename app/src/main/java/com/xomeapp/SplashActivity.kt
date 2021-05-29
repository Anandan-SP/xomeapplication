package com.xomeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()!!.hide()
        setContentView(R.layout.activity_splash)
        navigateScreen()
    }

    /**
     * Navigate the screen from splash to Search Screen
     */
    private fun navigateScreen() {
        Handler(Looper.getMainLooper()).postDelayed({
            val mainIntent = Intent(this@SplashActivity, SearchActivity::class.java)
            startActivity(mainIntent)
            finish()
        }, 2500)
    }
}