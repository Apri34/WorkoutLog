package com.workoutlog.workoutlog.ui.fragments

import androidx.appcompat.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.DialogFragment
import com.workoutlog.workoutlog.R

class WorkoutDoneDialogFragment: DialogFragment() {

    private var listener: IWorkoutAgain? = null
    fun setListener(listener: IWorkoutAgain) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val typedValue = TypedValue()
        val theme = context!!.theme
        theme.resolveAttribute(R.attr.customDialogTheme, typedValue, true)
        val builder = AlertDialog.Builder(context!!, typedValue.data)
        builder.setMessage(getString(R.string.you_have_already_workoud_out_another_workout))
            .setTitle(getString(R.string.workout))
            .setPositiveButton(android.R.string.yes) {_,_->
                if(listener != null) listener!!.workoutAgain()
            }
            .setNegativeButton(android.R.string.no) {_,_->

            }
        return builder.create()
    }

    interface IWorkoutAgain {
        fun workoutAgain()
    }
}