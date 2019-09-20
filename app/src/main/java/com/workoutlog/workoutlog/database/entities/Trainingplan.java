package com.workoutlog.workoutlog.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Trainingplan {

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "TP_ID")
    public int tpId;

    @ColumnInfo (name = "TP_Name")
    public String tpName;

    /*public Trainingplan(String name) {
        tpName = name;
    }*/
}
