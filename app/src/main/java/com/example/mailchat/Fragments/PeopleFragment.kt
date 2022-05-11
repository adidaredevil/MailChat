package com.example.mailchat.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mailchat.Modals.User
import com.example.mailchat.R
import com.example.mailchat.UserViewHolder
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_people.*
import java.lang.Exception

class PeopleFragment : Fragment() {
    lateinit var mAdapter: FirestorePagingAdapter<User,UserViewHolder>
    val auth by lazy{
        FirebaseAuth.getInstance()
    }
    val database by lazy{
        FirebaseFirestore.getInstance().collection("users")
            .orderBy("name", Query.Direction.DESCENDING)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupAdapter()
        return inflater.inflate(R.layout.fragment_people, container, false)
    }

    private fun setupAdapter() {
        val config = PagedList.Config.Builder()
                        .setPrefetchDistance(2)
                        .setPageSize(20)
                        .setEnablePlaceholders(false)
                        .build()
        val options = FirestorePagingOptions.Builder<User>()
            .setLifecycleOwner(viewLifecycleOwner)
            .setQuery(database,config,User::class.java)
            .build()
        mAdapter= object : FirestorePagingAdapter<User,UserViewHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
                val view = layoutInflater.inflate(R.layout.list_item,parent,false)
                return UserViewHolder(view)
            }

            override fun onBindViewHolder(holder: UserViewHolder, position: Int, model: User) {
                holder.bind(user = model)
            }

            override fun onError(e: Exception) {
                super.onError(e)
            }

            override fun onLoadingStateChanged(state: LoadingState) {
                super.onLoadingStateChanged(state)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rclPeople.apply{
            layoutManager = LinearLayoutManager(requireContext())
           adapter=mAdapter
        }
    }
}