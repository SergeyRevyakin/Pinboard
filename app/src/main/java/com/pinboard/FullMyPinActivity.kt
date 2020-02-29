package com.pinboard

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

	private val stringPIN = "pin"

	private var user: FirebaseUser? = null
	private var mDatabase: DatabaseReference? = null
	private var mMessageReference: DatabaseReference? = null
	private var pin: Pin? = null

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

		pin = intent.getSerializableExtra(stringPIN) as Pin

		Glide.with(this@FullMyPinActivity)
			.load(pin?.imageURL)
			.placeholder(R.drawable.cloud_download_outline)
			.fallback(R.drawable.alert_circle)
			.error(R.drawable.alert_circle)
			.centerCrop()
			.into(findViewById(R.id.full_pin_imageview))
		header_fullpin_textview.text = pin?.header
		description_fullpin_textview.text = pin?.description

		messages_pin_button.setOnClickListener {
			val intent = Intent(this, ChatActivity::class.java)
			startActivity(intent.putExtra(stringPIN, pin))
		}

	}

	private fun deleteAlert() {
		val builder = AlertDialog.Builder(this)
		builder.setTitle("Warning")
		builder.setMessage("Are you sure to delete pin ${pin?.header}?")

		builder.setPositiveButton(android.R.string.yes) { dialog, which ->
			deletePin()
		}

		builder.setNegativeButton(android.R.string.no) { dialog, which -> }
		builder.show()
	}

	private fun deletePin() {
		if (user?.uid.equals(pin?.authorID)) {
			mMessageReference!!.child(pin?.pinID.toString()).removeValue()
			FirebaseStorage.getInstance().getReference("/images/${pin?.pinID}").delete()
			Toast.makeText(
				this@FullMyPinActivity,
				"Your pin ${pin?.header} was deleted",
				Toast.LENGTH_SHORT
			).show()
			finish()
		} else {
			Toast.makeText(
				this@FullMyPinActivity,
				"You cant delete this stringPIN",
				Toast.LENGTH_SHORT
			).show()
			finish()
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		if (user?.uid.equals(pin?.authorID)) {
			menuInflater.inflate(R.menu.menu_full_my_pin_bottom, menu)
		}
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
		R.id.delete_my_pin_button -> {
			deleteAlert()
			true
		}

		else -> {
			super.onOptionsItemSelected(item)
		}
	}
}
