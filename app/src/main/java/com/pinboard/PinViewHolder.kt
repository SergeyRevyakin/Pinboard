package com.pinboard

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.pin_item_view.view.*

class PinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

	fun bindMessage(pin: Pin?) {
		with(pin!!) {
			itemView.pin_header_textview.text = header
			itemView.pin_price_textview.text = price
			//itemView.pin_header.text = header
		}
	}
}