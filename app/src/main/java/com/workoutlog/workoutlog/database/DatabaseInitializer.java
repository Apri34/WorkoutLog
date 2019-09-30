package com.workoutlog.workoutlog.database;

import android.os.AsyncTask;
import androidx.annotation.NonNull;
import com.workoutlog.workoutlog.database.daos.*;
import com.workoutlog.workoutlog.database.entities.*;

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
}
