package com.workoutlog.workoutlog.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.workoutlog.workoutlog.database.daos.*;
import com.workoutlog.workoutlog.database.entities.*;

@Database(entities = {Dropset.class,
        Exercise.class,
        ExerciseDone.class,
        Normal.class,
        Routine.class,
        SetDone.class,
        Superset.class,
        Trainingplan.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})

public abstract class AppDatabase extends RoomDatabase {

    public abstract DropsetDao dropsetDao();
    public abstract ExerciseDao exerciseDao();
    public abstract ExerciseDoneDao exerciseDoneDao();
    public abstract NormalDao normalDao();
    public abstract RoutineDao routineDao();
    public abstract SetDoneDao setDoneDao();
    public abstract SupersetDao supersetDao();
    public abstract TrainingplanDao trainingplanDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "database")
                    .build();
        }
        return INSTANCE;
    }
}
