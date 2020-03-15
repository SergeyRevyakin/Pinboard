package com.pinboard

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.pinboard.Adapter.ViewPagerAdapter
import com.viewpagerindicator.CirclePageIndicator
import kotlinx.android.synthetic.main.activity_full_my_pin.*
import java.util.*

class FullMyPinActivity : AppCompatActivity() {

	private val stringPIN = "pin"

	private var user: FirebaseUser? = null
	private var mDatabase: DatabaseReference? = null
	private var mMessageReference: DatabaseReference? = null
	private var currentPinRef: DatabaseReference? = null
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

		pin = intent.getSerializableExtra(stringPIN) as Pin

		setUpAdapter(pin!!)

		mDatabase = FirebaseDatabase.getInstance().reference
		mMessageReference =
			FirebaseDatabase.getInstance().getReference(getString(R.string.pin_folder_name))
		currentPinRef = FirebaseDatabase.getInstance()
			.getReference(getString(R.string.pin_folder_name).plus(pin?.pinID))
		user = FirebaseAuth.getInstance().currentUser

		header_fullpin_textview.text = pin?.header
		description_fullpin_textview.text = pin?.description
		price_fullpin_textview.text = pin?.price

		messages_pin_button.setOnClickListener {
			if (user?.uid.equals(pin?.userData?.userID)) {
				//if (currentPinRef?.equals("ChatMessages")!!) {
				val intent = Intent(this, PickChatActivity::class.java)
				startActivity(intent.putExtra(stringPIN, pin))
				//} else Toast.makeText(
				//	this,
				//	"You have no chat messages on this PIN",
				//	Toast.LENGTH_SHORT
				//).show()
			} else {
				val intent = Intent(this, ChatActivity::class.java)
				startActivity(intent.putExtra(stringPIN, pin))
			}
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
		if (user?.uid.equals(pin?.userData?.userID)) {
			currentPinRef?.removeValue()
			FirebaseStorage.getInstance().getReference("/images/${pin?.pinID}").delete()
			Toast.makeText(
				this,
				"Your pin ${pin?.header} was deleted",
				Toast.LENGTH_SHORT
			).show()
			finish()
		} else {
			Toast.makeText(
				this,
				"You cant delete this stringPIN",
				Toast.LENGTH_SHORT
			).show()
			finish()
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		if (user?.uid.equals(pin?.userData?.userID)) {
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


	private fun setUpAdapter(pin: Pin) {

		mPager = findViewById(R.id.viewpager)
		mPager!!.adapter = ViewPagerAdapter(this, pin)
		NUM_PAGES = pin.imageURL!!.size

		if (NUM_PAGES > 1) {

			val indicator = findViewById<CirclePageIndicator>(R.id.indicator)

			indicator.setViewPager(mPager)

			val density = resources.displayMetrics.density

			indicator.radius = 5 * density

			val handler = Handler()
			val Update = Runnable {
				if (currentPage == NUM_PAGES) {
					currentPage = 0
				}
				mPager!!.setCurrentItem(currentPage++, true)
			}
			val swipeTimer = Timer()
			swipeTimer.schedule(object : TimerTask() {
				override fun run() {
					handler.post(Update)
				}
			}, 5000, 5000)

			indicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {

				override fun onPageSelected(position: Int) {
					currentPage = position
				}

				override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {}

				override fun onPageScrollStateChanged(pos: Int) {}
			})
		}
	}

	companion object {

		private var mPager: ViewPager? = null
		private var currentPage = 0
		private var NUM_PAGES = 0
	}
}
