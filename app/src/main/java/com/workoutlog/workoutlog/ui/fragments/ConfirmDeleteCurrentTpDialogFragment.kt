package com.workoutlog.workoutlog.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.TypedValue
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.workoutlog.workoutlog.R

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
            Toast.makeText(context!!, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        message = arguments?.getString(KEY_MESSAGE) ?: getString(R.string.are_you_sure)
        title = arguments?.getString(KEY_TITLE)
        val typedValue = TypedValue()
        val theme = context!!.theme
        theme.resolveAttribute(R.attr.customDialogTheme, typedValue, true)
        val builder = AlertDialog.Builder(context!!, typedValue.data)
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