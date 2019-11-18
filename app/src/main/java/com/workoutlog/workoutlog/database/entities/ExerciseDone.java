package com.workoutlog.workoutlog.database.entities;

import androidx.room.*;

import java.sql.Date;

import static androidx.room.ForeignKey.CASCADE;

@SuppressWarnings("UnusedReturnValue")
@Entity (indices = {@Index(value = {"E_ID"})},
        foreignKeys = @ForeignKey(entity = Exercise.class,
                                    parentColumns = "E_ID",
                                    childColumns = "E_ID",
                                    onDelete = CASCADE))
public class ExerciseDone {

    @Ignore
    public ExerciseDone(){}

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name="ED_ID")
    private int edId;

    @ColumnInfo (name = "Date")
    private Date date;

    @ColumnInfo (name = "E_ID")
    private int eId;

    @ColumnInfo (name = "Pos_On_Date")
    private int posOnDate;

    public int getEdId() {
        return edId;
    }

    public Date getDate() {
        return date;
    }

    public int getEId() {
        return eId;
    }

    public int getPosOnDate() {
        return posOnDate;
    }

    public void setEdId(int edId) {
        this.edId = edId;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setEId(int eId) {
        this.eId = eId;
    }

    public void setPosOnDate(int posOnDate) {
        this.posOnDate = posOnDate;
    }

    public ExerciseDone(int edId, Date date, int eId, int posOnDate) {
        this.edId = edId;
        this.date = date;
        this.eId = eId;
        this.posOnDate = posOnDate;
    }
}
