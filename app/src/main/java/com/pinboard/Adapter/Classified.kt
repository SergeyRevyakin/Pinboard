package com.pinboard.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pinboard.R


class Classified(private val values: List<String>) : RecyclerView.Adapter<Classified.MyHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		MyHolder(
			LayoutInflater.from(parent.context).inflate(
				R.layout.list_item_view,
				parent,
				false
			)
		)
	//return itemView

	override fun getItemCount(): Int {
		return values.size
	}

	override fun onBindViewHolder(holder: MyHolder, position: Int) {
		holder?.textView?.text = values[position]
	}

	class MyHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
		var textView: TextView? = null

		init {
			textView = itemView?.findViewById(R.id.pin_header)
		}

	}

}