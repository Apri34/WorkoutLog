package com.workoutlog.workoutlog.ui.activities

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.ui.fragments.LoginFragment
import com.workoutlog.workoutlog.ui.fragments.RegisterFragment
import com.workoutlog.workoutlog.views.CustomEditText

class LoginRegisterFromNavigationActivity : AppCompatActivity(), LoginFragment.ILogin, RegisterFragment.IRegister {

    override fun returnToLogin() {
        supportFragmentManager.popBackStack()
    }

    override fun login(email: String, password: String, stayLoggedIn: Boolean) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(getString(R.string.continue_guest), false).apply()
                PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(getString(R.string.stay_logged_in), stayLoggedIn).apply()
                startActivity(Intent(this, NavigationActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                when((it as FirebaseAuthException).errorCode) {
                    "ERROR_INVALID_EMAIL" -> {
                        fragmentLogin.showErrorMessageOnEmail(CustomEditText.Error.ERROR_WRONG_EMAIL)
                    }
                    "ERROR_USER_NOT_FOUND" -> {
                        fragmentLogin.showErrorMessageOnEmail(CustomEditText.Error.ERROR_WRONG_EMAIL)
                    }
                    "ERROR_WRONG_PASSWORD" -> {
                        fragmentLogin.showErrorMessageOnPassword(CustomEditText.Error.ERROR_WRONG_PASSWORD)
                    }
                }
            }
    }

    override fun goToRegisterScreen() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
            .addToBackStack(null)
            .replace(R.id.frame_layout_login_register_from_navigation_activity, fragmentRegister)
            .commit()
    }

    override fun continueAsGuest() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(getString(R.string.continue_guest), true).apply()
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(getString(R.string.stay_logged_in), false).apply()
        startActivity(Intent(this, NavigationActivity::class.java))
        finish()
    }

    override fun register(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(getString(R.string.continue_guest), false).apply()
                startActivity(Intent(this, NavigationActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                when((it as FirebaseAuthException).errorCode) {
                    "ERROR_EMAIL_ALREADY_IN_USE" -> {
                        fragmentRegister.showErrorMessageOnEmail(CustomEditText.Error.ERROR_EMAIL_ALREADY_USED)
                    }
                    "ERROR_INVALID_EMAIL" -> {
                        fragmentRegister.showErrorMessageOnEmail(CustomEditText.Error.ERROR_EMAIL_NOT_EXISTS)
                    }
                    "ERROR_WEAK_PASSWORD" -> {
                        fragmentRegister.showErrorMessageOnPassword(CustomEditText.Error.ERROR_PASSWORD_REQUIREMENTS_NOT_MET)
                    }
                }
            }
    }

    private lateinit var fragmentLogin: LoginFragment
    private lateinit var fragmentRegister: RegisterFragment
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register_from_navigation)
        fragmentLogin = LoginFragment()
        fragmentRegister = RegisterFragment()

        mAuth = FirebaseAuth.getInstance()
        supportFragmentManager.beginTransaction().add(R.id.frame_layout_login_register_from_navigation_activity, fragmentLogin).commit()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, NavigationActivity::class.java))
        finish()
    }
}
