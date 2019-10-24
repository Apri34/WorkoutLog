package com.workoutlog.workoutlog.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.TextWatcherMin1
import com.workoutlog.workoutlog.TextWatcherPerCentNoZeroMax100

class CreateCurrentTpDeloadFragment: Fragment() {

    private lateinit var editTextCycles: EditText
    private lateinit var editTextVolume: EditText
    private lateinit var editTextWeight: EditText
    private lateinit var buttonCyclesUp: ImageButton
    private lateinit var buttonCyclesDown: ImageButton
    private lateinit var buttonVolumeUp: ImageButton
    private lateinit var buttonVolumeDown: ImageButton
    private lateinit var buttonWeightUp: ImageButton
    private lateinit var buttonWeightDown: ImageButton
    private lateinit var buttonNoDeload: Button

    private var listener: IDeloadFragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_create_current_tp_deload, container, false)

        editTextCycles = view.findViewById(R.id.edit_text_deload_fragment_cycles)
        editTextVolume = view.findViewById(R.id.edit_text_deload_fragment_volume)
        editTextWeight = view.findViewById(R.id.edit_text_deload_fragment_weight)
        editTextVolume.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                (v as EditText).setSelection(v.text.toString().length - 1)
            }
        }
        editTextWeight.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                (v as EditText).setSelection(v.text.toString().length - 1)
            }
        }
        editTextCycles.addTextChangedListener(TextWatcherMin1(editTextCycles))
        editTextWeight.addTextChangedListener(TextWatcherPerCentNoZeroMax100(editTextWeight))
        editTextVolume.addTextChangedListener(TextWatcherPerCentNoZeroMax100(editTextVolume))

        editTextWeight.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                (v as EditText).setSelection(0, v.text.length)
            }
        }
        editTextVolume.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                (v as EditText).setSelection(0, v.text.length)
            }
        }

        buttonWeightUp = view.findViewById(R.id.button_deload_fragment_weight_up)
        buttonWeightDown = view.findViewById(R.id.button_deload_fragment_weight_down)
        buttonVolumeUp = view.findViewById(R.id.button_deload_fragment_volume_up)
        buttonVolumeDown = view.findViewById(R.id.button_deload_fragment_volume_down)
        buttonCyclesUp = view.findViewById(R.id.button_deload_fragment_cycles_up)
        buttonCyclesDown = view.findViewById(R.id.button_deload_fragment_cycles_down)

        buttonWeightUp.setOnClickListener {
            var percentage = editTextWeight.text.toString().replace("%", "").toInt()
            percentage += 10
            editTextWeight.setText(String.format("%s", percentage))
        }
        buttonWeightDown.setOnClickListener {
            var percentage = editTextWeight.text.toString().replace("%", "").toInt()
            percentage -= 10
            editTextWeight.setText(String.format("%s", percentage))
        }
        buttonVolumeUp.setOnClickListener {
            var percentage = editTextVolume.text.toString().replace("%", "").toInt()
            percentage += 10
            editTextVolume.setText(String.format("%s", percentage))
        }
        buttonVolumeDown.setOnClickListener {
            var percentage = editTextVolume.text.toString().replace("%", "").toInt()
            percentage -= 10
            editTextVolume.setText(String.format("%s", percentage))
        }
        buttonCyclesUp.setOnClickListener {
            var cycles = editTextCycles.text.toString().toInt()
            cycles++
            editTextCycles.setText(cycles.toString())
        }
        buttonCyclesDown.setOnClickListener {
            var cycles = editTextCycles.text.toString().toInt()
            cycles--
            editTextCycles.setText(cycles.toString())
        }

        buttonNoDeload = view.findViewById(R.id.button_no_deload_deload_fragment)
        buttonNoDeload.setOnClickListener {
            if(listener != null) listener!!.noDeload()
        }

        return view
    }

    fun getCycles() = editTextCycles.text.toString().toInt()
    fun getVolume() = editTextVolume.text.toString().replace("%", "").toInt()
    fun getWeight() = editTextWeight.text.toString().replace("%", "").toInt()

    interface IDeloadFragment {
        fun noDeload()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as IDeloadFragment
        } catch (e: ClassCastException) {
            Log.i(context.toString(), " must implement IDeloadFragment")
        }
    }
}