package com.example.firebaseproject137.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import gov.rajasthan.firstapp137.databinding.ActivityAdminLoginBinding

class AdminLoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityAdminLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdminLogin.setOnClickListener {
            startActivity(Intent(this, AdminHomeActivity::class.java))
        }
    }
}