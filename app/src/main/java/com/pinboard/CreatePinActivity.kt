package com.pinboard

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.pinboard.Dialog.WarningDialog
import kotlinx.android.synthetic.main.activity_create_pin.*
import java.text.SimpleDateFormat
import java.util.*

class CreatePinActivity : AppCompatActivity() {

	private var mDatabase: DatabaseReference? = null
	private var mMessageReference: DatabaseReference? = null
	private var user: FirebaseUser? = null

	var selectedPhotoUri: Uri? = null

	private val pinNameUUID = UUID.randomUUID().toString()


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_create_pin)

		val tb: Toolbar = findViewById(R.id.toolbar)
		setSupportActionBar(tb)
		tb.setNavigationIcon(R.drawable.arrow_left)
		tb.setNavigationOnClickListener {
			onBackPressed()
		}


		mDatabase = FirebaseDatabase.getInstance().reference
		mMessageReference = FirebaseDatabase.getInstance().getReference("messages")
		user = FirebaseAuth.getInstance().currentUser

		upload_image_button.setOnClickListener {
			val intent = Intent(Intent.ACTION_PICK)
			intent.type = "image/*"
			startActivityForResult(intent, 0)
		}

		button3.setOnClickListener {
			//uploadImageToFirebaseStorage()
			if (pin_header_edittext.text.isEmpty()) {
				pin_header_edittext.error = "REQUIRED"
				val warningDialog: WarningDialog? = null
				val manager: FragmentManager = supportFragmentManager;

				val transaction: FragmentTransaction = manager.beginTransaction()
				warningDialog?.show(transaction, "dialog")

			} else if (price_edittext_create_pin.text.isEmpty()) {
				price_edittext_create_pin.error = "REQUIRED"
			} else {
				submitMessage()
				finish()
			}
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
			selectedPhotoUri = data.data
			val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

			Glide.with(imageView_pincard).load(bitmap)
				//.crossFade()
				.thumbnail(0.5f)
				.placeholder(R.drawable.cloud_download_outline)
				.fallback(R.drawable.alert_circle)
				.error(R.drawable.alert_circle)
				.centerInside()
				//.centerCrop()
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.into(imageView_pincard)
			imageView_pincard.alpha = 1F
		}

	}

	private fun uploadImageToFirebaseStorage() {
		if (selectedPhotoUri == null) return

		//val pinNameUUID = UUID.randomUUID().toString()
		val ref = FirebaseStorage.getInstance().getReference("/images/$pinNameUUID")
		ref.putFile(selectedPhotoUri!!).addOnSuccessListener {
			ref.downloadUrl.addOnSuccessListener {
				//imageLinkInStorage=it.toString()
			}
		}


	}

	private fun submitMessage() {

		mDatabase!!.child("users").child(user!!.uid)
			.addListenerForSingleValueEvent(object : ValueEventListener {
				override fun onDataChange(dataSnapshot: DataSnapshot) {
					val user = dataSnapshot.getValue(User::class.java)
					val userName = user?.userName
					val userID = user?.userID
					if (user == null) {
						Toast.makeText(
							this@CreatePinActivity,
							"onDataChange: User data is null!",
							Toast.LENGTH_SHORT
						).show()
						return
					}


					writeNewMessage(userName, userID)
				}

				override fun onCancelled(error: DatabaseError) {
					// Failed to read value
					//Log.e(TAG, "onCancelled: Failed to read user!")
				}
			})
	}


	private fun writeNewMessage(userName: String?, userID: String?) {

		var imageLinkInStorage: String?
		if (selectedPhotoUri == null) return

		val ref = FirebaseStorage.getInstance().getReference("/images/$pinNameUUID")
		ref.putFile(selectedPhotoUri!!).addOnSuccessListener {
			ref.downloadUrl.addOnSuccessListener {
				imageLinkInStorage = it?.toString()

				val time =
					SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().time)
				val message = Pin(
					userName,
					userID,
					pin_header_edittext.text.toString(),
					time,
					description_edittext_create.text.toString(),
					price_edittext_create_pin.text.toString(),
					imageLinkInStorage
				)

				val messageValues = message.toMap()
				val childUpdates = HashMap<String, Any>()

				//val key = mDatabase!!.child("messages").push().pinNameUUID

				childUpdates.put("/messages/" + pinNameUUID, messageValues)
				childUpdates.put("/user-messages/" + user!!.uid + "/" + pinNameUUID, messageValues)

				mDatabase!!.updateChildren(childUpdates)
			}
		}
			.addOnFailureListener {
				return@addOnFailureListener
			}


	}
}
