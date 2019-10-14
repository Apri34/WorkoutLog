package com.workoutlog.workoutlog.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.workoutlog.workoutlog.R
import java.lang.ClassCastException

class SaveChangesDialogFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context, R.style.CustomDialogTheme)
        builder.setTitle("You are about to leave. Do you want to save the changes?")
            .setPositiveButton(R.string.save) {_,_->
                if(listener != null) listener!!.save()
            }
            .setNegativeButton(R.string.discard) {_,_->
                if(listener != null) listener!!.discard()
            }
        return builder.create()
    }

    interface ISaveChangesListener {
        fun save()
        fun discard()
    }

    private var listener: ISaveChangesListener? = null
    fun setListener(context: Context) {
        try {
            listener = context as ISaveChangesListener
        } catch (e: ClassCastException) {
            Log.i(context.toString(), " must implement ISaveChangesListener")
        }
    }
}