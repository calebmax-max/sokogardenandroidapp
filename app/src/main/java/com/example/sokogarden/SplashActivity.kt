package com.example.sokogarden

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Install the official Splash Screen API
        val splashScreen = installSplashScreen()
        
        super.onCreate(savedInstanceState)
        
        // 2. Set the custom layout for the animated part of the splash
        setContentView(R.layout.activity_splash)

        val logo = findViewById<ImageView>(R.id.splashLogo)
        val title = findViewById<TextView>(R.id.splashText)

        // 3. Add professional animations
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        logo.startAnimation(fadeIn)
        title.startAnimation(fadeIn)

        // 4. Smoothly transition to MainActivity after 3 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Ensure the user can't go back to the splash screen
        }, 3000)
    }
}