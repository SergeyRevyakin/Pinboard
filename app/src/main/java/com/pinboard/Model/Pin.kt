package com.pinboard

import java.io.Serializable
import java.util.*

/**
 *
 */

data class Pin(
	var author: String? = null,
	var userData: User? = null,
	var pinID: String? = null,
	var header: String? = null,
	var time: String? = null,
	var timeStamp: Long? = null,
	var description: String? = null,
	var price: String? = null,
	var imageURL: MutableList<String>? = arrayListOf()//HashMap<String, String>? = hashMapOf()
) : Serializable {

	constructor() : this("", null, "", "", "", -1, "", "", null) {}

	fun contain(text: String): Boolean? {
		if (author?.toLowerCase(Locale.getDefault())?.contains(text) ?: return null) return true
		if (header?.toLowerCase(Locale.ROOT)?.contains(text) ?: return null) return true
		if (price?.toLowerCase(Locale.ROOT)?.contains(text) ?: return null) return true
		if (description?.toLowerCase(Locale.ROOT)?.contains(text) ?: return null) return true
		return false

	}
}