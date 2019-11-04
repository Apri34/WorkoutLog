package com.workoutlog.workoutlog.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.workoutlog.workoutlog.R
import java.lang.ClassCastException

class WorkoutSetsNotDoneDialogFragment: DialogFragment() {

    private var listener: IWorkoutSetsNotDoneDialog? = null

    companion object {
        private const val KEY_EXERCISE = "exercise"
        private const val KEY_SETS = "sets"

        fun newInstance(exercise: String, sets: Int): WorkoutSetsNotDoneDialogFragment {
            val fragment = WorkoutSetsNotDoneDialogFragment()
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
            .setMessage(String.format("You have not completed your goal yet: %d sets", arguments!!.getInt(KEY_SETS)))
            .setPositiveButton(R.string.stay) {_,_->

            }
            .setNegativeButton(R.string.next) {_,_->
                if(listener != null) listener!!.notDoneNext()
            }

        return builder.create()
    }

    interface IWorkoutSetsNotDoneDialog {
        fun notDoneNext()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as IWorkoutSetsNotDoneDialog
        } catch (e: ClassCastException) {
            Log.i(context.toString(), " must implement IWorkoutSetsNotDone")
        }
    }
}