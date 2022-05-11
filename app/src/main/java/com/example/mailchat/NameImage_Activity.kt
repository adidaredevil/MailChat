package com.example.mailchat

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mailchat.Modals.User
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_nameimage.*


class NameImage_Activity : AppCompatActivity() {
    val firebaseStorage by lazy{
        FirebaseStorage.getInstance()
    }
    val firebaseAuth by lazy{
        FirebaseAuth.getInstance()
    }
    val firebaseDatabase by lazy{
        FirebaseFirestore.getInstance()
    }
    lateinit var downloadUrl:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nameimage)
        progressBar.visibility= View.GONE
    }

    fun profilePictureOnClick(view: View) {
        checkPermissionImage()
    }


        private fun checkPermissionImage() {
            if ((checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            ) {
                val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                val permissionWrite = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

                requestPermissions(
                    permission,
                    1001
                ) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_READ LIKE 1001
                requestPermissions(
                    permissionWrite,
                    1002
                ) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_WRITE LIKE 1002
            } else {
                pickImageFromGallery()
            }
        }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        startActivityForResult(
            intent,
            1000
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK && requestCode==1000){

            data?.data?.let{
                imgPersonProfilePicture.setImageURI(it)
                uploadImage(it)
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun uploadImage(it: Uri) {
        btnNext.isEnabled=false
        btnNext.isClickable=false
        progressBar.visibility= View.VISIBLE
        val ref= firebaseStorage.reference.child("uploads/"+ firebaseAuth.uid.toString())
        val uploadTask:UploadTask=ref.putFile(it)
        uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task <Uri>>{task ->
            if(!task.isSuccessful){
                task.exception?.let{it
                    throw it
                }
            }
            return@Continuation ref.downloadUrl
        }).addOnCompleteListener{task ->
            btnNext.isEnabled=true
            btnNext.isClickable=true
            progressBar.visibility= View.GONE
            if(task.isSuccessful){
                downloadUrl=task.result.toString()
                Log.i("TAG", "download url = $downloadUrl")
            }else{
                Log.d("TAG","downloadurl failed")
            }
        }.addOnFailureListener{it
            Log.d("TAG",it.toString())

        }
    }

    @SuppressLint("ResourceAsColor")
    fun btnNextProfile(view: View) {

        val name= edtPersonName.text.toString()
        if(name.isEmpty()){
            Toast.makeText(this,"Name cannot be empty",Toast.LENGTH_SHORT).show()
        }else if(!::downloadUrl.isInitialized){
            Toast.makeText(this,"Please upload a profile image",Toast.LENGTH_SHORT).show()
        }else{
            btnNext.isEnabled=false
            btnNext.isClickable=false
            val user = User(name,downloadUrl,downloadUrl,firebaseAuth.uid!!);
            firebaseDatabase.collection("users").document(firebaseAuth.uid!!).set(user).addOnSuccessListener {
                val intent= Intent(this, Chat_Activity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener{
                btnNext.isEnabled=true
                btnNext.isClickable=true
            }
        }
    }
}