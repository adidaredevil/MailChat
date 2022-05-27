package com.example.mailchat.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mailchat.formatAsTime
import com.example.mailchat.Modals.ChatEvent
import com.example.mailchat.Modals.DateHeader
import com.example.mailchat.Modals.Message
import com.example.mailchat.R
import kotlinx.android.synthetic.main.list_item_chat_sent.view.*
import kotlinx.android.synthetic.main.list_item_date_header.view.*

class ChatAdapter(private var list: MutableList<ChatEvent>,private val currUID:String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflate = {layout :Int ->
            LayoutInflater.from(parent.context).inflate(layout,parent,false)
        }
        return when (viewType){
            TEXT_MESSAGE_RECEIVED -> {
                MessageViewHolder(inflate(R.layout.list_item_chat_recieved))
            }
            TEXT_MESSAGE_SENT ->{
                MessageViewHolder(inflate(R.layout.list_item_chat_sent))
            }
            DATE_HEADER ->{
                DateViewHolder(inflate(R.layout.list_item_date_header))
            }
            else ->{
                EmptyViewHolder(inflate(R.layout.empty_view))
            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when ( val item = list[position]){
            is DateHeader ->{
                holder.itemView.textViewHeader.text= item.date
            }
            is Message ->{
                holder.itemView.apply {
                    content.text= item.msg
                    time.text = item.sentAt.formatAsTime()
                }
            }
        }
    }

    override fun getItemCount(): Int =  list.size


    override fun getItemViewType(position: Int): Int {
        return when(val event = list[position]){
            is Message -> {
                if(event.senderId==currUID){
                    TEXT_MESSAGE_SENT
                }else{
                    TEXT_MESSAGE_RECEIVED
                }
            }
            is DateHeader ->{
                DATE_HEADER
            }
            else ->{
                UNSUPPORTED
            }
        }
    }
    class DateViewHolder(view: View):RecyclerView.ViewHolder(view)

    class MessageViewHolder(view: View):RecyclerView.ViewHolder(view)


    companion object{
        private const val UNSUPPORTED = -1
        private const val TEXT_MESSAGE_RECEIVED = 0
        private const val TEXT_MESSAGE_SENT = 1
        private const val DATE_HEADER = 2


    }
}