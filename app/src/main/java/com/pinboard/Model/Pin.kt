package com.pinboard

import java.io.Serializable

/**
 *
 */

class Pin : Serializable {

	var author: String? = null
	var authorID: String? = null
	var pinID: String? = null
	var header: String? = null
	var time: String? = null
	var timeStamp: Long? = null
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
		timeStamp: Long,
		description: String?,
		price: String?,
		imageURL: String?
	) {
		this.author = author
		this.authorID = authorID
		this.pinID = pinID
		this.header = header
		this.time = time
		this.timeStamp = timeStamp
		this.description = description
		this.price = price
		this.imageURL = imageURL
	}

//	fun toMap(): Map<String, Any> {
//		val result = HashMap<String, Any>()
//		result["author"] = author!!
//		result["authorID"] = authorID!!
//		result["pinID"] = pinID!!
//		result["header"] = header!!
//		result["time"] = time!!
//		result["timeStamp"] = timeStamp!!
//		result["description"] = description!!
//		result["price"] = price!!
//		result["imageURL"] = imageURL!!
//
//		return result
//	}

	fun contain(text: String): Boolean? {
		if (pinID?.contains(text) ?: return null) return true
		if (authorID?.contains(text) ?: return null) return true
		if (author?.contains(text) ?: return null) return true
		if (header?.contains(text) ?: return null) return true
		if (time?.contains(text) ?: return null) return true
		if (price?.contains(text) ?: return null) return true
		if (description?.contains(text) ?: return null) return true
		return false

	}
}