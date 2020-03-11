package com.pinboard

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.showProgress
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_create_pin.*
import java.text.SimpleDateFormat
import java.util.*

class CreatePinActivity : AppCompatActivity() {
	private val REQUIRED = "REQUIRED"

	private var mDatabase: DatabaseReference? = null
	private var mMessageReference: DatabaseReference? = null
	private var user: FirebaseUser? = null

	var selectedPhotoUri: Uri? = null
	var imageUrisOnDevice: MutableList<Uri> = arrayListOf()

	var imagesStorageMap: MutableList<String> = arrayListOf()//HashMap<String, String> = hashMapOf()
	var PICK_IMAGE_MULTIPLE = 1


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
		mMessageReference =
			FirebaseDatabase.getInstance().getReference(R.string.pin_folder_name.toString())
		user = FirebaseAuth.getInstance().currentUser

		upload_image_button.setOnClickListener {
			val intent = Intent(Intent.ACTION_GET_CONTENT)
			intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
			intent.addCategory(Intent.CATEGORY_OPENABLE)
			intent.type = "image/*"

			startActivityForResult(intent, PICK_IMAGE_MULTIPLE);

		}

		bindProgressButton(submit_button)
		submit_button.setOnClickListener {

			if (pin_header_edittext.text.isEmpty()) {
				pin_header_edittext.error = REQUIRED

			} else if (price_edittext_create_pin.text.isEmpty()) {
				price_edittext_create_pin.error = REQUIRED
			} else {
				submitMessage()

			}
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		imageUrisOnDevice.clear()
		if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == Activity.RESULT_OK
			&& null != data
		) {
			if (data.clipData != null) {
				for (i in 0 until data.clipData.itemCount) {
					var imageUri: Uri = data.clipData.getItemAt(i).uri
					imageUrisOnDevice.add(imageUri)
				}
			} else if (data.data != null) {
				imageUrisOnDevice.add(data.data ?: return)
			}
			val bitmap = MediaStore.Images.Media.getBitmap(
				contentResolver,
				imageUrisOnDevice[0]
			)

			Glide.with(imageView_pincard).load(bitmap)
				//.crossFade()
				.thumbnail(0.5f)
				.placeholder(R.drawable.cloud_download_outline)
				.fallback(R.drawable.alert_circle)
				.error(R.drawable.alert_circle)
				.centerCrop()
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.into(imageView_pincard)
			imageView_pincard.alpha = 1F


		}


//		if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
//			selectedPhotoUri = data.data
//			val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
//
//			Glide.with(imageView_pincard).load(bitmap)
//				//.crossFade()
//				.thumbnail(0.5f)
//				.placeholder(R.drawable.cloud_download_outline)
//				.fallback(R.drawable.alert_circle)
//				.error(R.drawable.alert_circle)
//				.centerCrop()
//				.diskCacheStrategy(DiskCacheStrategy.ALL)
//				.into(imageView_pincard)
//			imageView_pincard.alpha = 1F


	}

	private fun submitMessage() {

		mDatabase!!.child("users").child(user!!.uid)
			.addListenerForSingleValueEvent(object : ValueEventListener {
				override fun onDataChange(dataSnapshot: DataSnapshot) {
					val user = dataSnapshot.getValue(User::class.java)
					val userName = user?.userName
					if (user == null) {
						Toast.makeText(
							this@CreatePinActivity,
							"onDataChange: User data is null!",
							Toast.LENGTH_SHORT
						).show()
						return
					}


					writeNewMessage(userName, user)
				}

				override fun onCancelled(error: DatabaseError) {
					// Failed to read value
					//Log.e(TAG, "onCancelled: Failed to read user!")
				}
			})
	}

	internal fun writeNewMessage(userName: String?, user: User?) {
		val pushID = mMessageReference?.push()?.key
		if (imageUrisOnDevice.isNullOrEmpty()) return

		val ref = FirebaseStorage.getInstance().getReference("/images/$pushID/")
		var imageNumber = 0

		progressBar.visibility = View.VISIBLE

		submit_button.showProgress {
			buttonTextRes = R.string.loading
			progressColor = R.color.colorAccent
		}
		for (i in 0 until imageUrisOnDevice.size) {

			val putImage = ref.child(imageUrisOnDevice[i].lastPathSegment!!)
			progressBar.setProgress(i / imageUrisOnDevice.size * 100)
			putImage.putFile(imageUrisOnDevice[i])
				.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->

					if (!task.isSuccessful) {
						task.exception?.let {
							throw it
						}
					}
					return@Continuation putImage.downloadUrl
				})
				.addOnCompleteListener { task ->
					if (task.isSuccessful) {

						imagesStorageMap.add(task.result.toString())

						val time =
							SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().time)
						val message = Pin(
							userName,
							user,
							pushID,
							pin_header_edittext.text.toString(),
							time,
							System.currentTimeMillis(),
							description_edittext_create.text.toString(),
							price_edittext_create_pin.text.toString(),
							imagesStorageMap
						)

						FirebaseDatabase.getInstance()
							.getReference(getString(R.string.pin_folder_name).plus(pushID))
							.setValue(message)

						progressBar!!.visibility = View.INVISIBLE
						Toast.makeText(
							this,
							getString(R.string.pin_has_been_created),
							Toast.LENGTH_LONG
						).show()
						finish()
					}
				}

		}
	}
}
