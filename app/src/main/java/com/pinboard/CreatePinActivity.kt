package com.pinboard

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_create_pin.*
import java.util.*

class CreatePinActivity : AppCompatActivity() {

	private var mDatabase: DatabaseReference? = null
	private var mMessageReference: DatabaseReference? = null
	private var user: FirebaseUser? = null
	var selectedPhotoUri: Uri? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_create_pin)

		setSupportActionBar(findViewById(R.id.toolbar))


		mDatabase = FirebaseDatabase.getInstance().reference
		mMessageReference = FirebaseDatabase.getInstance().getReference("messages")
		user = FirebaseAuth.getInstance().currentUser

		//firebaseListenerInit()

		upload_image_button.setOnClickListener {
			val intent = Intent(Intent.ACTION_PICK)
			intent.type = "image/*"
			startActivityForResult(intent, 0)
		}

		button3.setOnClickListener {
			uploadImageToFirebaseStorage()
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
			selectedPhotoUri = data.data
			val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

			imageView.setImageBitmap(bitmap)
		}
	}

	private fun uploadImageToFirebaseStorage() {
		if (selectedPhotoUri == null) return

		val fileNameUUID = UUID.randomUUID().toString()
		val ref = FirebaseStorage.getInstance().getReference("/images/$fileNameUUID")
		ref.putFile(selectedPhotoUri!!)

	}
}
