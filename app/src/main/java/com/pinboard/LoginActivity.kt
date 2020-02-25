package com.pinboard

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContentView(R.layout.activity_login)
		//requestWindowFeature(Window.FEATURE_ NO_TITLE)

		login_button.setOnClickListener {
			performLogin()
		}

		to_registration_textview.setOnClickListener {
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

				val intent = Intent(this, PinBrowsingActivity::class.java)
				intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
				startActivity(intent)
			}
			.addOnFailureListener {
				Toast.makeText(this, "Failed to log in: ${it.message}", Toast.LENGTH_SHORT).show()
			}
	}

}



