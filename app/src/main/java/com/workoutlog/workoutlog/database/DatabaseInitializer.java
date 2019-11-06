package com.workoutlog.workoutlog.database;

import android.os.AsyncTask;
import androidx.annotation.NonNull;
import com.workoutlog.workoutlog.database.daos.*;
import com.workoutlog.workoutlog.database.entities.*;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DatabaseInitializer {

    private DatabaseInitializer(){}
    private static DatabaseInitializer INSTANCE = null;
    public static DatabaseInitializer getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new DatabaseInitializer();
        }
        return INSTANCE;
    }

    public List<Dropset> getAllDropsets(@NonNull final DropsetDao dao) throws ExecutionException, InterruptedException {
        GetAllDropsets task = new GetAllDropsets(dao);
        return task.execute().get();
    }

    private static class GetAllDropsets extends AsyncTask<Void, Void, List<Dropset>> {

        final DropsetDao mDao;

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

        final ExerciseDao mDao;

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

        final ExerciseDoneDao mDao;

        GetAllExerciseDones(ExerciseDoneDao dao) { mDao = dao; }

        @Override
        protected List<ExerciseDone> doInBackground(Void... voids) {
            return mDao.getAll();
        }
    }

    public List<Normal> getAllNormals(@NonNull final NormalDao dao) throws ExecutionException, InterruptedException {
        GetAllNormals task = new GetAllNormals(dao);
        return task.execute().get();
    }

    private static class GetAllNormals extends AsyncTask<Void, Void, List<Normal>> {

        final NormalDao mDao;

        GetAllNormals(NormalDao dao) { mDao = dao; }

        @Override
        protected List<Normal> doInBackground(Void... voids) {
            return mDao.getAll();
        }
    }

    public List<Routine> getAllRoutines(@NonNull final RoutineDao dao) throws ExecutionException, InterruptedException {
        GetAllRoutines task = new GetAllRoutines(dao);
        return task.execute().get();
    }

    private static class GetAllRoutines extends AsyncTask<Void, Void, List<Routine>> {

        final RoutineDao mDao;

        GetAllRoutines(RoutineDao dao) { mDao = dao; }

        @Override
        protected List<Routine> doInBackground(Void... voids) {
            return mDao.getAll();
        }
    }

    public List<SetDone> getAllSetDones(@NonNull final SetDoneDao dao) throws ExecutionException, InterruptedException {
        GetAllSetDones task = new GetAllSetDones(dao);
        return task.execute().get();
    }

    private static class GetAllSetDones extends AsyncTask<Void, Void, List<SetDone>> {

        final SetDoneDao mDao;

        GetAllSetDones(SetDoneDao dao) { mDao = dao; }

        @Override
        protected List<SetDone> doInBackground(Void... voids) {
            return mDao.getAll();
        }
    }

    public List<Superset> getAllSupersets(@NonNull final SupersetDao dao) throws ExecutionException, InterruptedException {
        GetAllSupersets task = new GetAllSupersets(dao);
        return task.execute().get();
    }

    private static class GetAllSupersets extends AsyncTask<Void, Void, List<Superset>> {

        final SupersetDao mDao;

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

        final TrainingplanDao mDao;

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

        final ExerciseDao mDao;

        InsertExercise(ExerciseDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Exercise... exercises) {
            mDao.insertExercise(exercises[0]);
            return null;
        }
    }

    public void insertExerciseDone(@NonNull final ExerciseDoneDao dao, ExerciseDone exerciseDone) {
        InsertExerciseDone task = new InsertExerciseDone(dao);
        task.execute(exerciseDone);
    }

    private static class InsertExerciseDone extends AsyncTask<ExerciseDone, Void, Void> {

        final ExerciseDoneDao mDao;

        InsertExerciseDone(ExerciseDoneDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(ExerciseDone... exerciseDones) {
            mDao.insertExerciseDone(exerciseDones[0]);
            return null;
        }
    }

    public void insertTrainingplan(@NonNull final TrainingplanDao dao, Trainingplan trainingplan) {
        InsertTrainingplan task = new InsertTrainingplan(dao);
        task.execute(trainingplan);
    }

    private static class InsertTrainingplan extends AsyncTask<Trainingplan, Void, Void> {

        final TrainingplanDao mDao;

        InsertTrainingplan(TrainingplanDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Trainingplan... trainingplans) {
            mDao.insertTrainingplan(trainingplans[0]);
            return null;
        }
    }

    public void updateExercise(@NonNull final ExerciseDao dao, Exercise exercise) {
        UpdateExercise task = new UpdateExercise(dao);
        task.execute(exercise);
    }

    private static class UpdateExercise extends AsyncTask<Exercise, Void, Void> {

        final ExerciseDao mDao;

        UpdateExercise(ExerciseDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Exercise... exercises) {
            mDao.updateExercise(exercises[0].getEId(), exercises[0].getEName());
            return null;
        }
    }

    public List<String> getAllExerciseNames(@NonNull final ExerciseDao dao) throws ExecutionException, InterruptedException {
        GetAllExerciseNames task = new GetAllExerciseNames(dao);
        return task.execute().get();
    }

    private static class GetAllExerciseNames extends AsyncTask<Void, Void, List<String>> {

        final ExerciseDao mDao;

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

        final ExerciseDao mDao;

        DeleteExercise(ExerciseDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Exercise... exercises) {
            mDao.deleteExercise(exercises[0]);
            return null;
        }
    }

    public void deleteTrainingplan(@NonNull final TrainingplanDao dao, Trainingplan trainingplan) {
        DeleteTrainingplan task = new DeleteTrainingplan(dao);
        task.execute(trainingplan);
    }

    private static class DeleteTrainingplan extends AsyncTask<Trainingplan, Void, Void> {

        final TrainingplanDao mDao;

        DeleteTrainingplan(TrainingplanDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Trainingplan... trainingplans) {
            mDao.deleteTrainingplan(trainingplans[0]);
            return null;
        }
    }

    public List<String> getAllTrainingplanNames(@NonNull final TrainingplanDao dao) throws ExecutionException, InterruptedException {
        GetAllTrainingplanNames task = new GetAllTrainingplanNames(dao);
        return task.execute().get();
    }

    private static class GetAllTrainingplanNames extends AsyncTask<Void, Void, List<String>> {

        final TrainingplanDao mDao;

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

        final TrainingplanDao mDao;

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

        final TrainingplanDao mDao;

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

        final RoutineDao mDao;

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

        final RoutineDao mDao;

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

        final RoutineDao mDao;

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

        final RoutineDao mDao;

         InsertRoutine(RoutineDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Routine... routines) {
             mDao.insertRoutine(routines[0]);
            return null;
        }
    }

    public Routine getLastRoutine(@NonNull final RoutineDao dao) throws ExecutionException, InterruptedException {
        GetLastRoutine task = new GetLastRoutine(dao);
        return task.execute().get();
    }

    private static class GetLastRoutine extends AsyncTask<Void, Void, Routine> {

        final RoutineDao mDao;

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

        final RoutineDao mDao;

        DeleteRoutine(RoutineDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Routine... routines) {
            mDao.deleteRoutine(routines[0]);
            return null;
        }
    }

    public void updateTrainingplan(@NonNull final TrainingplanDao dao, Trainingplan trainingplan) {
        UpdateTrainingplan task = new UpdateTrainingplan(dao);
        task.execute(trainingplan);
    }

    private static class UpdateTrainingplan extends AsyncTask<Trainingplan, Void, Void> {

         final TrainingplanDao mDao;

         UpdateTrainingplan(TrainingplanDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Trainingplan... trainingplans) {
            mDao.updateTrainingplan(trainingplans[0].getTpId(), trainingplans[0].getTpName());
            return null;
        }
    }

    public Routine getRoutineById(@NonNull final RoutineDao dao, int rId) throws ExecutionException, InterruptedException {
        GetRoutineById task = new GetRoutineById(dao);
        return task.execute(rId).get();
    }

    private static class GetRoutineById extends AsyncTask<Integer, Void, Routine> {

        final RoutineDao mDao;

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

        final NormalDao mDao;

        InsertNormal(NormalDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Normal... normals) {
            mDao.insertNormal(normals[0]);
            return null;
        }
    }

    public void insertSuperset(@NonNull final SupersetDao dao, Superset superset) {
        InsertSuperset task = new InsertSuperset(dao);
        task.execute(superset);
    }

    private static class InsertSuperset extends AsyncTask<Superset, Void, Void> {

        final SupersetDao mDao;

        InsertSuperset(SupersetDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Superset... supersets) {
            mDao.insertSuperset(supersets[0]);
            return null;
        }
    }

    public void insertDropset(@NonNull final DropsetDao dao, Dropset dropset) {
        InsertDropset task = new InsertDropset(dao);
        task.execute(dropset);
    }

    private static class InsertDropset extends AsyncTask<Dropset, Void, Void> {

        final DropsetDao mDao;

        InsertDropset(DropsetDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Dropset... dropsets) {
            mDao.insertDropset(dropsets[0]);
            return null;
        }
    }

    public void deleteNormal(@NonNull final NormalDao normalDao, Normal normal) {
        DeleteNormal task = new DeleteNormal(normalDao);
        task.execute(normal);
    }

    private static class DeleteNormal extends AsyncTask<Normal, Void, Void> {

        final NormalDao mDao;

        DeleteNormal(NormalDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Normal... normals) {
            mDao.deleteNormal(normals[0]);

            return null;
        }
    }

    public void deleteSuperset(@NonNull final SupersetDao supersetDao, Superset superset) {
        DeleteSuperset task = new DeleteSuperset(supersetDao);
        task.execute(superset);
    }

    private static class DeleteSuperset extends AsyncTask<Superset, Void, Void> {

        final SupersetDao mDao;

        DeleteSuperset(SupersetDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Superset... supersets) {
            mDao.deleteSuperset(supersets[0]);
            return null;
        }
    }

    public void deleteDropset(@NonNull final DropsetDao dropsetDao, Dropset dropset) {
        DeleteDropset task = new DeleteDropset(dropsetDao);
        task.execute(dropset);
    }

    private static class DeleteDropset extends AsyncTask<Dropset, Void, Void> {

        final DropsetDao mDao;

        DeleteDropset(DropsetDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Dropset... dropsets) {
            mDao.deleteDropset(dropsets[0]);
            return null;
        }
    }

    public List<Normal> getNormalsByRoutineId(@NonNull final NormalDao dao, int rId) throws ExecutionException, InterruptedException {
        GetNormalsByRoutineId task = new GetNormalsByRoutineId(dao);
        return task.execute(rId).get();
    }

    private static class GetNormalsByRoutineId extends AsyncTask<Integer, Void, List<Normal>> {

        final NormalDao mDao;

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

        final SupersetDao mDao;

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

        final DropsetDao mDao;

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

        final RoutineDao mDao;

        UpdateRoutine(RoutineDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(Routine... routines) {
            mDao.updateRoutine(routines[0].getRId(), routines[0].getRName());
            return null;
        }
    }

    public List<Normal> getNormalsByEId(@NonNull final NormalDao dao, int eId) throws ExecutionException, InterruptedException {
        GetNormalsByEId task = new GetNormalsByEId(dao);
        return task.execute(eId).get();
    }

    private static class GetNormalsByEId extends AsyncTask<Integer, Void, List<Normal>> {

        final NormalDao mDao;

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

        final SupersetDao mDao;

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

        final DropsetDao mDao;

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

        final ExerciseDoneDao mDao;

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

        final SetDoneDao mDao;

        InsertSetDone(SetDoneDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(SetDone... setDones) {
            mDao.insertSetDone(setDones[0]);
            return null;
        }
    }

    public List<SetDone> getSetDonesByEdId(@NonNull final SetDoneDao dao, int edId) throws ExecutionException, InterruptedException {
        GetSetDonesByEdId task = new GetSetDonesByEdId(dao);
        return task.execute(edId).get();
    }

    private static class GetSetDonesByEdId extends AsyncTask<Integer, Void, List<SetDone>> {

        final SetDoneDao mDao;

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

        final ExerciseDoneDao mDao;

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

        final ExerciseDoneDao mDao;

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

        final ExerciseDao mDao;

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

        final ExerciseDoneDao mDao;

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

        final ExerciseDoneDao mDao;

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

        final ExerciseDoneDao mDao;

        GetNumExerciseDonesUntilDate(ExerciseDoneDao dao) { mDao = dao; }

        @Override
        protected Integer doInBackground(Date... dates) {
            return mDao.getNumExerciseDonesUntilDate(dates[0]);
        }
    }
}
