package com.pinboard

import java.io.Serializable

data class User(
	var userID: String,
	var userName: String,
	var email: String
) : Serializable {
	constructor() : this("", "", "") {}

//	constructor(userID: String?, username: String?, email: String?) {
//		this.userID = userID
//		this.userName = username
//		this.email = email
//	}

}