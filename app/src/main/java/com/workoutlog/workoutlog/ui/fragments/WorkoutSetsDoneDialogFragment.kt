package com.workoutlog.workoutlog.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.workoutlog.workoutlog.R
import java.lang.ClassCastException

class WorkoutSetsDoneDialogFragment: DialogFragment() {

    private var listener: IWorkoutSetsDoneDialog? = null

    companion object {
        private const val KEY_EXERCISE = "exercise"
        private const val KEY_SETS = "sets"

        fun newInstance(exercise: String, sets: Int): WorkoutSetsDoneDialogFragment {
            val fragment = WorkoutSetsDoneDialogFragment()
            val args = Bundle()
            args.putString(KEY_EXERCISE, exercise)
            args.putInt(KEY_SETS, sets)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context, R.style.CustomDialogTheme)

        builder.setTitle(arguments!!.getString(KEY_EXERCISE))
            .setMessage(String.format(resources.getQuantityString(R.plurals.you_have_completed_your_goal, arguments!!.getInt(KEY_SETS)), arguments!!.getInt(KEY_SETS)))
            .setPositiveButton(R.string.next) {_,_->
                if(listener != null) listener!!.next()
            }
            .setNegativeButton(R.string.stay) {_,_->

            }

        return builder.create()
    }

    interface IWorkoutSetsDoneDialog {
        fun next()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as IWorkoutSetsDoneDialog
        } catch (e: ClassCastException) {
            Log.i(context.toString(), " must implement IWorkoutSetsDoneDialog")
        }
    }
}