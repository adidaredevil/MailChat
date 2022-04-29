package com.example.mailchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SplashScreen_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen)


        Handler().postDelayed({
            val firebaseAuth = FirebaseAuth.getInstance()
            val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
            if(firebaseUser==null){
                val intent = Intent(this, SignIn_Activity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this, Chat_Activity::class.java)
                startActivity(intent)
                finish()
            }

        }, 1500)
    }
}