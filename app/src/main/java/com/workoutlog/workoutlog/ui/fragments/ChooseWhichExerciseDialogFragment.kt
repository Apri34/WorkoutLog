package com.workoutlog.workoutlog.ui.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.workoutlog.workoutlog.R

class ChooseWhichExerciseDialogFragment: DialogFragment() {

    companion object {
        private const val NORMAL_EXERCISE = 0
        private const val SUPERSET = 1
        private const val DROPSET = 2
    }

    private var listener: IChooseExercise? = null
    fun setListener(context: Context) {
        try {
            listener = context as IChooseExercise
        } catch (e: ClassCastException) {
            Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.CustomDialogTheme)
            builder.setTitle(getString(R.string.which_type_of_exercise))
                .setItems(R.array.exercise_types) { _, which ->
                    if(listener != null) {
                        when(which) {
                            NORMAL_EXERCISE -> listener!!.addNormalExercise()
                            SUPERSET -> listener!!.addSuperset()
                            DROPSET -> listener!!.addDropset()
                        }
                    }
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    interface IChooseExercise {
        fun addNormalExercise()
        fun addSuperset()
        fun addDropset()
    }
}