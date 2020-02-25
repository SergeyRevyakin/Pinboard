package com.pinboard

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
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

			//val targetImageView = viewHolder.itemView.imageview_latest_message
			Picasso.get().load(pin.imageURL).into(itemView.imageView_pincard)
			//itemView.pin_header.text = header
		}
	}
}
