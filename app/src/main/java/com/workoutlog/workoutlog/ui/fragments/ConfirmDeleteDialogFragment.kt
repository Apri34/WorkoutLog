package com.workoutlog.workoutlog.ui.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.workoutlog.workoutlog.R

class ConfirmDeleteDialogFragment<T>: DialogFragment() {

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
            Toast.makeText(context!!, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
        }
    }
    fun setListener(context: Context) {
        try {
            listener = context as IConfirmDelete<T>
        } catch (e: ClassCastException) {
            Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if(title == null) {
            title = getString(R.string.delete_question)
        }
        if(message == null) {
            message = getString(R.string.this_cannot_be_returned)
        }

        val typedValue = TypedValue()
        val theme = context!!.theme
        theme.resolveAttribute(R.attr.customDialogTheme, typedValue, true)
        val builder = AlertDialog.Builder(context!!, typedValue.data)
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