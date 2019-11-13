package com.workoutlog.workoutlog.database;

import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import com.workoutlog.workoutlog.database.daos.*;
import com.workoutlog.workoutlog.database.entities.*;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DatabaseInitializer {

    private static DatabaseSynchronizer databaseSynchronizer;
    private DatabaseInitializer(){}
    private static DatabaseInitializer INSTANCE = null;
    public static DatabaseInitializer getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = new DatabaseInitializer();
            databaseSynchronizer = DatabaseSynchronizer.Companion.getInstance(context);
        }
        return INSTANCE;
    }

    List<Dropset> getAllDropsets(@NonNull final DropsetDao dao) throws ExecutionException, InterruptedException {
        GetAllDropsets task = new GetAllDropsets(dao);
        return task.execute().get();
    }

    private static class GetAllDropsets extends AsyncTask<Void, Void, List<Dropset>> {

        private final DropsetDao mDao;

        GetAllDropsets(DropsetDao dao) {
            mDao = dao;
        }

        @Override
        protected List<Dropset> doInBackground(Void... voids) {
            return mDao.getAll();
        }
    }

    public List<Exercise> getAllExercises(@NonNull final ExerciseDao dao) throws ExecutionException, InterruptedException {
        GetAllExercises task = new GetAllExercises(dao);
        return task.execute().get();
    }

    private static class GetAllExercises extends AsyncTask<Void, Void, List<Exercise>> {

        private final ExerciseDao mDao;

        GetAllExercises(ExerciseDao dao) {
            mDao = dao;
        }

        @Override
        protected List<Exercise> doInBackground(Void... voids) {
            return mDao.getAll();
        }
    }

    public List<ExerciseDone> getAllExerciseDones(@NonNull final ExerciseDoneDao dao) throws ExecutionException, InterruptedException {
        GetAllExerciseDones task = new GetAllExerciseDones(dao);
        return task.execute().get();
    }

    private static class GetAllExerciseDones extends AsyncTask<Void, Void, List<ExerciseDone>> {

        private final ExerciseDoneDao mDao;

        GetAllExerciseDones(ExerciseDoneDao dao) { mDao = dao; }

        @Override
        protected List<ExerciseDone> doInBackground(Void... voids) {
            return mDao.getAll();
        }
    }

    List<Normal> getAllNormals(@NonNull final NormalDao dao) throws ExecutionException, InterruptedException {
        GetAllNormals task = new GetAllNormals(dao);
        return task.execute().get();
    }

    private static class GetAllNormals extends AsyncTask<Void, Void, List<Normal>> {

        private final NormalDao mDao;

        GetAllNormals(NormalDao dao) { mDao = dao; }

        @Override
        protected List<Normal> doInBackground(Void... voids) {
            return mDao.getAll();
        }
    }

    List<Routine> getAllRoutines(@NonNull final RoutineDao dao) throws ExecutionException, InterruptedException {
        GetAllRoutines task = new GetAllRoutines(dao);
        return task.execute().get();
    }

    private static class GetAllRoutines extends AsyncTask<Void, Void, List<Routine>> {

        private final RoutineDao mDao;

        GetAllRoutines(RoutineDao dao) { mDao = dao; }

        @Override
        protected List<Routine> doInBackground(Void... voids) {
            return mDao.getAll();
        }
    }

    List<SetDone> getAllSetDones(@NonNull final SetDoneDao dao) throws ExecutionException, InterruptedException {
        GetAllSetDones task = new GetAllSetDones(dao);
        return task.execute().get();
    }

    private static class GetAllSetDones extends AsyncTask<Void, Void, List<SetDone>> {

        private final SetDoneDao mDao;

        GetAllSetDones(SetDoneDao dao) { mDao = dao; }

        @Override
        protected List<SetDone> doInBackground(Void... voids) {
            return mDao.getAll();
        }
    }

    List<Superset> getAllSupersets(@NonNull final SupersetDao dao) throws ExecutionException, InterruptedException {
        GetAllSupersets task = new GetAllSupersets(dao);
        return task.execute().get();
    }

    private static class GetAllSupersets extends AsyncTask<Void, Void, List<Superset>> {

        private final SupersetDao mDao;

        GetAllSupersets(SupersetDao dao) { mDao = dao; }

        @Override
        protected List<Superset> doInBackground(Void... voids) {
            return mDao.getAll();
        }
    }

    public List<Trainingplan> getAllTrainingplans(@NonNull final TrainingplanDao dao) throws ExecutionException, InterruptedException {
        GetAllTrainingplans task = new GetAllTrainingplans(dao);
        return task.execute().get();
    }

    private static class GetAllTrainingplans extends AsyncTask<Void, Void, List<Trainingplan>> {

        private final TrainingplanDao mDao;

        GetAllTrainingplans(TrainingplanDao dao) { mDao = dao; }

        @Override
        protected List<Trainingplan> doInBackground(Void... voids) {
            return mDao.getAll();
        }
    }

    public void insertExercise(@NonNull final ExerciseDao dao, Exercise exercise) {
        InsertExercise task = new InsertExercise(dao);
        task.execute(exercise);
    }

    private static class InsertExercise extends AsyncTask<Exercise, Void, Void> {

        private final ExerciseDao mDao;
        private Exercise exercise;
        private int before;
        private int after;

        InsertExercise(ExerciseDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Exercise... exercises) {
            before = mDao.getCount();
            mDao.insertExercise(exercises[0]);
            exercise = mDao.getLastExercise();
            after = mDao.getCount();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(after > before) {
                databaseSynchronizer.insertExercise(exercise);
            }
        }
    }

    public void insertExerciseDone(@NonNull final ExerciseDoneDao dao, ExerciseDone exerciseDone) {
        InsertExerciseDone task = new InsertExerciseDone(dao);
        task.execute(exerciseDone);
    }

    private static class InsertExerciseDone extends AsyncTask<ExerciseDone, Void, Void> {

        private final ExerciseDoneDao mDao;
        private ExerciseDone exerciseDone;
        private int before;
        private int after;

        InsertExerciseDone(ExerciseDoneDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(ExerciseDone... exerciseDones) {
            before = mDao.getCount();
            mDao.insertExerciseDone(exerciseDones[0]);
            exerciseDone = mDao.getLastExerciseDone();
            after = mDao.getCount();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(after > before) {
                databaseSynchronizer.insertExerciseDone(exerciseDone);
            }
        }
    }

    public void insertTrainingplan(@NonNull final TrainingplanDao dao, Trainingplan trainingplan) {
        InsertTrainingplan task = new InsertTrainingplan(dao);
        task.execute(trainingplan);
    }

    private static class InsertTrainingplan extends AsyncTask<Trainingplan, Void, Void> {

        private final TrainingplanDao mDao;
        private Trainingplan trainingplan;
        private int before;
        private int after;

        InsertTrainingplan(TrainingplanDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Trainingplan... trainingplans) {
            before = mDao.getCount();
            mDao.insertTrainingplan(trainingplans[0]);
            trainingplan = mDao.getLastTrainingplan();
            after = mDao.getCount();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(after > before) {
                databaseSynchronizer.insertTrainingplan(trainingplan);
            }
        }
    }

    public void updateExercise(@NonNull final ExerciseDao dao, Exercise exercise) {
        UpdateExercise task = new UpdateExercise(dao);
        task.execute(exercise);
    }

    private static class UpdateExercise extends AsyncTask<Exercise, Void, Void> {

        private final ExerciseDao mDao;
        private Exercise exercise;
        private String exerciseAfter;

        UpdateExercise(ExerciseDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Exercise... exercises) {
            exercise = exercises[0];
            mDao.updateExercise(exercises[0].getEId(), exercises[0].getEName());
            exerciseAfter = mDao.getExerciseNameById(exercise.getEId());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(exerciseAfter.equals(exercise.getEName())) {
                databaseSynchronizer.updateExercise(exercise);
            }
        }
    }

    public List<String> getAllExerciseNames(@NonNull final ExerciseDao dao) throws ExecutionException, InterruptedException {
        GetAllExerciseNames task = new GetAllExerciseNames(dao);
        return task.execute().get();
    }

    private static class GetAllExerciseNames extends AsyncTask<Void, Void, List<String>> {

        private final ExerciseDao mDao;

        GetAllExerciseNames(ExerciseDao dao) { mDao = dao; }

        @Override
        protected List<String> doInBackground(Void... voids) {
            return mDao.getAllExerciseNames();
        }
    }

    public void deleteExercise(@NonNull final ExerciseDao dao, Exercise exercise) {
        DeleteExercise task = new DeleteExercise(dao);
        task.execute(exercise);
    }

    private static class DeleteExercise extends AsyncTask<Exercise, Void, Void> {

        private final ExerciseDao mDao;
        private Exercise exercise;
        private int before;
        private int after;

        DeleteExercise(ExerciseDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Exercise... exercises) {
            before = mDao.getCount();
            exercise = exercises[0];
            mDao.deleteExercise(exercises[0]);
            after = mDao.getCount();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(after < before) {
                databaseSynchronizer.deleteExercise(exercise);
            }
        }
    }

    public void deleteTrainingplan(@NonNull final TrainingplanDao dao, Trainingplan trainingplan) {
        DeleteTrainingplan task = new DeleteTrainingplan(dao);
        task.execute(trainingplan);
    }

    private static class DeleteTrainingplan extends AsyncTask<Trainingplan, Void, Void> {

        private final TrainingplanDao mDao;
        private Trainingplan trainingplan;
        private int before;
        private int after;

        DeleteTrainingplan(TrainingplanDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Trainingplan... trainingplans) {
            trainingplan = trainingplans[0];
            before = mDao.getCount();
            mDao.deleteTrainingplan(trainingplans[0]);
            after = mDao.getCount();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(after < before) {
                databaseSynchronizer.deleteTrainingplan(trainingplan);
            }
        }
    }

    public List<String> getAllTrainingplanNames(@NonNull final TrainingplanDao dao) throws ExecutionException, InterruptedException {
        GetAllTrainingplanNames task = new GetAllTrainingplanNames(dao);
        return task.execute().get();
    }

    private static class GetAllTrainingplanNames extends AsyncTask<Void, Void, List<String>> {

        private final TrainingplanDao mDao;

        GetAllTrainingplanNames(TrainingplanDao dao) { mDao = dao; }

        @Override
        protected List<String> doInBackground(Void... voids) {
            return mDao.getAllTrainingplanNames();
        }
    }

    public Trainingplan getTrainingplanById(@NonNull final TrainingplanDao dao, int id) throws ExecutionException, InterruptedException {
        GetTrainingplanById task = new GetTrainingplanById(dao);
        return task.execute(id).get();
    }

    private static class GetTrainingplanById extends AsyncTask<Integer, Void, Trainingplan> {

        private final TrainingplanDao mDao;

        GetTrainingplanById(TrainingplanDao dao) { mDao = dao; }

        @Override
        protected Trainingplan doInBackground(Integer... integers) {
            return mDao.getTrainingplanById(integers[0]);
        }
    }

    public Trainingplan getLastTrainingplan(@NonNull final TrainingplanDao dao) throws ExecutionException, InterruptedException {
        GetLastTrainingplan task = new GetLastTrainingplan(dao);
        return task.execute().get();
    }

    private static class GetLastTrainingplan extends AsyncTask<Void, Void, Trainingplan> {

        private final TrainingplanDao mDao;

        GetLastTrainingplan(TrainingplanDao dao) { mDao = dao; }

        @Override
        protected Trainingplan doInBackground(Void... voids) {
            return mDao.getLastTrainingplan();
        }
    }

    public List<Routine> getRoutinesByTpId(@NonNull final RoutineDao dao, int tpId) throws ExecutionException, InterruptedException {
        GetRoutinesByTpId task = new GetRoutinesByTpId(dao);
        return task.execute(tpId).get();
    }

    private static class GetRoutinesByTpId extends AsyncTask<Integer, Void, List<Routine>> {

        private final RoutineDao mDao;

        GetRoutinesByTpId(RoutineDao dao) { mDao = dao; }

        @Override
        protected List<Routine> doInBackground(Integer... integers) {
            return mDao.getRoutinesByTpId(integers[0]);
        }
    }

    public List<String> getRoutineNamesByTpId(@NonNull final RoutineDao dao, int tpId) throws ExecutionException, InterruptedException {
        GetRoutineNamesByTpId task = new GetRoutineNamesByTpId(dao);
        return task.execute(tpId).get();
    }

    private static class GetRoutineNamesByTpId extends AsyncTask<Integer, Void, List<String>> {

        private final RoutineDao mDao;

        GetRoutineNamesByTpId(RoutineDao dao) { mDao = dao; }

        @Override
        protected List<String> doInBackground(Integer... integers) {
            return mDao.getRoutineNamesByTpId(integers[0]);
        }
    }

    public int getNumberRoutinesInTp(@NonNull final RoutineDao dao, int tpId) throws ExecutionException, InterruptedException {
        GetNumberRoutinesInTp task = new GetNumberRoutinesInTp(dao);
        return task.execute(tpId).get();
    }

    private static class GetNumberRoutinesInTp extends AsyncTask<Integer, Void, Integer> {

        private final RoutineDao mDao;

        GetNumberRoutinesInTp(RoutineDao dao) { mDao = dao; }

        @Override
        protected Integer doInBackground(Integer... integers) {
            return mDao.getNumberRoutinesInTp(integers[0]);
        }
    }

    public void insertRoutine(@NonNull final RoutineDao dao, Routine routine){
        InsertRoutine task = new InsertRoutine(dao);
        task.execute(routine);
    }

    private static class InsertRoutine extends AsyncTask<Routine, Void, Void> {

        private final RoutineDao mDao;
        private Routine routine;
        private int before;
        private int after;

        InsertRoutine(RoutineDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Routine... routines) {
            before = mDao.getCount();
            mDao.insertRoutine(routines[0]);
            routine = mDao.getLastRoutine();
            after = mDao.getCount();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(after > before) {
                databaseSynchronizer.insertRoutine(routine);
            }
        }
    }

    public Routine getLastRoutine(@NonNull final RoutineDao dao) throws ExecutionException, InterruptedException {
        GetLastRoutine task = new GetLastRoutine(dao);
        return task.execute().get();
    }

    private static class GetLastRoutine extends AsyncTask<Void, Void, Routine> {

        private final RoutineDao mDao;

        GetLastRoutine(RoutineDao dao) { mDao = dao; }

        @Override
        protected Routine doInBackground(Void... voids) {
            return mDao.getLastRoutine();
        }
    }

    public void deleteRoutine(@NonNull final RoutineDao dao, Routine routine) {
        DeleteRoutine task = new DeleteRoutine(dao);
        task.execute(routine);
    }

    private static class DeleteRoutine extends AsyncTask<Routine, Void, Void> {

        private final RoutineDao mDao;
        private Routine routine;
        private int before;
        private int after;

        DeleteRoutine(RoutineDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Routine... routines) {
            routine = routines[0];
            before = mDao.getCount();
            mDao.deleteRoutine(routines[0]);
            after = mDao.getCount();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(after < before) {
                databaseSynchronizer.deleteRoutine(routine);
            }
        }
    }

    public void updateTrainingplan(@NonNull final TrainingplanDao dao, Trainingplan trainingplan) {
        UpdateTrainingplan task = new UpdateTrainingplan(dao);
        task.execute(trainingplan);
    }

    private static class UpdateTrainingplan extends AsyncTask<Trainingplan, Void, Void> {

        private final TrainingplanDao mDao;
        private Trainingplan trainingplan;
        private String trainingplanAfter;

        UpdateTrainingplan(TrainingplanDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Trainingplan... trainingplans) {
            trainingplan = trainingplans[0];
            mDao.updateTrainingplan(trainingplans[0].getTpId(), trainingplans[0].getTpName());
            trainingplanAfter = mDao.getTrainingplanById(trainingplan.getTpId()).getTpName();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(trainingplanAfter.equals(trainingplan.getTpName())) {
                databaseSynchronizer.updateTrainingplan(trainingplan);
            }
        }
    }

    public Routine getRoutineById(@NonNull final RoutineDao dao, int rId) throws ExecutionException, InterruptedException {
        GetRoutineById task = new GetRoutineById(dao);
        return task.execute(rId).get();
    }

    private static class GetRoutineById extends AsyncTask<Integer, Void, Routine> {

        private final RoutineDao mDao;

        GetRoutineById(RoutineDao dao) { mDao = dao; }

        @Override
        protected Routine doInBackground(Integer... integers) {
            return mDao.getRoutineById(integers[0]);
        }
    }

    public void insertNormal(@NonNull final NormalDao dao, Normal normal) {
        InsertNormal task = new InsertNormal(dao);
        task.execute(normal);
    }

    private static class InsertNormal extends AsyncTask<Normal, Void, Void> {

        private final NormalDao mDao;
        private Normal normal;
        private int before;
        private int after;

        InsertNormal(NormalDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Normal... normals) {
            before = mDao.getCount();
            mDao.insertNormal(normals[0]);
            normal = mDao.getLastNormal();
            after = mDao.getCount();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(after > before) {
                databaseSynchronizer.insertNormal(normal);
            }
        }
    }

    public void insertSuperset(@NonNull final SupersetDao dao, Superset superset) {
        InsertSuperset task = new InsertSuperset(dao);
        task.execute(superset);
    }

    private static class InsertSuperset extends AsyncTask<Superset, Void, Void> {

        private final SupersetDao mDao;
        private Superset superset;
        private int before;
        private int after;

        InsertSuperset(SupersetDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Superset... supersets) {
            before = mDao.getCount();
            mDao.insertSuperset(supersets[0]);
            superset = mDao.getLastSuperset();
            after = mDao.getCount();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(after > before) {
                databaseSynchronizer.insertSuperset(superset);
            }
        }
    }

    public void insertDropset(@NonNull final DropsetDao dao, Dropset dropset) {
        InsertDropset task = new InsertDropset(dao);
        task.execute(dropset);
    }

    private static class InsertDropset extends AsyncTask<Dropset, Void, Void> {

        private final DropsetDao mDao;
        private Dropset dropset;
        private int before;
        private int after;

        InsertDropset(DropsetDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Dropset... dropsets) {
            before = mDao.getCount();
            mDao.insertDropset(dropsets[0]);
            dropset = mDao.getLastDropset();
            after = mDao.getCount();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(after > before) {
                databaseSynchronizer.insertDropset(dropset);
            }
        }
    }

    public void deleteNormal(@NonNull final NormalDao normalDao, Normal normal) {
        DeleteNormal task = new DeleteNormal(normalDao);
        task.execute(normal);
    }

    private static class DeleteNormal extends AsyncTask<Normal, Void, Void> {

        private final NormalDao mDao;
        private Normal normal;
        private int before;
        private int after;

        DeleteNormal(NormalDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Normal... normals) {
            normal = normals[0];
            before = mDao.getCount();
            mDao.deleteNormal(normals[0]);
            after = mDao.getCount();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(after < before) {
                databaseSynchronizer.deleteNormal(normal);
            }
        }
    }

    public void deleteSuperset(@NonNull final SupersetDao supersetDao, Superset superset) {
        DeleteSuperset task = new DeleteSuperset(supersetDao);
        task.execute(superset);
    }

    private static class DeleteSuperset extends AsyncTask<Superset, Void, Void> {

        private final SupersetDao mDao;
        private Superset superset;
        private int before;
        private int after;

        DeleteSuperset(SupersetDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Superset... supersets) {
            superset = supersets[0];
            before = mDao.getCount();
            mDao.deleteSuperset(supersets[0]);
            after = mDao.getCount();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(after < before) {
                databaseSynchronizer.deleteSuperset(superset);
            }
        }
    }

    public void deleteDropset(@NonNull final DropsetDao dropsetDao, Dropset dropset) {
        DeleteDropset task = new DeleteDropset(dropsetDao);
        task.execute(dropset);
    }

    private static class DeleteDropset extends AsyncTask<Dropset, Void, Void> {

        private final DropsetDao mDao;
        private Dropset dropset;
        private int before;
        private int after;

        DeleteDropset(DropsetDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Dropset... dropsets) {
            dropset = dropsets[0];
            before = mDao.getCount();
            mDao.deleteDropset(dropsets[0]);
            after = mDao.getCount();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(after < before) {
                databaseSynchronizer.deleteDropset(dropset);
            }
        }
    }

    public List<Normal> getNormalsByRoutineId(@NonNull final NormalDao dao, int rId) throws ExecutionException, InterruptedException {
        GetNormalsByRoutineId task = new GetNormalsByRoutineId(dao);
        return task.execute(rId).get();
    }

    private static class GetNormalsByRoutineId extends AsyncTask<Integer, Void, List<Normal>> {

        private final NormalDao mDao;

        GetNormalsByRoutineId(NormalDao dao) { mDao = dao; }

        @Override
        protected List<Normal> doInBackground(Integer... integers) {
            return mDao.getNormalsByRoutineId(integers[0]);
        }
    }

    public List<Superset> getSupersetsByRoutineId(@NonNull final SupersetDao dao, int rId) throws ExecutionException, InterruptedException {
        GetSupersetsByRoutineId task = new GetSupersetsByRoutineId(dao);
        return task.execute(rId).get();
    }

    private static class GetSupersetsByRoutineId extends AsyncTask<Integer, Void, List<Superset>> {

        private final SupersetDao mDao;

        GetSupersetsByRoutineId(SupersetDao dao) { mDao = dao; }

        @Override
        protected List<Superset> doInBackground(Integer... integers) {
            return mDao.getSupersetsByRoutineId(integers[0]);
        }
    }

    public List<Dropset> getDropsetsByRoutineId(@NonNull final DropsetDao dao, int rId) throws ExecutionException, InterruptedException {
        GetDropsetsByRoutineId task = new GetDropsetsByRoutineId(dao);
        return task.execute(rId).get();
    }

    private static class GetDropsetsByRoutineId extends AsyncTask<Integer, Void, List<Dropset>> {

        private final DropsetDao mDao;

        GetDropsetsByRoutineId(DropsetDao dao) { mDao = dao; }

        @Override
        protected List<Dropset> doInBackground(Integer... integers) {
            return mDao.getDropsetsByRoutineId(integers[0]);
        }
    }

    public void updateRoutine(@NonNull final RoutineDao dao, int rId, String rName) {
        UpdateRoutine task = new UpdateRoutine(dao);
        Routine routine = new Routine(rId, rName, 0, 0);
        task.execute(routine);
    }

    private static class UpdateRoutine extends AsyncTask<Routine, Void, Void> {

        private final RoutineDao mDao;
        private Routine routine;
        private String routineAfter;

        UpdateRoutine(RoutineDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Routine... routines) {
            routine = routines[0];
            mDao.updateRoutine(routines[0].getRId(), routines[0].getRName());
            routineAfter = mDao.getRoutineById(routine.getRId()).getRName();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(routineAfter.equals(routine.getRName())) {
                databaseSynchronizer.updateRoutine(routine);
            }
        }
    }

    public List<Normal> getNormalsByEId(@NonNull final NormalDao dao, int eId) throws ExecutionException, InterruptedException {
        GetNormalsByEId task = new GetNormalsByEId(dao);
        return task.execute(eId).get();
    }

    private static class GetNormalsByEId extends AsyncTask<Integer, Void, List<Normal>> {

        private final NormalDao mDao;

        GetNormalsByEId(NormalDao dao) { mDao = dao; }

        @Override
        protected List<Normal> doInBackground(Integer... integers) {
            return mDao.getNormalsByEId(integers[0]);
        }
    }

    public List<Superset> getSupersetsByEId(@NonNull final SupersetDao dao, int eId) throws ExecutionException, InterruptedException {
        GetSupersetsByEId task = new GetSupersetsByEId(dao);
        return task.execute(eId).get();
    }

    private static class GetSupersetsByEId extends AsyncTask<Integer, Void, List<Superset>> {

        private final SupersetDao mDao;

        GetSupersetsByEId(SupersetDao dao) { mDao = dao; }

        @Override
        protected List<Superset> doInBackground(Integer... integers) {
            return mDao.getSupersetsByEId(integers[0]);
        }
    }

    public List<Dropset> getDropsetsByEId(@NonNull final DropsetDao dao, int eId) throws ExecutionException, InterruptedException {
        GetDropsetsByEId task = new GetDropsetsByEId(dao);
        return task.execute(eId).get();
    }

    private static class GetDropsetsByEId extends AsyncTask<Integer, Void, List<Dropset>> {

        private final DropsetDao mDao;

        GetDropsetsByEId(DropsetDao dao) { mDao = dao; }

        @Override
        protected List<Dropset> doInBackground(Integer... integers) {
            return mDao.getDropsetsByEId(integers[0]);
        }
    }

    public List<ExerciseDone> getExerciseDonesByEId(@NonNull final ExerciseDoneDao dao, int eId) throws ExecutionException, InterruptedException {
        GetExerciseDonesByEId task = new GetExerciseDonesByEId(dao);
        return task.execute(eId).get();
    }

    private static class GetExerciseDonesByEId extends AsyncTask<Integer, Void, List<ExerciseDone>> {

        private final ExerciseDoneDao mDao;

        GetExerciseDonesByEId(ExerciseDoneDao dao) { mDao = dao; }

        @Override
        protected List<ExerciseDone> doInBackground(Integer... integers) {
            return mDao.getExerciseDonesByEId(integers[0]);
        }
    }

    public void insertSetDone(@NonNull final SetDoneDao dao, SetDone setDone) {
        InsertSetDone task = new InsertSetDone(dao);
        task.execute(setDone);
    }

    private static class InsertSetDone extends AsyncTask<SetDone, Void, Void> {

        private final SetDoneDao mDao;
        private SetDone setDone;
        private int before;
        private int after;

        InsertSetDone(SetDoneDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(SetDone... setDones) {
            before = mDao.getCount();
            mDao.insertSetDone(setDones[0]);
            setDone = mDao.getLastSetDone();
            after = mDao.getCount();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(after > before) {
                databaseSynchronizer.insertSetDone(setDone);
            }
        }
    }

    public List<SetDone> getSetDonesByEdId(@NonNull final SetDoneDao dao, int edId) throws ExecutionException, InterruptedException {
        GetSetDonesByEdId task = new GetSetDonesByEdId(dao);
        return task.execute(edId).get();
    }

    private static class GetSetDonesByEdId extends AsyncTask<Integer, Void, List<SetDone>> {

        private final SetDoneDao mDao;

        GetSetDonesByEdId(SetDoneDao dao) { mDao = dao; }

        @Override
        protected List<SetDone> doInBackground(Integer... integers) {
            return mDao.getSetDonesByEdId(integers[0]);
        }
    }

    public int getNumberExerciseDonesOnDate(@NonNull final ExerciseDoneDao dao, Date date) throws ExecutionException, InterruptedException {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date.getTime());
        c.add(Calendar.DAY_OF_MONTH, -1);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        Date startDate = new Date(c.getTimeInMillis());
        c.setTimeInMillis(date.getTime());
        c.add(Calendar.DAY_OF_MONTH, +1);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        Date endDate = new Date(c.getTimeInMillis());
        GetNumberExerciseDonesOnDate task = new GetNumberExerciseDonesOnDate(dao);
        return task.execute(startDate, endDate).get();
    }

    private static class GetNumberExerciseDonesOnDate extends AsyncTask<Date, Void, Integer> {

        private final ExerciseDoneDao mDao;

        GetNumberExerciseDonesOnDate(ExerciseDoneDao dao) { mDao = dao; }

        @Override
        protected Integer doInBackground(Date... dates) {
            return mDao.getNumberOnDate(dates[0], dates[1]);
        }
    }

    public int getLastEdId(@NonNull final ExerciseDoneDao dao) throws ExecutionException, InterruptedException {
        GetLastEdId task = new GetLastEdId(dao);
        return task.execute().get();
    }

    private static class GetLastEdId extends AsyncTask<Void, Void, Integer> {

        private final ExerciseDoneDao mDao;

        GetLastEdId(ExerciseDoneDao dao) { mDao = dao; }

        @Override
        protected Integer doInBackground(Void... voids) {
            return mDao.getLastEdId();
        }
    }

    public String getExerciseNameById(@NonNull final ExerciseDao dao, int eId) throws ExecutionException, InterruptedException {
        GetExerciseNameById task = new GetExerciseNameById(dao);
        return task.execute(eId).get();
    }

    private static class GetExerciseNameById extends AsyncTask<Integer, Void, String> {

        private final ExerciseDao mDao;

        GetExerciseNameById(ExerciseDao dao) { mDao = dao; }

        @Override
        protected String doInBackground(Integer... integers) {
            return mDao.getExerciseNameById(integers[0]);
        }
    }

    public List<ExerciseDone> getExerciseDonesByDate(@NonNull final ExerciseDoneDao dao, Date date) throws ExecutionException, InterruptedException {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date.getTime());
        c.add(Calendar.DAY_OF_MONTH, -1);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        Date startDate = new Date(c.getTimeInMillis());
        c.setTimeInMillis(date.getTime());
        c.add(Calendar.DAY_OF_MONTH, +1);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        Date endDate = new Date(c.getTimeInMillis());

        GetExerciseDonesByDate task = new GetExerciseDonesByDate(dao);
        return task.execute(startDate, endDate).get();
    }

    private static class GetExerciseDonesByDate extends AsyncTask<Date, Void, List<ExerciseDone>> {

        private final ExerciseDoneDao mDao;

        GetExerciseDonesByDate(ExerciseDoneDao dao) { mDao = dao; }

        @Override
        protected List<ExerciseDone> doInBackground(Date... dates) {
            return mDao.getExerciseDonesByDate(dates[0], dates[1]);
        }
    }

    public boolean existsNoNextWorkout(@NonNull final ExerciseDoneDao dao, Date date) throws ExecutionException, InterruptedException {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date.getTime());
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        GetNumExerciseDonesFromDate task = new GetNumExerciseDonesFromDate(dao);
        int num = task.execute(new Date(c.getTimeInMillis())).get();
        return num <= 0;
    }

    private static class GetNumExerciseDonesFromDate extends AsyncTask<Date, Void, Integer> {

        private final ExerciseDoneDao mDao;

        GetNumExerciseDonesFromDate(ExerciseDoneDao dao) { mDao = dao; }

        @Override
        protected Integer doInBackground(Date... dates) {
            return mDao.getNumExerciseDonesFromDate(dates[0]);
        }
    }

    public boolean existsNoPrevWorkout(@NonNull final ExerciseDoneDao dao, Date date) throws ExecutionException, InterruptedException {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date.getTime());
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        GetNumExerciseDonesUntilDate task = new GetNumExerciseDonesUntilDate(dao);
        int num = task.execute(new Date(c.getTimeInMillis())).get();
        return num <= 0;
    }

    private static class GetNumExerciseDonesUntilDate extends AsyncTask<Date, Void, Integer> {

        private final ExerciseDoneDao mDao;

        GetNumExerciseDonesUntilDate(ExerciseDoneDao dao) { mDao = dao; }

        @Override
        protected Integer doInBackground(Date... dates) {
            return mDao.getNumExerciseDonesUntilDate(dates[0]);
        }
    }

    public boolean existsWorkoutOnDate(@NonNull final ExerciseDoneDao dao, Date date) throws ExecutionException, InterruptedException {
        return getExerciseDonesByDate(dao, date).size() > 0;
    }

    public void deleteAll(@NonNull final DropsetDao dropsetDao,
                          @NonNull final ExerciseDao exerciseDao,
                          @NonNull final ExerciseDoneDao exerciseDoneDao,
                          @NonNull final NormalDao normalDao,
                          @NonNull final RoutineDao routineDao,
                          @NonNull final SetDoneDao setDoneDao,
                          @NonNull final SupersetDao supersetDao,
                          @NonNull final TrainingplanDao trainingplanDao) {
        DeleteAll task = new DeleteAll(dropsetDao,exerciseDao, exerciseDoneDao, normalDao, routineDao, setDoneDao, supersetDao, trainingplanDao);
        task.execute();
    }

    private static class DeleteAll extends AsyncTask<Void, Void, Void> {

        private final DropsetDao mDropsetDao;
        private final ExerciseDao mExerciseDao;
        private final ExerciseDoneDao mExerciseDoneDao;
        private final NormalDao mNormalDao;
        private final RoutineDao mRoutineDao;
        private final SetDoneDao mSetDoneDao;
        private final SupersetDao mSupersetDao;
        private final TrainingplanDao mTrainingplanDao;

        DeleteAll(DropsetDao dropsetDao,
                  ExerciseDao exerciseDao,
                  ExerciseDoneDao exerciseDoneDao,
                  NormalDao normalDao,
                  RoutineDao routineDao,
                  SetDoneDao setDoneDao,
                  SupersetDao supersetDao,
                  TrainingplanDao trainingplanDao) {
            mDropsetDao = dropsetDao;
            mExerciseDao = exerciseDao;
            mExerciseDoneDao = exerciseDoneDao;
            mNormalDao = normalDao;
            mRoutineDao = routineDao;
            mSetDoneDao = setDoneDao;
            mSupersetDao = supersetDao;
            mTrainingplanDao = trainingplanDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            List<Dropset> dropsets = mDropsetDao.getAll();
            for(int i = 0; i < dropsets.size(); i++) {
                mDropsetDao.deleteDropset(dropsets.get(i));
            }
            List<Exercise> exercises = mExerciseDao.getAll();
            for (int i = 0; i < exercises.size(); i++) {
                mExerciseDao.deleteExercise(exercises.get(i));
            }
            List<ExerciseDone> exerciseDones = mExerciseDoneDao.getAll();
            for (int i = 0; i < exerciseDones.size(); i++) {
                mExerciseDoneDao.deleteExerciseDone(exerciseDones.get(i));
            }
            List<Normal> normals = mNormalDao.getAll();
            for(int i = 0; i < normals.size(); i++) {
                mNormalDao.deleteNormal(normals.get(i));
            }
            List<Routine> routines = mRoutineDao.getAll();
            for(int i = 0; i < routines.size(); i++) {
                mRoutineDao.deleteRoutine(routines.get(i));
            }
            List<SetDone> setDones = mSetDoneDao.getAll();
            for(int i = 0; i < setDones.size(); i++) {
                mSetDoneDao.deleteSetDone(setDones.get(i));
            }
            List<Superset> supersets = mSupersetDao.getAll();
            for(int i = 0; i < supersets.size(); i++) {
                mSupersetDao.deleteSuperset(supersets.get(i));
            }
            List<Trainingplan> trainingplans = mTrainingplanDao.getAll();
            for(int i = 0; i < trainingplans.size(); i++) {
                mTrainingplanDao.deleteTrainingplan(trainingplans.get(i));
            }
            return null;
        }
    }
}
