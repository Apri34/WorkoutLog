package com.workoutlog.workoutlog.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.TextWatcherNoZero
import com.workoutlog.workoutlog.database.entities.Dropset
import com.workoutlog.workoutlog.database.entities.Exercise
import com.workoutlog.workoutlog.database.entities.Normal
import com.workoutlog.workoutlog.database.entities.Superset

class ExercisesInRoutineAdapter(private val context: Context, private var exercises: MutableList<Exercise>, private var mNormals: MutableList<Normal>, private var mSupersets: MutableList<Superset>, private var mDropsets: MutableList<Dropset>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun addNormal(normal: Normal) {
        mNormals.add(normal)
        setEditableItem(normal.posInRoutine)
    }

    fun addSuperset(superset: Superset) {
        mSupersets.add(superset)
        setEditableItem(superset.posInRoutine)
    }

    fun addDropset(dropset: Dropset) {
        mDropsets.add(dropset)
        setEditableItem(dropset.posInRoutine)
    }

    fun getNormals(): List<Normal> = mNormals
    fun getSupersets(): List<Superset> = mSupersets
    fun getDropsets(): List<Dropset> = mDropsets

    fun moveItem(from: Int, to: Int) {
        mNormals.forEach {
            if(it.posInRoutine == from) {
                it.posInRoutine = to
            } else {
                if(from < to && it.posInRoutine in (from + 1)..to) {
                    it.reducePosInRoutine()
                } else if(from > to && it.posInRoutine in to until from) {
                    it.increasePosInRoutine()
                }
            }
        }
        mSupersets.forEach {
            if(it.posInRoutine == from) {
                it.posInRoutine = to
            } else {
                if(from < to && it.posInRoutine in (from + 1)..to) {
                    it.reducePosInRoutine()
                } else if(from > to && it.posInRoutine in to until from) {
                    it.increasePosInRoutine()
                }
            }
        }
        mDropsets.forEach {
            if(it.posInRoutine == from) {
                it.posInRoutine = to
            } else {
                if(from < to && it.posInRoutine in (from + 1)..to) {
                    it.reducePosInRoutine()
                } else if(from > to && it.posInRoutine in to until from) {
                    it.increasePosInRoutine()
                }
            }
        }
    }

    fun deleteNormal(normal: Normal) {
        mNormals.remove(normal)
        reducePosInRoutine(normal.posInRoutine + 1)
        notifyDataSetChanged()
        setEditableItem(-1)
    }

    fun deleteSuperset(superset: Superset) {
        mSupersets.remove(superset)
        reducePosInRoutine(superset.posInRoutine + 1)
        notifyDataSetChanged()
        setEditableItem(-1)

    }

    fun deleteDropset(dropset: Dropset) {
        mDropsets.remove(dropset)
        reducePosInRoutine(dropset.posInRoutine + 1)
        notifyDataSetChanged()
        setEditableItem(-1)
    }

    companion object {
        private const val TYPE_ERROR = -1
        private const val TYPE_NORMAL = 0
        private const val TYPE_SUPERSET = 1
        private const val TYPE_DROPSET = 2
    }

    private var listener: IExerciseInRoutineAdapter? = null
    private var editableItem = -1
    fun setListener(context: Context) {
        try {
            listener = context as IExerciseInRoutineAdapter
        } catch (e: ClassCastException) {}
    }
    fun setEditableItem(position: Int) {
        editableItem = position
        notifyDataSetChanged()
        if(listener != null) {
            listener!!.onEditableChanged()
        }
    }
    fun getEditableItem(): Int = editableItem

    private fun reducePosInRoutine(start: Int) {
        mNormals.forEach {
            if(it.posInRoutine >= start) it.reducePosInRoutine()
        }
        mSupersets.forEach {
            if(it.posInRoutine >= start) it.reducePosInRoutine()
        }
        mDropsets.forEach {
            if(it.posInRoutine >= start) it.reducePosInRoutine()
        }
    }

    inner class ErrorViewHolder(private val textView: TextView) : RecyclerView.ViewHolder(textView) {
        fun bind(message: String) {
            textView.text = message
        }
    }

    inner class NormalViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var textViewReps: TextView
        private lateinit var textViewSets: TextView
        private lateinit var textViewRpe: TextView
        private lateinit var textViewBreak: TextView
        private lateinit var textViewNumber: TextView
        private lateinit var spinner: Spinner
        private lateinit var buttonFinish: ImageButton
        private lateinit var buttonEdit: ImageButton
        private lateinit var buttonDelete: ImageButton
        private lateinit var etSets: EditText
        private lateinit var etReps: EditText
        private lateinit var etRpe: EditText
        private lateinit var etBreak: EditText
        private lateinit var dragHandler: ImageView
        private lateinit var cardView: CardView
        private var posInRoutine = 0
        private var routineId = 0

        @SuppressLint("ClickableViewAccessibility")
        fun bind(normal: Normal) {

            spinner = view.findViewById(R.id.spinner_exercises_view_normal)
            val exerciseList = ArrayList<String>()
            val exerciseIdList = ArrayList<Int>()
            exercises.forEach {
                exerciseList.add(it.eName)
                exerciseIdList.add(it.eId)
            }
            exerciseList.add(context.getString(R.string.choose_exercise))
            val adapter = HintAdapter(context, R.layout.spinner_item_exercises_in_routine, exerciseList)
            spinner.adapter = adapter
            if(normal.eId != 0) {
                val position = exerciseIdList.indexOf(normal.eId)
                spinner.setSelection(position)
            } else {
                spinner.setSelection(spinner.adapter.count)
            }

            textViewBreak = view.findViewById(R.id.text_view_break_view_normal)
            textViewReps = view.findViewById(R.id.text_view_reps_view_normal)
            textViewSets = view.findViewById(R.id.text_view_sets_view_normal)
            textViewRpe = view.findViewById(R.id.text_view_rpe_view_normal)
            buttonFinish = view.findViewById(R.id.button_finish_card_view_normal)
            buttonEdit = view.findViewById(R.id.button_edit_card_view_normal)
            buttonDelete = view.findViewById(R.id.button_delete_card_view_normal)
            etSets = view.findViewById(R.id.edit_text_sets_view_normal)
            etReps = view.findViewById(R.id.edit_text_reps_view_normal)
            etRpe = view.findViewById(R.id.edit_text_rpe_view_normal)
            etBreak = view.findViewById(R.id.edit_text_break_view_normal)
            dragHandler = view.findViewById(R.id.image_view_drag_view_normal)
            cardView = view.findViewById(R.id.card_view_normal)
            posInRoutine = normal.posInRoutine
            routineId = normal.rId
            setEditable(posInRoutine == editableItem)
            textViewNumber = view.findViewById(R.id.text_view_exercise_number_view_normal)
            textViewNumber.text = String.format(context.getString(R.string.exercise_number), (posInRoutine + 1).toString())
            etSets.addTextChangedListener(TextWatcherNoZero(etSets))
            etBreak.addTextChangedListener(TextWatcherNoZero(etBreak))
            etRpe.addTextChangedListener(TextWatcherNoZero(etRpe))
            etReps.addTextChangedListener(TextWatcherNoZero(etReps))

             etSets.setText(getString(normal.sets))
             etReps.setText(getString(normal.reps))
             etRpe.setText(getString(normal.rpe))
             etBreak.setText(getString(normal.breakInSeconds))

             buttonFinish.setOnClickListener {
                 var error = false

                 if(etSets.text.isEmpty() || etSets.text.toString() == "0") {
                     error = true
                     etSets.isActivated = true
                 } else {
                     etSets.isActivated = false
                 }
                 if(etReps.text.isEmpty() || etReps.text.toString() == "0") {
                     error = true
                     etReps.isActivated = true
                 } else {
                     etReps.isActivated = false
                 }
                 if (spinner.selectedItem != null && spinner.selectedItem == context.getString(R.string.choose_exercise)) {
                     error = true
                     spinner.isActivated = true
                 } else {
                     spinner.isActivated = false
                 }

                 if(!error) {
                     var eId = 0
                     exercises.forEach {
                         if(spinner.selectedItem == it.eName) eId = it.eId
                     }
                     val newNormal = Normal(normal.nId, eId, getInt(etSets.text.toString())!!, getInt(etReps.text.toString())!!, getInt(etBreak.text.toString()), getInt(etRpe.text.toString()), posInRoutine, routineId)

                     val index = mNormals.indexOf(normal)
                     mNormals.remove(normal)
                     mNormals.add(index, newNormal)
                     notifyDataSetChanged()

                     setEditableItem(-1)
                 }
             }

             buttonEdit.setOnClickListener {
                 setEditableItem(posInRoutine)
             }

             buttonDelete.setOnClickListener {
                 if(listener != null)
                     listener!!.deleteNormal(normal)
             }
            
            dragHandler.setOnTouchListener { _, event ->
                if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                    if (listener != null)
                        listener!!.startDragging(this)
                }
                return@setOnTouchListener true
            }
        }

        private fun setEditable(editable: Boolean) {
            buttonFinish.isClickable = editable
            buttonFinish.isEnabled = editable
            if(editable) {
                buttonFinish.visibility = View.VISIBLE
            } else {
                buttonFinish.visibility = View.INVISIBLE
            }
            if(editableItem != -1) {
                buttonEdit.isClickable = false
                buttonEdit.isEnabled = false
                buttonEdit.visibility = View.INVISIBLE
                dragHandler.visibility = View.INVISIBLE
                dragHandler.isClickable = false
                dragHandler.isEnabled = false
                if(editable) {
                    buttonDelete.isClickable = true
                    buttonDelete.isEnabled = true
                    buttonDelete.visibility = View.VISIBLE
                } else {
                    buttonDelete.isClickable = false
                    buttonDelete.isEnabled = false
                    buttonDelete.visibility = View.INVISIBLE
                }
            } else {
                buttonEdit.isClickable = true
                buttonEdit.isEnabled = true
                buttonEdit.visibility = View.VISIBLE
                buttonDelete.isClickable = true
                buttonDelete.isEnabled = true
                buttonDelete.visibility = View.VISIBLE
                dragHandler.visibility = View.VISIBLE
                dragHandler.isClickable = true
                dragHandler.isEnabled = true
            }

            spinner.isEnabled = editable
            spinner.isClickable = editable
            etSets.isClickable = editable
            etSets.isEnabled = editable
            etBreak.isClickable = editable
            etBreak.isEnabled = editable
            etReps.isClickable = editable
            etReps.isEnabled = editable
            etRpe.isClickable = editable
            etRpe.isEnabled = editable
            cardView.isSelected = editable
            textViewBreak.isEnabled = editable
            textViewReps.isEnabled = editable
            textViewRpe.isEnabled = editable
            textViewSets.isEnabled = editable
        }
    }

    inner class SupersetViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var textViewReps1: TextView
        private lateinit var textViewReps2: TextView
        private lateinit var textViewRpe2: TextView
        private lateinit var textViewSets: TextView
        private lateinit var textViewRpe1: TextView
        private lateinit var textViewBreak: TextView
        private lateinit var textViewNumber: TextView
        private lateinit var spinner1: Spinner
        private lateinit var spinner2: Spinner
        private lateinit var buttonFinish: ImageButton
        private lateinit var buttonEdit: ImageButton
        private lateinit var buttonDelete: ImageButton
        private lateinit var etSets: EditText
        private lateinit var etReps1: EditText
        private lateinit var etReps2: EditText
        private lateinit var etRpe1: EditText
        private lateinit var etRpe2: EditText
        private lateinit var etBreak: EditText
        private lateinit var dragHandler: ImageView
        private lateinit var cardView: CardView

        @SuppressLint("ClickableViewAccessibility")
        fun bind(superset: Superset) {
            spinner1 = view.findViewById(R.id.spinner_exercises1_view_superset)
            spinner2 = view.findViewById(R.id.spinner_exercises2_view_superset)
            val exerciseList = ArrayList<String>()
            val exerciseIdList = ArrayList<Int>()
            exercises.forEach {
                exerciseList.add(it.eName)
                exerciseIdList.add(it.eId)
            }
            exerciseList.add(context.getString(R.string.choose_exercise))
            val adapter = HintAdapter(context, R.layout.spinner_item_exercises_in_routine, exerciseList)
            spinner1.adapter = adapter
            spinner2.adapter = adapter
            if(superset.eId1 != 0) {
                val position1 = exerciseIdList.indexOf(superset.eId1)
                val position2 = exerciseIdList.indexOf(superset.eId2)
                spinner1.setSelection(position1)
                spinner2.setSelection(position2)
            } else {
                spinner1.setSelection(spinner1.adapter.count)
                spinner2.setSelection(spinner2.adapter.count)
            }

            textViewReps1 = view.findViewById(R.id.text_view_reps1_view_superset)
            textViewReps2 = view.findViewById(R.id.text_view_reps2_view_superset)
            textViewSets = view.findViewById(R.id.text_view_sets_view_superset)
            textViewRpe1 = view.findViewById(R.id.text_view_rpe1_view_superset)
            textViewRpe2 = view.findViewById(R.id.text_view_rpe2_view_superset)
            textViewBreak = view.findViewById(R.id.text_view_break_view_superset)
            buttonFinish = view.findViewById(R.id.button_finish_card_view_superset)
            buttonEdit = view.findViewById(R.id.button_edit_card_view_superset)
            buttonDelete = view.findViewById(R.id.button_delete_card_view_superset)
            etSets = view.findViewById(R.id.edit_text_sets_view_superset)
            etReps1 = view.findViewById(R.id.edit_text_reps1_view_superset)
            etReps2 = view.findViewById(R.id.edit_text_reps2_view_superset)
            etRpe1 = view.findViewById(R.id.edit_text_rpe1_view_superset)
            etRpe2 = view.findViewById(R.id.edit_text_rpe2_view_superset)
            etBreak = view.findViewById(R.id.edit_text_break_view_superset)
            dragHandler = view.findViewById(R.id.image_view_drag_view_superset)
            cardView = view.findViewById(R.id.card_view_superset)
            etSets.setText(getString(superset.sets))
            etReps1.setText(getString(superset.reps1))
            etReps2.setText(getString(superset.reps2))
            etRpe1.setText(getString(superset.rpe1))
            etRpe2.setText(getString(superset.rpe2))
            etBreak.setText(getString(superset.breakInSeconds))
            setEditable(superset.posInRoutine == editableItem)
            textViewNumber = view.findViewById(R.id.text_view_exercise_number_view_superset)
            textViewNumber.text = String.format(context.getString(R.string.exercise_number_superset), (superset.posInRoutine + 1).toString())
            etSets.addTextChangedListener(TextWatcherNoZero(etSets))
            etBreak.addTextChangedListener(TextWatcherNoZero(etBreak))
            etRpe1.addTextChangedListener(TextWatcherNoZero(etRpe1))
            etReps1.addTextChangedListener(TextWatcherNoZero(etReps1))
            etRpe2.addTextChangedListener(TextWatcherNoZero(etRpe2))
            etReps2.addTextChangedListener(TextWatcherNoZero(etReps2))

            buttonFinish.setOnClickListener {

                var error = false
                if(etSets.text.isEmpty() || etSets.text.toString() == "0") {
                    error = true
                    etSets.isActivated = true
                } else {
                    etSets.isActivated = false
                }
                if (etReps1.text.isEmpty() || etReps1.text.toString() == "0") {
                    error = true
                    etReps1.isActivated = true
                } else {
                    etReps1.isActivated = false
                }
                if(etReps2.text.isEmpty() || etReps2.text.toString() == "0") {
                    error = true
                    etReps2.isActivated = true
                } else {
                    etReps2.isActivated = false
                }
                if(spinner1.selectedItem != null && spinner1.selectedItem == context.getString(R.string.choose_exercise)) {
                    error = true
                    spinner1.isActivated = true
                } else {
                    spinner1.isActivated = false
                }
                if(spinner2.selectedItem != null && spinner2.selectedItem == context.getString(R.string.choose_exercise)) {
                    error = true
                    spinner2.isActivated = true
                } else {
                    spinner2.isActivated = false
                }
                if(spinner1.selectedItem == spinner2.selectedItem) {
                    error = true
                    spinner1.isActivated = true
                    spinner2.isActivated = true
                } else {
                    spinner1.isActivated = false
                    spinner2.isActivated = false
                }

                if(!error) {

                    var eId1 = 0
                    var eId2 = 0
                    exercises.forEach {
                        if(spinner1.selectedItem == it.eName) eId1 = it.eId
                        if(spinner2.selectedItem == it.eName) eId2 = it.eId
                    }
                    val newSuperset = Superset(superset.sId, eId1, eId2, getInt(etSets.text.toString())!!, getInt(etReps1.text.toString())!!, getInt(etReps2.text.toString())!!, getInt(etBreak.text.toString()), getInt(etRpe1.text.toString()), getInt(etRpe2.text.toString()), superset.posInRoutine, superset.rId)

                    val index = mSupersets.indexOf(superset)
                    mSupersets.remove(superset)
                    mSupersets.add(index, newSuperset)
                    notifyDataSetChanged()

                    setEditableItem(-1)
                }
            }

            buttonEdit.setOnClickListener {
                setEditableItem(superset.posInRoutine)
            }

            buttonDelete.setOnClickListener {
                if(listener != null)
                    listener!!.deleteSuperset(superset)
            }

            dragHandler.setOnTouchListener { _, event ->
                if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                    if (listener != null)
                        listener!!.startDragging(this)
                }
                return@setOnTouchListener true
            }
        }

        private fun setEditable(editable: Boolean) {
            buttonFinish.isClickable = editable
            buttonFinish.isEnabled = editable
            if(editable) {
                buttonFinish.visibility = View.VISIBLE
            } else {
                buttonFinish.visibility = View.INVISIBLE
            }
            if(editableItem != -1) {
                buttonEdit.isClickable = false
                buttonEdit.isEnabled = false
                buttonEdit.visibility = View.INVISIBLE
                dragHandler.visibility = View.INVISIBLE
                dragHandler.isClickable = false
                dragHandler.isEnabled = false
                if(editable) {
                    buttonDelete.isClickable = true
                    buttonDelete.isEnabled = true
                    buttonDelete.visibility = View.VISIBLE
                } else {
                    buttonDelete.isClickable = false
                    buttonDelete.isEnabled = false
                    buttonDelete.visibility = View.INVISIBLE
                }
            } else {
                buttonEdit.isClickable = true
                buttonEdit.isEnabled = true
                buttonEdit.visibility = View.VISIBLE
                buttonDelete.isClickable = true
                buttonDelete.isEnabled = true
                buttonDelete.visibility = View.VISIBLE
                dragHandler.visibility = View.VISIBLE
                dragHandler.isClickable = true
                dragHandler.isEnabled = true
            }

            spinner1.isEnabled = editable
            spinner1.isClickable = editable
            spinner2.isEnabled = editable
            spinner2.isClickable = editable
            etSets.isClickable = editable
            etSets.isEnabled = editable
            etBreak.isClickable = editable
            etBreak.isEnabled = editable
            etReps1.isClickable = editable
            etReps1.isEnabled = editable
            etReps2.isClickable = editable
            etReps2.isEnabled = editable
            etRpe1.isClickable = editable
            etRpe1.isEnabled = editable
            etRpe2.isClickable = editable
            etRpe2.isEnabled = editable
            cardView.isSelected = editable
            textViewBreak.isEnabled = editable
            textViewReps1.isEnabled = editable
            textViewRpe1.isEnabled = editable
            textViewReps2.isEnabled = editable
            textViewRpe2.isEnabled = editable
            textViewSets.isEnabled = editable
        }
    }

    inner class DropsetViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var textViewReps: TextView
        private lateinit var textViewSets: TextView
        private lateinit var textViewDrops: TextView
        private lateinit var textViewBreak: TextView
        private lateinit var textViewNumber: TextView
        private lateinit var spinner: Spinner
        private lateinit var buttonFinish: ImageButton
        private lateinit var buttonEdit: ImageButton
        private lateinit var buttonDelete: ImageButton
        private lateinit var etSets: EditText
        private lateinit var etReps: EditText
        private lateinit var etDrops: EditText
        private lateinit var etBreak: EditText
        private lateinit var dragHandler: ImageView
        private lateinit var cardView: CardView

        @SuppressLint("ClickableViewAccessibility")
        fun bind(dropset: Dropset) {
            spinner = view.findViewById(R.id.spinner_exercises_view_dropset)
            val exerciseList = ArrayList<String>()
            val exerciseIdList = ArrayList<Int>()
            exercises.forEach {
                exerciseList.add(it.eName)
                exerciseIdList.add(it.eId)
            }
            exerciseList.add(context.getString(R.string.choose_exercise))
            val adapter = HintAdapter(context, R.layout.spinner_item_exercises_in_routine, exerciseList)
            spinner.adapter = adapter
            if(dropset.eId != 0) {
                val position = exerciseIdList.indexOf(dropset.eId)
                spinner.setSelection(position)
            } else {
                spinner.setSelection(spinner.adapter.count)
            }

            textViewBreak = view.findViewById(R.id.text_view_break_view_dropset)
            textViewReps = view.findViewById(R.id.text_view_reps_view_dropset)
            textViewSets = view.findViewById(R.id.text_view_sets_view_dropset)
            textViewDrops = view.findViewById(R.id.text_view_drops_view_dropset)
            buttonFinish = view.findViewById(R.id.button_finish_card_view_dropset)
            buttonEdit = view.findViewById(R.id.button_edit_card_view_dropset)
            buttonDelete = view.findViewById(R.id.button_delete_card_view_dropset)
            etSets = view.findViewById(R.id.edit_text_sets_view_dropset)
            etReps = view.findViewById(R.id.edit_text_reps_view_dropset)
            etDrops = view.findViewById(R.id.edit_text_drops_view_dropset)
            etBreak = view.findViewById(R.id.edit_text_break_view_dropset)
            dragHandler = view.findViewById(R.id.image_view_drag_view_dropset)
            cardView = view.findViewById(R.id.card_view_dropset)
            etSets.setText(getString(dropset.sets))
            etReps.setText(getString(dropset.reps))
            etDrops.setText(getString(dropset.drops))
            etBreak.setText(getString(dropset.breakInSeconds))
            setEditable(dropset.posInRoutine == editableItem)
            textViewNumber = view.findViewById(R.id.text_view_exercise_number_view_dropset)
            textViewNumber.text = String.format(context.getString(R.string.exercise_number_dropset), (dropset.posInRoutine + 1).toString())
            etSets.addTextChangedListener(TextWatcherNoZero(etSets))
            etBreak.addTextChangedListener(TextWatcherNoZero(etBreak))
            etDrops.addTextChangedListener(TextWatcherNoZero(etDrops))
            etReps.addTextChangedListener(TextWatcherNoZero(etReps))

            buttonFinish.setOnClickListener {

                var error = false
                if(etSets.text.isEmpty() || etSets.text.toString() == "0") {
                    error = true
                    etSets.isActivated = true
                } else {
                    etSets.isActivated = false
                }
                if (etReps.text.isEmpty() || etSets.text.toString() == "0") {
                    error = true
                    etReps.isActivated = true
                } else {
                    etReps.isActivated = false
                }
                if(etDrops.text.isEmpty() || etDrops.text.toString() == "0") {
                    error = true
                    etDrops.isActivated = true
                } else {
                    etDrops.isActivated = false
                }
                if(spinner.selectedItem != null && spinner.selectedItem == context.getString(R.string.choose_exercise)) {
                    error = true
                    spinner.isActivated = true
                } else {
                    spinner.isActivated = false
                }

                if(!error) {

                    var eId = 0
                    exercises.forEach {
                        if(spinner.selectedItem == it.eName) eId = it.eId
                    }

                    val newDropset = Dropset(dropset.dId, eId, getInt(etSets.text.toString())!!, getInt(etReps.text.toString())!!, getInt(etBreak.text.toString()), getInt(etDrops.text.toString())!!, dropset.posInRoutine, dropset.rId)

                    val index = mDropsets.indexOf(dropset)
                    mDropsets.remove(dropset)
                    mDropsets.add(index, newDropset)
                    notifyDataSetChanged()

                    setEditableItem(-1)
                }
            }

            buttonEdit.setOnClickListener {
                setEditableItem(dropset.posInRoutine)
            }

            buttonDelete.setOnClickListener {
                if(listener != null)
                    listener!!.deleteDropset(dropset)
            }

            dragHandler.setOnTouchListener { _, event ->
                if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                    if (listener != null)
                        listener!!.startDragging(this)
                }
                return@setOnTouchListener true
            }
        }

        private fun setEditable(editable: Boolean) {
            buttonFinish.isClickable = editable
            buttonFinish.isEnabled = editable
            if(editable) {
                buttonFinish.visibility = View.VISIBLE
            } else {
                buttonFinish.visibility = View.INVISIBLE
            }
            if(editableItem != -1) {
                buttonEdit.isClickable = false
                buttonEdit.isEnabled = false
                buttonEdit.visibility = View.INVISIBLE
                dragHandler.visibility = View.INVISIBLE
                dragHandler.isClickable = false
                dragHandler.isEnabled = false
                if(editable) {
                    buttonDelete.isClickable = true
                    buttonDelete.isEnabled = true
                    buttonDelete.visibility = View.VISIBLE
                } else {
                    buttonDelete.isClickable = false
                    buttonDelete.isEnabled = false
                    buttonDelete.visibility = View.INVISIBLE
                }
            } else {
                buttonEdit.isClickable = true
                buttonEdit.isEnabled = true
                buttonEdit.visibility = View.VISIBLE
                buttonDelete.isClickable = true
                buttonDelete.isEnabled = true
                buttonDelete.visibility = View.VISIBLE
                dragHandler.visibility = View.VISIBLE
                dragHandler.isClickable = true
                dragHandler.isEnabled = true
            }

            spinner.isEnabled = editable
            spinner.isClickable = editable
            etSets.isClickable = editable
            etSets.isEnabled = editable
            etBreak.isClickable = editable
            etBreak.isEnabled = editable
            etReps.isClickable = editable
            etReps.isEnabled = editable
            etDrops.isClickable = editable
            etDrops.isEnabled = editable
            cardView.isSelected = editable
            textViewBreak.isEnabled = editable
            textViewReps.isEnabled = editable
            textViewDrops.isEnabled = editable
            textViewSets.isEnabled = editable
        }
    }

    override fun getItemViewType(position: Int): Int {
        var viewType = TYPE_ERROR
        mNormals.forEach {
            if(position == it.posInRoutine) viewType = TYPE_NORMAL
        }
        mSupersets.forEach {
            if (position == it.posInRoutine) {
                if(viewType == TYPE_ERROR) viewType = TYPE_SUPERSET
                else return TYPE_ERROR
            }
        }
        mDropsets.forEach {
            if(position == it.posInRoutine) {
                if(viewType == TYPE_ERROR) viewType = TYPE_DROPSET
                else return TYPE_ERROR
            }
        }
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {
            TYPE_NORMAL -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_normal, parent, false)
                return NormalViewHolder(view)
            }
            TYPE_SUPERSET -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_superset, parent, false)
                return SupersetViewHolder(view)
            }
            TYPE_DROPSET -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_dropset, parent, false)
                return DropsetViewHolder(view)
            }
        }
        val tv = TextView(parent.context)
        tv.text = context.getString(R.string.error)
        return ErrorViewHolder(tv)
    }

    override fun getItemCount() = mNormals.size + mSupersets.size + mDropsets.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {
            TYPE_ERROR -> {
                val errorViewHolder = holder as ErrorViewHolder
                errorViewHolder.bind(context.getString(R.string.error))
            }
            TYPE_NORMAL -> {
                val normalViewHolder = holder as NormalViewHolder
                var normal: Normal? = null
                mNormals.forEach {
                    if(position == it.posInRoutine) normal = it
                }
                if(normal != null) {
                    normalViewHolder.bind(normal!!)
                }
            }
            TYPE_SUPERSET -> {
                val supersetViewHolder = holder as SupersetViewHolder
                var superset: Superset? = null
                mSupersets.forEach {
                    if(position == it.posInRoutine) superset = it
                }
                if(superset != null) supersetViewHolder.bind(superset!!)
            }
            TYPE_DROPSET -> {
                val dropsetViewHolder = holder as DropsetViewHolder
                var dropset: Dropset? = null
                mDropsets.forEach {
                    if(position == it.posInRoutine)  dropset = it
                }
                if(dropset != null) dropsetViewHolder.bind(dropset!!)
            }
        }
    }

    interface IExerciseInRoutineAdapter{
        fun onEditableChanged()
        fun startDragging(viewHolder: RecyclerView.ViewHolder)
        fun deleteNormal(normal: Normal)
        fun deleteSuperset(superset: Superset)
        fun deleteDropset(dropset: Dropset)
    }

    private fun getInt(s: String): Int? {
        return try {
            if(Integer.valueOf(s) != 0)
                Integer.valueOf(s)
            else
                null
        } catch (e: NumberFormatException) {
            null
        }
    }

    private fun getString(i: Int?): String {
        return if (i == 0 || i == null) "" else i.toString()
    }
}