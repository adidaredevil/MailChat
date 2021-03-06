package com.example.mailchat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mailchat.isSameDayAs
import com.example.mailchat.Adapters.ChatAdapter
import com.example.mailchat.Modals.*
import com.example.mailchat.Utils.KeyboardVisibilityUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.vanniktech.emoji.*
import com.vanniktech.emoji.google.GoogleEmojiProvider
import kotlinx.android.synthetic.main.activity_chating.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Chating_Activity : AppCompatActivity() {

    private val friendId: String by lazy { intent.getStringExtra("id").toString() }
    private val name: String by lazy { intent.getStringExtra("name").toString() }
    private val image: String by lazy { intent.getStringExtra("photo").toString() }
    private val currUID: String by lazy { FirebaseAuth.getInstance().currentUser?.uid.toString() }
    private val db:FirebaseDatabase by lazy { FirebaseDatabase.getInstance("https://mailchat-app-default-rtdb.asia-southeast1.firebasedatabase.app") }
    lateinit var currentUser:User
    private val messages = mutableListOf<ChatEvent>()
    lateinit var  chatAdapter:ChatAdapter
    private lateinit var keyboardVisibilityHelper: KeyboardVisibilityUtil
    private val emojiPopup:EmojiPopup by lazy {EmojiPopup.Builder.fromRootView(rootView).setOnEmojiPopupDismissListener{smileBtn.setBackgroundResource(R.drawable.ic_chat_smile)}.build(msgEdtv)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EmojiManager.install(GoogleEmojiProvider())
        setContentView(R.layout.activity_chating)


        keyboardVisibilityHelper = KeyboardVisibilityUtil(rootView) {
            msgRv.scrollToPosition(messages.size - 1)
        }

        val window: Window = this.getWindow()
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.dark))
        supportActionBar?.show()


        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        swipeToLoad.setOnRefreshListener {
            val workerScope = CoroutineScope(Dispatchers.Main)
            workerScope.launch {
                delay(2000)
                swipeToLoad.isRefreshing = false
            }
        }

        smileBtn.setBackgroundResource(R.drawable.ic_chat_smile);
        val intent: Intent by lazy { getIntent() }
        FirebaseFirestore.getInstance().collection("users").document(currUID).get().addOnSuccessListener {
            currentUser=it.toObject(User::class.java)!!
        }

        chatAdapter = ChatAdapter(messages,currUID)
        msgRv.apply {
            layoutManager= LinearLayoutManager(this@Chating_Activity)
            adapter= chatAdapter
        }
        nameTv.text=name
        Picasso.get().load(image).into(userImgView)
        listenToMessages()
        if(intent.getStringExtra("source").equals("inbox"))
        updateReadCount()
    }
    private fun updateReadCount() {
        Log.d("mazza","$friendId+$currUID")
        getInbox(currUID!!,friendId!!).child("count").setValue(0)
    }
    private fun getId(friendId:String):String{ // ID for the messages
        return if(friendId>currUID){
            currUID+friendId
        }else{
            friendId+currUID
        }
    }

    private fun getInbox(toUser:String, fromUser:String) =
        db.reference.child("chats/$toUser/$fromUser")


    private fun getMessages(friendId:String) =
        db.reference.child("messages/${getId(friendId)}")

    fun sendMessageOnClick(view: View) {
        msgEdtv.text?.let{
            if(it.isNotEmpty()){
                sendMessage(it.toString())
                it.clear()
            }
        }
    }

    private fun sendMessage(msg: String) {
        val id =getMessages(friendId).push().key    //we make a new key for every new message
        checkNotNull(id){ "Cannot be null" }
        val msgMap= Message(msg,currUID,id)
        getMessages(friendId).child(id).setValue(msgMap).addOnSuccessListener {
        }
        updateLastMessage(msgMap)
    }

    private fun updateLastMessage(message: Message) {
        val inboxMap = Inbox(
            message.msg,
            currUID,
            name,
            image,
            friendId,
            count=0,
        )
        getInbox(currUID,friendId).setValue(inboxMap).addOnSuccessListener {
            getInbox(friendId,currUID).addListenerForSingleValueEvent(object:ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                    val value=snapshot.getValue(Inbox::class.java)
                    inboxMap.apply {
                        name = currentUser.name
                        image=currentUser.imageUrl
                        count=1
                        id = currUID
                    }
                    value?.let{
                        if(it.from == message.senderId){
                            inboxMap.count=value.count+1
                        }
                    }
                    getInbox(friendId,currUID).setValue(inboxMap)
                }

                override fun onCancelled(error: DatabaseError) {
                    // do nothing

                }
            })
        }
    }
    private fun markAsRead(){
        getInbox(friendId,currUID).child("count").setValue(0)
    }
    private fun listenToMessages(){
        getMessages(friendId)
            .orderByKey()
            .addChildEventListener(object:ChildEventListener{
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val msg = snapshot.getValue(Message::class.java)!!
                    addMessage(msg)
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun addMessage(msg: Message) {
        val eventBefore = messages.lastOrNull()
        if((eventBefore != null && !eventBefore.sentAt.isSameDayAs(msg.sentAt)) || eventBefore==null){
            messages.add(
                DateHeader(
                     msg.sentAt,
                    context = this
                    )
            )
       }
        messages.add(msg)
        chatAdapter.notifyItemInserted(messages.size-1)
        msgRv.scrollToPosition(messages.size-1)
    }

    fun emojiOnClick(view: View) {
        if(emojiPopup.isShowing()){
            emojiPopup.dismiss()
            smileBtn.setBackgroundResource(R.drawable.ic_chat_smile);
        }
        else {
            emojiPopup.toggle()
            smileBtn.setBackgroundResource(R.drawable.ic_keyboard);

        }
    }

    fun edtOnClick(view: View) {
        if(emojiPopup.isShowing()) {
            emojiPopup.dismiss()
            smileBtn.setBackgroundResource(R.drawable.ic_chat_smile);
        }
    }



}