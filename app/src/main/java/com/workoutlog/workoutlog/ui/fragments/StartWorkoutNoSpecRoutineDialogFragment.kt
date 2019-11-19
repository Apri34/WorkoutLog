package com.workoutlog.workoutlog.ui.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
        val typedValue = TypedValue()
        val theme = context!!.theme
        theme.resolveAttribute(R.attr.customDialogTheme, typedValue, true)
        val builder = AlertDialog.Builder(context!!, typedValue.data)

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
            Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
        }
    }
}