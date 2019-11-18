package com.workoutlog.workoutlog.ui.fragments

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.workoutlog.workoutlog.R

class SendVerificationDialogFragment: DialogFragment() {

    private var listener: ISendVerification? = null
    fun setListener(listener: ISendVerification) {
        this.listener = listener
    }

    companion object {
        private const val KEY_MESSAGE = "message"
        private const val KEY_TITLE = "title"

        fun newInstance(title: String, message: String): SendVerificationDialogFragment {
            val fragment = SendVerificationDialogFragment()
            val args = Bundle()
            args.putString(KEY_MESSAGE, message)
            args.putString(KEY_TITLE, title)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context!!, R.style.CustomDialogTheme)
        builder.setTitle(arguments!!.getString(KEY_TITLE))
            .setMessage(arguments!!.getString(KEY_MESSAGE))
            .setPositiveButton(android.R.string.ok) {_,_->
                if(listener != null) listener!!.sendVerification()
            }
            .setNegativeButton(android.R.string.cancel) {_,_->

            }
        return builder.create()
    }

    interface ISendVerification {
        fun sendVerification()
        fun signOut()
    }

    override fun onDismiss(dialog: DialogInterface) {
        if(listener != null) listener!!.signOut()
        super.onDismiss(dialog)
    }
}