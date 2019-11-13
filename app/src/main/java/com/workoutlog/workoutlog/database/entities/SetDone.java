package com.workoutlog.workoutlog.database.entities;

import androidx.room.*;

import static androidx.room.ForeignKey.CASCADE;

@Entity (indices = {@Index(value = {"ED_ID"})},
        foreignKeys = @ForeignKey(entity = ExerciseDone.class,
                                    parentColumns = "ED_ID",
                                    childColumns = "ED_ID",
                                    onDelete = CASCADE))
public class SetDone {

    @Ignore
    public SetDone(){}

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "SD_ID")
    private int sdId;

    @ColumnInfo (name = "Reps")
    private int reps;

    @ColumnInfo (name = "weight")
    private float weightInKg ;

    @ColumnInfo (name = "RPE")
    private float rpe;

    @ColumnInfo (name = "Pos_In_Exc_Done")
    private int posInExcDone;

    @ColumnInfo (name = "ED_ID")
    private int edId;

    public int getSdId() {
        return sdId;
    }

    public int getReps() {
        return reps;
    }

    public float getWeightInKg() {
        return weightInKg;
    }

    public float getRpe() {
        return rpe;
    }

    public int getPosInExcDone() {
        return posInExcDone;
    }

    public int getEdId() {
        return edId;
    }

    public void setSdId(int sdId) {
        this.sdId = sdId;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public void setWeightInKg(float weightInKg) {
        this.weightInKg = weightInKg;
    }

    public void setRpe(float rpe) {
        this.rpe = rpe;
    }

    public void setPosInExcDone(int posInExcDone) {
        this.posInExcDone = posInExcDone;
    }

    public void setEdId(int edId) {
        this.edId = edId;
    }

    public SetDone(int sdId, int reps, float weightInKg, float rpe, int posInExcDone, int edId) {
        this.sdId = sdId;
        this.reps = reps;
        this.weightInKg = weightInKg;
        this.rpe = rpe;
        this.posInExcDone = posInExcDone;
        this.edId = edId;
    }
}
