package com.workoutlog.workoutlog.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.workoutlog.workoutlog.R
import java.lang.ClassCastException

class ConfirmDeleteCurrentTpDialogFragment: DialogFragment() {

    private lateinit var message: String
    private var title: String? = null
    private var listener: IConfirmDeleteCurrentTpDialog? = null

    companion object {
        private const val KEY_MESSAGE = "message"
        private const val KEY_TITLE = "title"

        fun newInstance(title: String?, message: String?): ConfirmDeleteCurrentTpDialogFragment {
            val dialog = ConfirmDeleteCurrentTpDialogFragment()
            val args = Bundle()
            args.putString(KEY_MESSAGE, message)
            args.putString(KEY_TITLE, title)
            dialog.arguments = args
            return dialog
        }
    }

    fun setListener(listener: Fragment) {
        try {
            this.listener = listener as IConfirmDeleteCurrentTpDialog
        } catch (e: ClassCastException) {
            Log.i(listener.toString(), " must implement IConfirmDeleteCurrentTpDialog")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        message = arguments?.getString(KEY_MESSAGE) ?: getString(R.string.are_you_sure)
        title = arguments?.getString(KEY_TITLE)
        val builder = AlertDialog.Builder(context, R.style.CustomDialogTheme)
        if(title != null)
            builder.setTitle(title!!)
        builder.setMessage(message)
            .setPositiveButton(android.R.string.ok) {_,_->
                if (listener != null) listener!!.deleteAndCreateNew()
            }
            .setNegativeButton(android.R.string.cancel) {_,_->

            }

        return builder.create()
    }

    interface IConfirmDeleteCurrentTpDialog {
        fun deleteAndCreateNew()
    }
}