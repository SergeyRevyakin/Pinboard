package com.pinboard

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_full_my_pin.*

class FullMyPinActivity() : AppCompatActivity() {

	private var user: FirebaseUser? = null
	private var mDatabase: DatabaseReference? = null
	private var mMessageReference: DatabaseReference? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_full_my_pin)

		val tb: Toolbar = findViewById(R.id.toolbar)
		setSupportActionBar(tb)
		tb.setNavigationIcon(R.drawable.arrow_left)
		tb.setNavigationOnClickListener {
			onBackPressed()
		}

		mDatabase = FirebaseDatabase.getInstance().reference
		mMessageReference = FirebaseDatabase.getInstance().getReference("messages")
		user = FirebaseAuth.getInstance().currentUser

		val pin: Pin = intent.getSerializableExtra("pin") as Pin

		Glide.with(this@FullMyPinActivity)
			.load(pin.imageURL)
			.placeholder(R.drawable.cloud_download_outline)
			.fallback(R.drawable.alert_circle)
			.error(R.drawable.alert_circle)
			.centerCrop()
			.into(findViewById(R.id.full_pin_imageview))
		header_fullpin_textview.text = pin.header
		description_fullpin_textview.text = pin.description

		delete_pin_button.setOnClickListener {
			alert(pin)
		}

	}

	private fun alert(pin: Pin) {
		val builder = AlertDialog.Builder(this)
		builder.setTitle("Warning")
		builder.setMessage("Are you sure to delete pin ${pin.header}?")
		//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

		builder.setPositiveButton(android.R.string.yes) { dialog, which ->
			deletePin(pin)
		}

		builder.setNegativeButton(android.R.string.no) { dialog, which ->

		}

		builder.show()
	}

	private fun deletePin(pin: Pin) {
		if (user?.uid.equals(pin.authorID)) {
			mMessageReference!!.child(pin.pinID.toString()).removeValue()
			FirebaseStorage.getInstance().getReference("/images/${pin.pinID}").delete()
			Toast.makeText(
				this@FullMyPinActivity,
				"Your pin ${pin.header} was deleted",
				Toast.LENGTH_SHORT
			).show()
			finish()
		} else {
			Toast.makeText(
				this@FullMyPinActivity,
				"You cant delete this PIN",
				Toast.LENGTH_SHORT
			).show()
			finish()
		}
	}
}
