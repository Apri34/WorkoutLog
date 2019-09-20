package com.workoutlog.workoutlog.database.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.*;

import static androidx.room.ForeignKey.CASCADE;

@Entity (indices = {@Index(value = {"E_ID"}, unique = true)},
        foreignKeys = {@ForeignKey(entity = Exercise.class,
                                    parentColumns = "E_ID",
                                    childColumns = "E_ID",
                                    onDelete = CASCADE),
                        @ForeignKey(entity = Routine.class,
                                    parentColumns = "R_ID",
                                    childColumns = "R_ID",
                                    onDelete = CASCADE)},
        primaryKeys = {"R_ID", "Pos_In_Routine"})
public class Normal {
    @ColumnInfo(name = "E_ID")
    public int eId;

    @ColumnInfo(name = "Sets")
    public int sets;

    @ColumnInfo(name = "Reps")
    public int reps;

    @ColumnInfo(name = "Break")
    public int breakInSeconds;

    @ColumnInfo(name = "RPE")
    public int rpe;

    @ColumnInfo(name = "Pos_In_Routine")
    public int posInRoutine;

    @ColumnInfo(name = "R_ID")
    public int rID;

    /*
    public Normal(int eId, int sets, int reps, int _break, int rpe, int posInRoutine, int rId) {
        this.eId = eId;
        this.sets = sets;
        this.reps = reps;
        this.breakInSeconds = _break;
        this.rpe = rpe;
        this.posInRoutine = posInRoutine;
        this.rID = rId;
    }
    */
}
