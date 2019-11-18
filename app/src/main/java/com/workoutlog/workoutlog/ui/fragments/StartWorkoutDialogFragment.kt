package com.workoutlog.workoutlog.ui.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.workoutlog.workoutlog.R

class StartWorkoutDialogFragment: DialogFragment() {

    private var listener: IStartWorkout? = null

    companion object {
        private const val KEY_TP = "tp"
        private const val KEY_ROUTINE = "routine"
        private const val KEY_RID = "rId"

        fun newInstance(routine: String, trainingplan: String, rId: Int): StartWorkoutDialogFragment {
            val dialog = StartWorkoutDialogFragment()
            val args = Bundle()
            args.putString(KEY_TP, trainingplan)
            args.putString(KEY_ROUTINE, routine)
            args.putInt(KEY_RID, rId)
            dialog.arguments = args
            return dialog
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context!!, R.style.CustomDialogTheme)
        val message = arguments?.getString(KEY_TP) + ":\n" + arguments?.getString(KEY_ROUTINE)

        builder.setTitle(getString(R.string.start_workout))
            .setMessage(message)
            .setPositiveButton(R.string.start) { _, _->
                if(listener != null) listener!!.startCurrentWorkout(arguments!!.getInt(KEY_RID))
            }
            .setNegativeButton(getString(R.string.choose_other_workout)) {_,_->
                if(listener != null) listener!!.chooseOtherWorkout()
            }

        return builder.create()
    }

    interface IStartWorkout {
        fun startCurrentWorkout(rId: Int)
        fun chooseOtherWorkout()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as IStartWorkout
        } catch (e: ClassCastException) {
            Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
        }
    }
}