package com.workoutlog.workoutlog.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.TextWatcherNoZero
import com.workoutlog.workoutlog.database.entities.Normal

class NormalExerciseFragment: Fragment() {

    private var listener: INormalSetDoneListener? = null

    companion object {
        private const val KEY_NORMAL = "normal"

        fun newInstance(normal: Normal): NormalExerciseFragment {
            val fragment = NormalExerciseFragment()
            val args = Bundle()
            args.putParcelable(KEY_NORMAL, normal)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var tvSet: TextView
    private lateinit var etReps: EditText
    private lateinit var etWeight: EditText
    private lateinit var etRpe: EditText
    private lateinit var buttonRepsUp: ImageButton
    private lateinit var buttonRepsDown: ImageButton
    private lateinit var buttonWeightUp: ImageButton
    private lateinit var buttonWeightDown: ImageButton
    private lateinit var buttonRpeUp: ImageButton
    private lateinit var buttonRpeDown: ImageButton
    private lateinit var buttonAddSet: ImageButton
    private lateinit var buttonFinish: ImageButton

    private lateinit var normal: Normal
    private var sets = 0
    fun getSets() = sets

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_normal_exercise, container, false)

        tvSet = view.findViewById(R.id.text_view_set_fragment_normal_exercise)
        etReps = view.findViewById(R.id.edit_text_reps_fragment_normal_exercise)
        etWeight = view.findViewById(R.id.edit_text_weight_fragment_normal_exercise)
        etRpe = view.findViewById(R.id.edit_text_rpe_fragment_normal_exercise)
        buttonRepsUp = view.findViewById(R.id.button_reps_up_normal_exercise_fragment)
        buttonRepsDown = view.findViewById(R.id.button_reps_down_normal_exercise_fragment)
        buttonWeightUp = view.findViewById(R.id.button_weight_up_normal_exercise_fragment)
        buttonWeightDown = view.findViewById(R.id.button_weight_down_normal_exercise_fragment)
        buttonRpeUp = view.findViewById(R.id.button_rpe_up_normal_exercise_fragment)
        buttonRpeDown = view.findViewById(R.id.button_rpe_down_normal_exercise_fragment)
        buttonAddSet = view.findViewById(R.id.button_add_set_normal_exercise_fragment)
        buttonFinish = view.findViewById(R.id.button_finish_exercise_normal_exercise_fragment)

        etReps.addTextChangedListener(TextWatcherNoZero(etReps))
        etWeight.addTextChangedListener(TextWatcherNoZero(etWeight))
        etRpe.addTextChangedListener(TextWatcherNoZero(etRpe))

        normal = arguments!!.getParcelable(KEY_NORMAL)!!

        tvSet.text = String.format(getString(R.string.number_sets), sets + 1)
        etReps.setText(normal.reps.toString())
        if(normal.rpe != null)
            etRpe.setText(normal.rpe!!.toString())
        buttonRepsUp.setOnClickListener {
            repsUp()
        }
        buttonRepsDown.setOnClickListener {
            repsDown()
        }
        buttonWeightUp.setOnClickListener {
            weightUp()
        }
        buttonWeightDown.setOnClickListener {
            weightDown()
        }
        buttonRpeUp.setOnClickListener {
            rpeUp()
        }
        buttonRpeDown.setOnClickListener {
            rpeDown()
        }
        buttonAddSet.setOnClickListener {
            var error = false
            if(etReps.text.isEmpty()) {
                error = true
                etReps.isActivated = true
            } else {
                etReps.isActivated = false
            }
            if(etWeight.text.isEmpty()) {
                error = true
                etWeight.isActivated = true
            } else {
                etWeight.isActivated = false
            }
            if(etRpe.text.isEmpty()) {
                error = true
                etRpe.isActivated = true
            } else {
                etRpe.isActivated = false
            }

            if(!error) {
                val sWeight = etWeight.text.toString()
                sWeight.removeSuffix(".")
                val weight = sWeight.toFloat()

                val sRpe = etRpe.text.toString()
                sRpe.removeSuffix(".")
                val rpe = sRpe.toFloat()

                val reps = etReps.text.toString().toInt()

                sets++
                if(listener != null) listener!!.normalSetDone(normal, reps, weight, rpe, sets - 1)
                tvSet.text = String.format(getString(R.string.number_sets), sets + 1)
            }
        }

        buttonFinish.setOnClickListener {
            if(listener != null) listener!!.finishNormal(normal.eId, normal.sets, sets + 1 >= normal.sets)
        }

        return view
    }

    private fun repsUp() {
        if(etReps.text.isNotEmpty()) {
            var reps = etReps.text.toString().toInt()
            reps++
            etReps.setText(reps.toString())
        } else {
            etReps.setText("1")
        }
    }
    private fun repsDown() {
        if(etReps.text.isNotEmpty()) {
            var reps = etReps.text.toString().toInt()
            reps++
            etReps.setText(reps.toString())
        }
    }
    private fun weightUp() {
        if(etWeight.text.isNotEmpty()) {
            var text = etWeight.text.toString()
            text = text.removeSuffix(".")
            var weight = text.toFloat()
            weight++
            etWeight.setText(weight.toString())
        } else {
            etWeight.setText("1")
        }
    }
    private fun weightDown() {
        if(etWeight.text.isNotEmpty()) {
            var text = etWeight.text.toString()
            text = text.removeSuffix(".")
            var weight = text.toFloat()
            weight--
            etWeight.setText(weight.toString())
        }
    }
    private fun rpeUp() {
        if(etRpe.text.isNotEmpty()) {
            var text = etRpe.text.toString()
            text = text.removeSuffix(".")
            var rpe = text.toFloat()
            rpe++
            etRpe.setText(rpe.toString())
        } else {
            etRpe.setText("1")
        }
    }
    private fun rpeDown() {
        if(etRpe.text.isNotEmpty()) {
            var text = etRpe.text.toString()
            text = text.removeSuffix(".")
            var rpe = text.toFloat()
            rpe--
            etRpe.setText(rpe.toString())
        }
    }

    interface INormalSetDoneListener {
        fun normalSetDone(normal: Normal, reps: Int, weight: Float, rpe: Float, pos: Int)
        fun finishNormal(eId: Int, sets: Int, isExerciseFinished: Boolean)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as INormalSetDoneListener
        } catch (e: ClassCastException) {
            Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
        }
    }
}