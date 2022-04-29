package com.example.mailchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_chat.*

class Chat_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
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