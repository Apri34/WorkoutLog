package com.workoutlog.workoutlog.database.entities;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.room.*;

import static androidx.room.ForeignKey.CASCADE;

@Entity (indices = {@Index(value = {"E_ID"}),
                    @Index(value = {"R_ID"})},
        foreignKeys = {@ForeignKey(entity = Exercise.class,
                                    parentColumns = "E_ID",
                                    childColumns = "E_ID",
                                    onDelete = CASCADE),
                        @ForeignKey(entity = Routine.class,
                                    parentColumns = "R_ID",
                                    childColumns = "R_ID",
                                    onDelete = CASCADE)})
public class Normal extends ExerciseInRoutine implements Parcelable {
    @ColumnInfo(name = "N_ID")
    @PrimaryKey(autoGenerate = true)
    private final int nId;

    @ColumnInfo(name = "E_ID")
    private final int eId;

    @ColumnInfo(name = "Sets")
    private final int sets;

    @ColumnInfo(name = "Reps")
    private final int reps;

    @ColumnInfo(name = "Break")
    private final @Nullable Integer breakInSeconds;

    @ColumnInfo(name = "RPE")
    private final @Nullable Integer rpe;

    @ColumnInfo(name = "Pos_In_Routine")
    private int posInRoutine;

    @ColumnInfo(name = "R_ID")
    private final int rId;

    protected Normal(Parcel in) {
        nId = in.readInt();
        eId = in.readInt();
        sets = in.readInt();
        reps = in.readInt();
        if (in.readByte() == 0) {
            breakInSeconds = null;
        } else {
            breakInSeconds = in.readInt();
        }
        if (in.readByte() == 0) {
            rpe = null;
        } else {
            rpe = in.readInt();
        }
        posInRoutine = in.readInt();
        rId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(nId);
        dest.writeInt(eId);
        dest.writeInt(sets);
        dest.writeInt(reps);
        if (breakInSeconds == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(breakInSeconds);
        }
        if (rpe == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(rpe);
        }
        dest.writeInt(posInRoutine);
        dest.writeInt(rId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Normal> CREATOR = new Creator<Normal>() {
        @Override
        public Normal createFromParcel(Parcel in) {
            return new Normal(in);
        }

        @Override
        public Normal[] newArray(int size) {
            return new Normal[size];
        }
    };

    public int getNId() {
        return nId;
    }

    public int getEId() {
        return eId;
    }

    public int getSets() {
        return sets;
    }

    public int getReps() {
        return reps;
    }

    public @Nullable Integer getBreakInSeconds() {
        return breakInSeconds;
    }

    public @Nullable Integer getRpe() {
        return rpe;
    }

    public int getPosInRoutine() {
        return posInRoutine;
    }

    public int getRId() {
        return rId;
    }

    public void reducePosInRoutine() {
        posInRoutine--;
    }

    public void increasePosInRoutine() { posInRoutine++; }

    public void setPosInRoutine(int posInRoutine) { this.posInRoutine = posInRoutine; }

    public Normal(int nId, int eId, int sets, int reps, @Nullable Integer breakInSeconds, @Nullable Integer rpe, int posInRoutine, int rId) {
        this.nId = nId;
        this.eId = eId;
        this.sets = sets;
        this.reps = reps;
        this.breakInSeconds = breakInSeconds;
        this.rpe = rpe;
        this.posInRoutine = posInRoutine;
        this.rId = rId;
    }
}
