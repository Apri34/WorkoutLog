package com.workoutlog.workoutlog.ui.activities

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.adapters.ExercisesInRoutineAdapter
import com.workoutlog.workoutlog.database.AppDatabase
import com.workoutlog.workoutlog.database.DatabaseInitializer
import com.workoutlog.workoutlog.database.entities.*
import com.workoutlog.workoutlog.ui.fragments.ChooseWhichExerciseDialogFragment
import com.workoutlog.workoutlog.ui.fragments.ConfirmDeleteDialog
import com.workoutlog.workoutlog.ui.fragments.EditRoutineNameDialogFragment
import com.workoutlog.workoutlog.ui.fragments.SaveChangesDialogFragment

class EditRoutineActivity : AppCompatActivity(),
    ChooseWhichExerciseDialogFragment.IChooseExercise,
    ExercisesInRoutineAdapter.IExerciseInRoutineAdapter,
    EditRoutineNameDialogFragment.IEditRoutine,
    SaveChangesDialogFragment.ISaveChangesListener,
    ConfirmDeleteDialog.IConfirmDelete<ExerciseInRoutine> {

    override fun delete(id: Int?, item: ExerciseInRoutine) {
        when(id) {
            NORMAL_EXERCISE_ID -> {
                (recyclerView.adapter as ExercisesInRoutineAdapter).deleteNormal(item as Normal)
            }
            SUPERSET_ID -> {
                (recyclerView.adapter as ExercisesInRoutineAdapter).deleteSuperset(item as Superset)
            }
            DROPSET_ID -> {
                (recyclerView.adapter as ExercisesInRoutineAdapter).deleteDropset(item as Dropset)
            }
        }
    }

    override fun deleteNormal(normal: Normal) {
        val dialog = ConfirmDeleteDialog<ExerciseInRoutine>()
        dialog.setConfirmDeleteDialogId(NORMAL_EXERCISE_ID)
        dialog.setListener(this)
        dialog.setMessage(getString(R.string.confirm_delete_exercise))
        dialog.setItem(normal)
        dialog.show(supportFragmentManager, "deleteNormal")
    }

    override fun deleteSuperset(superset: Superset) {
        val dialog = ConfirmDeleteDialog<ExerciseInRoutine>()
        dialog.setConfirmDeleteDialogId(SUPERSET_ID)
        dialog.setListener(this)
        dialog.setMessage(getString(R.string.confirm_delete_exercise))
        dialog.setItem(superset)
        dialog.show(supportFragmentManager, "deleteSuperset")
    }

    override fun deleteDropset(dropset: Dropset) {
        val dialog = ConfirmDeleteDialog<ExerciseInRoutine>()
        dialog.setConfirmDeleteDialogId(DROPSET_ID)
        dialog.setListener(this)
        dialog.setMessage(getString(R.string.confirm_delete_exercise))
        dialog.setItem(dropset)
        dialog.show(supportFragmentManager, "deleteDropset")
    }

    override fun discard() {
        super.onBackPressed()
    }

    override fun save() {
        saveChanges()
        super.onBackPressed()
    }

    override fun startDragging(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
        isDragging = true
    }

    override fun editRoutine(rId: Int, rName: String) {
        dbInitializer.updateRoutine(database.routineDao(), rId, rName)
        routine = dbInitializer.getRoutineById(database.routineDao(), rId)
        supportActionBar!!.title = routine.rName
    }

    override fun onEditableChanged() {
        invalidateOptionsMenu()
    }

    override fun addNormalExercise() {
        val normal = Normal(0, 0, 0, 0, 0, 0, getNumberExercisesInThisRoutine(), routine.rId)
        (recyclerView.adapter as ExercisesInRoutineAdapter).addNormal(normal)
        recyclerView.adapter!!.notifyDataSetChanged()
        recyclerView.scrollToPosition(recyclerView.adapter!!.itemCount - 1)
    }

    override fun addSuperset() {
        val superset = Superset(0, 0, 0, 0, 0, 0, 0, 0, 0, getNumberExercisesInThisRoutine(), routine.rId)
        (recyclerView.adapter as ExercisesInRoutineAdapter).addSuperset(superset)
        recyclerView.adapter!!.notifyDataSetChanged()
        recyclerView.scrollToPosition(recyclerView.adapter!!.itemCount - 1)
    }

    override fun addDropset() {
        val dropset = Dropset(0, 0, 0, 0, 0, 0, getNumberExercisesInThisRoutine(), routine.rId)
        (recyclerView.adapter as ExercisesInRoutineAdapter).addDropset(dropset)
        recyclerView.adapter!!.notifyDataSetChanged()
        recyclerView.scrollToPosition(recyclerView.adapter!!.itemCount - 1)
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: Toolbar

    private lateinit var routine: Routine
    private lateinit var dbInitializer: DatabaseInitializer
    private lateinit var database: AppDatabase

    private lateinit var mNormals: MutableList<Normal>
    private lateinit var mSupersets: MutableList<Superset>
    private lateinit var mDropsets: MutableList<Dropset>
    private lateinit var mExercises: MutableList<Exercise>

    private lateinit var itemTouchHelper: ItemTouchHelper

    private var isDragging = false

    companion object {
        private const val ROUTINE_ID_KEY = "routineId"
        private const val NORMAL_EXERCISE_ID = 0
        private const val SUPERSET_ID = 1
        private const val DROPSET_ID = 2
        private const val NORMALS_KEY = "normals"
        private const val SUPERSETS_KEY = "supersets"
        private const val DROPSETS_KEY = "dropsets"
        private const val EDITABLE_ITEM_KEY = "editableItem"
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorSecondaryDark)
        }
        setContentView(R.layout.activity_edit_routine)

        dbInitializer = DatabaseInitializer.getInstance()
        database = AppDatabase.getInstance(this)
        routine = dbInitializer.getRoutineById(database.routineDao(), intent.extras!!.getInt(ROUTINE_ID_KEY))

        toolbar = findViewById(R.id.toolbar_edit_routine_activity)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = routine.rName

        recyclerView = findViewById(R.id.recycler_view_edit_routine_activity)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(MarginItemDecoration(resources.getDimension(R.dimen.padding_recyclerview_side).toInt()))
        recyclerView.setOnTouchListener { _, event ->
            if(isDragging && (event.actionMasked == MotionEvent.ACTION_UP || event.actionMasked == MotionEvent.ACTION_CANCEL)) {
                recyclerView.adapter!!.notifyDataSetChanged()
                isDragging = false
                return@setOnTouchListener true
            }
            return@setOnTouchListener false
        }

        if(savedInstanceState != null) {
            mNormals = savedInstanceState.getParcelableArrayList(NORMALS_KEY)!!
            mSupersets = savedInstanceState.getParcelableArrayList(SUPERSETS_KEY)!!
            mDropsets = savedInstanceState.getParcelableArrayList(DROPSETS_KEY)!!
        } else {
            mNormals = dbInitializer.getNormalsByRoutineId(database.normalDao(), routine.rId)
            mSupersets = dbInitializer.getSupersetsByRoutineId(database.supersetDao(), routine.rId)
            mDropsets = dbInitializer.getDropsetsByRoutineId(database.dropsetDao(), routine.rId)
        }

        mExercises = dbInitializer.getAllExercises(database.exerciseDao())
        val adapter = ExercisesInRoutineAdapter(this, mExercises, mNormals, mSupersets, mDropsets)
        adapter.setListener(this)
        if(savedInstanceState != null)
            adapter.setEditableItem(savedInstanceState.getInt(EDITABLE_ITEM_KEY))
        recyclerView.adapter = adapter

        itemTouchHelper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(UP or DOWN, 0) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val rAdapter = recyclerView.adapter as ExercisesInRoutineAdapter
                    val from = viewHolder.adapterPosition
                    val to = target.adapterPosition

                    rAdapter.moveItem(from, to)
                    rAdapter.notifyItemMoved(from, to)
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

            })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val adapter = recyclerView.adapter!! as ExercisesInRoutineAdapter
        val dropsets = adapter.getDropsets()
        val supersets = adapter.getSupersets()
        val normals = adapter.getNormals()
        val editableItem = adapter.getEditableItem()
        val listDropsets = ArrayList<Parcelable>()
        val listSupersets = ArrayList<Parcelable>()
        val listNormals = ArrayList<Parcelable>()
        dropsets.forEach {
            listDropsets.add(it)
        }
        supersets.forEach {
            listSupersets.add(it)
        }
        normals.forEach {
            listNormals.add(it)
        }
        outState.putParcelableArrayList(NORMALS_KEY, listNormals)
        outState.putParcelableArrayList(SUPERSETS_KEY, listSupersets)
        outState.putParcelableArrayList(DROPSETS_KEY, listDropsets)
        outState.putInt(EDITABLE_ITEM_KEY, editableItem)
    }

    override fun onBackPressed() {
        val dialog = SaveChangesDialogFragment()
        dialog.setListener(this)
        dialog.show(supportFragmentManager, "saveChangesDialog")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_edit_trainingplan, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu!!.findItem(R.id.action_add).isEnabled =
            (recyclerView.adapter as ExercisesInRoutineAdapter).getEditableItem() == -1
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId) {
            R.id.action_finished -> {
                saveChanges()
                finish()
            }
            R.id.action_edit -> {
                val dialog = EditRoutineNameDialogFragment()
                dialog.setListener(this)
                dialog.setRoutine(routine)
                dialog.show(supportFragmentManager, "editRoutineName")
            }
            R.id.action_add -> {
                addExercise()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addExercise() {
        val dialog = ChooseWhichExerciseDialogFragment()
        dialog.setListener(this)
        dialog.show(supportFragmentManager, "chooseWhichExercise")
    }

    private fun getNumberExercisesInThisRoutine(): Int {
        val normals = (recyclerView.adapter as ExercisesInRoutineAdapter).getNormals()
        val supersets = (recyclerView.adapter as ExercisesInRoutineAdapter).getSupersets()
        val dropsets = (recyclerView.adapter as ExercisesInRoutineAdapter).getDropsets()
        return normals.size + supersets.size + dropsets.size
    }

    private fun saveChanges() {
        val normals = (recyclerView.adapter as ExercisesInRoutineAdapter).getNormals()
        val supersets = (recyclerView.adapter as ExercisesInRoutineAdapter).getSupersets()
        val dropsets = (recyclerView.adapter as ExercisesInRoutineAdapter).getDropsets()

        val dbNormals = dbInitializer.getNormalsByRoutineId(database.normalDao(), routine.rId)
        val dbSupersets = dbInitializer.getSupersetsByRoutineId(database.supersetDao(), routine.rId)
        val dbDropsets = dbInitializer.getDropsetsByRoutineId(database.dropsetDao(), routine.rId)

        dbNormals.forEach {
            dbInitializer.deleteNormal(database.normalDao(), it)
        }
        dbSupersets.forEach {
            dbInitializer.deleteSuperset(database.supersetDao(), it)
        }
        dbDropsets.forEach {
            dbInitializer.deleteDropset(database.dropsetDao(), it)
        }

        normals.forEach {
            if(it.eId != 0)
                dbInitializer.insertNormal(database.normalDao(), it)
        }
        supersets.forEach {
            if(it.eId1 != 0 && it.eId2 != 0)
                dbInitializer.insertSuperset(database.supersetDao(), it)
        }
        dropsets.forEach {
            if(it.eId != 0)
                dbInitializer.insertDropset(database.dropsetDao(), it)
        }
    }

    inner class MarginItemDecoration(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            with(outRect) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    top = spaceHeight
                }
                left = spaceHeight
                right = spaceHeight
                bottom = spaceHeight
            }
        }
    }
}
