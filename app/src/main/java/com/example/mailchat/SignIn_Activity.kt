package com.example.mailchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class SignIn_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) { 
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_signin)
    }

    fun signUpOnclick(view: View) {
        val intent = Intent(this,SignUp_Activity::class.java)
        this.startActivity(intent)
    }
}