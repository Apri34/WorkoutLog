package com.workoutlog.workoutlog.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity (foreignKeys = @ForeignKey(entity = Trainingplan.class,
                                    parentColumns = "TP_ID",
                                    childColumns = "TP_ID",
                                    onDelete = CASCADE))
public class Routine {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "R_ID")
    public int rID;

    @ColumnInfo (name = "R_Name")
    @NonNull
    public String rName;

    @ColumnInfo (name = "TP_ID")
    public int tpId;

    @ColumnInfo (name = "Pos_In_TP")
    public int posInTp;

    public Routine(@NonNull String name, @NonNull Integer tpId, @NonNull Integer posInTp) {
        rName = name;
        this.tpId = tpId;
        this.posInTp = posInTp;
    }
}
