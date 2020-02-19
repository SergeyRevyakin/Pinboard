package com.pinboard

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.message_activity.*
import java.text.SimpleDateFormat
import java.util.*


import java.util.Calendar
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ChildEventListener
import com.firebase.ui.database.FirebaseRecyclerAdapter

import com.firebase.ui.database.ChangeEventListener


class MessageActivity : AppCompatActivity() {
    private val TAG = "MessageActivity"
    private val REQUIRED = "Required"

    private var user: FirebaseUser? = null

    private var mDatabase: DatabaseReference? = null
    private var mMessageReference: DatabaseReference? = null
    private var mMessageListener: ChildEventListener? = null

    private var mAdapter: FirebaseRecyclerAdapter<Pin, MessageViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.message_activity)

        mDatabase = FirebaseDatabase.getInstance().reference
        mMessageReference = FirebaseDatabase.getInstance().getReference("messages")
        user = FirebaseAuth.getInstance().currentUser

        firebaseListenerInit()

        btnSend.setOnClickListener {
            submitMessage()
            edtSentText.setText("")
        }

        btnBack.setOnClickListener {
            finish()
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerview)
//
//        val layoutManager = LinearLayoutManager(this)
//        layoutManager.reverseLayout = false
        recyclerView.setHasFixedSize(true)
        //recyclerView.layoutManager = layoutManager
        recyclerView.layoutManager = LinearLayoutManager(this)


        val query = mMessageReference!!.limitToLast(8)

        mAdapter = object : FirebaseRecyclerAdapter<Pin, MessageViewHolder>(
            Pin::class.java, R.layout.list_item_view, MessageViewHolder::class.java, query
        ) {

            override fun populateViewHolder(
                viewHolder: MessageViewHolder?,
                model: Pin?,
                position: Int
            ) {
                viewHolder!!.bindMessage(model)
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

    private fun firebaseListenerInit() {

        val childEventListener = object : ChildEventListener {

            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val message = dataSnapshot.getValue(Pin::class.java)
                Log.e(TAG, "onChildAdded:" + message!!.header)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.e(TAG, "onChildChanged:" + dataSnapshot.key)

                // A message has changed
                val message = dataSnapshot.getValue(Pin::class.java)
                Toast.makeText(
                    this@MessageActivity,
                    "onChildChanged: " + message!!.header,
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.e(TAG, "onChildRemoved:" + dataSnapshot.key)

                // A message has been removed
                val message = dataSnapshot.getValue(Pin::class.java)
                Toast.makeText(
                    this@MessageActivity,
                    "onChildRemoved: " + message!!.header,
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.e(TAG, "onChildMoved:" + dataSnapshot.key)

                // A message has changed position
                val message = dataSnapshot.getValue(Pin::class.java)
                Toast.makeText(
                    this@MessageActivity,
                    "onChildMoved: " + message!!.header,
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "postMessages:onCancelled", databaseError.toException())
                Toast.makeText(this@MessageActivity, "Failed to load Pin.", Toast.LENGTH_SHORT)
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

    private fun submitMessage() {
        val body = edtSentText.text.toString()

        if (TextUtils.isEmpty(body)) {
            edtSentText.error = REQUIRED
            return
        }

        // User data change listener
        mDatabase!!.child("users").child(user!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user = dataSnapshot.getValue(User::class.java)

                    if (user == null) {
                        Log.e(TAG, "onDataChange: User data is null!")
                        Toast.makeText(
                            this@MessageActivity,
                            "onDataChange: User data is null!",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }

                    writeNewMessage(body)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.e(TAG, "onCancelled: Failed to read user!")
                }
            })
    }

    private fun writeNewMessage(body: String) {
        val time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().time)
        val message = Pin(getUsernameFromEmail(user!!.email), body, time, "DESC", "222")

        val messageValues = message.toMap()
        val childUpdates = HashMap<String, Any>()

        val key = mDatabase!!.child("messages").push().key

        childUpdates.put("/messages/" + key, messageValues)
        childUpdates.put("/user-messages/" + user!!.uid + "/" + key, messageValues)

        mDatabase!!.updateChildren(childUpdates)
    }

    private fun getUsernameFromEmail(email: String?): String {
        return if (email!!.contains("@")) {
            email.split("@".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        } else {
            email
        }
    }
}

