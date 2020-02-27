package com.pinboard

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

/**
 *
 */
@IgnoreExtraProperties
class Pin : Serializable {

	var author: String? = null
	var authorID: String? = null
	var pinID: String? = null
	var header: String? = null
	var time: String? = null
	var description: String? = null
	var price: String? = null
	var imageURL: String? = null


	constructor() {}

	constructor(
		author: String?,
		authorID: String?,
		pinID: String?,
		header: String?,
		time: String?,
		description: String?,
		price: String?,
		imageURL: String?
	) {
		this.author = author
		this.authorID = authorID
		this.pinID = pinID
		this.header = header
		this.time = time
		this.description = description
		this.price = price
		this.imageURL = imageURL
	}

	fun toMap(): Map<String, Any> {
		val result = HashMap<String, Any>()
		result["author"] = author!!
		result["authorID"] = authorID!!
		result["pinID"] = pinID!!
		result["header"] = header!!
		result["time"] = time!!
		result["description"] = description!!
		result["price"] = price!!
		result["imageURL"] = imageURL!!

		return result
	}
}