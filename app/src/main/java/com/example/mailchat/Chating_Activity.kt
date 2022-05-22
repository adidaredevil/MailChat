package com.example.mailchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.google.GoogleEmojiProvider
import kotlinx.android.synthetic.main.activity_after_login.*
import kotlinx.android.synthetic.main.activity_chating.*

class Chating_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EmojiManager.install(GoogleEmojiProvider())
        setContentView(R.layout.activity_chating)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.dark))
        supportActionBar?.show()
        setSupportActionBar(toolBar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }


    }

    fun hell(view: View) {
        Toast.makeText(this, "Hell", Toast.LENGTH_SHORT).show()
    }
}