package com.workoutlog.workoutlog.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.application.WorkoutLog
import com.workoutlog.workoutlog.database.DatabaseSynchronizer
import com.workoutlog.workoutlog.ui.fragments.*
import com.workoutlog.workoutlog.views.CustomEditText

class LoginRegisterFromNavigationActivity : AppCompatActivity(), LoginFragment.ILogin, RegisterFragment.IRegister {

    override fun returnToLogin() {
        supportFragmentManager.popBackStack()
    }

    override fun login(email: String, password: String, stayLoggedIn: Boolean) {
        var isRegistrationDone = false
        var isLoggedIn = false
        var isCancelled = false
        val dialog = ProgressDialogFragment.newInstance(getString(R.string.login_))
        dialog.setListener(object : ProgressDialogFragment.ITryAgainListener{
            override fun tryAgain() {
                if(isLoggedIn) {
                    mAuth.signOut()
                    login(email, password, stayLoggedIn)
                } else {
                    login(email, password, stayLoggedIn)
                }
            }

            override fun cancel() {
                isCancelled = true
            }

            override fun cancelOperation() {
                isCancelled = true
            }
        })
        dialog.show(supportFragmentManager, "login")
        Handler().postDelayed({
            if(!isRegistrationDone)
                dialog.setCancelable(getString(R.string.this_operation_is_taking_very_long))
        }, 15000)
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                if(isCancelled) return@addOnSuccessListener
                isRegistrationDone = true
                isLoggedIn = true
                if(mAuth.currentUser!!.isEmailVerified) {
                    if(isCancelled) return@addOnSuccessListener
                    databaseSynchronizer.loginUser(mAuth.currentUser!!, this)
                    PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean(getString(R.string.continue_guest), false).apply()
                    PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean(getString(R.string.stay_logged_in), stayLoggedIn).apply()
                    startActivity(Intent(this, NavigationActivity::class.java))
                    finish()
                } else {
                    val verifDialog = SendVerificationDialogFragment.newInstance(getString(R.string.email_not_verified), getString(R.string.send_verification_email_))
                    verifDialog.setListener(object : SendVerificationDialogFragment.ISendVerification{
                        override fun signOut() {
                            mAuth.signOut()
                        }

                        override fun sendVerification() {
                            mAuth.currentUser!!.sendEmailVerification()
                            mAuth.signOut()
                        }
                    })
                    dialog.dismiss()
                    verifDialog.show(supportFragmentManager, "verifDialog")
                }
            }
            .addOnFailureListener { e->
                if(isCancelled) return@addOnFailureListener
                isRegistrationDone = true
                if(e !is FirebaseAuthException) {
                    dialog.setError(getString(R.string.error), getString(R.string.something_went_wrong_check_internet))
                } else {
                    when (e.errorCode) {
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
                    dialog.dismiss()
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
        var isRegistrationDone = false
        var isCancelled = false
        var isRegistered = false
        val dialog = ProgressDialogFragment.newInstance(getString(R.string.register_))
        dialog.setListener(object: ProgressDialogFragment.ITryAgainListener {
            override fun tryAgain() {
                if(isRegistered) {
                    mAuth.currentUser?.sendEmailVerification()
                        ?.addOnSuccessListener {
                            dialog.dismiss()
                            MessageDialogFragment.newInstance(getString(R.string.successfully_registered)).show(supportFragmentManager, "successfullyRegistered")
                            supportFragmentManager.popBackStack()
                        }
                } else {
                    register(email, password)
                }
            }

            override fun cancel() {
                isCancelled = true
            }

            override fun cancelOperation() {
                isCancelled = true
            }
        })
        dialog.show(supportFragmentManager, "register")
        Handler().postDelayed({
            if(!isRegistrationDone) {
                dialog.setCancelable(getString(R.string.this_operation_is_taking_very_long))
            }
        }, 15000)
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                if(isCancelled) return@addOnSuccessListener
                isRegistrationDone = true
                isRegistered = true
                mAuth.currentUser!!.sendEmailVerification()
                    .addOnSuccessListener {
                        dialog.dismiss()
                        MessageDialogFragment.newInstance(getString(R.string.successfully_registered)).show(supportFragmentManager, "successfullyRegistered")
                        supportFragmentManager.popBackStack()
                    }
                    .addOnFailureListener {
                        dialog.setError(getString(R.string.error), getString(R.string.something_went_wrong_sending_verification_email))
                    }
                mAuth.signOut()
                isRegistered = true
            }
            .addOnFailureListener {e->
                if(isCancelled) return@addOnFailureListener
                isRegistrationDone = true
                if(e !is FirebaseAuthException) {
                    dialog.setError(getString(R.string.error), getString(R.string.something_went_wrong_check_internet))
                } else {
                    when (e.errorCode) {
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
                    dialog.dismiss()
                }
            }
    }

    private lateinit var fragmentLogin: LoginFragment
    private lateinit var fragmentRegister: RegisterFragment
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseSynchronizer: DatabaseSynchronizer
    private lateinit var mWorkoutLog: WorkoutLog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorSecondaryDark)
        }
        setContentView(R.layout.activity_login_register_from_navigation)
        mWorkoutLog = this.applicationContext as WorkoutLog
        fragmentLogin = LoginFragment()
        fragmentRegister = RegisterFragment()

        mAuth = FirebaseAuth.getInstance()
        databaseSynchronizer = DatabaseSynchronizer.getInstance(this)
        supportFragmentManager.beginTransaction().add(R.id.frame_layout_login_register_from_navigation_activity, fragmentLogin).commit()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, NavigationActivity::class.java))
        finish()
    }

    override fun onResume() {
        super.onResume()
        mWorkoutLog.currentActivity = this
    }

    override fun onPause() {
        clearReferences()
        super.onPause()
    }

    override fun onDestroy() {
        clearReferences()
        super.onDestroy()
    }

    private fun clearReferences() {
        val currActivity = mWorkoutLog.currentActivity
        if (this == currActivity)
            mWorkoutLog.currentActivity = null
    }
}
