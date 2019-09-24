package com.workoutlog.workoutlog.database.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    public int nId;

    @ColumnInfo(name = "E_ID")
    public final int eId;

    @ColumnInfo(name = "Sets")
    public final int sets;

    @ColumnInfo(name = "Reps")
    public final int reps;

    @ColumnInfo(name = "Break")
    public final int breakInSeconds;

    @ColumnInfo(name = "RPE")
    public final int rpe;

    @ColumnInfo(name = "Pos_In_Routine")
    public final int posInRoutine;

    @ColumnInfo(name = "R_ID")
    public final int rId;

    public Normal(int eId, int sets, int reps, int breakInSeconds, int rpe, int posInRoutine, int rId) {
        this.eId = eId;
        this.sets = sets;
        this.reps = reps;
        this.breakInSeconds = breakInSeconds;
        this.rpe = rpe;
        this.posInRoutine = posInRoutine;
        this.rId = rId;
    }
}
