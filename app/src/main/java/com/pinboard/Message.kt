package com.pinboard

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Message {

    var author: String? = null
    var body: String? = null
    var time: String? = null

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    constructor(author: String, body: String, time: String) {
        this.author = author
        this.body = body
        this.time = time
    }
}