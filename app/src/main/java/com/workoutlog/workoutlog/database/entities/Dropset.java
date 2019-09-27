package com.workoutlog.workoutlog.database.entities;

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
    private final int dId;

    @ColumnInfo(name = "E_ID")
    private final int eId;

    @ColumnInfo(name = "Sets")
    private final int sets;

    @ColumnInfo(name = "Reps")
    private final int reps;

    @ColumnInfo(name = "Break")
    private final int breakInSeconds;

    @ColumnInfo(name = "Drops")
    private final int drops;

    @ColumnInfo(name = "Pos_In_Routine")
    private final int posInRoutine;

    public int getDId() {
        return dId;
    }

    public int getEId() {
        return eId;
    }

    public int getSets() {
        return sets;
    }

    public int getReps() {
        return reps;
    }

    public int getBreakInSeconds() {
        return breakInSeconds;
    }

    public int getDrops() {
        return drops;
    }

    public int getPosInRoutine() {
        return posInRoutine;
    }

    public int getRId() {
        return rId;
    }

    @ColumnInfo(name = "R_ID")
    private final int rId;

    public Dropset(int dId, int eId, int sets, int reps, int breakInSeconds, int drops, int posInRoutine, int rId) {
        this.dId = dId;
        this.eId = eId;
        this.sets = sets;
        this.reps = reps;
        this.breakInSeconds = breakInSeconds;
        this.drops = drops;
        this.posInRoutine = posInRoutine;
        this.rId = rId;
    }
}
