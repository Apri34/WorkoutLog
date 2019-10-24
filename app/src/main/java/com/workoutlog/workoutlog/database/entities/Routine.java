package com.workoutlog.workoutlog.database.entities;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.*;

import static androidx.room.ForeignKey.CASCADE;

@Entity (indices = {@Index(value = {"TP_ID"})},
        foreignKeys = @ForeignKey(entity = Trainingplan.class,
                                    parentColumns = "TP_ID",
                                    childColumns = "TP_ID",
                                    onDelete = CASCADE))
public class Routine implements Parcelable {
    protected Routine(Parcel in) {
        rId = in.readInt();
        rName = in.readString();
        tpId = in.readInt();
        posInTp = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(rId);
        dest.writeString(rName);
        dest.writeInt(tpId);
        dest.writeInt(posInTp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Routine> CREATOR = new Creator<Routine>() {
        @Override
        public Routine createFromParcel(Parcel in) {
            return new Routine(in);
        }

        @Override
        public Routine[] newArray(int size) {
            return new Routine[size];
        }
    };

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
