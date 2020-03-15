package com.pinboard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.pinboard.Model.ChatMessage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_pick_chat.*
import kotlinx.android.synthetic.main.pick_chat_username.view.*


class PickChatActivity : AppCompatActivity() {
	private var user: FirebaseUser? = null
	private var mUserReference: DatabaseReference? = null
	private var currentPinRef: DatabaseReference? = null
	private var pin: Pin? = null

	val pickChatAdapter = GroupAdapter<ViewHolder>()
	var addedUsersIDs: MutableList<String?> = mutableListOf()
	var addedUserNames: MutableList<String?> = mutableListOf()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_pick_chat)

		val tb: Toolbar = findViewById(R.id.toolbar)
		tb.title = "Choose user"
		setSupportActionBar(tb)
		tb.setNavigationIcon(R.drawable.arrow_left)
		tb.setNavigationOnClickListener {
			onBackPressed()
		}

		pick_chat_recyclerview.layoutManager = LinearLayoutManager(this)

		pick_chat_recyclerview.addItemDecoration(
			DividerItemDecoration(
				this,
				LinearLayoutManager.VERTICAL
			)
		)

		pick_chat_recyclerview.adapter = pickChatAdapter

		pin = intent.getSerializableExtra("pin") as Pin

		mUserReference = FirebaseDatabase.getInstance().getReference("users")
		currentPinRef =
			FirebaseDatabase.getInstance()
				.getReference(getString(R.string.pin_folder_name).plus("${pin?.pinID}/ChatMessages"))
		user = FirebaseAuth.getInstance().currentUser

		userMessages()


	}

//	inline fun RecyclerView.setOnItemClickListener(crossinline listener: (position: Int) -> Unit) {
//		addOnItemTouchListener(
//			RecyclerItemAdapter(
//				this,
//				object : RecyclerItemAdapter.OnItemClickListener {
//					override fun onItemClick(view: View, position: Int) {
//						listener(position)
//					}
//				})
//		)
//	}

	private fun userMessages() {
		//val mQueryRef = mUserReference
		currentPinRef?.addChildEventListener(object : ChildEventListener {
			override fun onCancelled(p0: DatabaseError) {}

			override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

			override fun onChildChanged(p0: DataSnapshot, p1: String?) {}

			override fun onChildAdded(p0: DataSnapshot, p1: String?) {
				//val chatMessage =
				var userID: String? = null
				p0.children.forEach {
					userID = it.getValue(ChatMessage::class.java)?.fromID
					if (!(addedUsersIDs.contains(userID))) {
						addedUsersIDs.add(userID)
					}

					//val userID = p1//chatMessage?.fromID
					//mUserReference.
//				if (!(addedUsersIDs!!.contains(userID))) {
//					addedUsersIDs!!.add(userID)

					//pickChatAdapter.add(userNameShow(userID))
				}
			}

			override fun onChildRemoved(p0: DataSnapshot) {}
		})
		getUsernameByID(addedUsersIDs)
	}

	class userNameShow(
		val user: User?
	) : Item<ViewHolder>() {
		override fun getLayout(): Int {

			return R.layout.pick_chat_username
		}

		override fun bind(viewHolder: ViewHolder, position: Int) {
			viewHolder.itemView.pick_chat_username_textview.text = user?.userName
		}
	}

	private fun getUsernameByID(userIDs: MutableList<String?>) {
		mUserReference?.addChildEventListener(object : ChildEventListener {
			override fun onCancelled(p0: DatabaseError) {}

			override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

			override fun onChildChanged(p0: DataSnapshot, p1: String?) {}

			override fun onChildAdded(p0: DataSnapshot, p1: String?) {
				val userP = p0.getValue(User::class.java)

				if (userIDs.contains(userP?.userID) && (!userP?.userID.equals(user?.uid))) {
					pickChatAdapter.add(userNameShow(userP))
				}

				pickChatAdapter.setOnItemClickListener { item, view ->

					val userItem = item as userNameShow

					val intent = Intent(view.context, ChatActivity::class.java)

					intent.putExtra("userID", userItem.user?.userID)
					intent.putExtra("pin", pin)
					startActivity(intent)

					finish()

				}
			}

			override fun onChildRemoved(p0: DataSnapshot) {}

		})
	}
}
