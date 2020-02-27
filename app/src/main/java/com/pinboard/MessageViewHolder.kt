package com.pinboard

import android.view.View
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.pin_cardview.view.*

class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

//	fun bindMessage(pin: Pin?) {
//		with(pin!!) {
//			itemView.pin_owner.text = author
//			itemView.pin_content.text = description
//			itemView.pin_header.text = header
//		}
//	}

	fun bindMessage(pin: Pin?) {
		with(pin!!) {
			itemView.ImageNameTextView_pincard.text = header
			//itemView.pin_price_textview.text = price


		}
	}
}
