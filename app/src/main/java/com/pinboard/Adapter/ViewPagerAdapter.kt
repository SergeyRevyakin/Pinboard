package com.pinboard.Adapter

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.pinboard.Pin
import com.pinboard.R


class ViewPagerAdapter(private val context: Context, val pin: Pin) : PagerAdapter() {
	private val inflater: LayoutInflater = LayoutInflater.from(context)

	override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
		container.removeView(`object` as View)
	}

	override fun getCount(): Int {
		return pin.imageURL!!.size
	}

	override fun instantiateItem(view: ViewGroup, position: Int): Any {
		val imageLayout = inflater.inflate(R.layout.image_viewpager, view, false)!!

		val imageView = imageLayout
			.findViewById(R.id.pager_imageview) as ImageView
//		var imageURL=0
//		if (position>pin.imageURL!!.size) imageURL=0
//		else imageURL= position
		Glide.with(context).load(pin.imageURL?.get(position))
			.thumbnail(0.5f)
			.placeholder(R.drawable.cloud_download_outline)
			.fallback(R.drawable.alert_circle)
			.error(R.drawable.alert_circle)
			.centerCrop()
			.diskCacheStrategy(DiskCacheStrategy.ALL)
			.into(imageView)

		view.addView(imageLayout, 0)

		return imageLayout
	}

	override fun isViewFromObject(view: View, `object`: Any): Boolean {
		return view == `object`
	}

	override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

	override fun saveState(): Parcelable? {
		return null
	}


}

//	: Item<ViewHolder>() {
//	override fun getLayout(): Int {
//
//		return R.layout.image_viewpager
//	}
//
//	override fun bind(viewHolder: ViewHolder, position: Int) {
//
//		Glide.with(viewHolder.itemView.pager_imageview).load(pin.imageURL?.get(position))
//			//.crossFade()
//			.thumbnail(0.5f)
//			.placeholder(R.drawable.cloud_download_outline)
//			.fallback(R.drawable.alert_circle)
//			.error(R.drawable.alert_circle)
//			.centerCrop()
//			.diskCacheStrategy(DiskCacheStrategy.ALL)
//			.into(viewHolder.itemView.pager_imageview)
//	}
//
//}
//class ViewPagerAdapter(val pin: Pin) : PagerAdapter() {
//	override fun isViewFromObject(view: View, `object`: Any): Boolean =
//		view == `object` as ConstraintLayout
//
//	override fun getCount(): Int {
//		return pin.imageURL?.size!!
//	}
//
//	override fun instantiateItem(container: ViewGroup, position: Int): Any {
//		val view:View = LayoutInflater.from(container.context).inflate(R.layout.image_viewpager, container, false)
//		val image = view.findViewById<ImageView>(R.id.pager_imageview)
//		image.setImageURI(pin.imageURL?.get(position)?.toUri())
//		container.addView(view)
//		return view
//	}

//	private val colors = intArrayOf(
//		android.R.color.black,
//		android.R.color.holo_red_light,
//		android.R.color.holo_blue_dark,
//		android.R.color.holo_purple
//	)
//
//	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
//		PagerVH(LayoutInflater.from(parent.context).inflate(R.layout.image_viewpager, parent, false))
//
//	override fun getItemCount(): Int = colors.size
//
//	override fun onBindViewHolder(holder: PagerVH, position: Int) = holder.itemView.run {
//		Glide.with(holder.itemView.pager_imageview).load(pin.imageURL?.get(0))
//			//.crossFade()
//			.thumbnail(0.5f)
//			.placeholder(R.drawable.cloud_download_outline)
//			.fallback(R.drawable.alert_circle)
//			.error(R.drawable.alert_circle)
//			.centerCrop()
//			.diskCacheStrategy(DiskCacheStrategy.ALL)
//			.into(holder.itemView.pager_imageview)
//
//	}
//}
//
//class PagerVH(itemView: View) : RecyclerView.ViewHolder(itemView)