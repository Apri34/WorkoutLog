package com.workoutlog.workoutlog.database

import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Handler
import android.preference.PreferenceManager
import android.util.SparseBooleanArray
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.application.WorkoutLog
import com.workoutlog.workoutlog.database.entities.*
import com.workoutlog.workoutlog.ui.fragments.MessageDialogFragment
import com.workoutlog.workoutlog.ui.fragments.ProgressDialogFragment
import com.workoutlog.workoutlog.ui.fragments.SynchronizeDataDialogFragment
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.sql.Date

class DatabaseSynchronizer private constructor(context: Context) {

    private val firebaseFirestore: FirebaseFirestore

    init {
        databaseInitializer = DatabaseInitializer.getInstance(context)
        appDatabase = AppDatabase.getInstance(context)
        firebaseFirestore = FirebaseFirestore.getInstance()
    }

    companion object {
        private lateinit var databaseInitializer: DatabaseInitializer
        private lateinit var appDatabase: AppDatabase

        private var INSTANCE: DatabaseSynchronizer? = null
        private var firebaseUser: FirebaseUser? = null

        fun getInstance(context: Context): DatabaseSynchronizer {
            if (INSTANCE == null) {
                INSTANCE = DatabaseSynchronizer(context)
            }
            return INSTANCE!!
        }

        private const val KEY_TP_ID = "tpId"
        private const val KEY_CURRENT_TP_STATE = "currentTpState"
        private const val NO_CURRENT_TP = 0
        private const val TP_SELECTED = 1
        private const val INTERVAL_CHOSEN = 2
        private const val KEY_INTERVAL = "interval"
        private const val KEY_DELOAD_CYCLES = "deloadCycles"
        private const val KEY_DELOAD_VOLUME = "deloadVolume"
        private const val KEY_DELOAD_WEIGHT = "deloadWeight"
        private const val KEY_DELOAD_SET = "deloadSet"
        private const val KEY_START_DAY = "startDay"
        private const val KEY_START_MONTH = "startMonth"
        private const val KEY_START_YEAR = "startYear"

        private const val UID = "uid"
        private const val USERS = "users"
        private const val EXERCISES = "exercises"
        private const val EXERCISES_NAME = "ename"
        private const val TRAINING_PLANS = "trainingplans"
        private const val TRAINING_PLANS_NAME = "tpName"
        private const val TRAININGPLAN_ID = "tpId"
        private const val POS_IN_TRAININGPLAN = "posInTp"
        private const val ROUTINES = "routines"
        private const val ROUTINE_ID = "rid"
        private const val ROUTINE_NAME = "rname"
        private const val NORMAL_EXERCISES = "normalexc"
        private const val NORMAL_ID = "nid"
        private const val SUPERSET = "supersetexc"
        private const val SUPERSET_ID = "sid"
        private const val DROPSET = "dropsetexc"
        private const val DROPSET_ID = "did"
        private const val EID = "eid"
        private const val SETS = "sets"
        private const val REPS = "reps"
        private const val BREAK = "breakInSeconds"
        private const val DROPS = "drops"
        private const val RPE = "rpe"
        private const val POS_IN_ROUTINE = "posInRoutine"
        private const val EXERCISE_DONE = "exercisedone"
        private const val EXERCISE_DONE_ID = "edId"
        private const val SET_DONE = "setdone"
        private const val SET_DONE_ID = "sdId"
        private const val WEIGHT = "weightInKg"
        private const val POS_IN_EXC_DONE = "posInExcDone"
        private const val DATE = "date"
        private const val POS_ON_DATE = "posOnDate"

        private const val TIMER = 15000L
    }

    private class IsDatabaseInSync(private val weakReferenceFirebaseFirestore: WeakReference<FirebaseFirestore>, private val weakReferenceContext: WeakReference<Context>): AsyncTask<Void, Void, Void>() {

        private lateinit var dropsetData: List<Dropset>
        private lateinit var exerciseData: List<Exercise>
        private lateinit var exerciseDoneData: List<ExerciseDone>
        private lateinit var normalData: List<Normal>
        private lateinit var routineData: List<Routine>
        private lateinit var setDoneData: List<SetDone>
        private lateinit var supersetData: List<Superset>
        private lateinit var trainingplanData: List<Trainingplan>

        private var notInSyncListener: INotInSyncListener? = null
        fun setNotInSyncListener(listener: INotInSyncListener) {
            notInSyncListener = listener
        }

        override fun onPreExecute() {
            dropsetData = databaseInitializer.getAllDropsets(appDatabase.dropsetDao())
            exerciseData = databaseInitializer.getAllExercises(appDatabase.exerciseDao())
            exerciseDoneData = databaseInitializer.getAllExerciseDones(appDatabase.exerciseDoneDao())
            normalData = databaseInitializer.getAllNormals(appDatabase.normalDao())
            routineData = databaseInitializer.getAllRoutines(appDatabase.routineDao())
            setDoneData = databaseInitializer.getAllSetDones(appDatabase.setDoneDao())
            supersetData = databaseInitializer.getAllSupersets(appDatabase.supersetDao())
            trainingplanData = databaseInitializer.getAllTrainingplans(appDatabase.trainingplanDao())
        }

        override fun doInBackground(vararg params: Void?): Void? {
            val firebaseFirestore = weakReferenceFirebaseFirestore.get()!!

            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(DROPSET)
                .get()
                .addOnSuccessListener {collection->
                    if(collection.size() != dropsetData.size) {
                        if(notInSyncListener != null && !isCancelled && firebaseUser != null) notInSyncListener!!.notInSync(weakReferenceContext.get())
                    }
                }

            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(EXERCISES)
                .get()
                .addOnSuccessListener { collection ->
                    if (collection.size() != exerciseData.size) {
                        if (notInSyncListener != null && !isCancelled && firebaseUser != null) notInSyncListener!!.notInSync(weakReferenceContext.get())
                    }
                }

            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(EXERCISE_DONE)
                .get()
                .addOnSuccessListener {collection->
                    if (collection.size() != exerciseDoneData.size) {
                        if (notInSyncListener != null && !isCancelled && firebaseUser != null) notInSyncListener!!.notInSync(weakReferenceContext.get())
                    }
                }

            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(NORMAL_EXERCISES)
                .get()
                .addOnSuccessListener {collection->
                    if (collection.size() != normalData.size) {
                        if (notInSyncListener != null && !isCancelled && firebaseUser != null) notInSyncListener!!.notInSync(weakReferenceContext.get())
                    }
                }

            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(ROUTINES)
                .get()
                .addOnSuccessListener {collection->
                    if (collection.size() != routineData.size) {
                        if (notInSyncListener != null && !isCancelled && firebaseUser != null) notInSyncListener!!.notInSync(weakReferenceContext.get())
                    }
                }

            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(SET_DONE)
                .get()
                .addOnSuccessListener {collection->
                    if (collection.size() != setDoneData.size) {
                        if (notInSyncListener != null && !isCancelled && firebaseUser != null) notInSyncListener!!.notInSync(weakReferenceContext.get())
                    }
                }

            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(SUPERSET)
                .get()
                .addOnSuccessListener {collection->
                    if (collection.size() != supersetData.size) {
                        if (notInSyncListener != null && !isCancelled && firebaseUser != null) notInSyncListener!!.notInSync(weakReferenceContext.get())
                    }
                }

            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(TRAINING_PLANS)
                .get()
                .addOnSuccessListener {collection->
                    if (collection.size() != trainingplanData.size) {
                        if (notInSyncListener != null && !isCancelled && firebaseUser != null) notInSyncListener!!.notInSync(weakReferenceContext.get())
                    }
                }

            dropsetData.forEach {dropset ->
                val docRef = firebaseFirestore.collection(USERS)
                    .document(firebaseUser!!.uid)
                    .collection(DROPSET)
                    .document(dropset.dId.toString())
                docRef.get()
                    .addOnSuccessListener { document ->
                        if(document.exists()) {
                            if(!(document.get(DROPSET_ID) == dropset.dId.toLong()
                                        && document.get(EID) == dropset.eId.toLong()
                                        && document.get(SETS) == dropset.sets.toLong()
                                        && document.get(REPS) == dropset.reps.toLong()
                                        && document.get(BREAK) == dropset.breakInSeconds?.toLong()
                                        && document.get(DROPS) == dropset.drops.toLong()
                                        && document.get(POS_IN_ROUTINE) == dropset.posInRoutine.toLong()
                                        && document.get(ROUTINE_ID) == dropset.rId.toLong()
                                        )) {
                                if(notInSyncListener != null && !isCancelled && firebaseUser != null) notInSyncListener!!.notInSync(weakReferenceContext.get())
                            }
                        } else {
                            if(notInSyncListener != null && !isCancelled && firebaseUser != null) notInSyncListener!!.notInSync(weakReferenceContext.get())
                        }
                    }
            }

            exerciseData.forEach {exercise ->
                val docRef = firebaseFirestore.collection(USERS)
                    .document(firebaseUser!!.uid)
                    .collection(EXERCISES)
                    .document(exercise.eId.toString())
                docRef.get()
                    .addOnSuccessListener { document ->
                        if(document.exists()) {
                            if(!(document.get(EID) == exercise.eId.toLong()
                                        && document.getString(EXERCISES_NAME) == exercise.eName
                                        )) {
                                if(notInSyncListener != null && !isCancelled && firebaseUser != null) notInSyncListener!!.notInSync(weakReferenceContext.get())
                            }
                        } else {
                            if(notInSyncListener != null && !isCancelled && firebaseUser != null) notInSyncListener!!.notInSync(weakReferenceContext.get())
                        }
                    }
            }

            exerciseDoneData.forEach {exerciseDone ->
                val docRef = firebaseFirestore.collection(USERS)
                    .document(firebaseUser!!.uid)
                    .collection(EXERCISE_DONE)
                    .document(exerciseDone.edId.toString())
                docRef.get()
                    .addOnSuccessListener { document ->
                        if(document.exists()) {
                            val date = document.get(DATE) as Timestamp
                            val millis = date.seconds * 1000 + date.nanoseconds / 1000000
                            val dateDb = exerciseDone.date
                            val dateDbMillis = dateDb.time
                            if(!(document.get(EXERCISE_DONE_ID) == exerciseDone.edId.toLong()
                                        && millis == dateDbMillis
                                        && document.get(EID) == exerciseDone.eId.toLong()
                                        && document.get(POS_ON_DATE) == exerciseDone.posOnDate.toLong()
                                        )) {
                                if(notInSyncListener != null && !isCancelled && firebaseUser != null) notInSyncListener!!.notInSync(weakReferenceContext.get())
                            }
                        } else {
                            if(notInSyncListener != null && !isCancelled && firebaseUser != null) notInSyncListener!!.notInSync(weakReferenceContext.get())
                        }
                    }
            }

            normalData.forEach {normal ->
                val docRef = firebaseFirestore.collection(USERS)
                    .document(firebaseUser!!.uid)
                    .collection(NORMAL_EXERCISES)
                    .document(normal.nId.toString())
                docRef.get()
                    .addOnSuccessListener { document ->
                        if(document.exists()) {
                            if(!(document.get(NORMAL_ID) == normal.nId.toLong()
                                        && document.get(EID) == normal.eId.toLong()
                                        && document.get(SETS) == normal.sets.toLong()
                                        && document.get(REPS) == normal.reps.toLong()
                                        && document.get(BREAK) == normal.breakInSeconds?.toLong()
                                        && document.get(RPE) == normal.rpe?.toLong()
                                        && document.get(POS_IN_ROUTINE) == normal.posInRoutine.toLong()
                                        && document.get(ROUTINE_ID) == normal.rId.toLong()
                                        )) {
                                if(notInSyncListener != null && !isCancelled && firebaseUser != null) notInSyncListener!!.notInSync(weakReferenceContext.get())
                            }
                        } else {
                            if(notInSyncListener != null && !isCancelled && firebaseUser != null) notInSyncListener!!.notInSync(weakReferenceContext.get())
                        }
                    }
            }

            supersetData.forEach {superset ->
                val docRef = firebaseFirestore.collection(USERS)
                    .document(firebaseUser!!.uid)
                    .collection(SUPERSET)
                    .document(superset.sId.toString())
                docRef.get()
                    .addOnSuccessListener { document ->
                        if(document.exists()) {
                            if(!(document.get(SUPERSET_ID) == superset.sId.toLong()
                                        && document.get(EID + "1") == superset.eId1.toLong()
                                        && document.get(EID + "2") == superset.eId2.toLong()
                                        && document.get(SETS) == superset.sets.toLong()
                                        && document.get(REPS + "1") == superset.reps1.toLong()
                                        && document.get(REPS + "2") == superset.reps2.toLong()
                                        && document.get(BREAK) == superset.breakInSeconds?.toLong()
                                        && document.get(RPE + "1") == superset.rpe1?.toLong()
                                        && document.get(RPE + "2") == superset.rpe2?.toLong()
                                        && document.get(POS_IN_ROUTINE) == superset.posInRoutine.toLong()
                                        && document.get(ROUTINE_ID) == superset.rId.toLong()
                                        )) {
                                if(notInSyncListener != null && !isCancelled && firebaseUser != null) notInSyncListener!!.notInSync(weakReferenceContext.get())
                            }
                        } else {
                            if(notInSyncListener != null && !isCancelled && firebaseUser != null) notInSyncListener!!.notInSync(weakReferenceContext.get())
                        }
                    }
            }

            routineData.forEach {routine ->
                val docRef = firebaseFirestore.collection(USERS)
                    .document(firebaseUser!!.uid)
                    .collection(ROUTINES)
                    .document(routine.rId.toString())
                docRef.get()
                    .addOnSuccessListener { document ->
                        if(document.exists()) {
                            if(!(document.get(ROUTINE_ID) == routine.rId.toLong()
                                        && document.getString(ROUTINE_NAME) == routine.rName
                                        && document.get(TRAININGPLAN_ID) == routine.tpId.toLong()
                                        && document.get(POS_IN_TRAININGPLAN) == routine.posInTp.toLong()
                                        )) {
                                if(notInSyncListener != null && !isCancelled && firebaseUser != null) notInSyncListener!!.notInSync(weakReferenceContext.get())
                            }
                        } else {
                            if(notInSyncListener != null && !isCancelled && firebaseUser != null) notInSyncListener!!.notInSync(weakReferenceContext.get())
                        }
                    }
            }

            setDoneData.forEach {setDone ->
                val docRef = firebaseFirestore.collection(USERS)
                    .document(firebaseUser!!.uid)
                    .collection(SET_DONE)
                    .document(setDone.sdId.toString())
                docRef.get()
                    .addOnSuccessListener { document ->
                        if(document.exists()) {
                            if(!(document.get(SET_DONE_ID) == setDone.sdId.toLong()
                                        && document.get(REPS) == setDone.reps.toLong()
                                        && document.get(WEIGHT) == setDone.weightInKg.toDouble()
                                        && document.get(RPE) == setDone.rpe.toDouble()
                                        && document.get(POS_IN_EXC_DONE) == setDone.posInExcDone.toLong()
                                        && document.get(EXERCISE_DONE_ID) == setDone.edId.toLong()
                                        )) {
                                if(notInSyncListener != null && !isCancelled && firebaseUser != null) notInSyncListener!!.notInSync(weakReferenceContext.get())
                            }
                        } else {
                            if(notInSyncListener != null && !isCancelled && firebaseUser != null) notInSyncListener!!.notInSync(weakReferenceContext.get())
                        }
                    }
            }

            trainingplanData.forEach {trainingplan ->
                val docRef = firebaseFirestore.collection(USERS)
                    .document(firebaseUser!!.uid)
                    .collection(TRAINING_PLANS)
                    .document(trainingplan.tpId.toString())
                docRef.get()
                    .addOnSuccessListener { document ->
                        if(document.exists()) {
                            if(!(document.get(TRAININGPLAN_ID) == trainingplan.tpId.toLong()
                                        && document.getString(TRAINING_PLANS_NAME) == trainingplan.tpName
                                        )) {
                                if(notInSyncListener != null && !isCancelled && firebaseUser != null) notInSyncListener!!.notInSync(weakReferenceContext.get())
                            }
                        } else {
                            if(notInSyncListener != null && !isCancelled && firebaseUser != null) notInSyncListener!!.notInSync(weakReferenceContext.get())
                        }
                    }
            }
            return null
        }

        interface INotInSyncListener {
            fun notInSync(context: Context?)
        }
    }

    private fun checkDatabaseInSync(context: Context) {
        if(!isNetworkAvailable(context)) return
        if(firebaseUser != null) {
            val dialog = SynchronizeDataDialogFragment()
            dialog.setListener(object : SynchronizeDataDialogFragment.ISynchronizeDialog {
                override fun upload() {
                    uploadData(context)
                }

                override fun download() {
                    downloadData(context)
                }
            })
            val task = IsDatabaseInSync(WeakReference(firebaseFirestore), WeakReference(context))
            task.setNotInSyncListener(object: IsDatabaseInSync.INotInSyncListener {
                override fun notInSync(context: Context?) {
                    task.cancel(true)

                    if(context != null && !dialog.isAdded) {
                        val activity = (context.applicationContext as WorkoutLog).currentActivity as AppCompatActivity
                        dialog.show(activity.supportFragmentManager, "syncDialog")
                    }
                }
            })
            task.execute()
        }
    }

    private fun uploadData(context: Context) {
        if(firebaseUser == null) return
        if(!isNetworkAvailable(context)) {
            MessageDialogFragment.newInstance(context.getString(R.string.upload_failed)).show(((context.applicationContext as WorkoutLog).currentActivity as AppCompatActivity).supportFragmentManager, "uploadFailed")
            return
        }
        var isError = false
        val taskDelete = DeleteAllUserCollections(WeakReference(firebaseFirestore))
        val taskUpload = LoadWholeDataToFirestore(WeakReference(firebaseFirestore))
        val dialog = ProgressDialogFragment.newInstance(context.getString(R.string.uploading_data))
        dialog.setListener(object: ProgressDialogFragment.ITryAgainListener{
            override fun cancelOperation() {
                taskDelete.cancel(true)
                taskUpload.cancel(true)
            }

            override fun tryAgain() {
                uploadData(context)
            }

            override fun cancel(){}
        })
        dialog.show(((context.applicationContext as WorkoutLog).currentActivity as AppCompatActivity).supportFragmentManager, "uploadData")
        taskDelete.setOnDeleteListener(object : DeleteAllUserCollections.OnDeletedListener {
            override fun deleted() {
                taskUpload.setOnUploadFinishedListener(object: LoadWholeDataToFirestore.OnUploadFinishedListener {
                    override fun uploadFinished() {
                        dialog.dismiss()
                    }

                    override fun uploadFailed() {
                        taskUpload.cancel(true)
                        dialog.setError(context.getString(R.string.error), context.getString(R.string.an_error_occurred_uploading_the_data))
                        isError = true
                    }
                })
                taskUpload.execute()
            }

            override fun deleteFailed() {
                taskDelete.cancel(true)
                dialog.setError(context.getString(R.string.error), context.getString(R.string.an_error_occurred_uploading_the_data))
                isError = true
            }
        })
        taskDelete.execute()
        Handler().postDelayed({
            if(!isError) dialog.setCancelable(context.getString(R.string.this_operation_is_taking_very_long))
        }, TIMER)
    }

    private fun downloadData(context: Context) {
        if(!isNetworkAvailable(context)) {
            MessageDialogFragment.newInstance(context.getString(R.string.download_failed)).show(((context.applicationContext as WorkoutLog).currentActivity as AppCompatActivity).supportFragmentManager, "uploadFailed")
            return
        }
        if(firebaseUser == null) return
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        var tp: Trainingplan? = null
        var interval: ArrayList<Int>? = null
        if(sharedPref.contains(KEY_CURRENT_TP_STATE) || sharedPref.getInt(KEY_CURRENT_TP_STATE, NO_CURRENT_TP) >= INTERVAL_CHOSEN) {
            val tpId = sharedPref.getInt(KEY_TP_ID, -1)
            tp = databaseInitializer.getTrainingplanById(appDatabase.trainingplanDao(), tpId)
        } else if (sharedPref.contains(KEY_CURRENT_TP_STATE) || sharedPref.getInt(KEY_CURRENT_TP_STATE, NO_CURRENT_TP) >= TP_SELECTED) {
            interval = getArrayListFromJsonString(sharedPref.getString(KEY_INTERVAL, "")!!)
        }

        val task = DownloadWholeDataFromFirestore(WeakReference(firebaseFirestore), WeakReference(context), context.getString(R.string.downloading_data), context.getString(R.string.error), context.getString(R.string.an_error_occurred_downloading_the_data), context.getString(R.string.this_operation_is_taking_very_long))
        task.setOnDownloadFinishedListener(object : DownloadWholeDataFromFirestore.OnDownloadFinishedListener{
            override fun downloadFinished() {
                checkCurrentTrainingplan(context, tp, interval)
            }
        })
        task.execute()
    }

    private fun checkCurrentTrainingplan(context: Context, tp: Trainingplan?, interval: ArrayList<Int>?) {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        if(tp == null) return
        val tpNew = databaseInitializer.getTrainingplanById(appDatabase.trainingplanDao(), tp.tpId)
        var notInSync = false
        if(tpNew == null) notInSync = true
        else {
            if(tpNew.tpName != tp.tpName) notInSync = true
        }
        if(notInSync) {
            sharedPref.edit()
                .putInt(KEY_CURRENT_TP_STATE, NO_CURRENT_TP)
                .remove(KEY_TP_ID)
                .remove(KEY_INTERVAL)
                .remove(KEY_START_DAY)
                .remove(KEY_START_MONTH)
                .remove(KEY_START_YEAR)
                .remove(KEY_DELOAD_CYCLES)
                .remove(KEY_DELOAD_VOLUME)
                .remove(KEY_DELOAD_WEIGHT)
                .remove(KEY_DELOAD_SET)
                .apply()
            return
        }

        if(interval == null) return

        interval.forEach { rId->
            if(rId != 0) {
                val routine = databaseInitializer.getRoutineById(appDatabase.routineDao(), rId)
                if(routine == null || routine.tpId != tpNew.tpId) {
                    sharedPref.edit()
                        .putInt(KEY_CURRENT_TP_STATE, TP_SELECTED)
                        .remove(KEY_INTERVAL)
                        .remove(KEY_START_DAY)
                        .remove(KEY_START_MONTH)
                        .remove(KEY_START_YEAR)
                        .remove(KEY_DELOAD_CYCLES)
                        .remove(KEY_DELOAD_VOLUME)
                        .remove(KEY_DELOAD_WEIGHT)
                        .remove(KEY_DELOAD_SET)
                        .apply()
                    return
                }
            }
        }
    }

    private class DeleteAllUserCollections(private val weakReferenceFirebaseFirestore: WeakReference<FirebaseFirestore>): AsyncTask<Void, Void, Void>() {

        private var deleteListener: OnDeletedListener? = null
        private var sparseFinishedArray = SparseBooleanArray()

        fun setOnDeleteListener(listener: OnDeletedListener) {
            deleteListener = listener
        }

        companion object {
            private const val IS_DROPSET_FINISHED = 0
            private const val IS_EXERCISE_DONE_FINISHED = 1
            private const val IS_EXERCISE_FINISHED = 2
            private const val IS_NORMAL_EXC_FINISHED = 3
            private const val IS_ROUTINES_FINISHED = 4
            private const val IS_SET_DONE_FINISHED = 5
            private const val IS_SUPERSET_FINISHED = 6
            private const val IS_TRAININGPLAN_FINISHED = 7
        }

        init {
            for (i in IS_DROPSET_FINISHED..IS_TRAININGPLAN_FINISHED) {
                sparseFinishedArray.put(i, false)
            }
        }

        override fun doInBackground(vararg params: Void?): Void? {
            val firebaseFirestore = weakReferenceFirebaseFirestore.get()!!
            val dropsetRef = firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(DROPSET)
            deleteCollection(dropsetRef, IS_DROPSET_FINISHED)
            val exerciseDoneRef = firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(EXERCISE_DONE)
            deleteCollection(exerciseDoneRef, IS_EXERCISE_DONE_FINISHED)
            val exercisesRef = firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(EXERCISES)
            deleteCollection(exercisesRef, IS_EXERCISE_FINISHED)
            val normalRef = firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(NORMAL_EXERCISES)
            deleteCollection(normalRef, IS_NORMAL_EXC_FINISHED)
            val routinesRef = firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(ROUTINES)
            deleteCollection(routinesRef, IS_ROUTINES_FINISHED)
            val setDoneRef = firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(SET_DONE)
            deleteCollection(setDoneRef, IS_SET_DONE_FINISHED)
            val supersetRef = firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(SUPERSET)
            deleteCollection(supersetRef, IS_SUPERSET_FINISHED)
            val trainingplansRef = firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(TRAINING_PLANS)
            deleteCollection(trainingplansRef, IS_TRAININGPLAN_FINISHED)
            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .delete()

            return null
        }

        private fun deleteCollection(collection: CollectionReference, which: Int) {
            collection.get()
                .addOnSuccessListener {
                    if(!isCancelled) {
                        for (document in it) {
                            document.reference.delete()
                                .addOnFailureListener {
                                    if (deleteListener != null && !isCancelled) {
                                        deleteListener!!.deleteFailed()
                                        cancel(true)
                                    }
                                }
                        }
                        sparseFinishedArray.put(which, true)
                        if (isFinished() && deleteListener != null && !isCancelled) deleteListener!!.deleted()
                    }
                }
                .addOnFailureListener {
                    if(deleteListener != null && !isCancelled) {
                        deleteListener!!.deleteFailed()
                        cancel(true)
                    }
                }
        }

        interface OnDeletedListener {
            fun deleted()
            fun deleteFailed()
        }

        private fun isFinished(): Boolean {
            for(i in IS_DROPSET_FINISHED..IS_TRAININGPLAN_FINISHED) {
                if(!sparseFinishedArray.get(i)) return false
            }
            return true
        }
    }

    private class DownloadWholeDataFromFirestore(private val weakReferenceFirebaseFirestore: WeakReference<FirebaseFirestore>, private val weakReferenceContext: WeakReference<Context>, progressbarTitle: String, private val errorTitle: String, private val errorMessage: String, private val cancelableMessage: String): AsyncTask<Void, Void, Void>() {

        private val dialog = ProgressDialogFragment.newInstance(progressbarTitle)
        private val sparseFinishedArray = SparseBooleanArray()

        private var listOldDropsets: List<Dropset>
        private var listOldExercises: List<Exercise>
        private var listOldExerciseDones: List<ExerciseDone>
        private var listOldNormals: List<Normal>
        private var listOldRoutines: List<Routine>
        private var listOldSetDones: List<SetDone>
        private var listOldSupersets: List<Superset>
        private var listOldTrainingplans: List<Trainingplan>

        private var isError = false

        private var onFinishedListener: OnDownloadFinishedListener? = null
        fun setOnDownloadFinishedListener(listener: OnDownloadFinishedListener) {
            onFinishedListener = listener
        }

        companion object {
            private const val IS_DROPSET_FINISHED = 0
            private const val IS_EXERCISE_DONE_FINISHED = 1
            private const val IS_EXERCISE_FINISHED = 2
            private const val IS_NORMAL_EXC_FINISHED = 3
            private const val IS_ROUTINES_FINISHED = 4
            private const val IS_SET_DONE_FINISHED = 5
            private const val IS_SUPERSET_FINISHED = 6
            private const val IS_TRAININGPLAN_FINISHED = 7
        }

        init {
            for (i in IS_DROPSET_FINISHED..IS_TRAININGPLAN_FINISHED) {
                sparseFinishedArray.put(i, false)
            }
            listOldDropsets = databaseInitializer.getAllDropsets(appDatabase.dropsetDao())
            listOldExerciseDones = databaseInitializer.getAllExerciseDones(appDatabase.exerciseDoneDao())
            listOldExercises = databaseInitializer.getAllExercises(appDatabase.exerciseDao())
            listOldNormals = databaseInitializer.getAllNormals(appDatabase.normalDao())
            listOldRoutines = databaseInitializer.getAllRoutines(appDatabase.routineDao())
            listOldSetDones = databaseInitializer.getAllSetDones(appDatabase.setDoneDao())
            listOldSupersets = databaseInitializer.getAllSupersets(appDatabase.supersetDao())
            listOldTrainingplans = databaseInitializer.getAllTrainingplans(appDatabase.trainingplanDao())
        }

        override fun onPreExecute() {
            super.onPreExecute()
            val activity = (weakReferenceContext.get()!!.applicationContext as WorkoutLog).currentActivity as AppCompatActivity
            databaseInitializer.deleteAll(appDatabase.dropsetDao(), appDatabase.exerciseDao(), appDatabase.exerciseDoneDao(), appDatabase.normalDao(), appDatabase.routineDao(), appDatabase.setDoneDao(), appDatabase.supersetDao(), appDatabase.trainingplanDao())
            dialog.setListener(object : ProgressDialogFragment.ITryAgainListener {
                override fun cancelOperation() {
                    this@DownloadWholeDataFromFirestore.cancel(true)
                }

                override fun tryAgain() {
                    this@DownloadWholeDataFromFirestore.cancel(true)
                    execute()
                }

                override fun cancel() {
                    this@DownloadWholeDataFromFirestore.cancel(true)
                }
            })
            dialog.show(activity.supportFragmentManager, "downloadData")
            Handler().postDelayed({
                if(!isError) {
                    dialog.setCancelable(cancelableMessage)
                }
            }, TIMER)
        }

        override fun doInBackground(vararg params: Void?): Void? {
            val firebaseFirestore = weakReferenceFirebaseFirestore.get()!!

            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(DROPSET)
                .get()
                .addOnSuccessListener {result->
                    val listDropsets = ArrayList<Dropset>()
                    for (document in result) {
                        val dropset = document.toObject(Dropset::class.java)
                        listDropsets.add(dropset)
                    }
                    insertDropsets(listDropsets)
                }
                .addOnFailureListener {
                    setError()
                }

            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(EXERCISE_DONE)
                .get()
                .addOnSuccessListener {result->
                    if(!isCancelled) {
                        val listExerciseDones = ArrayList<ExerciseDone>()
                        for (document in result) {
                            val timestamp = document.get(DATE) as Timestamp
                            val millis = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
                            val date = Date(millis)
                            val edId = document.get(EXERCISE_DONE_ID) as Long
                            val eId = document.get(EID) as Long
                            val posOnDate = document.get(POS_ON_DATE) as Long
                            val exerciseDone = ExerciseDone(edId.toInt(), date, eId.toInt(), posOnDate.toInt())
                            listExerciseDones.add(exerciseDone)
                        }
                        insertExerciseDones(listExerciseDones)
                    }
                }
                .addOnFailureListener {
                    if(!isCancelled) {
                        setError()
                    }
                }

            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(EXERCISES)
                .get()
                .addOnSuccessListener {result->
                    if(!isCancelled) {
                        val listExercises = ArrayList<Exercise>()
                        for (document in result) {
                            val exercise = document.toObject(Exercise::class.java)
                            listExercises.add(exercise)
                        }
                        insertExercises(listExercises)
                    }
                }
                .addOnFailureListener {
                    if(!isCancelled) {
                        setError()
                    }
                }

            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(NORMAL_EXERCISES)
                .get()
                .addOnSuccessListener {result->
                    if(!isCancelled) {
                        val listNormals = ArrayList<Normal>()
                        for (document in result) {
                            val normal = document.toObject(Normal::class.java)
                            listNormals.add(normal)
                        }
                        insertNormals(listNormals)
                    }
                }
                .addOnFailureListener {
                    if(!isCancelled) {
                        setError()
                    }
                }

            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(ROUTINES)
                .get()
                .addOnSuccessListener {result->
                    if(!isCancelled) {
                        val listRoutines = ArrayList<Routine>()
                        for (document in result) {
                            val routine = document.toObject(Routine::class.java)
                            listRoutines.add(routine)
                        }
                        insertRoutines(listRoutines)
                    }
                }
                .addOnFailureListener {
                    if (!isCancelled) {
                        setError()
                    }
                }

            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(SET_DONE)
                .get()
                .addOnSuccessListener {result->
                    if(!isCancelled) {
                        val listSetDone = ArrayList<SetDone>()
                        for (document in result) {
                            val setDone = document.toObject(SetDone::class.java)
                            listSetDone.add(setDone)
                        }
                        insertSetDones(listSetDone)
                    }
                }
                .addOnFailureListener {
                    if(!isCancelled) {
                        setError()
                    }
                }

            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(TRAINING_PLANS)
                .get()
                .addOnSuccessListener {result->
                    if(!isCancelled) {
                        val listTrainingplans = ArrayList<Trainingplan>()
                        for (document in result) {
                            val trainingplan = document.toObject(Trainingplan::class.java)
                            listTrainingplans.add(trainingplan)
                        }
                        insertTrainingplans(listTrainingplans)
                    }
                }
                .addOnFailureListener {
                    if(!isCancelled) {
                        setError()
                    }
                }

            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(SUPERSET)
                .get()
                .addOnSuccessListener {result->
                    if(!isCancelled) {
                        val listSupersets = ArrayList<Superset>()
                        for (document in result) {
                            val superset = document.toObject(Superset::class.java)
                            listSupersets.add(superset)
                        }
                        insertSupersets(listSupersets)
                    }
                }
                .addOnFailureListener {
                    if(!isCancelled) {
                        setError()
                    }
                }

            return null
        }

        override fun onCancelled() {
            restoreOldData()
        }

        private fun dismissDialog() {
            dialog.dismiss()
        }

        private fun setError() {
            isError = true
            dialog.setError(errorTitle, errorMessage)
        }

        private fun isFinished(): Boolean {
            for(i in IS_DROPSET_FINISHED..IS_TRAININGPLAN_FINISHED) {
                if(!sparseFinishedArray.get(i)) return false
            }
            return true
        }

        private fun insertDropsets(listDropset: List<Dropset>) {
            if(sparseFinishedArray.get(IS_EXERCISE_FINISHED) && sparseFinishedArray.get(IS_ROUTINES_FINISHED)) {
                listDropset.forEach { dro->
                    databaseInitializer.insertDropset(appDatabase.dropsetDao(), dro)
                }
                sparseFinishedArray.put(IS_DROPSET_FINISHED, true)
                if (isFinished()){
                    dismissDialog()
                    if(onFinishedListener != null) onFinishedListener!!.downloadFinished()
                }
            } else {
                Handler().postDelayed({
                    insertDropsets(listDropset)
                }, 300)
            }
        }

        private fun insertExercises(listExercise: List<Exercise>) {
            listExercise.forEach { exc->
                databaseInitializer.insertExercise(appDatabase.exerciseDao(), exc)
            }
            sparseFinishedArray.put(IS_EXERCISE_FINISHED, true)
            if (isFinished()){
                dismissDialog()
                if(onFinishedListener != null) onFinishedListener!!.downloadFinished()
            }
        }

        private fun insertExerciseDones(listExerciseDone: List<ExerciseDone>) {
            if(sparseFinishedArray.get(IS_EXERCISE_FINISHED)) {
                listExerciseDone.forEach { exd->
                    databaseInitializer.insertExerciseDone(appDatabase.exerciseDoneDao(), exd)
                }
                sparseFinishedArray.put(IS_EXERCISE_DONE_FINISHED, true)
                if (isFinished()){
                    dismissDialog()
                    if(onFinishedListener != null) onFinishedListener!!.downloadFinished()
                }
            } else {
                Handler().postDelayed({
                    insertExerciseDones(listExerciseDone)
                }, 300)
            }
        }

        private fun insertNormals(listNormal: List<Normal>) {
            if(sparseFinishedArray.get(IS_EXERCISE_FINISHED) && sparseFinishedArray.get(IS_ROUTINES_FINISHED)) {
                listNormal.forEach { nor->
                    databaseInitializer.insertNormal(appDatabase.normalDao(), nor)
                }
                sparseFinishedArray.put(IS_NORMAL_EXC_FINISHED, true)
                if (isFinished()){
                    dismissDialog()
                    if(onFinishedListener != null) onFinishedListener!!.downloadFinished()
                }
            } else {
                Handler().postDelayed({
                    insertNormals(listNormal)
                }, 300)
            }
        }

        private fun insertRoutines(listRoutine: List<Routine>) {
            if(sparseFinishedArray.get(IS_TRAININGPLAN_FINISHED)) {
                listRoutine.forEach { rou->
                    databaseInitializer.insertRoutine(appDatabase.routineDao(), rou)
                }
                sparseFinishedArray.put(IS_ROUTINES_FINISHED, true)
                if (isFinished()){
                    dismissDialog()
                    if(onFinishedListener != null) onFinishedListener!!.downloadFinished()
                }
            } else {
                Handler().postDelayed({
                    insertRoutines(listRoutine)
                }, 300)
            }
        }

        private fun insertSetDones(listSetDone: List<SetDone>) {
            if(sparseFinishedArray.get(IS_EXERCISE_DONE_FINISHED)) {
                listSetDone.forEach { sed->
                    databaseInitializer.insertSetDone(appDatabase.setDoneDao(), sed)
                }
                sparseFinishedArray.put(IS_SET_DONE_FINISHED, true)
                if (isFinished()){
                    dismissDialog()
                    if(onFinishedListener != null) onFinishedListener!!.downloadFinished()
                }
            } else {
                Handler().postDelayed({
                    insertSetDones(listSetDone)
                }, 300)
            }
        }

        private fun insertSupersets(listSuperset: List<Superset>) {
            if(sparseFinishedArray.get(IS_EXERCISE_FINISHED) && sparseFinishedArray.get(IS_ROUTINES_FINISHED)) {
                listSuperset.forEach { sup->
                    databaseInitializer.insertSuperset(appDatabase.supersetDao(), sup)
                }
                sparseFinishedArray.put(IS_SUPERSET_FINISHED, true)
                if (isFinished()){
                    dismissDialog()
                    if(onFinishedListener != null) onFinishedListener!!.downloadFinished()
                }
            } else {
                Handler().postDelayed({
                    insertSupersets(listSuperset)
                }, 300)
            }
        }

        private fun insertTrainingplans(listTrainingplan: List<Trainingplan>) {
            listTrainingplan.forEach { trp->
                databaseInitializer.insertTrainingplan(appDatabase.trainingplanDao(), trp)
            }
            sparseFinishedArray.put(IS_TRAININGPLAN_FINISHED, true)
            if (isFinished()){
                dismissDialog()
                if(onFinishedListener != null) onFinishedListener!!.downloadFinished()
            }
        }

        private fun restoreOldData() {
            databaseInitializer.deleteAll(appDatabase.dropsetDao(), appDatabase.exerciseDao(), appDatabase.exerciseDoneDao(), appDatabase.normalDao(), appDatabase.routineDao(), appDatabase.setDoneDao(), appDatabase.supersetDao(), appDatabase.trainingplanDao())
            listOldTrainingplans.forEach {
                databaseInitializer.insertTrainingplan(appDatabase.trainingplanDao(), it)
            }
            listOldSupersets.forEach {
                databaseInitializer.insertSuperset(appDatabase.supersetDao(), it)
            }
            listOldSetDones.forEach {
                databaseInitializer.insertSetDone(appDatabase.setDoneDao(), it)
            }
            listOldRoutines.forEach {
                databaseInitializer.insertRoutine(appDatabase.routineDao(), it)
            }
            listOldNormals.forEach {
                databaseInitializer.insertNormal(appDatabase.normalDao(), it)
            }
            listOldTrainingplans.forEach {
                databaseInitializer.insertTrainingplan(appDatabase.trainingplanDao(), it)
            }
            listOldExerciseDones.forEach {
                databaseInitializer.insertExerciseDone(appDatabase.exerciseDoneDao(), it)
            }
            listOldExercises.forEach {
                databaseInitializer.insertExercise(appDatabase.exerciseDao(), it)
            }
        }

        interface OnDownloadFinishedListener {
            fun downloadFinished()
        }
    }

    private class LoadWholeDataToFirestore(private val weakReferenceFirebaseFirestore: WeakReference<FirebaseFirestore>): AsyncTask<Void, Void, Void>() {

        private lateinit var dropsetData: List<Dropset>
        private lateinit var exerciseData: List<Exercise>
        private lateinit var exerciseDoneData: List<ExerciseDone>
        private lateinit var normalData: List<Normal>
        private lateinit var routineData: List<Routine>
        private lateinit var setDoneData: List<SetDone>
        private lateinit var supersetData: List<Superset>
        private lateinit var trainingplanData: List<Trainingplan>
        private val sparseFinishedArray = SparseBooleanArray()

        private var listener: OnUploadFinishedListener? = null
        fun setOnUploadFinishedListener(listener: OnUploadFinishedListener) {
            this.listener = listener
        }

        companion object {
            private const val IS_DROPSET_FINISHED = 0
            private const val IS_EXERCISE_DONE_FINISHED = 1
            private const val IS_EXERCISE_FINISHED = 2
            private const val IS_NORMAL_EXC_FINISHED = 3
            private const val IS_ROUTINES_FINISHED = 4
            private const val IS_SET_DONE_FINISHED = 5
            private const val IS_SUPERSET_FINISHED = 6
            private const val IS_TRAININGPLAN_FINISHED = 7
        }

        init {
            for(i in IS_DROPSET_FINISHED..IS_TRAININGPLAN_FINISHED) {
                sparseFinishedArray.put(i, false)
            }
        }

        override fun onPreExecute() {
            dropsetData = databaseInitializer.getAllDropsets(appDatabase.dropsetDao())
            exerciseData = databaseInitializer.getAllExercises(appDatabase.exerciseDao())
            exerciseDoneData = databaseInitializer.getAllExerciseDones(appDatabase.exerciseDoneDao())
            normalData = databaseInitializer.getAllNormals(appDatabase.normalDao())
            routineData = databaseInitializer.getAllRoutines(appDatabase.routineDao())
            setDoneData = databaseInitializer.getAllSetDones(appDatabase.setDoneDao())
            supersetData = databaseInitializer.getAllSupersets(appDatabase.supersetDao())
            trainingplanData = databaseInitializer.getAllTrainingplans(appDatabase.trainingplanDao())
        }

        override fun doInBackground(vararg params: Void?): Void? {
            val uid = firebaseUser!!.uid
            val firebaseFirestore = weakReferenceFirebaseFirestore.get()!!

            val uidMap = hashMapOf(
                UID to uid
            )

            firebaseFirestore.collection(USERS)
                .document(uid)
                .set(uidMap)

            var exerciseCount = 0
            if(exerciseData.isEmpty()) sparseFinishedArray.put(IS_EXERCISE_FINISHED, true)
            for (i in exerciseData.indices) {
                val exc = exerciseData[i]
                firebaseFirestore.collection(USERS)
                    .document(uid)
                    .collection(EXERCISES)
                    .document(exc.eId.toString())
                    .set(exc)
                    .addOnSuccessListener {
                        exerciseCount++
                        if(exerciseCount == exerciseData.size) {
                            sparseFinishedArray.put(IS_EXERCISE_FINISHED, true)
                            if(isFinished() && !isCancelled) {
                                listener!!.uploadFinished()
                                cancel(true)
                            }
                        }
                    }
                    .addOnFailureListener {
                        if(listener != null && !isCancelled) listener!!.uploadFailed()
                        cancel(true)
                    }
            }

            var routineCount = 0
            if(routineData.isEmpty()) sparseFinishedArray.put(IS_ROUTINES_FINISHED, true)
            for (i in routineData.indices) {
                val rou = routineData[i]
                firebaseFirestore.collection(USERS)
                    .document(uid)
                    .collection(ROUTINES)
                    .document(rou.rId.toString())
                    .set(rou)
                    .addOnSuccessListener {
                        routineCount++
                        if(routineCount == routineData.size) {
                            sparseFinishedArray.put(IS_ROUTINES_FINISHED, true)
                            if(isFinished() && !isCancelled) {
                                listener!!.uploadFinished()
                                cancel(true)
                            }
                        }
                    }
                    .addOnFailureListener {
                        if(listener != null && !isCancelled) listener!!.uploadFailed()
                        cancel(true)
                    }
            }

            var trainingplanCount = 0
            if(trainingplanData.isEmpty()) sparseFinishedArray.put(IS_TRAININGPLAN_FINISHED, true)
            for (i in trainingplanData.indices) {
                val trp = trainingplanData[i]
                firebaseFirestore.collection(USERS)
                    .document(uid)
                    .collection(TRAINING_PLANS)
                    .document(trp.tpId.toString())
                    .set(trp)
                    .addOnSuccessListener {
                        trainingplanCount++
                        if(trainingplanCount == trainingplanData.size) {
                            sparseFinishedArray.put(IS_TRAININGPLAN_FINISHED, true)
                            if(isFinished() && !isCancelled) {
                                listener!!.uploadFinished()
                                cancel(true)
                            }
                        }
                    }
                    .addOnFailureListener {
                        if(listener != null && !isCancelled) listener!!.uploadFailed()
                        cancel(true)
                    }
            }

            var normalCount = 0
            if(normalData.isEmpty()) sparseFinishedArray.put(IS_NORMAL_EXC_FINISHED, true)
            for (i in normalData.indices) {
                val nor = normalData[i]
                firebaseFirestore.collection(USERS)
                    .document(uid)
                    .collection(NORMAL_EXERCISES)
                    .document(nor.nId.toString())
                    .set(nor)
                    .addOnSuccessListener {
                        normalCount++
                        if(normalCount == normalData.size) {
                            sparseFinishedArray.put(IS_NORMAL_EXC_FINISHED, true)
                            if(isFinished() && !isCancelled) {
                                listener!!.uploadFinished()
                                cancel(true)
                            }
                        }
                    }
                    .addOnFailureListener {
                        if(listener != null && !isCancelled) listener!!.uploadFailed()
                        cancel(true)
                    }
            }

            var supersetCount = 0
            if(supersetData.isEmpty()) sparseFinishedArray.put(IS_SUPERSET_FINISHED, true)
            for (i in supersetData.indices) {
                val sup = supersetData[i]
                firebaseFirestore.collection(USERS)
                    .document(uid)
                    .collection(SUPERSET)
                    .document(sup.sId.toString())
                    .set(sup)
                    .addOnSuccessListener {
                        supersetCount++
                        if(supersetCount == supersetData.size) {
                            sparseFinishedArray.put(IS_SUPERSET_FINISHED, true)
                            if(isFinished() && !isCancelled) {
                                listener!!.uploadFinished()
                                cancel(true)
                            }
                        }
                    }
                    .addOnFailureListener {
                        if(listener != null && !isCancelled) listener!!.uploadFailed()
                        cancel(true)
                    }
            }

            var dropsetCount = 0
            if(dropsetData.isEmpty()) sparseFinishedArray.put(IS_DROPSET_FINISHED, true)
            for (i in dropsetData.indices) {
                val drp = dropsetData[i]
                firebaseFirestore.collection(USERS)
                    .document(uid)
                    .collection(DROPSET)
                    .document(drp.dId.toString())
                    .set(drp)
                    .addOnSuccessListener {
                        dropsetCount++
                        if(dropsetCount == dropsetData.size) {
                            sparseFinishedArray.put(IS_DROPSET_FINISHED, true)
                            if(isFinished() && !isCancelled) {
                                listener!!.uploadFinished()
                                cancel(true)
                            }
                        }
                    }
                    .addOnFailureListener {
                        if(listener != null && !isCancelled) listener!!.uploadFailed()
                        cancel(true)
                    }
            }

            var exerciseDoneCount = 0
            if(exerciseDoneData.isEmpty()) sparseFinishedArray.put(IS_EXERCISE_DONE_FINISHED, true)
            for (i in exerciseDoneData.indices) {
                val exd = exerciseDoneData[i]
                firebaseFirestore.collection(USERS)
                    .document(uid)
                    .collection(EXERCISE_DONE)
                    .document(exd.edId.toString())
                    .set(exd)
                    .addOnSuccessListener {
                        exerciseDoneCount++
                        if(exerciseDoneCount == exerciseDoneData.size) {
                            sparseFinishedArray.put(IS_EXERCISE_DONE_FINISHED, true)
                            if(isFinished() && !isCancelled) {
                                listener!!.uploadFinished()
                                cancel(true)
                            }
                        }
                    }
                    .addOnFailureListener {
                        if(listener != null && !isCancelled) listener!!.uploadFailed()
                        cancel(true)
                    }
            }

            var setDoneCount = 0
            if(setDoneData.isEmpty()) sparseFinishedArray.put(IS_SET_DONE_FINISHED, true)
            for (i in setDoneData.indices) {
                val sed = setDoneData[i]
                firebaseFirestore.collection(USERS)
                    .document(uid)
                    .collection(SET_DONE)
                    .document(sed.sdId.toString())
                    .set(sed)
                    .addOnSuccessListener {
                        setDoneCount++
                        if(setDoneCount == setDoneData.size) {
                            sparseFinishedArray.put(IS_SET_DONE_FINISHED, true)
                            if(isFinished() && !isCancelled) {
                                listener!!.uploadFinished()
                                cancel(true)
                            }
                        }
                    }
                    .addOnFailureListener {
                        if(listener != null && !isCancelled) listener!!.uploadFailed()
                        cancel(true)
                    }
            }
            if(isFinished() && !isCancelled) {
                listener!!.uploadFinished()
                cancel(true)
            }
            return null
        }

        private fun isFinished(): Boolean {
            for(i in IS_DROPSET_FINISHED..IS_TRAININGPLAN_FINISHED) {
                if(!sparseFinishedArray.get(i)) return false
            }
            return true
        }

        interface OnUploadFinishedListener {
            fun uploadFinished()
            fun uploadFailed()
        }
    }

    private fun registerUser(user: FirebaseUser, context: Context) {
        if(!isNetworkAvailable(context)) {
            MessageDialogFragment.newInstance(context.getString(R.string.upload_failed)).show(((context.applicationContext as WorkoutLog).currentActivity as AppCompatActivity).supportFragmentManager, "uploadFailed")
            return
        }
        var isError = false
        val task = LoadWholeDataToFirestore(WeakReference(firebaseFirestore))
        val dialog = ProgressDialogFragment.newInstance(context.getString(R.string.uploading_data))
        dialog.setListener(object: ProgressDialogFragment.ITryAgainListener {
            override fun cancelOperation() {
                task.cancel(true)
            }

            override fun cancel() {
                val taskDelete = DeleteAllUserCollections(WeakReference(firebaseFirestore))
                taskDelete.execute()
            }

            override fun tryAgain() {
                uploadData(context)
            }
        })
        dialog.show(((context.applicationContext as WorkoutLog).currentActivity as AppCompatActivity).supportFragmentManager, "registerUser")
        firebaseUser = user
        task.setOnUploadFinishedListener(object: LoadWholeDataToFirestore.OnUploadFinishedListener {
            override fun uploadFinished() {
                dialog.dismiss()
            }

            override fun uploadFailed() {
                dialog.setError(context.getString(R.string.error), context.getString(R.string.an_error_occurred_uploading_the_data))
                isError = true
            }
        })
        task.execute()
        Handler().postDelayed({
            if(!isError) dialog.setCancelable(context.getString(R.string.this_operation_is_taking_very_long))
        }, TIMER)
    }

    fun loginUser(user: FirebaseUser, context: Context) {
        firebaseUser = user
        firebaseFirestore.collection(USERS)
            .document(user.uid)
            .get()
            .addOnSuccessListener { doc->
                if(doc.exists()) {
                    checkDatabaseInSync(context)
                } else {
                    registerUser(user, context)
                }
            }
    }

    fun logoutUser() {
        firebaseUser = null
    }

    fun insertExercise(exc: Exercise) {
        if (firebaseUser != null) {
            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(EXERCISES)
                .document(exc.eId.toString())
                .set(exc)
        }
    }

    fun insertExerciseDone(exd: ExerciseDone) {
        if (firebaseUser != null) {
            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(EXERCISE_DONE)
                .document(exd.edId.toString())
                .set(exd)
        }
    }

    fun insertTrainingplan(trp: Trainingplan) {
        if (firebaseUser != null) {
            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(TRAINING_PLANS)
                .document(trp.tpId.toString())
                .set(trp)
        }
    }

    fun updateExercise(exc: Exercise) {
        if (firebaseUser != null) {
            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(EXERCISES)
                .document(exc.eId.toString())
                .update(
                    EXERCISES_NAME, exc.eName
                )
        }
    }

    fun deleteExercise(exc: Exercise) {
        if (firebaseUser != null) {
            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(EXERCISES)
                .document(exc.eId.toString())
                .delete()
        }
    }

    fun deleteTrainingplan(trp: Trainingplan) {
        if (firebaseUser != null) {
            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(TRAINING_PLANS)
                .document(trp.tpId.toString())
                .delete()
        }
    }

    fun insertRoutine(rou: Routine) {
        if (firebaseUser != null) {
            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(ROUTINES)
                .document(rou.rId.toString())
                .set(rou)
        }
    }

    fun deleteRoutine(rou: Routine) {
        if (firebaseUser != null) {
            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(ROUTINES)
                .document(rou.rId.toString())
                .delete()
        }
    }

    fun updateTrainingplan(trp: Trainingplan) {
        if (firebaseUser != null) {
            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(TRAINING_PLANS)
                .document(trp.tpId.toString())
                .update(
                    TRAINING_PLANS_NAME, trp.tpName
                )
        }
    }

    fun insertNormal(nor: Normal) {
        if (firebaseUser != null) {
            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(NORMAL_EXERCISES)
                .document(nor.nId.toString())
                .set(nor)
        }
    }

    fun insertSuperset(sup: Superset) {
        if (firebaseUser != null) {
            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(SUPERSET)
                .document(sup.sId.toString())
                .set(sup)
        }
    }

    fun insertDropset(drp: Dropset) {
        if (firebaseUser != null) {
            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(DROPSET)
                .document(drp.dId.toString())
                .set(drp)
        }
    }

    fun deleteNormal(nor: Normal) {
        if (firebaseUser != null) {
            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(NORMAL_EXERCISES)
                .document(nor.nId.toString())
                .delete()
        }
    }

    fun deleteSuperset(sup: Superset) {
        if (firebaseUser != null) {
            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(SUPERSET)
                .document(sup.sId.toString())
                .delete()
        }
    }

    fun deleteDropset(drp: Dropset) {
        if (firebaseUser != null) {
            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(DROPSET)
                .document(drp.dId.toString())
                .delete()
        }
    }

    fun updateRoutine(rou: Routine) {
        if (firebaseUser != null) {
            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(ROUTINES)
                .document(rou.rId.toString())
                .update(
                    ROUTINE_NAME, rou.rName
                )
        }
    }

    fun insertSetDone(sed: SetDone) {
        if (firebaseUser != null) {
            firebaseFirestore.collection(USERS)
                .document(firebaseUser!!.uid)
                .collection(ROUTINES)
                .document(sed.sdId.toString())
                .set(sed)
        }
    }

    private fun getArrayListFromJsonString(json: String): java.util.ArrayList<Int> {
        val list = java.util.ArrayList<Int>()
        val obj = JSONObject(json)
        var x = 0
        while(obj.has(x.toString())) {
            list.add(obj.getInt(x.toString()))
            x++
        }
        return list
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}