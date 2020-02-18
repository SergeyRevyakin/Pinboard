package com.pinboard

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Pin {

    var author: String? = null
    var header: String? = null
    var time: String? = null
    var description: String? = null
    var price: String? = null


    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    constructor(
        author: String?,
        header: String?,
        time: String?,
        description: String?,
        price: String?
    ) {
        this.author = author
        this.header = header
        this.time = time
        this.description = description
        this.price = price
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