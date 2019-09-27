package com.workoutlog.workoutlog.database;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.workoutlog.workoutlog.database.entities.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DatabaseSynchronizer {

    private class References
    {
        private static final String USERS = "Users";
        private static final String EXERCISES = "Exercises";
        private static final String EXERCISES_NAME = "eName";
        private static final String EXERCISES_ID = "eId";
        private static final String TRAINING_PLANS = "trainingPlans";
        private static final String TRAINING_PLANS_ID = "tpId";
        private static final String TRAINING_PLANS_NAME = "tpName";
        private static final String ROUTINES = "routines";
        private static final String ROUTINE_NAME = "rName";
        private static final String ROUTINE_TP_ID = "tpId";
        private static final String ROUTINE_POS_IN_TP = "posInTp";
        private static final String NORMAL_EXERCISES = "normalExc";
        private static final String NORMAL_EXERCISE_ID = "nId";
        private static final String SUPERSET = "supersetExc";
        private static final String SUPERSET_ID = "sId";
        private static final String DROPSET = "dropsetExc";
        private static final String DROPSET_ID = "dId";
        private static final String EXERCISE_E_ID = "eId";
        private static final String EXERCISE_E_ID1 = "eId1";
        private static final String EXERCISE_E_ID2 = "eId2";
        private static final String EXERCISE_SETS = "sets";
        private static final String EXERCISE_REPS = "reps";
        private static final String EXERCISE_REPS1 = "reps1";
        private static final String EXERCISE_REPS2 = "reps2";
        private static final String EXERCISE_BREAK = "break";
        private static final String EXERCISE_RPE = "rpe";
        private static final String EXERCISE_RPE1 = "rpe1";
        private static final String EXERCISE_RPE2 = "rpe2";
        private static final String EXERCISE_POS_IN_ROUTINE = "posInRoutine";
        private static final String EXERCISE_ROUTINE_ID = "rId";
        private static final String EXERCISE_DROPS = "drops";
        private static final String EXERCISE_DONE = "exerciseDone";
        private static final String EXERCISE_DONE_DATE = "date";
        private static final String EXERCISE_DONE_E_ID = "eId";
        private static final String EXERCISE_DONE_POS_ON_DATE = "posOnDte";
        private static final String SET_DONE = "setDone";
        private static final String SET_DONE_REPS = "reps";
        private static final String SET_DONE_WEIGHT = "weightInKg";
        private static final String SET_DONE_RPE = "rpe";
        private static final String SET_DONE_POS_IN_EXERCISE_DONE = "posInExcDone";
        private static final String SET_DONE_ED_ID = "edId";
    }

    private static DatabaseSynchronizer INSTANCE = null;
    private final DatabaseInitializer databaseInitializer;
    private final FirebaseFirestore firebaseFirestore;
    private final AppDatabase appDatabase;

    private DatabaseSynchronizer(Context context){
        databaseInitializer = DatabaseInitializer.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        appDatabase = AppDatabase.getInstance(context);
    }

    public static DatabaseSynchronizer getInstance(@NonNull Context context) {
        if(INSTANCE == null) {
            INSTANCE = new DatabaseSynchronizer(context);
        }
        return INSTANCE;
    }


    public void registerUser(@NonNull FirebaseUser user) throws ExecutionException, InterruptedException {
        String uid = user.getUid();
        List<Dropset> dropsetData = databaseInitializer.getAllDropsets(appDatabase.dropsetDao());
        List<Exercise> exerciseData = databaseInitializer.getAllExercises(appDatabase.exerciseDao());
        List<ExerciseDone> exerciseDoneData = databaseInitializer.getAllExerciseDones(appDatabase.exerciseDoneDao());
        List<Normal> normalData = databaseInitializer.getAllNormals(appDatabase.normalDao());
        List<Routine> routineData = databaseInitializer.getAllRoutines(appDatabase.routineDao());
        List<SetDone> setDoneData = databaseInitializer.getAllSetDones(appDatabase.setDoneDao());
        List<Superset> supersetData = databaseInitializer.getAllSupersets(appDatabase.supersetDao());
        List<Trainingplan> trainingplanData = databaseInitializer.getAllTrainingplans(appDatabase.trainingplanDao());

        for(int i = 0; i < exerciseData.size(); i++) {
            Exercise exc = exerciseData.get(i);
            firebaseFirestore.collection(References.USERS)
                    .document(uid)
                    .collection(References.EXERCISES)
                    .document(String.valueOf(exc.getEId()))
                    .set(exc);
        }

        for(int i = 0; i < routineData.size(); i++) {
            Routine rou = routineData.get(i);
            firebaseFirestore.collection(References.USERS)
                    .document(uid)
                    .collection(References.ROUTINES)
                    .document(String.valueOf(rou.getRId()))
                    .set(rou);
        }

        for(int i = 0; i < trainingplanData.size(); i++) {
            Trainingplan trp = trainingplanData.get(i);
            firebaseFirestore.collection(References.USERS)
                    .document(uid)
                    .collection(References.TRAINING_PLANS)
                    .document(String.valueOf(trp.getTpId()))
                    .set(trp);
        }

        for(int i = 0; i < normalData.size(); i++) {
            Normal nor = normalData.get(i);
            firebaseFirestore.collection(References.USERS)
                    .document(uid)
                    .collection(References.NORMAL_EXERCISES)
                    .document(String.valueOf(nor.getNId()))
                    .set(nor);
        }

        for(int i = 0; i < supersetData.size(); i++) {
            Superset sup = supersetData.get(i);
            firebaseFirestore.collection(References.USERS)
                    .document(uid)
                    .collection(References.SUPERSET)
                    .document(String.valueOf(sup.getSId()))
                    .set(sup);
        }

        for(int i = 0; i < dropsetData.size(); i++) {
            Dropset drp = dropsetData.get(i);
            firebaseFirestore.collection(References.USERS)
                    .document(uid)
                    .collection(References.DROPSET)
                    .document(String.valueOf(drp.getDId()))
                    .set(drp);
        }

        for(int i = 0; i < exerciseDoneData.size(); i++) {
            ExerciseDone exd = exerciseDoneData.get(i);
            firebaseFirestore.collection(References.USERS)
                    .document(uid)
                    .collection(References.EXERCISE_DONE)
                    .document(String.valueOf(exd.getEdId()))
                    .set(exd);
        }

        for(int i = 0; i < setDoneData.size(); i++) {
            SetDone sed = setDoneData.get(i);
            firebaseFirestore.collection(References.USERS)
                    .document(uid)
                    .collection(References.SET_DONE)
                    .document(String.valueOf(sed.getSdId()))
                    .set(sed);
        }
    }
}
