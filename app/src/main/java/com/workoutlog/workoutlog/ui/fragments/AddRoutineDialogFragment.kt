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
import com.workoutlog.workoutlog.views.CustomEditText
import java.lang.ClassCastException

class AddRoutineDialogFragment : DialogFragment() {

    companion object {
        private const val ERROR_KEY = "error"
        private const val TP_ID_KEY = "tpId"

        fun getInstance(tpId: Int): AddRoutineDialogFragment {
            val fragment = AddRoutineDialogFragment()
            val arguments = Bundle()
            arguments.putInt(TP_ID_KEY, tpId)
            fragment.arguments = arguments
            return fragment
        }
    }

    private var listener: IAddRoutine? = null
    private lateinit var etName: CustomEditText
    private lateinit var routines: List<String>

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_add_routine, null)
        etName = view.findViewById(R.id.edit_text_dialog_add_routine)

        if(savedInstanceState != null) {
            val error = savedInstanceState.getSerializable(ERROR_KEY) as CustomEditText.Error
            etName.showErrorMessage(error)
        }

        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.CustomDialogTheme)
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
        outState.putSerializable(ERROR_KEY, etName.error)
    }

    interface IAddRoutine{
        fun addRoutine(rName: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        routines = DatabaseInitializer.getInstance(context)
            .getRoutineNamesByTpId(AppDatabase.getInstance(context).routineDao(), arguments!!.getInt(TP_ID_KEY, 0))
        try {
            listener = context as IAddRoutine
        } catch (e: ClassCastException) {
            Log.i(context.toString(), " must implement IAddRoutine")
        }
    }

    override fun onResume() {
        super.onResume()
        val d = dialog as AlertDialog
        val positiveButton = d.getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            when {
                etName.textField.text.toString().isEmpty() -> etName.showErrorMessage(CustomEditText.Error.ERROR_FIELD_EMPTY)
                etName.textField.text.toString() in routines -> etName.showErrorMessage(CustomEditText.Error.ROUTINE_ALREADY_EXISTS)
                else -> {
                    etName.hideErrorMessage()
                    if(listener != null) {
                        listener!!.addRoutine(etName.textField.text.toString())
                    }
                    dismiss()
                }
            }
        }
    }
}