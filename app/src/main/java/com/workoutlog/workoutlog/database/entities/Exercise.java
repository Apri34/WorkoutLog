package com.workoutlog.workoutlog.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Exercise {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "E_ID")
    public int eID;

    @ColumnInfo(name = "E_Name")
    public String eName;

    /*
    public Exercise(String name) {
        eName = name;
    }
    */
}
