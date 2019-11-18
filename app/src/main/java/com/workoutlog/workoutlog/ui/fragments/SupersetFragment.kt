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
import com.workoutlog.workoutlog.database.entities.Superset

class SupersetFragment: Fragment(){

    private var listener: ISupersetDoneListener? = null

    companion object {
        private const val KEY_SUPERSET = "superset"

        fun newInstance(superset: Superset): SupersetFragment {
            val fragment = SupersetFragment()
            val args = Bundle()
            args.putParcelable(KEY_SUPERSET, superset)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var tvSet: TextView
    private lateinit var tvExc1: TextView
    private lateinit var tvExc2: TextView
    private lateinit var etReps1: EditText
    private lateinit var etWeight1: EditText
    private lateinit var etRpe1: EditText
    private lateinit var buttonRepsUp1: ImageButton
    private lateinit var buttonRepsDown1: ImageButton
    private lateinit var buttonWeightUp1: ImageButton
    private lateinit var buttonWeightDown1: ImageButton
    private lateinit var buttonRpeUp1: ImageButton
    private lateinit var buttonRpeDown1: ImageButton
    private lateinit var etReps2: EditText
    private lateinit var etWeight2: EditText
    private lateinit var etRpe2: EditText
    private lateinit var buttonRepsUp2: ImageButton
    private lateinit var buttonRepsDown2: ImageButton
    private lateinit var buttonWeightUp2: ImageButton
    private lateinit var buttonWeightDown2: ImageButton
    private lateinit var buttonRpeUp2: ImageButton
    private lateinit var buttonRpeDown2: ImageButton
    private lateinit var buttonAddSet: ImageButton
    private lateinit var buttonFinish: ImageButton

    private lateinit var superset: Superset
    private var sets = 0
    fun getSets() = sets

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_superset, container, false)

        tvSet = view.findViewById(R.id.text_view_set_fragment_superset)
        tvExc1 = view.findViewById(R.id.text_view_exercise1_superset_fragment)
        tvExc2 = view.findViewById(R.id.text_view_exercise2_superset_fragment)
        etReps1 = view.findViewById(R.id.edit_text_reps_fragment_superset1)
        etWeight1 = view.findViewById(R.id.edit_text_weight_fragment_superset1)
        etRpe1 = view.findViewById(R.id.edit_text_rpe_fragment_superset1)
        buttonRepsUp1 = view.findViewById(R.id.button_reps_up_superset_fragment1)
        buttonRepsDown1 = view.findViewById(R.id.button_reps_down_superset_fragment1)
        buttonWeightUp1 = view.findViewById(R.id.button_weight_up_superset_fragment1)
        buttonWeightDown1 = view.findViewById(R.id.button_weight_down_superset_fragment1)
        buttonRpeUp1 = view.findViewById(R.id.button_rpe_up_superset_fragment1)
        buttonRpeDown1 = view.findViewById(R.id.button_rpe_down_superset_fragment1)
        etReps2 = view.findViewById(R.id.edit_text_reps_fragment_superset2)
        etWeight2 = view.findViewById(R.id.edit_text_weight_fragment_superset2)
        etRpe2 = view.findViewById(R.id.edit_text_rpe_fragment_superset2)
        buttonRepsUp2 = view.findViewById(R.id.button_reps_up_superset_fragment2)
        buttonRepsDown2 = view.findViewById(R.id.button_reps_down_superset_fragment2)
        buttonWeightUp2 = view.findViewById(R.id.button_weight_up_superset_fragment2)
        buttonWeightDown2 = view.findViewById(R.id.button_weight_down_superset_fragment2)
        buttonRpeUp2 = view.findViewById(R.id.button_rpe_up_superset_fragment2)
        buttonRpeDown2 = view.findViewById(R.id.button_rpe_down_superset_fragment2)
        buttonAddSet = view.findViewById(R.id.button_add_set_superset_fragment)
        buttonFinish = view.findViewById(R.id.button_finish_exercise_superset_fragment)

        etReps1.addTextChangedListener(TextWatcherNoZero(etReps1))
        etWeight1.addTextChangedListener(TextWatcherNoZero(etWeight1))
        etRpe1.addTextChangedListener(TextWatcherNoZero(etRpe1))
        etReps2.addTextChangedListener(TextWatcherNoZero(etReps2))
        etWeight2.addTextChangedListener(TextWatcherNoZero(etWeight2))
        etRpe2.addTextChangedListener(TextWatcherNoZero(etRpe2))

        superset = arguments!!.getParcelable(KEY_SUPERSET)!!

        tvSet.text = String.format(getString(R.string.number_sets), sets + 1)
        etReps1.setText(superset.reps1.toString())
        etReps2.setText(superset.reps2.toString())
        if(superset.rpe1 != null)
            etRpe1.setText(superset.rpe1!!.toString())
        if(superset.rpe2 != null)
            etRpe1.setText(superset.rpe2!!.toString())
        buttonRepsUp1.setOnClickListener {
            repsUp1()
        }
        buttonRepsDown1.setOnClickListener {
            repsDown1()
        }
        buttonWeightUp1.setOnClickListener {
            weightUp1()
        }
        buttonWeightDown1.setOnClickListener {
            weightDown1()
        }
        buttonRpeUp1.setOnClickListener {
            rpeUp1()
        }
        buttonRpeDown1.setOnClickListener {
            rpeDown1()
        }
        buttonRepsUp2.setOnClickListener {
            repsUp2()
        }
        buttonRepsDown2.setOnClickListener {
            repsDown2()
        }
        buttonWeightUp2.setOnClickListener {
            weightUp2()
        }
        buttonWeightDown2.setOnClickListener {
            weightDown2()
        }
        buttonRpeUp2.setOnClickListener {
            rpeUp2()
        }
        buttonRpeDown2.setOnClickListener {
            rpeDown2()
        }
        buttonAddSet.setOnClickListener {
            var error = false
            if(etReps1.text.isEmpty()) {
                error = true
                etReps1.isActivated = true
            } else {
                etReps1.isActivated = false
            }
            if(etWeight1.text.isEmpty()) {
                error = true
                etWeight1.isActivated = true
            } else {
                etWeight1.isActivated = false
            }
            if(etRpe1.text.isEmpty()) {
                error = true
                etRpe1.isActivated = true
            } else {
                etRpe1.isActivated = false
            }
            if(etReps2.text.isEmpty()) {
                error = true
                etReps2.isActivated = true
            } else {
                etReps2.isActivated = false
            }
            if(etWeight2.text.isEmpty()) {
                error = true
                etWeight2.isActivated = true
            } else {
                etWeight2.isActivated = false
            }
            if(etRpe2.text.isEmpty()) {
                error = true
                etRpe2.isActivated = true
            } else {
                etRpe2.isActivated = false
            }

            if(!error) {
                val sWeight1 = etWeight1.text.toString()
                sWeight1.removeSuffix(".")
                val weight1 = sWeight1.toFloat()

                val sRpe1 = etRpe1.text.toString()
                sRpe1.removeSuffix(".")
                val rpe1 = sRpe1.toFloat()

                val reps1 = etReps1.text.toString().toInt()

                val sWeight2 = etWeight2.text.toString()
                sWeight2.removeSuffix(".")
                val weight2 = sWeight2.toFloat()

                val sRpe2 = etRpe2.text.toString()
                sRpe2.removeSuffix(".")
                val rpe2 = sRpe2.toFloat()

                val reps2 = etReps2.text.toString().toInt()

                sets++
                if(listener != null)
                    listener!!.supersetDone(superset, reps1, weight1, rpe1, reps2, weight2, rpe2, sets - 1)
                tvSet.text = String.format(getString(R.string.number_sets), sets + 1)
            }
        }

        buttonFinish.setOnClickListener {
            if(listener != null) listener!!.finishSuperset(superset.eId1, superset.eId2, superset.sets, sets + 1 >= superset.sets)
        }

        return view
    }

    private fun repsUp1() {
        if(etReps1.text.isNotEmpty()) {
            var reps = etReps1.text.toString().toInt()
            reps++
            etReps1.setText(reps.toString())
        } else {
            etReps1.setText("1")
        }
    }
    private fun repsDown1() {
        if(etReps1.text.isNotEmpty()) {
            var reps = etReps1.text.toString().toInt()
            reps++
            etReps1.setText(reps.toString())
        }
    }
    private fun weightUp1() {
        if(etWeight1.text.isNotEmpty()) {
            var text = etWeight1.text.toString()
            text = text.removeSuffix(".")
            var weight = text.toFloat()
            weight++
            etWeight1.setText(weight.toString())
        } else {
            etWeight1.setText("1")
        }
    }
    private fun weightDown1() {
        if(etWeight1.text.isNotEmpty()) {
            var text = etWeight1.text.toString()
            text = text.removeSuffix(".")
            var weight = text.toFloat()
            weight--
            etWeight1.setText(weight.toString())
        }
    }
    private fun rpeUp1() {
        if(etRpe1.text.isNotEmpty()) {
            var text = etRpe1.text.toString()
            text = text.removeSuffix(".")
            var rpe = text.toFloat()
            rpe++
            etRpe1.setText(rpe.toString())
        } else {
            etRpe1.setText("1")
        }
    }
    private fun rpeDown1() {
        if(etRpe1.text.isNotEmpty()) {
            var text = etRpe1.text.toString()
            text = text.removeSuffix(".")
            var rpe = text.toFloat()
            rpe--
            etRpe1.setText(rpe.toString())
        }
    }
    private fun repsUp2() {
        if(etReps1.text.isNotEmpty()) {
            var reps = etReps1.text.toString().toInt()
            reps++
            etReps1.setText(reps.toString())
        } else {
            etReps1.setText("1")
        }
    }
    private fun repsDown2() {
        if(etReps2.text.isNotEmpty()) {
            var reps = etReps2.text.toString().toInt()
            reps++
            etReps2.setText(reps.toString())
        }
    }
    private fun weightUp2() {
        if(etWeight2.text.isNotEmpty()) {
            var text = etWeight2.text.toString()
            text = text.removeSuffix(".")
            var weight = text.toFloat()
            weight++
            etWeight2.setText(weight.toString())
        } else {
            etWeight2.setText("1")
        }
    }
    private fun weightDown2() {
        if(etWeight2.text.isNotEmpty()) {
            var text = etWeight2.text.toString()
            text = text.removeSuffix(".")
            var weight = text.toFloat()
            weight--
            etWeight2.setText(weight.toString())
        }
    }
    private fun rpeUp2() {
        if(etRpe2.text.isNotEmpty()) {
            var text = etRpe2.text.toString()
            text = text.removeSuffix(".")
            var rpe = text.toFloat()
            rpe++
            etRpe2.setText(rpe.toString())
        } else {
            etRpe2.setText("1")
        }
    }
    private fun rpeDown2() {
        if(etRpe2.text.isNotEmpty()) {
            var text = etRpe2.text.toString()
            text = text.removeSuffix(".")
            var rpe = text.toFloat()
            rpe--
            etRpe2.setText(rpe.toString())
        }
    }


    interface ISupersetDoneListener {
        fun supersetDone(superset: Superset, reps1: Int, weight1: Float, rpe1: Float, reps2: Int, weight2: Float, rpe2: Float, pos: Int)
        fun finishSuperset(eId1: Int, eId2: Int, sets: Int, isExerciseFinished: Boolean)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as ISupersetDoneListener
        } catch (e: ClassCastException) {
            Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
        }
    }
}