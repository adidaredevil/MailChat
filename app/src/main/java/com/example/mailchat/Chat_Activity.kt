package com.example.mailchat

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mailchat.Adapters.ScreenSliderAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_chat.*


class Chat_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val window: Window = this.getWindow()
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.light))
        supportActionBar?.show()
        setSupportActionBar(toolBar)
        viewPager.adapter = ScreenSliderAdapter(this)
        TabLayoutMediator(tabs,viewPager, TabLayoutMediator.TabConfigurationStrategy{ tab: TabLayout.Tab, pos: Int ->
            when(pos){
                0 -> tab.text="CHATS"
                else -> tab.text="PEOPLE"
            }
        }).attach()
    }
}