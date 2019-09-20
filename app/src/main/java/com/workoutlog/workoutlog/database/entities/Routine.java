package com.workoutlog.workoutlog.database.entities;

import androidx.annotation.NonNull;
import androidx.room.*;

import static androidx.room.ForeignKey.CASCADE;

@Entity (indices = {@Index(value = {"TP_ID"}, unique = true)},
        foreignKeys = @ForeignKey(entity = Trainingplan.class,
                                    parentColumns = "TP_ID",
                                    childColumns = "TP_ID",
                                    onDelete = CASCADE))
public class Routine {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "R_ID")
    public int rID;

    @ColumnInfo (name = "R_Name")
    public String rName;

    @ColumnInfo (name = "TP_ID")
    public int tpId;

    @ColumnInfo (name = "Pos_In_TP")
    public int posInTp;

    /*
    public Routine(String name, int tpId, int posInTp) {
        rName = name;
        this.tpId = tpId;
        this.posInTp = posInTp;
    }
    */
}
