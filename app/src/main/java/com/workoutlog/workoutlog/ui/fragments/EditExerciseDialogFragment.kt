package com.workoutlog.workoutlog.ui.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.database.AppDatabase
import com.workoutlog.workoutlog.database.DatabaseInitializer
import com.workoutlog.workoutlog.database.entities.Exercise
import com.workoutlog.workoutlog.views.CustomEditText

class EditExerciseDialogFragment : DialogFragment() {

    companion object {
        const val EXERCISE_NAME_KEY = "eName"
        const val EXERCISE_ID_KEY = "eId"
        const val ERROR_KEY = "error"
    }

    private var listener: IEditExercise? = null
    private lateinit var exercise: Exercise
    private lateinit var exercises: List<String>
    private lateinit var etName: CustomEditText

    fun setExercise(exercise: Exercise) {
        this.exercise = exercise
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_add_exercise, null)
        etName = view.findViewById(R.id.edit_text_dialog_add_exercise)

        if(savedInstanceState != null) {
            exercise = Exercise(savedInstanceState.getInt(EXERCISE_ID_KEY),
                savedInstanceState.getString(EXERCISE_NAME_KEY))
            val error = savedInstanceState.getSerializable(ERROR_KEY) as CustomEditText.Error
            etName.showErrorMessage(error)
        }

        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.CustomDialogTheme)
            etName.setText(exercise.eName)

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
        outState.putString(EXERCISE_NAME_KEY, exercise.eName)
        outState.putInt(EXERCISE_ID_KEY, exercise.eId)
        outState.putSerializable(ERROR_KEY, etName.error)
    }

    interface IEditExercise{
        fun editExercise(eId: Int, eName: String)
    }

    fun setListener(listener: Fragment) {
        try {
            this.listener = listener as IEditExercise
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
            if(etName.textField.text.toString().isEmpty()) {
                etName.showErrorMessage(CustomEditText.Error.ERROR_FIELD_EMPTY)
            } else if (etName.textField.text.toString() in exercises) {
                if(etName.textField.text.toString() != exercise.eName) {
                    etName.showErrorMessage(CustomEditText.Error.EXERCISE_ALREADY_EXISTS)
                } else {
                    d.dismiss()
                }
            } else {
                etName.hideErrorMessage()
                if(listener != null) {
                    listener!!.editExercise(exercise.eId, etName.textField.text.toString())
                }
                d.dismiss()
            }
        }
    }
}