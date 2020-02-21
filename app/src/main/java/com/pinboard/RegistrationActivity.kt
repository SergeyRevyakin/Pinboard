package com.pinboard

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.*

class RegistrationActivity: AppCompatActivity()  {
    companion object {
        val TAG = "RegisterActivity"

    }

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        registration_button.setOnClickListener {
            performRegister()
        }

        sign_in_button.setOnClickListener {
            Log.d(TAG, "Try to show login activity")

            // launch the login activity somehow
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        mAuth = FirebaseAuth.getInstance()

//        selectphoto_button_register.setOnClickListener {
//            Log.d(TAG, "Try to show photo selector")
//
//            val intent = Intent(Intent.ACTION_PICK)
//            intent.type = "image/*"
//            startActivityForResult(intent, 0)
//        }
    }

   //var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // proceed and check what the selected image was....
            Log.d(TAG, "Photo was selected")


//      val bitmapDrawable = BitmapDrawable(bitmap)
//      selectphoto_button_register.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun performRegister() {
        val email = email_edittext_registration.text.toString()
        val password = password_edittext_registration.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter text in email/pw", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d(TAG, "Attempting to create user with email: $email")

        // Firebase Authentication to create a user with email and password
        mAuth?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                // else if successful
                Log.d(TAG, "Successfully created user with uid: ${it.result?.user?.uid}")
                saveUserToFirebaseDatabase()

            }
            ?.addOnFailureListener{
                Log.d(TAG, "Failed to create user: ${it.message}")
                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageToFirebaseStorage() {
        //if (selectedPhotoUri == null) return

        //val filename = UUID.randomUUID().toString()
        //val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

//        ref.putFile(selectedPhotoUri!!)
//            .addOnSuccessListener {
//                Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")
//
//                ref.downloadUrl.addOnSuccessListener {
//                    Log.d(TAG, "File Location: $it")

                    saveUserToFirebaseDatabase()
//                }
//            }
//            .addOnFailureListener {
//                Log.d(TAG, "Failed to upload image to storage: ${it.message}")
//            }
    }

    private fun saveUserToFirebaseDatabase() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, username_register_edittext.text.toString(), email_edittext_registration.text.toString())

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d(TAG, "Finally we saved the user to Firebase Database")

                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to set value to database: ${it.message}")
            }
    }

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
//
//                    writeNewUser(user!!.uid, getUsernameFromEmail(user.email), user.email)
//                } else {
//                    Log.e(TAG, "createAccount: Fail!", task.exception)
//                    Toast.makeText(applicationContext, "Authentication failed!", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }
//
//    private fun writeNewUser(userId: String, username: String?, email: String?) {
//        val user = User(username, email)
//
//        FirebaseDatabase.getInstance().reference.child("users").child(userId).setValue(user)
//    }
}