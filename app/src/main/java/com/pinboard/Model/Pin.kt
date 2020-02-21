package com.pinboard

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Pin {

	var author: String? = null
	var header: String? = null
	var time: String? = null
	var description: String? = null
	var price: String? = null
	var imageURL: String? = null


	constructor() {}

	constructor(
		author: String?,
		header: String?,
		time: String?,
		description: String?,
		price: String?,
		imageURL: String?
	) {
		this.author = author
		this.header = header
		this.time = time
		this.description = description
		this.price = price
		this.imageURL = imageURL
	}

	fun toMap(): Map<String, Any> {
		val result = HashMap<String, Any>()
		result.put("author", author!!)
		result.put("header", header!!)
		result.put("time", time!!)
		result.put("description", description!!)
		result.put("price", price!!)

		return result
	}
}