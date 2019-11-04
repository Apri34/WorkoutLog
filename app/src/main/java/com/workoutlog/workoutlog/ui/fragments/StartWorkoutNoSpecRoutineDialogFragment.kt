package com.workoutlog.workoutlog.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.workoutlog.workoutlog.R

class StartWorkoutNoSpecRoutineDialogFragment: DialogFragment() {

    private lateinit var message: String
    private var listener: IStartWorkoutNoSpecRoutine? = null

    companion object {
        private const val KEY_MESSAGE = "message"

        fun newInstance(message: String): StartWorkoutNoSpecRoutineDialogFragment {
            val dialog = StartWorkoutNoSpecRoutineDialogFragment()
            val args = Bundle()
            args.putString(KEY_MESSAGE, message)
            dialog.arguments = args
            return dialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context, R.style.CustomDialogTheme)

        message = arguments?.getString(KEY_MESSAGE) ?: getString(R.string.choose_from_routines_or_create_new)
        builder.setTitle(R.string.start_workout)
            .setMessage(message)
            .setPositiveButton(R.string.choose_workout) {_,_->
                if(listener != null) listener!!.chooseRoutine()
            }
            .setNegativeButton(R.string.create_new) {_,_->
                if(listener != null) listener!!.createRoutine()
            }

        return builder.create()
    }

    interface IStartWorkoutNoSpecRoutine {
        fun createRoutine()
        fun chooseRoutine()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as IStartWorkoutNoSpecRoutine
        } catch (e: ClassCastException) {
            Log.i(context.toString(), " must implement IStartWorkoutNoSpecRoutine")
        }
    }
}