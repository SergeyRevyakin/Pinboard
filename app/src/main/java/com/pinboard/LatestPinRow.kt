package com.pinboard

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.pin_cardview.view.*


class LatestPinRow(val pin: Pin) : Item<ViewHolder>() {
	var completePin: Pin? = null

	override fun bind(viewHolder: ViewHolder, position: Int) {
		viewHolder.itemView.ImageNameTextView_pincard.text = pin.header

//		val chatPartnerId: String
//		if (pin.fromId == FirebaseAuth.getInstance().uid) {
//			chatPartnerId = pin.toId
//		} else {
//			chatPartnerId = pin.fromId
//		}

		val ref = FirebaseDatabase.getInstance().getReference("/messages/")
		ref.addListenerForSingleValueEvent(object : ValueEventListener {
			override fun onDataChange(p0: DataSnapshot) {
				completePin = p0.getValue(Pin::class.java)
				//viewHolder.itemView.username_textview_latest_message.text = chatPartnerUser?.username

				val targetImageView = viewHolder.itemView.imageView_pincard
				Picasso.get().load(completePin?.imageURL).into(targetImageView)
			}

			override fun onCancelled(p0: DatabaseError) {

			}
		})
	}

	override fun getLayout(): Int {
		return R.layout.pin_cardview
	}
}