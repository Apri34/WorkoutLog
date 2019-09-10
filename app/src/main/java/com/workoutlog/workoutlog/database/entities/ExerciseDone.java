package com.workoutlog.workoutlog.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.sql.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity (foreignKeys = @ForeignKey(entity = Exercise.class,
                                    parentColumns = "E_ID",
                                    childColumns = "E_ID",
                                    onDelete = CASCADE))
public class ExerciseDone {

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name="ED_ID")
    public int edId;

    @ColumnInfo (name = "Date")
    public Date date;

    @ColumnInfo (name = "E_ID")
    public int eId;

    @ColumnInfo (name = "Pos_On_Date")
    public int posOnDate;

    public ExerciseDone(@NonNull Date date, @NonNull Integer eId, @NonNull Integer posOnDate) {
        this.date = date;
        this.eId = eId;
        this.posOnDate = posOnDate;
    }
}
