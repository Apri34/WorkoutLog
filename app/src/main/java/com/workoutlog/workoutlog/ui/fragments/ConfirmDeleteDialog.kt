package com.workoutlog.workoutlog.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.workoutlog.workoutlog.R
import java.lang.ClassCastException

class ConfirmDeleteDialog<T>: DialogFragment() {

    private var item: T? = null
    fun setItem(item: T) {
        this.item = item
    }
    private var title: String? = null
    fun setTitle(title: String) {
        this.title = title
    }
    private var message: String? = null
    fun setMessage(message: String) {
        this.message = message
    }
    private var id: Int? = null
    fun setConfirmDeleteDialogId(id: Int) {
        this.id = id
    }
    private var listener: IConfirmDelete<T>? = null
    fun setListener(listener: Fragment) {
        try {
            this.listener = listener as IConfirmDelete<T>
        } catch (e: ClassCastException) {
            Log.i(listener.toString(), " must implement IConfirmDelete")
        }
    }
    fun setListener(context: Context) {
        try {
            listener = context as IConfirmDelete<T>
        } catch (e: ClassCastException) {
            Log.i(listener.toString(), " must implement IConfirmDelete")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if(title == null) {
            title = getString(R.string.delete_question)
        }
        if(message == null) {
            message = "This cannot be returned"
        }

        val builder = AlertDialog.Builder(context, R.style.CustomDialogTheme)
        builder.setTitle(title)
            .setMessage(message)
            .setNegativeButton(android.R.string.no) {_,_->}
            .setPositiveButton(android.R.string.yes) {_,_->
                if(listener != null && item != null) {
                    listener!!.delete(id, item!!)
                }
            }
        return builder.create()
    }

    interface IConfirmDelete<T> {
        fun delete(id: Int?, item: T)
    }
}