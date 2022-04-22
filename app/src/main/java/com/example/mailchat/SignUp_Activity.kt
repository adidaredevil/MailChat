package com.example.mailchat

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUp_Activity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var confirmPassword: String
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_sign_up)

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait.....!")
        progressDialog.setCanceledOnTouchOutside(false)
    }

    fun signInOnclick(view: View) {
        val intent = Intent(this, SignIn_Activity::class.java)
        startActivity(intent)
    }

    fun signUptoMailchat(view: View) {
        email = edtEmailSignUp.text.toString()
        password = edtPasswordSignUp.text.toString()
        confirmPassword = edtConfirmPasswordSignUp.text.toString()
        if (email.isEmpty()) {
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show()
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show()
        } else if (confirmPassword.isEmpty()) {
            Toast.makeText(this, "Confirm password cannot be empty", Toast.LENGTH_SHORT).show()
        } else if (!confirmPassword.equals(password)) {
            Toast.makeText(this, "Confirm password should be same as password", Toast.LENGTH_SHORT)
                .show()
        } else if (password.length < 6) {
            Toast.makeText(this, "Password should be of atleast 6 characters", Toast.LENGTH_SHORT)
                .show()
        } else if (!isValidEmail(email)) {
            Toast.makeText(this, "Please check your email address", Toast.LENGTH_SHORT).show()
        } else {
            //check if this email exists
            firebaseAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener { task ->
                    val isNewUser = task.result.signInMethods?.isEmpty()
                    if (isNewUser == true) {
                        Log.e("TAG", "Is New User!")
                        emailConfirmationDialog()
                    } else {
                        Log.e("TAG", "Is Old User!")
                        Toast.makeText(
                            this,
                            "Account with this email id already exist",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

        }
    }

    private fun emailConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setTitle("Verify your email!")
            .setMessage("Are you sure you want to continue with $email")
            .setPositiveButton("Send") { d, e ->
                sendEmailVerification()
            }
            .setNegativeButton("CANCEL") { d, e ->
                d.dismiss()
            }
            .show()
    }

    private fun sendEmailVerification() {
        //show progress bar
        progressDialog.setMessage("Sending email verification link to $email")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "createUserWithEmail:success")
                    firebaseUser = firebaseAuth.currentUser!!
                    //send verification link
                    firebaseUser!!.sendEmailVerification()
                        .addOnSuccessListener {
                            progressDialog.dismiss()
                            verificationConfirmationDialog()
                        }
                        .addOnFailureListener { e ->
                            progressDialog.dismiss()
                            Log.e("TAG", "Sending email verification link failed ${e.message}")
                            Toast.makeText(this, "Failed to send due to ${e.message}", Toast.LENGTH_SHORT)
                                .show()
                        }
                } else {
                    // If sign in fails, display a message to the user.
                    progressDialog.dismiss()
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }

    private fun verificationConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setTitle("Verify your email!")
            .setMessage("Please verify your email by using the verification link sent to $email and click continue")
            .setPositiveButton("Continue") { d, e ->
                firebaseUser= FirebaseAuth.getInstance().currentUser!!
                firebaseUser.reload()
                if(firebaseUser.isEmailVerified){
                    val intent = Intent(this,NameImage_Activity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    alreadyVerified()
                }

            }
            .setNegativeButton("CANCEL") { d, e ->
                d.dismiss()
            }
            .show()
    }

    private fun alreadyVerified() {
        firebaseUser.reload()
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setTitle("Verify your email!")
            .setMessage("Please verify your email by using the verification link sent to $email and click continue")
            .setPositiveButton("Already Verified?") { d, e ->
                firebaseUser= FirebaseAuth.getInstance().currentUser!!
                firebaseUser.reload()
                if(firebaseUser.isEmailVerified){
                    val intent = Intent(this,NameImage_Activity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    alreadyVerified()
                }

            }
            .setNegativeButton("CANCEL") { d, e ->
                d.dismiss()
            }
            .show()
    }


    fun isValidEmail(target: CharSequence?): Boolean {
        return if (target == null) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }
}