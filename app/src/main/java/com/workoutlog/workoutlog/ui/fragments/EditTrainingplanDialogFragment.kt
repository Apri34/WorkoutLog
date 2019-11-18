package com.workoutlog.workoutlog.ui.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.database.AppDatabase
import com.workoutlog.workoutlog.database.DatabaseInitializer
import com.workoutlog.workoutlog.database.entities.Trainingplan
import com.workoutlog.workoutlog.views.CustomEditText

class EditTrainingplanDialogFragment : DialogFragment() {

    companion object {
        const val TRAININGPLAN_NAME_KEY = "tpName"
        const val TRAININGPLAN_ID_KEY = "tpId"
        const val ERROR_KEY = "error"
    }

    private var listener: IEditTrainingplan? = null
    private lateinit var trainingplan: Trainingplan
    private lateinit var trainingplans: List<String>
    private lateinit var etName: CustomEditText

    fun setTrainingplan(trainingplan: Trainingplan) {
        this.trainingplan = trainingplan
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_add_trainingplan, null)
        etName = view.findViewById(R.id.edit_text_dialog_add_trainingplan)

        if(savedInstanceState != null) {
            trainingplan = Trainingplan(savedInstanceState.getInt(TRAININGPLAN_ID_KEY),
                savedInstanceState.getString(TRAININGPLAN_NAME_KEY))
            val error = savedInstanceState.getSerializable(ERROR_KEY) as CustomEditText.Error
            etName.showErrorMessage(error)
        }

        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.CustomDialogTheme)
            etName.setText(trainingplan.tpName)

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
        outState.putString(TRAININGPLAN_NAME_KEY, trainingplan.tpName)
        outState.putInt(TRAININGPLAN_ID_KEY, trainingplan.tpId)
        outState.putSerializable(ERROR_KEY, etName.error)
    }

    interface IEditTrainingplan{
        fun editTrainingplan(tpId: Int, tpName: String)
    }

    fun setListener(context: Context) {
        try {
            this.listener = context as IEditTrainingplan
        } catch (e: ClassCastException) {
            Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        trainingplans = DatabaseInitializer.getInstance(context)
            .getAllTrainingplanNames(AppDatabase.getInstance(context).trainingplanDao())
    }

    override fun onResume() {
        super.onResume()
        val d = dialog as AlertDialog
        val positiveButton = d.getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if(etName.textField.text.toString().isEmpty()) {
                etName.showErrorMessage(CustomEditText.Error.ERROR_FIELD_EMPTY)
            } else if (etName.textField.text.toString() in trainingplans) {
                if(etName.textField.text.toString() != trainingplan.tpName) {
                    etName.showErrorMessage(CustomEditText.Error.TRAININGPLAN_ALREADY_EXISTS)
                } else {
                    d.dismiss()
                }
            } else {
                etName.hideErrorMessage()
                if(listener != null) {
                    listener!!.editTrainingplan(trainingplan.tpId, etName.textField.text.toString())
                }
                d.dismiss()
            }
        }
    }
}