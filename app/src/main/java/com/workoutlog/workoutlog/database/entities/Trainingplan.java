package com.workoutlog.workoutlog.database.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Trainingplan {

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "TP_ID")
    private final int tpId;

    @ColumnInfo (name = "TP_Name")
    private final String tpName;

    public int getTpId() {
        return tpId;
    }

    public String getTpName() {
        return tpName;
    }

    public Trainingplan(int tpId, String tpName) {
        this.tpId = tpId;
        this.tpName = tpName;
    }
}
