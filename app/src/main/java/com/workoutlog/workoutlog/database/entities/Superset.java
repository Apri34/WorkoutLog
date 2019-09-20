package com.workoutlog.workoutlog.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

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
                onDelete = CASCADE)},
        primaryKeys = {"R_ID", "Pos_In_Routine"})
public class Superset {
    @ColumnInfo(name = "E_ID1")
    public int eId1;

    @ColumnInfo(name = "E_ID2")
    public int eId2;

    @ColumnInfo(name = "Sets")
    public int sets;

    @ColumnInfo(name = "Reps1")
    public int reps1;

    @ColumnInfo(name = "Reps2")
    public int reps2;

    @ColumnInfo(name = "Break")
    public int breakInSeconds;

    @ColumnInfo(name = "RPE1")
    public int rpe1;

    @ColumnInfo(name = "RPE2")
    public int rpe2;

    @ColumnInfo(name = "Pos_In_Routine")
    public int posInRoutine;

    @ColumnInfo(name = "R_ID")
    public int rID;

    /*
    public Superset(int eId1, int eId2, int sets, int reps1, int reps2, int _break, int rpe1, int rpe2, int posInRoutine, int rId) {
        this.eId1 = eId1;
        this.eId2 = eId2;
        this.sets = sets;
        this.reps1 = reps1;
        this.reps2 = reps2;
        this.breakInSeconds = _break;
        this.rpe1 = rpe1;
        this.rpe2 = rpe2;
        this.posInRoutine = posInRoutine;
        this.rID = rId;
    }
    */
}
