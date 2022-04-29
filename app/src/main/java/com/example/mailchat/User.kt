package com.example.mailchat

import com.google.firebase.firestore.FieldValue

data class User(
    val name:String,
    val imageUrl:String,
    val thumbUrl:String,
    val deviceToken:String,
    val status:String,
    val onlineStatus: String,
    val uid:String
) {

    /** Empty Constructor for firebase */
    constructor() :this("" , "","", "","","","")
    constructor(name: String,imageUrl: String,thumbUrl: String,uid: String) :this(name , imageUrl,thumbUrl, "","Hey there! I am using MailChat",FieldValue.serverTimestamp().toString(),uid)

}