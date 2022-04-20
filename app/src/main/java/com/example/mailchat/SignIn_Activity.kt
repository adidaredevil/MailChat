package com.example.mailchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SignIn_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) { 
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_signin)
    }
}