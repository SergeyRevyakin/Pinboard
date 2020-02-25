package com.pinboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FullPinActivity() : AppCompatActivity() {

	private var user: FirebaseUser? = null
	private var mDatabase: DatabaseReference? = null
	private var mMessageReference: DatabaseReference? = null
	//var pin:Pin=ipin

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_full_pin)

		val tb: Toolbar = findViewById(R.id.toolbar)
		setSupportActionBar(tb)
		tb.setNavigationIcon(R.drawable.arrow_left)
		tb.setNavigationOnClickListener {
			onBackPressed()
		}

		mDatabase = FirebaseDatabase.getInstance().reference
		mMessageReference = FirebaseDatabase.getInstance().getReference("messages")
		user = FirebaseAuth.getInstance().currentUser


//
//		Glide.with(this@FullPinActivity)
//			.load(pin.imageURL)
//			.placeholder(R.drawable.cloud_download_outline)
//			.fallback(R.drawable.alert_circle)
//			.error(R.drawable.alert_circle)
//			.centerCrop()
//			.into(findViewById(R.id.full_pin_imageview))


	}

}
