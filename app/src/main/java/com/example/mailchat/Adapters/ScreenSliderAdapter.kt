package com.example.mailchat.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mailchat.Fragments.InboxFragment
import com.example.mailchat.Fragments.PeopleFragment

class ScreenSliderAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int =2

    override fun createFragment(position: Int): Fragment = when(position) {
        0 -> InboxFragment()
        else -> PeopleFragment()
    }
}
