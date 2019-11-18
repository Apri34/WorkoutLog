package com.workoutlog.workoutlog.ui.fragments

import android.annotation.SuppressLint
import androidx.appcompat.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.database.entities.Normal
import java.lang.NumberFormatException

class CreateNormalForWorkoutDialogFragment: DialogFragment() {

    private var listener: ICreateNormalForWorkout? = null
    fun setListener(listener: ICreateNormalForWorkout) {
        this.listener = listener
    }

    companion object {
        private const val KEY_EXC_ID = "eId"
        private const val KEY_EXC_NAME = "eName"
        private const val KEY_ROUTINE_ID = "rId"
        private const val POS_IN_ROUTINE = "pos"

        fun newInstance(eName: String, eId: Int, rId: Int, posInRoutine: Int): CreateNormalForWorkoutDialogFragment {
            val fragment = CreateNormalForWorkoutDialogFragment()
            val args = Bundle()
            args.putInt(KEY_EXC_ID, eId)
            args.putString(KEY_EXC_NAME, eName)
            args.putInt(KEY_ROUTINE_ID, rId)
            args.putInt(POS_IN_ROUTINE, posInRoutine)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var etSets: EditText
    private lateinit var etReps: EditText
    private lateinit var etRpe: EditText
    private lateinit var etBreak: EditText

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_create_normal_for_workout, null)
        val builder = AlertDialog.Builder(context!!, R.style.CustomDialogTheme)
        builder.setTitle(arguments!!.getString(KEY_EXC_NAME))
        builder.setView(view)
            .setPositiveButton(android.R.string.ok) {_,_->

            }
            .setNegativeButton(android.R.string.cancel) {_,_->

            }

        etSets = view.findViewById(R.id.edit_text_sets_dialog_create_normal)
        etReps = view.findViewById(R.id.edit_text_reps_dialog_create_normal)
        etBreak = view.findViewById(R.id.edit_text_break_dialog_create_normal)
        etRpe = view.findViewById(R.id.edit_text_rpe_dialog_create_normal)

        return builder.create()
    }

    interface ICreateNormalForWorkout {
        fun addNormal(normal: Normal)
    }

    private fun getInt(s: String): Int? {
        return try {
            if(Integer.valueOf(s) != 0)
                Integer.valueOf(s)
            else
                null
        } catch (e: NumberFormatException) {
            null
        }
    }

    override fun onResume() {
        super.onResume()
        val d = dialog as AlertDialog
        val posButton = d.getButton(Dialog.BUTTON_POSITIVE)
        posButton.setOnClickListener {
            var error = false
            if(etSets.text.isEmpty() || etSets.text.toString().toInt() <= 0) {
                error = true
                etSets.isActivated = true
            } else {
                etSets.isActivated = false
            }
            if(etReps.text.isEmpty() || etReps.text.toString().toInt() <= 0) {
                error = true
                etReps.isActivated = true
            } else {
                etReps.isActivated = false
            }

            if(!error) {
                val newNormal = Normal(0, arguments!!.getInt(KEY_EXC_ID), getInt(etSets.text.toString())!!, getInt(etReps.text.toString())!!, getInt(etBreak.text.toString()), getInt(etRpe.text.toString()), arguments!!.getInt(POS_IN_ROUTINE), arguments!!.getInt(KEY_ROUTINE_ID))
                if(listener != null) listener!!.addNormal(newNormal)
                dismiss()
            }
        }
    }
}