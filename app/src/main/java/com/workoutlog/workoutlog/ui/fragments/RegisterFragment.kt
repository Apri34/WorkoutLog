package com.workoutlog.workoutlog.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.views.CustomEditText
import java.lang.ClassCastException

class RegisterFragment: Fragment() {

    private lateinit var etEmail: CustomEditText
    private lateinit var etPassword: CustomEditText
    private lateinit var etConfirmPassword: CustomEditText
    private lateinit var buttonRegister: Button
    private lateinit var buttonReturn: ImageButton

    private var listener: IRegister? = null

    fun showErrorMessageOnEmail(error: CustomEditText.Error) {
        etEmail.showErrorMessage(error)
    }
    fun showErrorMessageOnPassword(error: CustomEditText.Error) {
        etPassword.showErrorMessage(error)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        etEmail = view.findViewById(R.id.edittext_fragment_register_email)
        etPassword = view.findViewById(R.id.edittext_fragment_register_password)
        etConfirmPassword = view.findViewById(R.id.edittext_fragment_register_confirm_password)
        buttonRegister = view.findViewById(R.id.button_fragment_register_register)
        buttonRegister.setOnClickListener {
            register(etEmail.textField.text.toString(), etPassword.textField.text.toString(), etConfirmPassword.textField.text.toString())
        }
        buttonReturn = view.findViewById(R.id.button_fragment_register_return)
        buttonReturn.setOnClickListener {
            if(listener != null) {
                listener!!.returnToLogin()
            }
        }

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            listener = context as IRegister
        } catch (e: ClassCastException) {
            Log.i(context.toString(), " must implement IRegister")
        }
    }

    private fun register(email: String, password: String, confPassword: String) {
        var error = false

        when {
            email.isEmpty() -> {
                error = true
                etEmail.showErrorMessage(CustomEditText.Error.ERROR_FIELD_EMPTY)
            }
            false -> {//TODO Email does not exist
                error = true
                etEmail.showErrorMessage(CustomEditText.Error.ERROR_EMAIL_NOT_EXISTS)
            }
            else ->
                etEmail.hideErrorMessage()
        }
        when {
            password.isEmpty() -> {
                error = true
                etPassword.showErrorMessage(CustomEditText.Error.ERROR_FIELD_EMPTY)
            }
            else -> {
                etPassword.hideErrorMessage()
            }
        }
        when {
            confPassword.isEmpty() -> {
                error = true
                etConfirmPassword.showErrorMessage(CustomEditText.Error.ERROR_FIELD_EMPTY)
            }
            password != confPassword -> {
                error = true
                etConfirmPassword.showErrorMessage(CustomEditText.Error.ERROR_CONFIRM_PASSWORD_WRONG)
                etPassword.showErrorMessage(CustomEditText.Error.NO_ERROR_MESSAGE)
            }
            else -> {
                etConfirmPassword.hideErrorMessage()
            }
        }


        if(!error) {
            if(listener != null) {
                listener!!.register(email, password)
            }
        }
    }

    interface IRegister {
        fun register(email: String, password: String)
        fun returnToLogin()
    }
}