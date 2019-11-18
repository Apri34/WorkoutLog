package com.workoutlog.workoutlog.ui.fragments

import androidx.appcompat.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.workoutlog.workoutlog.R

class MessageDialogFragment: DialogFragment() {

    companion object {
        private const val KEY_MESSAGE = "message"
        fun newInstance(message: String): MessageDialogFragment {
            val args = Bundle()
            args.putString(KEY_MESSAGE, message)
            val dialog = MessageDialogFragment()
            dialog.arguments = args
            return dialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context!!, R.style.CustomDialogTheme)

        builder.setMessage(arguments!!.getString(KEY_MESSAGE))
            .setPositiveButton(android.R.string.ok) {_,_->}

        return builder.create()
    }
}