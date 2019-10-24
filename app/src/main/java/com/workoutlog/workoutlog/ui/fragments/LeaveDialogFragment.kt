package com.workoutlog.workoutlog.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.workoutlog.workoutlog.R
import java.lang.ClassCastException

class LeaveDialogFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(context, R.style.CustomDialogTheme)
        builder.setTitle(context!!.getString(R.string.current_trainingplan))
            .setMessage(getString(R.string.you_are_about_to_leave))
            .setPositiveButton(R.string.leave) {_,_->
                if(listener != null) listener!!.leave()
            }
            .setNegativeButton(android.R.string.cancel) {_,_->

            }

        return builder.create()
    }

    interface ILeave {
        fun leave()
    }

    private var listener: ILeave? = null
    fun setListener(context: Context) {
        try {
            listener = context as ILeave
        } catch (e: ClassCastException) {
            Log.i(context.toString(), " must implement ILeave")
        }
    }
}