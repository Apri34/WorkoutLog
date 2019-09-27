package com.workoutlog.workoutlog.database.entities;

import androidx.room.*;

import static androidx.room.ForeignKey.CASCADE;

@Entity (indices = {@Index(value = {"E_ID1"}, unique = true),
                    @Index(value = {"E_ID2"}, unique = true),
                    @Index(value = {"R_ID"}, unique = true)},
        foreignKeys = {@ForeignKey(entity = Exercise.class,
        parentColumns = "E_ID",
        childColumns = "E_ID1",
        onDelete = CASCADE),
        @ForeignKey(entity = Exercise.class,
                parentColumns = "E_ID",
                childColumns = "E_ID2",
                onDelete = CASCADE),
        @ForeignKey(entity = Routine.class,
                parentColumns = "R_ID",
                childColumns = "R_ID",
                onDelete = CASCADE)})
public class Superset {

    @ColumnInfo(name = "S_ID")
    @PrimaryKey(autoGenerate = true)
    private final int sId;

    @ColumnInfo(name = "E_ID1")
    private final int eId1;

    @ColumnInfo(name = "E_ID2")
    private final int eId2;

    @ColumnInfo(name = "Sets")
    private final int sets;

    @ColumnInfo(name = "Reps1")
    private final int reps1;

    @ColumnInfo(name = "Reps2")
    private final int reps2;

    @ColumnInfo(name = "Break")
    private final int breakInSeconds;

    @ColumnInfo(name = "RPE1")
    private final int rpe1;

    @ColumnInfo(name = "RPE2")
    private final int rpe2;

    @ColumnInfo(name = "Pos_In_Routine")
    private final int posInRoutine;

    @ColumnInfo(name = "R_ID")
    private final int rId;

    public int getSId() {
        return sId;
    }

    public int getEId1() {
        return eId1;
    }

    public int getEId2() {
        return eId2;
    }

    public int getSets() {
        return sets;
    }

    public int getReps1() {
        return reps1;
    }

    public int getReps2() {
        return reps2;
    }

    public int getBreakInSeconds() {
        return breakInSeconds;
    }

    public int getRpe1() {
        return rpe1;
    }

    public int getRpe2() {
        return rpe2;
    }

    public int getPosInRoutine() {
        return posInRoutine;
    }

    public int getRId() {
        return rId;
    }

    public Superset(int sId, int eId1, int eId2, int sets, int reps1, int reps2, int breakInSeconds, int rpe1, int rpe2, int posInRoutine, int rId) {
        this.sId = sId;
        this.eId1 = eId1;
        this.eId2 = eId2;
        this.sets = sets;
        this.reps1 = reps1;
        this.reps2 = reps2;
        this.breakInSeconds = breakInSeconds;
        this.rpe1 = rpe1;
        this.rpe2 = rpe2;
        this.posInRoutine = posInRoutine;
        this.rId = rId;
    }
}
