package com.example.mailchat.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mailchat.ChatViewHolder
import com.example.mailchat.Chating_Activity
import com.example.mailchat.Modals.Inbox
import com.example.mailchat.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_inbox.*



class InboxFragment : Fragment() {

    private lateinit var mAdapter: FirebaseRecyclerAdapter<Inbox, ChatViewHolder>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }
    private val mDatabase by lazy {
        FirebaseDatabase.getInstance("https://mailchat-app-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child("chats").child(auth.uid!!)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewManager = LinearLayoutManager(requireContext())
        setupAdapter()
        return inflater.inflate(R.layout.fragment_inbox, container, false)
    }

    private fun setupAdapter() {

        val options = FirebaseRecyclerOptions.Builder<Inbox>()
            .setLifecycleOwner(viewLifecycleOwner)
            .setQuery(mDatabase, Inbox::class.java)
            .build()
        // Instantiate Paging Adapter
        Log.d("gg","here")
        mAdapter = object : FirebaseRecyclerAdapter<Inbox, ChatViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
                val inflater = layoutInflater
                return ChatViewHolder(inflater.inflate(R.layout.list_item, parent, false))
            }

            override fun onBindViewHolder(
                viewHolder: ChatViewHolder,
                position: Int,
                inbox: Inbox
            ) {
                viewHolder.bind(item = inbox) { name: String, photo: String, id: String ->
                    val intent = Intent(requireContext(),Chating_Activity::class.java)
                    intent.putExtra("id",id)
                    intent.putExtra("name",name)
                    intent.putExtra("photo",photo)
                    intent.putExtra("source","inbox")
                    startActivity(intent)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mAdapter.stopListening()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rclInbox.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = mAdapter
        }

    }

}