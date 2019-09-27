package com.workoutlog.workoutlog.database.entities;

import androidx.room.*;

import static androidx.room.ForeignKey.CASCADE;

@Entity (indices = {@Index(value = {"ED_ID"}, unique = true)},
        foreignKeys = @ForeignKey(entity = ExerciseDone.class,
                                    parentColumns = "ED_ID",
                                    childColumns = "ED_ID",
                                    onDelete = CASCADE))
public class SetDone {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "SD_ID")
    private final int sdId;

    @ColumnInfo (name = "Reps")
    private final int reps;

    @ColumnInfo (name = "weight")
    private final int weightInKg ;

    @ColumnInfo (name = "RPE")
    private final int rpe;

    @ColumnInfo (name = "Pos_In_Exc_Done")
    private final int posInExcDone;

    @ColumnInfo (name = "ED_ID")
    private final int edId;

    public int getSdId() {
        return sdId;
    }

    public int getReps() {
        return reps;
    }

    public int getWeightInKg() {
        return weightInKg;
    }

    public int getRpe() {
        return rpe;
    }

    public int getPosInExcDone() {
        return posInExcDone;
    }

    public int getEdId() {
        return edId;
    }

    public SetDone(int sdId, int reps, int weightInKg, int rpe, int posInExcDone, int edId) {
        this.sdId = sdId;
        this.reps = reps;
        this.weightInKg = weightInKg;
        this.rpe = rpe;
        this.posInExcDone = posInExcDone;
        this.edId = edId;
    }
}
