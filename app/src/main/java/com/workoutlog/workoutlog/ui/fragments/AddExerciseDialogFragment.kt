package com.workoutlog.workoutlog.ui.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.database.AppDatabase
import com.workoutlog.workoutlog.database.DatabaseInitializer
import com.workoutlog.workoutlog.views.CustomEditText

class AddExerciseDialogFragment: DialogFragment() {

    companion object {
        const val ERROR_KEY = "error"
    }

    private var listener: IAddExercise? = null
    private lateinit var etName: CustomEditText
    private lateinit var exercises: List<String>

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_add_exercise, null)
        etName = view.findViewById(R.id.edit_text_dialog_add_exercise)

        if(savedInstanceState != null) {
            val error = savedInstanceState.getSerializable(ERROR_KEY) as CustomEditText.Error
            etName.showErrorMessage(error)
        }

        return activity?.let {
            val typedValue = TypedValue()
            val theme = context!!.theme
            theme.resolveAttribute(R.attr.customDialogTheme, typedValue, true)
            val builder = AlertDialog.Builder(context!!, typedValue.data)
            builder.setTitle(getString(R.string.exercise))
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

    interface IAddExercise{
        fun addExercise(eName: String)
    }

    fun setListener(listener: Fragment) {
        try {
            this.listener = listener as IAddExercise
        } catch (e: ClassCastException) {
            Toast.makeText(context!!, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        exercises = DatabaseInitializer.getInstance(context)
            .getAllExerciseNames(AppDatabase.getInstance(context).exerciseDao())
    }

    override fun onResume() {
        super.onResume()
        val d = dialog as AlertDialog
        val positiveButton = d.getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            when {
                etName.textField.text.toString().isEmpty() -> etName.showErrorMessage(CustomEditText.Error.ERROR_FIELD_EMPTY)
                etName.textField.text.toString() in exercises -> etName.showErrorMessage(CustomEditText.Error.EXERCISE_ALREADY_EXISTS)
                else -> {
                    etName.hideErrorMessage()
                    if(listener != null) {
                        listener!!.addExercise(etName.textField.text.toString())
                    }
                    dismiss()
                }
            }
        }
    }
}