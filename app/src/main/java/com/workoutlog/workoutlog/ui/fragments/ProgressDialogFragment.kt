package com.workoutlog.workoutlog.ui.fragments

import android.annotation.SuppressLint
import androidx.appcompat.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.workoutlog.workoutlog.R

class ProgressDialogFragment: DialogFragment() {

    private var listener: ITryAgainListener? = null
    fun setListener(listener: ITryAgainListener) {
        this.listener = listener
    }

    private lateinit var buttonCancel: Button
    private lateinit var buttonTryAgain: Button
    private lateinit var textViewMessage: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var dialog: AlertDialog

    companion object {
        private const val KEY_TITLE = "title"

        fun newInstance(title: String): ProgressDialogFragment {
            val fragment = ProgressDialogFragment()
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            fragment.arguments = args
            return fragment
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = arguments?.getString(KEY_TITLE) ?: getString(R.string.loading)
        val typedValue = TypedValue()
        val theme = context!!.theme
        theme.resolveAttribute(R.attr.customDialogTheme, typedValue, true)
        val builder = AlertDialog.Builder(context!!, typedValue.data)
        val view = LayoutInflater.from(context).inflate(R.layout.progress_dialog, null)
        buttonCancel = view.findViewById(R.id.button_cancel)
        buttonTryAgain = view.findViewById(R.id.button_try_again)
        textViewMessage = view.findViewById(R.id.text_view_progress_dialog_message)
        progressBar = view.findViewById(R.id.progress_bar_progress_dialog)
        builder.setTitle(title)
            .setView(view)
        dialog =  builder.create()
        return dialog
    }

    fun setError(title: String, message: String) {
        textViewMessage.text = message
        dialog.setTitle(title)
        buttonTryAgain.setOnClickListener {
            if(listener != null) listener!!.tryAgain()
            dismiss()
        }
        buttonCancel.setOnClickListener {
            if(listener != null) listener!!.cancelOperation()
            dismiss()
        }
        textViewMessage.visibility = View.VISIBLE
        buttonTryAgain.visibility = View.VISIBLE
        buttonCancel.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    fun setCancelable(message: String) {
        textViewMessage.text = message
        buttonCancel.setOnClickListener {
            if(listener != null) listener!!.cancelOperation()
            dismiss()
        }
        textViewMessage.visibility = View.VISIBLE
        buttonTryAgain.visibility = View.GONE
        buttonCancel.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE
    }

    interface ITryAgainListener {
        fun tryAgain()
        fun cancel()
        fun cancelOperation()
    }
}