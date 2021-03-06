package com.pinboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.pinboard.Model.ChatMessage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatActivity : AppCompatActivity() {

	private val stringPIN = "pin"

	private var user: FirebaseUser? = null
	private var mDatabase: DatabaseReference? = null
	private var mPinReference: DatabaseReference? = null
	var pin: Pin? = null
	var userID: String? = null

	protected val chatAdapter = GroupAdapter<ViewHolder>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_chat)

		pin = intent.getSerializableExtra(stringPIN) as Pin
		if (intent.hasExtra("userID")) {
			userID = intent.getStringExtra("userID")
		} else userID = FirebaseAuth.getInstance().uid

		chat_recyclerview.layoutManager = LinearLayoutManager(this)
		chat_recyclerview.adapter = chatAdapter


		mDatabase = FirebaseDatabase.getInstance().reference
		mPinReference =
			FirebaseDatabase.getInstance()
				.getReference(getString(R.string.pin_folder_name).plus("${pin!!.pinID}/ChatMessages/$userID"))
		user = FirebaseAuth.getInstance().currentUser

		val tb: Toolbar = findViewById(R.id.toolbar)
		if (pin?.userData?.userID != user?.uid) {
			tb.title = pin!!.author + " with pin " + pin!!.header
		}
		setSupportActionBar(tb)
		tb.setNavigationIcon(R.drawable.arrow_left)
		tb.setNavigationOnClickListener {
			onBackPressed()
		}

//		chat_edittext.setOnClickListener {
//			scrollView()
//		}
		send_chat_button.setOnClickListener {
			sendChatMessage()
		}

		chatListener()

	}

	fun scrollView() {
		chat_recyclerview.smoothScrollToPosition(chatAdapter.itemCount)
	}

	private fun chatListener() {
		mPinReference?.addChildEventListener(object : ChildEventListener {
			override fun onCancelled(p0: DatabaseError) {}

			override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

			override fun onChildChanged(p0: DataSnapshot, p1: String?) {}

			override fun onChildAdded(p0: DataSnapshot, p1: String?) {
				val chatMessage = p0.getValue(ChatMessage::class.java)
				if (!pin?.userData?.userID.equals(user?.uid)) {
					if (chatMessage!!.fromID.equals(userID)) {
						chatAdapter.add(ChatToItem(chatMessage.text.toString()))
					}
					if (chatMessage.fromID.equals(pin?.userData?.userID)) {
						chatAdapter.add(ChatFromItem(chatMessage.text.toString()))
					}
				} else {
					if (chatMessage?.fromID.equals(userID)) {
						chatAdapter.add(ChatFromItem(chatMessage?.text.toString()))
					}
					if (chatMessage?.fromID.equals(pin?.userData?.userID)) {
						chatAdapter.add(ChatToItem(chatMessage?.text.toString()))
					}
				}
				scrollView()
			}

			override fun onChildRemoved(p0: DataSnapshot) {}

		})

	}

	private fun sendChatMessage() {
		if (chat_edittext.text.toString() == "") return
		val ref = mPinReference?.push()
		val text = chat_edittext.text.toString()
		val chatMessage =
			ChatMessage(
				ref?.key,
				user?.uid,
				pin?.userData?.userID,
				text,
				System.currentTimeMillis()
			)
		ref?.setValue(chatMessage)?.addOnFailureListener {
				Snackbar.make(
						chat_recyclerview,
						"Cant send chat message because $it",
						Snackbar.LENGTH_LONG
					)
					.show()
			}
			?.addOnSuccessListener {
				chat_edittext.text = null
			}
	}

	class ChatFromItem(val text: String) : Item<ViewHolder>() {
		override fun getLayout(): Int {
			return R.layout.chat_from_row
		}

		override fun bind(viewHolder: ViewHolder, position: Int) {
			viewHolder.itemView.chat_from_row_textview.text = text
		}

	}

	class ChatToItem(val text: String) : Item<ViewHolder>() {
		override fun getLayout(): Int {

			return R.layout.chat_to_row
		}

		override fun bind(viewHolder: ViewHolder, position: Int) {
			viewHolder.itemView.chat_to_row_textview.text = text
		}

	}
}
