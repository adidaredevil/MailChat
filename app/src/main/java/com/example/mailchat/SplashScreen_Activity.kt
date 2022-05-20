package com.example.mailchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.example.mailchat.Auth.SignIn_Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SplashScreen_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.lightest))
        supportActionBar?.show()
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