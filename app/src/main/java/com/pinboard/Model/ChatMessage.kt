package com.pinboard.Model

import java.io.Serializable

class ChatMessage : Serializable {
	var pinID: String? = null
	var fromID: String? = null
	var text: String? = null
	var timestamp: Long? = null

	constructor() {}
}