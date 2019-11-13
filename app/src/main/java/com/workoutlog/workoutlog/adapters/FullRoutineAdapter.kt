package com.workoutlog.workoutlog.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.database.AppDatabase
import com.workoutlog.workoutlog.database.DatabaseInitializer
import com.workoutlog.workoutlog.database.entities.Dropset
import com.workoutlog.workoutlog.database.entities.Normal
import com.workoutlog.workoutlog.database.entities.Routine
import com.workoutlog.workoutlog.database.entities.Superset

class FullRoutineAdapter(private val context: Context, routine: Routine): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dbInitializer: DatabaseInitializer = DatabaseInitializer.getInstance(context)
    private val database: AppDatabase = AppDatabase.getInstance(context)
    private val normals: List<Normal>
    private val supersets: List<Superset>
    private val dropsets: List<Dropset>

    private var listener: IButtonClicked? = null
    fun setListener(listener: IButtonClicked) {
        this.listener = listener
    }

    companion object {
        private const val TYPE_NORMAL = 0
        private const val TYPE_SUPERSET = 1
        private const val TYPE_DROPSET = 2
        private const val TYPE_BUTTON = 3
    }

    init {
        normals = dbInitializer.getNormalsByRoutineId(database.normalDao(), routine.rId)
        supersets = dbInitializer.getSupersetsByRoutineId(database.supersetDao(), routine.rId)
        dropsets = dbInitializer.getDropsetsByRoutineId(database.dropsetDao(), routine.rId)
    }

    inner class ErrorViewHolder(private val textView: TextView): RecyclerView.ViewHolder(textView) {
        fun bind (message: String) {
            textView.text = message
        }
    }

    inner class NormalViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val tvExcName = view.findViewById<TextView>(R.id.text_view_view_full_normal_exc_name)
        private val tvSets = view.findViewById<TextView>(R.id.text_view_view_full_normal_sets)
        private val tvReps = view.findViewById<TextView>(R.id.text_view_view_full_normal_reps)
        private val tvRpe = view.findViewById<TextView>(R.id.text_view_view_full_normal_rpe)
        private val tvBreak = view.findViewById<TextView>(R.id.text_view_view_full_normal_break)

        fun bind(normal: Normal) {
            val excName = dbInitializer.getExerciseNameById(database.exerciseDao(), normal.eId)
            tvExcName.text = excName
            tvSets.text = normal.sets.toString()
            tvReps.text = normal.reps.toString()
            if(normal.rpe != null) tvRpe.text = normal.rpe!!.toString()
            if(normal.breakInSeconds != null) tvBreak.text = normal.breakInSeconds!!.toString()
        }
    }

    inner class DropsetViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val tvExcName = view.findViewById<TextView>(R.id.text_view_view_full_dropset_exc_name)
        private val tvSets = view.findViewById<TextView>(R.id.text_view_view_full_dropset_sets)
        private val tvReps = view.findViewById<TextView>(R.id.text_view_view_full_dropset_reps)
        private val tvDrops = view.findViewById<TextView>(R.id.text_view_view_full_dropset_drops)
        private val tvBreak = view.findViewById<TextView>(R.id.text_view_view_full_dropset_break)

        fun bind(dropset: Dropset) {
            val excName = dbInitializer.getExerciseNameById(database.exerciseDao(), dropset.eId)
            tvExcName.text = String.format(context.getString(R.string.dropset_, excName))
            tvSets.text = dropset.sets.toString()
            tvReps.text = dropset.reps.toString()
            tvDrops.text = dropset.drops.toString()
            if(dropset.breakInSeconds != null) tvBreak.text = dropset.breakInSeconds!!.toString()
        }
    }

    inner class SupersetViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val tvExcName1 = view.findViewById<TextView>(R.id.text_view_view_full_superset_exc_name1)
        private val tvExcName2 = view.findViewById<TextView>(R.id.text_view_view_full_superset_exc_name2)
        private val tvSets = view.findViewById<TextView>(R.id.text_view_view_full_superset_sets)
        private val tvReps1 = view.findViewById<TextView>(R.id.text_view_view_full_superset_reps1)
        private val tvRpe1 = view.findViewById<TextView>(R.id.text_view_view_full_superset_rpe1)
        private val tvReps2 = view.findViewById<TextView>(R.id.text_view_view_full_superset_reps2)
        private val tvRpe2 = view.findViewById<TextView>(R.id.text_view_view_full_superset_rpe2)
        private val tvBreak = view.findViewById<TextView>(R.id.text_view_view_full_superset_break)

        fun bind(superset: Superset) {
            val excName1 = dbInitializer.getExerciseNameById(database.exerciseDao(), superset.eId1)
            val excName2 = dbInitializer.getExerciseNameById(database.exerciseDao(), superset.eId2)
            tvExcName1.text = String.format(context.getString(R.string.superset_), excName1)
            tvExcName2.text = String.format(context.getString(R.string.superset_), excName2)
            tvSets.text = superset.sets.toString()
            tvReps1.text = superset.reps1.toString()
            tvReps2.text = superset.reps2.toString()
            if(superset.rpe1 != null) tvRpe1.text = superset.rpe1!!.toString()
            if(superset.rpe2 != null) tvRpe2.text = superset.rpe2!!.toString()
            if(superset.breakInSeconds != null) tvBreak.text = superset.breakInSeconds!!.toString()
        }
    }

    inner class ButtonViewHolder(private val btn: Button): RecyclerView.ViewHolder(btn) {
        fun bind(position: Int) {
            if(position == itemCount - 2) {
                btn.setText(R.string.start_workout)
                btn.setOnClickListener {
                    if(listener != null) listener!!.startWorkout()
                }
            } else if(position == itemCount - 1) {
                btn.setText(R.string.pause_today)
                btn.setOnClickListener{
                    if(listener != null) listener!!.pauseToday()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_NORMAL -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.view_full_normal, parent, false)
                NormalViewHolder(view)
            }
            TYPE_SUPERSET -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.view_full_superset, parent, false)
                SupersetViewHolder(view)
            }
            TYPE_DROPSET -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.view_full_dropset, parent, false)
                DropsetViewHolder(view)
            }
            TYPE_BUTTON -> {
                val btn = Button(context)
                btn.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, context.resources.getDimensionPixelSize(R.dimen.button_height))
                btn.background = ContextCompat.getDrawable(context, R.drawable.background_button)
                btn.setTextColor(ContextCompat.getColor(context, R.color.white))
                return ButtonViewHolder(btn)
            }
            else -> {
                ErrorViewHolder(TextView(parent.context))
            }
        }
    }

    override fun getItemCount() = normals.size + supersets.size + dropsets.size + 2

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {
            TYPE_NORMAL -> {
                normals.forEach {
                    if(it.posInRoutine == position) (holder as NormalViewHolder).bind(it)
                }
            }
            TYPE_SUPERSET -> {
                supersets.forEach {
                    if(it.posInRoutine == position) (holder as SupersetViewHolder).bind(it)
                }
            }
            TYPE_DROPSET -> {
                dropsets.forEach {
                    if(it.posInRoutine == position) (holder as DropsetViewHolder).bind(it)
                }
            }
            TYPE_BUTTON -> {
                (holder as ButtonViewHolder).bind(position)
            }
            -1 -> {
                (holder as ErrorViewHolder).bind("Error")
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(position >= itemCount - 2) return TYPE_BUTTON
        normals.forEach {
            if(it.posInRoutine == position) return@getItemViewType TYPE_NORMAL
        }
        supersets.forEach {
            if(it.posInRoutine == position) return@getItemViewType TYPE_SUPERSET
        }
        dropsets.forEach {
            if(it.posInRoutine == position) return@getItemViewType TYPE_DROPSET
        }
        return -1
    }

    interface IButtonClicked {
        fun startWorkout()
        fun pauseToday()
    }
}