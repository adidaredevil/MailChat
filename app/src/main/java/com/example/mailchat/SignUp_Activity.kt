package com.example.mailchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class SignUp_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_sign_up)
    }

    fun signInOnclick(view: View) {
        val intent= Intent(this, SignIn_Activity::class.java)
        startActivity(intent)
    }
}