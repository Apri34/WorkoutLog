package com.workoutlog.workoutlog.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.adapters.ExercisesAdapter
import com.workoutlog.workoutlog.database.AppDatabase
import com.workoutlog.workoutlog.database.DatabaseInitializer
import com.workoutlog.workoutlog.database.entities.Exercise

class ExercisesFragment: Fragment(), ExercisesAdapter.IExerciseAdapter, AddExerciseDialogFragment.IAddExercise, EditExerciseDialogFragment.IEditExercise {

    override fun editExercise(eId: Int, eName: String) {
        dbInitializer.updateExercise(database.exerciseDao(), Exercise(eId, eName))
        (recyclerView.adapter as ExercisesAdapter).setDataset(dbInitializer.getAllExercises(database.exerciseDao()))
        recyclerView.adapter!!.notifyDataSetChanged()
    }

    override fun addExercise(eName: String) {
        val exc = Exercise(0, eName)
        dbInitializer.insertExercise(database.exerciseDao(), exc)
        (recyclerView.adapter as ExercisesAdapter).setDataset(dbInitializer.getAllExercises(database.exerciseDao()))
        recyclerView.adapter!!.notifyDataSetChanged()
    }

    override fun exerciseLongClicked(exc: Exercise) {
        val dialogEditExercise = EditExerciseDialogFragment()
        dialogEditExercise.setExercise(exc)
        dialogEditExercise.setListener(this)
        dialogEditExercise.show(childFragmentManager, "editExercise")
    }

    private lateinit var buttonAdd: ImageButton
    private lateinit var recyclerView: RecyclerView

    private lateinit var database: AppDatabase
    private lateinit var dbInitializer: DatabaseInitializer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_exercises, container, false)

        buttonAdd = view.findViewById(R.id.button_add_fragment_exercises)
        buttonAdd.setOnClickListener { addExercise() }
        recyclerView = view.findViewById(R.id.recycler_view_fragment_exercises)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, LinearLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)

        val exercises = dbInitializer.getAllExercises(database.exerciseDao())
        val adapter = ExercisesAdapter(exercises)
        adapter.setListener(this)
        recyclerView.adapter = adapter

        return view
    }

    private fun addExercise() {
        val dialogAddExercise = AddExerciseDialogFragment()
        dialogAddExercise.setListener(this)
        dialogAddExercise.show(childFragmentManager, "addExercise")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        database = AppDatabase.getInstance(context)
        dbInitializer = DatabaseInitializer.getInstance()
    }
}