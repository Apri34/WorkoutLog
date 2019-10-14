package com.workoutlog.workoutlog.ui.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.database.AppDatabase
import com.workoutlog.workoutlog.database.DatabaseInitializer
import com.workoutlog.workoutlog.views.CustomEditText

class AddTrainingplanDialogFragment : DialogFragment() {

    companion object {
        const val ERROR_KEY = "error"
    }

    private var listener: IAddTrainingplan? = null
    private lateinit var etName: CustomEditText
    private lateinit var trainingplans: List<String>

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_add_trainingplan, null)
        etName = view.findViewById(R.id.edit_text_dialog_add_trainingplan)

        if(savedInstanceState != null) {
            val error = savedInstanceState.getSerializable(ERROR_KEY) as CustomEditText.Error
            etName.showErrorMessage(error)
        }

        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.CustomDialogTheme)
            builder.setTitle(getString(R.string.trainingplan))
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

    interface IAddTrainingplan{
        fun addTrainingplan(tpName: String)
    }

    fun setListener(listener: Fragment) {
        try {
            this.listener = listener as IAddTrainingplan
        } catch (e: ClassCastException) {
            Log.i(listener.toString(), " must implement IAddTrainingplan")
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        trainingplans = DatabaseInitializer.getInstance()
            .getAllTrainingplanNames(AppDatabase.getInstance(context).trainingplanDao())
    }

    override fun onResume() {
        super.onResume()
        val d = dialog as AlertDialog
        val positiveButton = d.getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            when {
                etName.textField.text.toString().isEmpty() -> etName.showErrorMessage(CustomEditText.Error.ERROR_FIELD_EMPTY)
                etName.textField.text.toString() in trainingplans -> etName.showErrorMessage(CustomEditText.Error.TRAININGPLAN_ALREADY_EXISTS)
                else -> {
                    etName.hideErrorMessage()
                    if(listener != null) {
                        listener!!.addTrainingplan(etName.textField.text.toString())
                    }
                    dismiss()
                }
            }
        }
    }
}