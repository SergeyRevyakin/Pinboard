package com.pinboard


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.ChangeEventListener
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class PinBrowsingActivity : AppCompatActivity() {
	private val TAG = "PinBrowsingActivity"
	private val REQUIRED = "Required"

	private var user: FirebaseUser? = null

	private var mDatabase: DatabaseReference? = null
	private var mMessageReference: DatabaseReference? = null
	private var mMessageListener: ChildEventListener? = null

	private var mAdapter: FirebaseRecyclerAdapter<Pin, MessageViewHolder>? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.message_activity)

		setSupportActionBar(findViewById(R.id.toolbar))

		mDatabase = FirebaseDatabase.getInstance().reference
		mMessageReference = FirebaseDatabase.getInstance().getReference("messages")
		user = FirebaseAuth.getInstance().currentUser

		firebaseListenerInit()

//		btnSend.setOnClickListener {
//			submitMessage()
//			edtSentText.setText("")
//		}
//
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
	}

	override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
		R.id.add_new_pin -> {
			val intent = Intent(this@PinBrowsingActivity, CreatePinActivity::class.java)
			//intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
			startActivity(intent)
			true
		}

//        R.id.action_favorite -> {
//            // User chose the "Favorite" action, mark the current item
//            // as a favorite...
//            true
//        }

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
				//Log.e(TAG, "onChildAdded:" + message!!.header)
			}

			override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
				//Log.e(TAG, "onChildChanged:" + dataSnapshot.key)

				// A message has changed
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
				Toast.makeText(this@PinBrowsingActivity, "Failed to load Pin.", Toast.LENGTH_SHORT)
					.show()
			}

		}

		mMessageReference!!.addChildEventListener(childEventListener)

		// copy for removing at onStop()
		mMessageListener = childEventListener
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


//	private val TAG = "PinBrowsingActivity"
//	private val REQUIRED = "Required"
//
//	private var user: FirebaseUser? = null
//
//	private var mDatabase: DatabaseReference? = null
//	private var mMessageReference: DatabaseReference? = null
//	private var mMessageListener: ChildEventListener? = null
//
//	private var mAdapter: FirebaseRecyclerAdapter<Pin, MessageViewHolder>? = null
//
//	companion object {
//		var currentUser: User? = null
//
//		var currentPin: Pin? = null
//
//		val TAG = "LatestMessages"
//	}
//
//	override fun onCreate(savedInstanceState: Bundle?) {
//		super.onCreate(savedInstanceState)
//		setContentView(R.layout.activity_pin_browsing)
//
////		pin_browsing_recycleviewer.adapter = adapter
////		pin_browsing_recycleviewer.addItemDecoration(
////			DividerItemDecoration(
////				this,
////				DividerItemDecoration.VERTICAL
////			)
////		)
//
//		// set item click listener on your adapter
////		adapter.setOnItemClickListener { item, view ->
////			Log.d(TAG, "123")
////			val intent = Intent(this, ChatLogActivity::class.java)
////
////			// we are missing the chat partner user
////
////			val row = item as LatestMessageRow
////			intent.putExtra(NewMessageActivity.USER_KEY, row.chatPartnerUser)
////			startActivity(intent)
////		}
//
////    setupDummyRows()
//		listenForLatestMessages()
//
//		fetchCurrentUser()
//
//		verifyUserIsLoggedIn()
//	}
//
//	val latestMessagesMap = HashMap<String, Pin>()
//
//	private fun refreshRecyclerViewMessages() {
//		adapter.clear()
//		latestMessagesMap.values.forEach {
//			adapter.add(LatestPinRow(it))
//		}
//	}
//
//	private fun listenForLatestMessages() {
//		val fromId = FirebaseAuth.getInstance().uid
//		val ref = FirebaseDatabase.getInstance().getReference("/messages/$fromId")
//		ref.addChildEventListener(object : ChildEventListener {
//			override fun onChildAdded(p0: DataSnapshot, p1: String?) {
//				val chatMessage = p0.getValue(Pin::class.java) ?: return
//				latestMessagesMap[p0.key!!] = chatMessage
//				refreshRecyclerViewMessages()
//			}
//
//			override fun onChildChanged(p0: DataSnapshot, p1: String?) {
//				val chatMessage = p0.getValue(Pin::class.java) ?: return
//				latestMessagesMap[p0.key!!] = chatMessage
//				refreshRecyclerViewMessages()
//			}
//
//			override fun onChildMoved(p0: DataSnapshot, p1: String?) {
//
//			}
//
//			override fun onChildRemoved(p0: DataSnapshot) {
//
//			}
//
//			override fun onCancelled(p0: DatabaseError) {
//
//			}
//		})
//	}
//
//	val adapter = GroupAdapter<ViewHolder>()
//
////  private fun setupDummyRows() {
////
////
////    adapter.add(LatestMessageRow())
////    adapter.add(LatestMessageRow())
////    adapter.add(LatestMessageRow())
////  }
//
//	private fun fetchCurrentUser() {
//		val uid = FirebaseAuth.getInstance().uid
//		val ref = FirebaseDatabase.getInstance().getReference("/messages/$uid")
//		ref.addListenerForSingleValueEvent(object : ValueEventListener {
//
//			override fun onDataChange(p0: DataSnapshot) {
//				currentPin = p0.getValue(Pin::class.java)
//				Log.d("LatestMessages", "Current user ${currentPin?.imageURL}")
//			}
//
//			override fun onCancelled(p0: DatabaseError) {
//
//			}
//		})
//	}
//
//	private fun verifyUserIsLoggedIn() {
//		val uid = FirebaseAuth.getInstance().uid
//		if (uid == null) {
//			val intent = Intent(this, RegistrationActivity::class.java)
//			intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
//			startActivity(intent)
//		}
//	}
//
////	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
////		when (item?.itemId) {
////			R.id.menu_new_message -> {
////				val intent = Intent(this, NewMessageActivity::class.java)
////				startActivity(intent)
////			}
////			R.id.menu_sign_out -> {
////				FirebaseAuth.getInstance().signOut()
////				val intent = Intent(this, RegisterActivity::class.java)
////				intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
////				startActivity(intent)
////			}
////		}
////
////		return super.onOptionsItemSelected(item)
////	}
//
//	override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
//		R.id.add_new_pin -> {
//			val intent = Intent(this@PinBrowsingActivity, CreatePinActivity::class.java)
//			//intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
//			startActivity(intent)
//			true
//		}
//
////        R.id.action_favorite -> {
////            // User chose the "Favorite" action, mark the current item
////            // as a favorite...
////            true
////        }
//
//		else -> {
//			// If we got here, the user's action was not recognized.
//			// Invoke the superclass to handle it.
//			super.onOptionsItemSelected(item)
//		}
//	}
//
//	override fun onCreateOptionsMenu(menu: Menu): Boolean {
//		//Inflate the menu; this adds items to the action bar if it is present.
//		menuInflater.inflate(R.menu.menu_add_pin_search, menu)
//		return true
//	}
//}
