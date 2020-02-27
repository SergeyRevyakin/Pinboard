package com.pinboard


class User {
	var userID: String? = null
	var userName: String? = null
	var email: String? = null

	constructor() {}

	constructor(userID: String?, username: String?, email: String?) {
		this.userID = userID
		this.userName = username
		this.email = email
	}

}