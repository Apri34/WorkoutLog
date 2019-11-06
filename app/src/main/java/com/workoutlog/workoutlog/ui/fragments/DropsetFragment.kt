package com.workoutlog.workoutlog.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.TextWatcherNoZero
import com.workoutlog.workoutlog.database.entities.Dropset

class DropsetFragment: Fragment() {

    private var listener: IDropsetDoneListener? = null

    companion object {
        private const val KEY_DROPSET = "dropset"

        fun newInstance(dropset: Dropset): DropsetFragment {
            val fragment = DropsetFragment()
            val args = Bundle()
            args.putParcelable(KEY_DROPSET, dropset)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var tvSet: TextView
    private lateinit var etReps: EditText
    private lateinit var etWeight: EditText
    private lateinit var buttonRepsUp: ImageButton
    private lateinit var buttonRepsDown: ImageButton
    private lateinit var buttonWeightUp: ImageButton
    private lateinit var buttonWeightDown: ImageButton
    private lateinit var buttonAddSet: ImageButton
    private lateinit var buttonFinish: ImageButton

    private lateinit var dropset: Dropset
    private var sets = 0
    fun getSets() = sets

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dropset, container, false)

        tvSet = view.findViewById(R.id.text_view_set_fragment_dropset)
        etReps = view.findViewById(R.id.edit_text_reps_fragment_dropset)
        etWeight = view.findViewById(R.id.edit_text_weight_fragment_dropset)
        buttonRepsUp = view.findViewById(R.id.button_reps_up_dropset_fragment)
        buttonRepsDown = view.findViewById(R.id.button_reps_down_dropset_fragment)
        buttonWeightUp = view.findViewById(R.id.button_weight_up_dropset_fragment)
        buttonWeightDown = view.findViewById(R.id.button_weight_down_dropset_fragment)
        buttonAddSet = view.findViewById(R.id.button_add_set_dropset_fragment)
        buttonFinish = view.findViewById(R.id.button_finish_exercise_dropset_fragment)

        etReps.addTextChangedListener(TextWatcherNoZero(etReps))
        etWeight.addTextChangedListener(TextWatcherNoZero(etWeight))

        dropset = arguments!!.getParcelable(KEY_DROPSET)!!

        tvSet.text = String.format(getString(R.string.number_sets), sets + 1)
        etReps.setText(dropset.reps.toString())
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

            if(!error) {
                val sWeight = etWeight.text.toString()
                sWeight.removeSuffix(".")
                val weight = sWeight.toFloat()

                val reps = etReps.text.toString().toInt()

                sets++
                if(listener != null) listener!!.dropsetDone(dropset, reps, weight, sets - 1)
                tvSet.text = String.format(getString(R.string.number_sets), sets + 1)
            }
        }

        buttonFinish.setOnClickListener {
            if(listener != null) listener!!.finishDropset(dropset.eId, dropset.sets, sets + 1 >= dropset.sets)
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

    interface IDropsetDoneListener {
        fun dropsetDone(dropset: Dropset, reps: Int, weight: Float, pos: Int)
        fun finishDropset(eId: Int, sets: Int, isExerciseFinished: Boolean)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as IDropsetDoneListener
        } catch (e: ClassCastException) {
            Log.i(context.toString(), " must implement IDropsetDoneListener")
        }
    }
}