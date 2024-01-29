package com.example.firebaseproject137.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import gov.rajasthan.firstapp137.databinding.ActivityAdminSplashBinding

class AdminSplashActivity : AppCompatActivity() {
    lateinit var binding: ActivityAdminSplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminSplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler().postDelayed(object : Runnable{
            override fun run() {
                startActivity(Intent(this@AdminSplashActivity, AdminLoginActivity::class.java))

                finish()
            }

        }, 2000)
    }
}