package com.pinboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class ChatActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_chat)

		val tb: Toolbar = findViewById(R.id.toolbar)
		setSupportActionBar(tb)
		tb.setNavigationIcon(R.drawable.arrow_left)
		tb.setNavigationOnClickListener {
			onBackPressed()
		}

	}

}
