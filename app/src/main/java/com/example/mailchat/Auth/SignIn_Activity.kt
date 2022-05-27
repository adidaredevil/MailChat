package com.example.mailchat.Auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.mailchat.After_Login_Activity
import com.example.mailchat.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signin.*

class SignIn_Activity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) { 
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_signin)
        firebaseAuth = FirebaseAuth.getInstance()

    }

    fun signUpOnclick(view: View) {
        val intent = Intent(this, SignUp_Activity::class.java)
        this.startActivity(intent)
    }

    fun signIntoMailchat(view: View) {
        email = edtEmailSignIn.text.toString()
        password = edtPasswordSignIn.text.toString()
        if(email.isEmpty()||password.isEmpty()){
            Toast.makeText(this,"Please fill your credentials",Toast.LENGTH_SHORT).show()
        }
        else{
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithEmail:success")
                        val intent = Intent(this, After_Login_Activity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed please check your credentials and try again.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }
}