package com.workoutlog.workoutlog.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Exercise {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "E_ID")
    private final int eId;

    @ColumnInfo(name = "E_Name")
    private final String eName;

    public int getEId() {
        return eId;
    }

    public String getEName() {
        return eName;
    }

    public Exercise(int eId, String eName) {
        this.eId = eId;
        this.eName = eName;
    }
}
