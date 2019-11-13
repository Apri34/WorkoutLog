package com.workoutlog.workoutlog.ui.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.database.AppDatabase
import com.workoutlog.workoutlog.database.DatabaseInitializer
import com.workoutlog.workoutlog.database.entities.Routine
import com.workoutlog.workoutlog.views.CustomEditText

class EditRoutineNameDialogFragment : DialogFragment() {

    companion object {
        const val ROUTINE_NAME_KEY = "rName"
        const val ROUTINE_ID_KEY = "rId"
        const val TP_ID_KEY = "tpId"
        const val POS_IN_TP_KEY = "posInTp"
        const val ERROR_KEY = "error"
    }

    private var listener: IEditRoutine? = null
    private lateinit var routine: Routine
    private lateinit var routines: List<String>
    private lateinit var etName: CustomEditText

    fun setRoutine(routine: Routine) {
        this.routine = routine
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_add_routine, null)
        etName = view.findViewById(R.id.edit_text_dialog_add_routine)

        if(savedInstanceState != null) {
            routine = Routine(savedInstanceState.getInt(ROUTINE_ID_KEY),
                savedInstanceState.getString(ROUTINE_NAME_KEY),
                savedInstanceState.getInt(TP_ID_KEY),
                savedInstanceState.getInt(POS_IN_TP_KEY))
            val error = savedInstanceState.getSerializable(ERROR_KEY) as CustomEditText.Error
            etName.showErrorMessage(error)
        }

        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.CustomDialogTheme)
            etName.setText(routine.rName)

            builder.setTitle(getString(R.string.routine))
                .setView(view)
                .setPositiveButton(R.string.save) { _, _ ->

                }
                .setNegativeButton(android.R.string.cancel) { _, _ ->

                }

            builder.create()
        } ?: throw IllegalAccessException("Activity cannot be null")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ROUTINE_NAME_KEY, routine.rName)
        outState.putInt(ROUTINE_ID_KEY, routine.rId)
        outState.putInt(TP_ID_KEY, routine.tpId)
        outState.putInt(POS_IN_TP_KEY, routine.posInTp)
        outState.putSerializable(ERROR_KEY, etName.error)
    }

    interface IEditRoutine{
        fun editRoutine(rId: Int, rName: String)
    }

    fun setListener(context: Context) {
        try {
            listener = context as IEditRoutine
        } catch (e: ClassCastException) {
            Log.i(context.toString(), " must implement IEditRoutine")
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        routines = DatabaseInitializer.getInstance(context)
            .getRoutineNamesByTpId(AppDatabase.getInstance(context).routineDao(), routine.tpId)
    }

    override fun onResume() {
        super.onResume()
        val d = dialog as AlertDialog
        val positiveButton = d.getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if(etName.textField.text.toString().isEmpty()) {
                etName.showErrorMessage(CustomEditText.Error.ERROR_FIELD_EMPTY)
            } else if (etName.textField.text.toString() in routines) {
                if(etName.textField.text.toString() != routine.rName) {
                    etName.showErrorMessage(CustomEditText.Error.EXERCISE_ALREADY_EXISTS)
                } else {
                    d.dismiss()
                }
            } else {
                etName.hideErrorMessage()
                if(listener != null) {
                    listener!!.editRoutine(routine.rId, etName.textField.text.toString())
                }
                d.dismiss()
            }
        }
    }
}