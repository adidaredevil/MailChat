package com.example.mailchat

import android.os.Build
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.mailchat.formatAsListItem
import com.example.mailchat.Modals.Inbox
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_inbox.view.*
import kotlinx.android.synthetic.main.list_item.view.*

class ChatViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: Inbox, onClick: (name:String, photo: String, id:String)-> Unit)=
        with(itemView) {
            countTV.isVisible = item.count >0
            countTV.text = item.count.toString()
            timeTV.text = item.time.formatAsListItem(context)
            Log.d("ff","here")
            titleTV.text = item.name
            subtitleTV.text = item.msg
            Picasso.get()
                .load(item.image)
                .placeholder(R.drawable.ic_default_profile_picture_foreground)
                .error(R.drawable.ic_default_profile_picture_foreground)
                .into(imgUser)
            setOnClickListener{
                onClick.invoke(item.name,item.image,item.from)
            }

        }

}