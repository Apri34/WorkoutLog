package com.workoutlog.workoutlog.ui.activities

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.database.AppDatabase
import com.workoutlog.workoutlog.database.DatabaseInitializer
import com.workoutlog.workoutlog.database.entities.Exercise
import com.workoutlog.workoutlog.database.entities.ExerciseDone
import com.workoutlog.workoutlog.database.entities.Trainingplan
import java.sql.Date

class NavigationActivity : AppCompatActivity() {

    private lateinit var tv: TextView
    private lateinit var buttonLogin: Button
    private lateinit var buttonLogout: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        mAuth = FirebaseAuth.getInstance()

        tv = findViewById(R.id.textview_activity_navigation_email)
        buttonLogin = findViewById(R.id.button_activity_navigation_login_register)
        buttonLogin.setOnClickListener {
            login()
        }
        buttonLogout = findViewById(R.id.button_activity_navigation_logout)
        buttonLogout.setOnClickListener {
            logout()
        }
        button = findViewById(R.id.button)
        button.setOnClickListener {
            fireBaseTest()
        }
    }

    override fun onResume() {
        super.onResume()
        getDefaultSharedPreferences(this).edit().putBoolean(getString(R.string.continue_guest), mAuth.currentUser == null).apply()

        if(mAuth.currentUser != null) {
            buttonLogin.visibility = View.GONE
            buttonLogout.visibility = View.VISIBLE
        } else {
            buttonLogout.visibility = View.GONE
            buttonLogin.visibility = View.VISIBLE
        }

        val user = mAuth.currentUser
        if(user != null) {
            tv.text = user.email!!
        } else {
            tv.text = "no user"
        }
    }

    private fun login() {
        startActivity(Intent(this, LoginRegisterFromNavigationActivity::class.java))
    }

    private fun logout() {
        if(mAuth.currentUser != null) {
            mAuth.signOut()
        }
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun fireBaseTest() {

        val ex1 = Exercise("BBGGDS")
        val ex2 = Exercise("felrkgm")
        val ex3 = Exercise("BBrfqaer")
        val ex4 = Exercise("BBfearfGGDS")
        val ex5 = Exercise("BBw4rGGDS")
        val ex6 = Exercise("BBGGDrrrrrrrrS")
        val ex7 = Exercise("BBGGBebebebeDS")
        val tp1 = Trainingplan("PPL")
        val tp2 = Trainingplan("OKUK")
        val tp3 = Trainingplan("PumperPlan")
        val tp4 = Trainingplan("GK")

        val initializer = DatabaseInitializer.getInstance()
        val db = AppDatabase.getInstance(this)

        initializer.insertExercise(db.exerciseDao(), ex1)
        initializer.insertExercise(db.exerciseDao(), ex2)
        initializer.insertExercise(db.exerciseDao(), ex3)
        initializer.insertExercise(db.exerciseDao(), ex4)
        initializer.insertExercise(db.exerciseDao(), ex5)
        initializer.insertExercise(db.exerciseDao(), ex6)
        initializer.insertExercise(db.exerciseDao(), ex7)

        initializer.insertTrainingplan(db.trainingplanDao(), tp1)
        initializer.insertTrainingplan(db.trainingplanDao(), tp2)
        initializer.insertTrainingplan(db.trainingplanDao(), tp3)
        initializer.insertTrainingplan(db.trainingplanDao(), tp4)
    }
}
