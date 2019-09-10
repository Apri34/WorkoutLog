package com.workoutlog.workoutlog.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity (foreignKeys = @ForeignKey(entity = ExerciseDone.class,
                                    parentColumns = "ED_ID",
                                    childColumns = "ED_ID",
                                    onDelete = CASCADE))
public class SetDone {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "SD_ID")
    public int sdId;

    @ColumnInfo (name = "Reps")
    public int reps;

    @ColumnInfo (name = "weight")
    public int weightInKg ;

    @ColumnInfo (name = "RPE")
    public int rpe;

    @ColumnInfo (name = "Pos_In_Exc_Done")
    public int posInExcDone;

    @ColumnInfo (name = "ED_ID")
    public int edId;

    public SetDone(@NonNull Integer reps, @NonNull Integer weightInKg, int rpe, @NonNull Integer posInExcDone, @NonNull Integer edId) {
        this.reps = reps;
        this.weightInKg = weightInKg;
        this.rpe = rpe;
        this.posInExcDone = posInExcDone;
        this.edId = edId;
    }
}
