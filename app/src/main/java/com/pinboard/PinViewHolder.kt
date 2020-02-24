package com.pinboard

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.pin_cardview.view.*

class PinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

	fun bindMessage(pin: Pin?) {
		with(pin!!) {
			itemView.ImageNameTextView_pincard.text = header
			//itemView.pin_price_textview.text = price

//			val targetImageView = viewHolder.itemView.imageview_latest_message
//			Picasso.get().load(chatPartnerUser?.profileImageUrl).into(targetImageView)
//			//itemView.pin_header.text = header
		}
	}


}