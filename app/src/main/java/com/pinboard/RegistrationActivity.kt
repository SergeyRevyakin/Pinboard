package com.pinboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity() {
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

	}

	//var selectedPhotoUri: Uri? = null

//	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//		super.onActivityResult(requestCode, resultCode, data)
//
//		if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
//			// proceed and check what the selected image was....
//			Log.d(TAG, "Photo was selected")
//
//		}
//	}

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
			?.addOnFailureListener {
				Log.d(TAG, "Failed to create user: ${it.message}")
				Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT)
					.show()
			}
	}

	private fun saveUserToFirebaseDatabase() {
		val uid = FirebaseAuth.getInstance().uid ?: ""
		val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

		val user = User(
			uid,
			username_register_edittext.text.toString(),
			email_edittext_registration.text.toString()
		)

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

}