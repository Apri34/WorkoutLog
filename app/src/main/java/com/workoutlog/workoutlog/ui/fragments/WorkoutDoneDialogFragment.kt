package com.workoutlog.workoutlog.ui.fragments

import android.app.AlertDialog.Builder
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.workoutlog.workoutlog.R

class WorkoutDoneDialogFragment: DialogFragment() {

    private var listener: IWorkoutAgain? = null
    fun setListener(listener: IWorkoutAgain) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = Builder(context, R.style.CustomDialogTheme)
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