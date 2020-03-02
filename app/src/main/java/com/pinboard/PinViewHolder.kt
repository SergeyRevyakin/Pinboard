package com.pinboard

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.pin_cardview.view.*

class PinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

	fun bindMessage(pin: Pin?) {
		with(pin!!) {
			itemView.ImageNameTextView_pincard.text = header
			itemView.pin_card_price_textview.text = price


			Glide.with(itemView.imageView_pincard).load(pin.imageURL)
				//.crossFade()
				.thumbnail(0.5f)
				.placeholder(R.drawable.cloud_download_outline)
				.fallback(R.drawable.alert_circle)
				.error(R.drawable.alert_circle)
				.centerCrop()
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.into(itemView.imageView_pincard)
		}
	}


}