package com.workoutlog.workoutlog.database.entities;

import androidx.annotation.NonNull;
import androidx.room.*;

import java.sql.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity (indices = {@Index(value = {"E_ID"}, unique = true)},
        foreignKeys = @ForeignKey(entity = Exercise.class,
                                    parentColumns = "E_ID",
                                    childColumns = "E_ID",
                                    onDelete = CASCADE))
public class ExerciseDone {

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name="ED_ID")
    public int edId;

    @ColumnInfo (name = "Date")
    public final Date date;

    @ColumnInfo (name = "E_ID")
    public final int eId;

    @ColumnInfo (name = "Pos_On_Date")
    public final int posOnDate;

    public ExerciseDone(Date date, int eId, int posOnDate) {
        this.date = date;
        this.eId = eId;
        this.posOnDate = posOnDate;
    }
}
