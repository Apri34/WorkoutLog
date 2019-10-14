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

public class Dropset extends ExerciseInRoutine implements Parcelable {

    @ColumnInfo(name = "D_ID")
    @PrimaryKey(autoGenerate = true)
    private final int dId;

    @ColumnInfo(name = "E_ID")
    private final int eId;

    @ColumnInfo(name = "Sets")
    private final int sets;

    @ColumnInfo(name = "Reps")
    private final int reps;

    @ColumnInfo(name = "Break")
    @Nullable
    private final Integer breakInSeconds;

    @ColumnInfo(name = "Drops")
    private final int drops;

    @ColumnInfo(name = "Pos_In_Routine")
    private int posInRoutine;

    protected Dropset(Parcel in) {
        dId = in.readInt();
        eId = in.readInt();
        sets = in.readInt();
        reps = in.readInt();
        if (in.readByte() == 0) {
            breakInSeconds = null;
        } else {
            breakInSeconds = in.readInt();
        }
        drops = in.readInt();
        posInRoutine = in.readInt();
        rId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dId);
        dest.writeInt(eId);
        dest.writeInt(sets);
        dest.writeInt(reps);
        if (breakInSeconds == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(breakInSeconds);
        }
        dest.writeInt(drops);
        dest.writeInt(posInRoutine);
        dest.writeInt(rId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Dropset> CREATOR = new Creator<Dropset>() {
        @Override
        public Dropset createFromParcel(Parcel in) {
            return new Dropset(in);
        }

        @Override
        public Dropset[] newArray(int size) {
            return new Dropset[size];
        }
    };

    public int getDId() {
        return dId;
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

    public int getDrops() {
        return drops;
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

    @ColumnInfo(name = "R_ID")
    private final int rId;

    public Dropset(int dId, int eId, int sets, int reps, @Nullable Integer breakInSeconds, int drops, int posInRoutine, int rId) {
        this.dId = dId;
        this.eId = eId;
        this.sets = sets;
        this.reps = reps;
        this.breakInSeconds = breakInSeconds;
        this.drops = drops;
        this.posInRoutine = posInRoutine;
        this.rId = rId;
    }
}
