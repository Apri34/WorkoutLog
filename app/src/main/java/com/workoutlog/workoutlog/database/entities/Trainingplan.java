package com.workoutlog.workoutlog.database.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Trainingplan {

    @Ignore
    public Trainingplan(){}

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "TP_ID")
    private int tpId;

    @ColumnInfo (name = "TP_Name")
    private String tpName;

    public int getTpId() {
        return tpId;
    }

    public String getTpName() {
        return tpName;
    }

    public void setTpId(int tpId) {
        this.tpId = tpId;
    }

    public void setTpName(String tpName) {
        this.tpName = tpName;
    }

    public Trainingplan(int tpId, String tpName) {
        this.tpId = tpId;
        this.tpName = tpName;
    }
}
