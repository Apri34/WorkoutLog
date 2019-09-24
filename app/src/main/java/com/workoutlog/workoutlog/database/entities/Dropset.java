package com.workoutlog.workoutlog.database.entities;

import androidx.annotation.NonNull;
import androidx.room.*;

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
                onDelete = CASCADE)})

public class Dropset {

    @ColumnInfo(name = "D_ID")
    @PrimaryKey(autoGenerate = true)
    public int dId;

    @ColumnInfo(name = "E_ID")
    public final int eId;

    @ColumnInfo(name = "Sets")
    public final int sets;

    @ColumnInfo(name = "Reps")
    public final int reps;

    @ColumnInfo(name = "Break")
    public final int breakInSeconds;

    @ColumnInfo(name = "Drops")
    public final int drops;

    @ColumnInfo(name = "Pos_In_Routine")
    public final int posInRoutine;

    @ColumnInfo(name = "R_ID")
    public final int rId;

    public Dropset(int eId, int sets, int reps, int breakInSeconds, int drops, int posInRoutine, int rId) {
        this.eId = eId;
        this.sets = sets;
        this.reps = reps;
        this.breakInSeconds = breakInSeconds;
        this.drops = drops;
        this.posInRoutine = posInRoutine;
        this.rId = rId;
    }
}
