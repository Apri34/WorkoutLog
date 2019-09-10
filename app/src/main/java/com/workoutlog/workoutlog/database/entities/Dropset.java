package com.workoutlog.workoutlog.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity (foreignKeys = {@ForeignKey(entity = Exercise.class,
        parentColumns = "E_ID",
        childColumns = "E_ID",
        onDelete = CASCADE),
        @ForeignKey(entity = Routine.class,
                parentColumns = "R_ID",
                childColumns = "R_ID",
                onDelete = CASCADE)})

public class Dropset {
    @ColumnInfo(name = "E_ID")
    public int eId;

    @ColumnInfo(name = "Sets")
    public int sets;

    @ColumnInfo(name = "Reps")
    public int reps;

    @ColumnInfo(name = "Break")
    public int breakInSeconds;

    @ColumnInfo(name = "Drops")
    public int drops;

    @ColumnInfo(name = "Pos_In_Routine")
    public int posInRoutine;

    @ColumnInfo(name = "R_ID")
    public int rID;

    public Dropset(@NonNull Integer eId, @NonNull Integer sets, @NonNull Integer reps, int _break, @NonNull Integer drops, @NonNull Integer posInRoutine, @NonNull Integer rId) {
        this.eId = eId;
        this.sets = sets;
        this.reps = reps;
        this.breakInSeconds = _break;
        this.drops = drops;
        this.posInRoutine = posInRoutine;
        this.rID = rId;
    }
}
