package com.example.mailchat

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.mailchat.Modals.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_splash_screen.view.*
import kotlinx.android.synthetic.main.list_item.view.*

class UserViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
    fun bind(user: User) = with(itemView){
        countTV.isVisible=false
        timeTV.isVisible=false
        titleTV.text = user.name
        subtitleTV.text=user.status
        Picasso.get()
            .load(user.thumbUrl)
            .placeholder(R.drawable.ic_default_profile_picture_foreground)
            .error(R.drawable.ic_default_profile_picture_foreground)
            .into(imgUser)

    }
}