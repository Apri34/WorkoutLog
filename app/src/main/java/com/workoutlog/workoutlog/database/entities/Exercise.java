package com.workoutlog.workoutlog.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Exercise {

    @Ignore
    public Exercise(){}

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "E_ID")
    private int eId;

    @ColumnInfo(name = "E_Name")
    private String eName;

    public int getEId() {
        return eId;
    }

    public String getEName() {
        return eName;
    }

    public void setEId(int eId) {
        this.eId = eId;
    }

    public void setEName(String eName) {
        this.eName = eName;
    }

    public Exercise(int eId, String eName) {
        this.eId = eId;
        this.eName = eName;
    }
}
