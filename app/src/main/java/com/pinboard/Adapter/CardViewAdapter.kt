package com.pinboard.Adapter

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.pinboard.Pin
import com.pinboard.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.pin_cardview.view.*

class CardViewAdapter(val pin: Pin) : Item<ViewHolder>() {
	override fun getLayout(): Int {

		return R.layout.pin_cardview
	}

	override fun bind(viewHolder: ViewHolder, position: Int) {
		viewHolder.itemView.ImageNameTextView_pincard.text = pin.header
		viewHolder.itemView.pin_card_price_textview.text = pin.price


		Glide.with(viewHolder.itemView.imageView_pincard).load(pin.imageURL)
			//.crossFade()
			.thumbnail(0.5f)
			.placeholder(R.drawable.cloud_download_outline)
			.fallback(R.drawable.alert_circle)
			.error(R.drawable.alert_circle)
			.centerCrop()
			.diskCacheStrategy(DiskCacheStrategy.ALL)
			.into(viewHolder.itemView.imageView_pincard)
	}

}