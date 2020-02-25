package com.pinboard.Dialog


import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.pinboard.R

class WarningDialog : DialogFragment() {

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		return activity?.let {
			val builder = AlertDialog.Builder(it)
			builder.setTitle("Warning!")
				.setMessage("Something went wrong! :(")
				.setIcon(R.drawable.alert_circle)
				.setPositiveButton("OK") { dialog, id ->
					dialog.cancel()
				}
			builder.create()
		} ?: throw IllegalStateException("Activity cannot be null")
	}
}