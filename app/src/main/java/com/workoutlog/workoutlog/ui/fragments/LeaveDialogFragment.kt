package com.workoutlog.workoutlog.ui.fragments

import androidx.appcompat.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.workoutlog.workoutlog.R

class LeaveDialogFragment :DialogFragment() {

    private var listener: ILeave? = null
    fun setListener(listener: ILeave) {
        this.listener = listener
    }

    companion object {
        private const val KEY_MESSAGE = "message"
        private const val KEY_TITLE = "title"

        fun newInstance(title: String, message: String): LeaveDialogFragment {
            val fragment = LeaveDialogFragment()
            val args = Bundle()
            args.putString(KEY_MESSAGE, message)
            args.putString(KEY_TITLE, title)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context!!, R.style.CustomDialogTheme)

        val title = arguments!!.getString(KEY_TITLE, getString(R.string.leave))
        val message = arguments!!.getString(KEY_MESSAGE, getString(R.string.sure_you_want_leave))

        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.yes) {_,_->
                if(listener != null) listener!!.leave()
            }
            .setNegativeButton(android.R.string.no) {_,_->

            }

        return builder.create()
    }

    interface ILeave {
        fun leave()
    }
}