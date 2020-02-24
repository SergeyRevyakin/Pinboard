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
		result["author"] = author!!
		result["header"] = header!!
		result["time"] = time!!
		result["description"] = description!!
		result["price"] = price!!
		result["imageURL"] = imageURL!!

		return result
	}
}