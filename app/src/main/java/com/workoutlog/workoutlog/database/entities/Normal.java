package com.workoutlog.workoutlog.database.entities;

import androidx.room.*;

import static androidx.room.ForeignKey.CASCADE;

@Entity (indices = {@Index(value = {"E_ID"}),
                    @Index(value = {"R_ID"})},
        foreignKeys = {@ForeignKey(entity = Exercise.class,
                                    parentColumns = "E_ID",
                                    childColumns = "E_ID",
                                    onDelete = CASCADE),
                        @ForeignKey(entity = Routine.class,
                                    parentColumns = "R_ID",
                                    childColumns = "R_ID",
                                    onDelete = CASCADE)})
public class Normal {
    @ColumnInfo(name = "N_ID")
    @PrimaryKey(autoGenerate = true)
    private final int nId;

    @ColumnInfo(name = "E_ID")
    private final int eId;

    @ColumnInfo(name = "Sets")
    private final int sets;

    @ColumnInfo(name = "Reps")
    private final int reps;

    @ColumnInfo(name = "Break")
    private final int breakInSeconds;

    @ColumnInfo(name = "RPE")
    private final int rpe;

    @ColumnInfo(name = "Pos_In_Routine")
    private final int posInRoutine;

    @ColumnInfo(name = "R_ID")
    private final int rId;

    public int getNId() {
        return nId;
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

    public int getRpe() {
        return rpe;
    }

    public int getPosInRoutine() {
        return posInRoutine;
    }

    public int getRId() {
        return rId;
    }

    public Normal(int nId, int eId, int sets, int reps, int breakInSeconds, int rpe, int posInRoutine, int rId) {
        this.nId = nId;
        this.eId = eId;
        this.sets = sets;
        this.reps = reps;
        this.breakInSeconds = breakInSeconds;
        this.rpe = rpe;
        this.posInRoutine = posInRoutine;
        this.rId = rId;
    }
}
