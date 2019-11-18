package com.workoutlog.workoutlog.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.views.CustomEditText

class LoginFragment: Fragment() {

    private lateinit var etEmail: CustomEditText
    private lateinit var etPassword: CustomEditText
    private lateinit var cbStayLoggedIn: CheckBox
    private lateinit var buttonLogin: Button
    private lateinit var tvContinueAsGuest: TextView
    private lateinit var tvRegister: TextView
    private var listener: ILogin? = null

    fun showErrorMessageOnEmail(error: CustomEditText.Error) {
        etEmail.showErrorMessage(error)
    }
    fun showErrorMessageOnPassword(error: CustomEditText.Error) {
        etPassword.showErrorMessage(error)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        etEmail = view.findViewById(R.id.edittext_fragment_login_email)
        etPassword = view.findViewById(R.id.edittext_fragment_login_password)
        cbStayLoggedIn = view.findViewById(R.id.checkbox_fragment_login_stay_logged_in)
        buttonLogin = view.findViewById(R.id.button_fragment_login_login)
        buttonLogin.setOnClickListener {
            login(etEmail.textField.text.toString(), etPassword.textField.text.toString(), cbStayLoggedIn.isChecked)
        }
        tvContinueAsGuest = view.findViewById(R.id.textview_fragment_login_continue_as_guest)
        tvContinueAsGuest.setOnClickListener {
            if(listener != null) {
                listener!!.continueAsGuest()
            }
        }
        tvRegister = view.findViewById(R.id.textview_fragment_login_register)
        tvRegister.setOnClickListener {
            if(listener != null) {
                listener!!.goToRegisterScreen()
            }
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as ILogin
        } catch (e: ClassCastException) {
            Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
        }
    }

    private fun login(email: String, password: String, stayLoggedIn: Boolean) {
        var error = false
        when {
            email.isEmpty() -> {
                error = true
                etEmail.showErrorMessage(CustomEditText.Error.ERROR_FIELD_EMPTY)
            }
            else -> etEmail.hideErrorMessage()
        }
        when {
            password.isEmpty() -> {
                error = true
                etPassword.showErrorMessage(CustomEditText.Error.ERROR_FIELD_EMPTY)
            }
            else -> etPassword.hideErrorMessage()
        }


        if(!error) {
            if(listener != null) {
                listener!!.login(email, password, stayLoggedIn)
            }
        }
    }

    interface ILogin {
        fun login(email: String, password: String, stayLoggedIn: Boolean)
        fun goToRegisterScreen()
        fun continueAsGuest()
    }
}