package com.pinboard


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.ChangeEventListener
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.pinboard.Adapter.RecyclerItemAdapter

class PinBrowsingActivity : AppCompatActivity() {
	private val TAG = "PinBrowsingActivity"
	private val REQUIRED = "Required"

	private var user: FirebaseUser? = null

	private var mDatabase: DatabaseReference? = null
	private var mMessageReference: DatabaseReference? = null
	private var mMessageListener: ChildEventListener? = null

	private var mAdapter: FirebaseRecyclerAdapter<Pin, PinViewHolder>? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_pin_browsing)

		setSupportActionBar(findViewById(R.id.toolbar))

		mDatabase = FirebaseDatabase.getInstance().reference
		mMessageReference = FirebaseDatabase.getInstance().getReference("messages")
		user = FirebaseAuth.getInstance().currentUser

		firebaseListenerInit()

//		btnBack.setOnClickListener {
//			FirebaseAuth.getInstance().signOut()
//			val intent = Intent(this, LoginActivity::class.java)
//			intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
//			startActivity(intent)
//		}

		val recyclerView: RecyclerView = findViewById(R.id.pin_browsing_recycleviewer)
//
//        val layoutManager = LinearLayoutManager(this)
//        layoutManager.reverseLayout = false
		//recyclerView.setHasFixedSize(true)
		//recyclerView.layoutManager = layoutManager
		recyclerView.layoutManager = LinearLayoutManager(this)


		val query = mMessageReference

		mAdapter = object : FirebaseRecyclerAdapter<Pin, PinViewHolder>(
			Pin::class.java, R.layout.pin_cardview, PinViewHolder::class.java, query
		) {

			override fun populateViewHolder(
				viewHolder: PinViewHolder?,
				model: Pin?,
				position: Int
			) {
				viewHolder?.bindMessage(model)
			}

			override fun onChildChanged(
				type: ChangeEventListener.EventType,
				snapshot: DataSnapshot?,
				index: Int,
				oldIndex: Int
			) {
				super.onChildChanged(type, snapshot, index, oldIndex)

				recyclerView.scrollToPosition(index)


			}
		}

		recyclerView.adapter = mAdapter

		recyclerView.setOnItemClickListener {

			val clickedPin: Pin =
				(mAdapter as FirebaseRecyclerAdapter<Pin, PinViewHolder>).getItem(it)

//			Toast.makeText(
//				this@PinBrowsingActivity,
//				"TEST " + clickedPin.header,
//				Toast.LENGTH_SHORT
//			).show()
			if (clickedPin.authorID.equals(FirebaseAuth.getInstance().uid)) {
				Toast.makeText(
					this@PinBrowsingActivity,
					"THATS MY PIN",
					Toast.LENGTH_SHORT
				).show()
				val intent = Intent(this@PinBrowsingActivity, FullMyPinActivity::class.java)
				//intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)

				startActivity(intent.putExtra("pin", clickedPin))
			} else {
				Toast.makeText(
					this@PinBrowsingActivity,
					"WRONG",
					Toast.LENGTH_SHORT
				).show()
			}
//			val intent = Intent(this@PinBrowsingActivity, FullMyPinActivity::class.java)
//			//intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
//
//			startActivity(intent.putExtra("pin", clickedPin))

		}
	}

	inline fun RecyclerView.setOnItemClickListener(crossinline listener: (position: Int) -> Unit) {
		addOnItemTouchListener(
			RecyclerItemAdapter(
				this,
				object : RecyclerItemAdapter.OnItemClickListener {
					override fun onItemClick(view: View, position: Int) {
						listener(position)
					}
				})
		)
	}

	override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
		R.id.add_new_pin -> {
			val intent = Intent(this@PinBrowsingActivity, CreatePinActivity::class.java)
			//intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)

			startActivity(intent)
			true
		}

		R.id.search -> {
			//Toast.makeText(this@PinBrowsingActivity, "BUTTON WORKS!", Toast.LENGTH_LONG)
			val intent = Intent(this@PinBrowsingActivity, CreatePinActivity::class.java)
			//intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
			startActivity(intent)
			true
		}

		R.id.logout_menu_button -> {
			val intent = Intent(this@PinBrowsingActivity, LoginActivity::class.java)
			intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
			startActivity(intent)
			true
		}

		else -> {
			// If we got here, the user's action was not recognized.
			// Invoke the superclass to handle it.
			super.onOptionsItemSelected(item)
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		//Inflate the menu; this adds items to the action bar if it is present.
		menuInflater.inflate(R.menu.menu_add_pin_search, menu)
		return true
	}

	private fun firebaseListenerInit() {

		val childEventListener = object : ChildEventListener {

			override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
				val message = dataSnapshot.getValue(Pin::class.java)
			}

			override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {

				val message = dataSnapshot.getValue(Pin::class.java)
				Toast.makeText(
					this@PinBrowsingActivity,
					"onChildChanged: " + message!!.header,
					Toast.LENGTH_SHORT
				).show()
			}

			override fun onChildRemoved(dataSnapshot: DataSnapshot) {
				//Log.e(TAG, "onChildRemoved:" + dataSnapshot.key)

				// A message has been removed
				val message = dataSnapshot.getValue(Pin::class.java)
				Toast.makeText(
					this@PinBrowsingActivity,
					"onChildRemoved: " + message!!.header,
					Toast.LENGTH_SHORT
				).show()
			}

			override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
				Log.e(TAG, "onChildMoved:" + dataSnapshot.key)

				// A message has changed position
				val message = dataSnapshot.getValue(Pin::class.java)
				Toast.makeText(
					this@PinBrowsingActivity,
					"onChildMoved: " + message!!.header,
					Toast.LENGTH_SHORT
				).show()
			}

			override fun onCancelled(databaseError: DatabaseError) {
				Log.e(TAG, "postMessages:onCancelled", databaseError.toException())
				Toast.makeText(
					this@PinBrowsingActivity,
					"Failed to load your Account. \n Please log in or register",
					Toast.LENGTH_SHORT
				)
					.show()
				finish()
				loginScreen()
			}

		}

		mMessageReference!!.addChildEventListener(childEventListener)

		mMessageListener = childEventListener
	}

	fun loginScreen() {
		FirebaseAuth.getInstance().signOut()
		val intent = Intent(this, LoginActivity::class.java)
		intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
		startActivity(intent)
	}

	override fun onStop() {
		super.onStop()

		if (mMessageListener != null) {
			mMessageReference!!.removeEventListener(mMessageListener!!)
		}
	}

	override fun onDestroy() {
		super.onDestroy()

		mAdapter!!.cleanup()
	}

}

