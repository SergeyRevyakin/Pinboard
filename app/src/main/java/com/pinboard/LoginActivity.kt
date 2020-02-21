package com.pinboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*



class LoginActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        //requestWindowFeature(Window.FEATURE_ NO_TITLE)

        login_button.setOnClickListener {
            performLogin()
        }

        to_registration_textview.setOnClickListener{
            val intent = Intent(this, RegistrationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun performLogin() {
        val email = email_edittext_login.text.toString()
        val password = password_edittext_login.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out email/pw.", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                Log.e(
                    "Login",
                    "Successfully logged in: ${it.result?.user?.uid}"
                )

                val intent = Intent(this, MessageActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to log in: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

}

//class LoginActivity : AppCompatActivity(), View.OnClickListener {
//
//    private val TAG = "LoginActivity"
//
//    private var mAth: FuirebaseAuth? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.login_activity)
//
//        btn_email_sign_in.setOnClickListener(this)
//        btn_email_create_account.setOnClickListener(this)
//        btn_sign_out.setOnClickListener(this)
//        btn_test_message.setOnClickListener(this)
//
//        mAuth = FirebaseAuth.getInstance()
//    }
//
//    override fun onStart() {
//        super.onStart()
//
//        val currentUser = mAuth!!.currentUser
//        updateUI(currentUser)
//    }
//
//    override fun onClick(view: View?) {
//        val i = (view ?: return).id
//
//        when (i) {
//            R.id.btn_email_create_account -> createAccount(
//                edtEmail.text.toString(),
//                edtPassword.text.toString()
//            )
//            R.id.btn_email_sign_in -> signIn(edtEmail.text.toString(), edtPassword.text.toString())
//            R.id.btn_sign_out -> signOut()
//            R.id.btn_test_message -> testMessage()
//        }
//    }
//
//    private fun createAccount(email: String, password: String) {
//        Log.e(TAG, "createAccount:" + email)
//        if (!validateForm(email, password)) {
//            return
//        }
//
//        mAuth!!.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    Log.e(TAG, "createAccount: Success!")
//
//                    // update UI with the signed-in user's information
//                    val user = mAuth!!.currentUser
//                    updateUI(user)
//                    writeNewUser(user!!.uid, getUsernameFromEmail(user.email), user.email)
//                } else {
//                    Log.e(TAG, "createAccount: Fail!", task.exception)
//                    Toast.makeText(applicationContext, "Authentication failed!", Toast.LENGTH_SHORT)
//                        .show()
//                    updateUI(null)
//                }
//            }
//    }
//
//    private fun signIn(email: String, password: String) {
//        Log.e(TAG, "signIn:" + email)
//        if (!validateForm(email, password)) {
//            return
//        }
//
//        mAuth!!.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    Log.e(TAG, "signIn: Success!")
//
//                    // update UI with the signed-in user's information
//                    val user = mAuth!!.currentUser
//                    updateUI(user)
//                } else {
//                    Log.e(TAG, "signIn: Fail!", task.exception)
//                    Toast.makeText(applicationContext, "Authentication failed!", Toast.LENGTH_SHORT)
//                        .show()
//                    updateUI(null)
//                }
//
//                if (!task.isSuccessful) {
//                    tvStatus.text = "Authentication failed!"
//                }
//            }
//    }
//
//    private fun signOut() {
//        mAuth!!.signOut()
//        updateUI(null)
//    }
//
//    private fun validateForm(email: String, password: String): Boolean {
//
//        if (TextUtils.isEmpty(email)) {
//            Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT).show()
//            return false
//        }
//
//        if (TextUtils.isEmpty(password)) {
//            Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
//            return false
//        }
//
//        if (password.length < 6) {
//            Toast.makeText(
//                applicationContext, "Password too short, enter minimum 6 characters!",
//                Toast.LENGTH_SHORT
//            ).show()
//            return false
//        }
//
//        return true
//    }
//
//    private fun updateUI(user: FirebaseUser?) {
//
//        if (user != null) {
//            tvStatus.text = "User Email: " + user.email
//            tvDetail.text = "Firebase User ID: " + user.uid
//
//            email_password_buttons.visibility = View.GONE
//            email_password_fields.visibility = View.GONE
//            layout_signed_in_buttons.visibility = View.VISIBLE
//        } else {
//            tvStatus.text = "Signed Out"
//            tvDetail.text = null
//
//            email_password_buttons.visibility = View.VISIBLE
//            email_password_fields.visibility = View.VISIBLE
//            layout_signed_in_buttons.visibility = View.GONE
//        }
//    }
//
//    private fun writeNewUser(userId: String, username: String?, email: String?) {
//        val user = User(username, email)
//
//        FirebaseDatabase.getInstance().reference.child("users").child(userId).setValue(user)
//    }
//
//    private fun getUsernameFromEmail(email: String?): String {
//        return if (email!!.contains("@")) {
//            email.split("@".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
//        } else {
//            email
//        }
//    }
//
//    private fun testMessage() {
//        startActivity(Intent(this, MessageActivity::class.java))
//    }
//}
//


