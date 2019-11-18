package com.workoutlog.workoutlog.ui.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.workoutlog.workoutlog.R

class DeleteOrEditDialogFragment<T>: DialogFragment() {

    private var item: T? = null
    fun setItem(item: T) {
        this.item = item
    }

    private var title: String? = null
    fun setTitle(title: String) {
        this.title = title
    }

    private var listener: IDeleteOrEditDialog<T>? = null

    fun setListener(listener: Fragment) {
        try {
            this.listener = listener as IDeleteOrEditDialog<T>
        } catch (e: ClassCastException) {
            Toast.makeText(context!!, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
        }
    }
    fun setListener(context: Context) {
        try {
            @Suppress("unchecked")
            listener = context as IDeleteOrEditDialog<T>
        } catch (e: ClassCastException) {
            Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context!!, R.style.CustomDialogTheme)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.dialog_delete_or_edit, null)
        val buttonDelete = view.findViewById<TextView>(R.id.button_delete_delete_or_edit_dialog)
        buttonDelete.setOnClickListener {
            if(listener != null && item != null) {
                listener!!.delete(item!!)
            }
            dismiss()
        }
        val buttonEdit = view.findViewById<TextView>(R.id.button_edit_delete_or_edit_dialog)
        buttonEdit.setOnClickListener {
            if(listener != null && item != null) {
                listener!!.edit(item!!)
            }
            dismiss()
        }
        builder.setView(view)
        if(title != null) {
            builder.setTitle(title)
        }
        return builder.create()
    }

    interface IDeleteOrEditDialog<T> {
        fun delete(item: T)
        fun edit(item: T)
    }
}