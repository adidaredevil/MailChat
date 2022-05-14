package com.example.mailchat.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mailchat.Modals.User
import com.example.mailchat.R
import com.example.mailchat.UserViewHolder
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.fragment_people.*
import java.lang.Exception

private const val NORMAL_VIEW_TYPE=1
private const val DELETED_VIEW_TYPE=2

class PeopleFragment : Fragment() {


    lateinit var mAdapter: FirestorePagingAdapter<User,RecyclerView.ViewHolder>
    val auth by lazy{
        FirebaseAuth.getInstance()
    }
    val database by lazy{
        FirebaseFirestore.getInstance().collection("users")
            .orderBy("name", Query.Direction.ASCENDING)
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
        mAdapter= object : FirestorePagingAdapter<User,RecyclerView.ViewHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                return when(viewType){
                    NORMAL_VIEW_TYPE -> UserViewHolder(layoutInflater.inflate(R.layout.list_item,parent,false))
                    else -> EmptyViewHolder(layoutInflater.inflate(R.layout.empty_view,parent,false))
                }

            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, model: User) {
                if(holder is UserViewHolder){
                    holder.bind(user = model)
                }else{
                    // do nothing because it will be empty
                }
            }

            override fun onError(e: Exception) {
                super.onError(e)
            }

            override fun onLoadingStateChanged(state: LoadingState) {
                super.onLoadingStateChanged(state)
            }

            override fun getItemViewType(position: Int): Int {
                val item = getItem(position)?.toObject(User::class.java)
                return if(auth.uid==item!!.uid){
                    DELETED_VIEW_TYPE
                }else{
                    NORMAL_VIEW_TYPE
                }
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