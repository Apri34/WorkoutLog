package com.workoutlog.workoutlog.database.entities;

import androidx.annotation.NonNull;
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
    public int sdId;

    @ColumnInfo (name = "Reps")
    public final int reps;

    @ColumnInfo (name = "weight")
    public final int weightInKg ;

    @ColumnInfo (name = "RPE")
    public final int rpe;

    @ColumnInfo (name = "Pos_In_Exc_Done")
    public final int posInExcDone;

    @ColumnInfo (name = "ED_ID")
    public final int edId;

    public SetDone(int reps, int weightInKg, int rpe, int posInExcDone, int edId) {
        this.reps = reps;
        this.weightInKg = weightInKg;
        this.rpe = rpe;
        this.posInExcDone = posInExcDone;
        this.edId = edId;
    }
}
