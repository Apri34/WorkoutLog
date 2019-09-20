package com.workoutlog.workoutlog.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import static androidx.room.ForeignKey.CASCADE;

@Entity (indices = {@Index(value = {"E_ID"}, unique = true),
                    @Index(value = {"R_ID"}, unique = true)},
        foreignKeys = {@ForeignKey(entity = Exercise.class,
        parentColumns = "E_ID",
        childColumns = "E_ID",
        onDelete = CASCADE),
        @ForeignKey(entity = Routine.class,
                parentColumns = "R_ID",
                childColumns = "R_ID",
                onDelete = CASCADE)},
        primaryKeys = {"R_ID", "Pos_In_Routine"})

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

    /*
    public Dropset(int eId, int sets, int reps, int _break, int drops, int posInRoutine, int rId) {
        this.eId = eId;
        this.sets = sets;
        this.reps = reps;
        this.breakInSeconds = _break;
        this.drops = drops;
        this.posInRoutine = posInRoutine;
        this.rID = rId;
    }
    */
}
