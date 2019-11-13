package com.workoutlog.workoutlog.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.workoutlog.workoutlog.R

class SynchronizeDataDialogFragment: DialogFragment() {

    private var listener: ISynchronizeDialog? = null
    fun setListener(listener: ISynchronizeDialog) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context, R.style.CustomDialogTheme)
        builder.setTitle(R.string.data_synchronization)
            .setMessage(R.string.data_synchronization_dialog_message)
            .setPositiveButton(R.string.upload) {_,_->
                if(listener != null) listener!!.upload()
            }
            .setNegativeButton(R.string.download) {_,_->
                if(listener != null) listener!!.download()
            }
            .setNeutralButton(android.R.string.cancel) {_,_->

            }
        return builder.create()
    }

    interface ISynchronizeDialog {
        fun upload()
        fun download()
    }
}