package com.workoutlog.workoutlog.database.entities;

import androidx.room.*;

import static androidx.room.ForeignKey.CASCADE;

@Entity (indices = {@Index(value = {"TP_ID"})},
        foreignKeys = @ForeignKey(entity = Trainingplan.class,
                                    parentColumns = "TP_ID",
                                    childColumns = "TP_ID",
                                    onDelete = CASCADE))
public class Routine {
    public int getRId() {
        return rId;
    }

    public String getRName() {
        return rName;
    }

    public int getTpId() {
        return tpId;
    }

    public int getPosInTp() {
        return posInTp;
    }

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "R_ID")
    private final int rId;

    @ColumnInfo (name = "R_Name")
    private final String rName;

    @ColumnInfo (name = "TP_ID")
    private final int tpId;

    @ColumnInfo (name = "Pos_In_TP")
    private final int posInTp;

    public Routine(int rId, String rName, int tpId, int posInTp) {
        this.rId = rId;
        this.rName = rName;
        this.tpId = tpId;
        this.posInTp = posInTp;
    }
}
