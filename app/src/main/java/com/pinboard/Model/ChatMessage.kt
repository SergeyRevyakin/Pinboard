package com.pinboard.Model

import java.io.Serializable

class ChatMessage : Serializable {
	var ID: String? = null
	var fromID: String? = null
	var toID: String? = null
	var text: String? = null
	var timestamp: Long? = null

	constructor() {}

	constructor(ID: String?, fromID: String?, toID: String?, text: String?, timestamp: Long?) {
		this.ID = ID
		this.fromID = fromID
		this.toID = toID
		this.text = text
		this.timestamp = timestamp
	}


}