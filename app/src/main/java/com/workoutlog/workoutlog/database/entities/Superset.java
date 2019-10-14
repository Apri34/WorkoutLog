package com.workoutlog.workoutlog.database.entities;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.room.*;

import static androidx.room.ForeignKey.CASCADE;

@Entity (indices = {@Index(value = {"E_ID1"}),
                    @Index(value = {"E_ID2"}),
                    @Index(value = {"R_ID"})},
        foreignKeys = {@ForeignKey(entity = Exercise.class,
        parentColumns = "E_ID",
        childColumns = "E_ID1",
        onDelete = CASCADE),
        @ForeignKey(entity = Exercise.class,
                parentColumns = "E_ID",
                childColumns = "E_ID2",
                onDelete = CASCADE),
        @ForeignKey(entity = Routine.class,
                parentColumns = "R_ID",
                childColumns = "R_ID",
                onDelete = CASCADE)})
public class Superset extends ExerciseInRoutine implements Parcelable {

    @ColumnInfo(name = "S_ID")
    @PrimaryKey(autoGenerate = true)
    private final int sId;

    @ColumnInfo(name = "E_ID1")
    private final int eId1;

    @ColumnInfo(name = "E_ID2")
    private final int eId2;

    @ColumnInfo(name = "Sets")
    private final int sets;

    @ColumnInfo(name = "Reps1")
    private final int reps1;

    @ColumnInfo(name = "Reps2")
    private final int reps2;

    @ColumnInfo(name = "Break")
    private final @Nullable Integer breakInSeconds;

    @ColumnInfo(name = "RPE1")
    private final @Nullable Integer rpe1;

    @ColumnInfo(name = "RPE2")
    private final @Nullable Integer rpe2;

    @ColumnInfo(name = "Pos_In_Routine")
    private int posInRoutine;

    @ColumnInfo(name = "R_ID")
    private final int rId;

    protected Superset(Parcel in) {
        sId = in.readInt();
        eId1 = in.readInt();
        eId2 = in.readInt();
        sets = in.readInt();
        reps1 = in.readInt();
        reps2 = in.readInt();
        if (in.readByte() == 0) {
            breakInSeconds = null;
        } else {
            breakInSeconds = in.readInt();
        }
        if (in.readByte() == 0) {
            rpe1 = null;
        } else {
            rpe1 = in.readInt();
        }
        if (in.readByte() == 0) {
            rpe2 = null;
        } else {
            rpe2 = in.readInt();
        }
        posInRoutine = in.readInt();
        rId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sId);
        dest.writeInt(eId1);
        dest.writeInt(eId2);
        dest.writeInt(sets);
        dest.writeInt(reps1);
        dest.writeInt(reps2);
        if (breakInSeconds == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(breakInSeconds);
        }
        if (rpe1 == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(rpe1);
        }
        if (rpe2 == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(rpe2);
        }
        dest.writeInt(posInRoutine);
        dest.writeInt(rId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Superset> CREATOR = new Creator<Superset>() {
        @Override
        public Superset createFromParcel(Parcel in) {
            return new Superset(in);
        }

        @Override
        public Superset[] newArray(int size) {
            return new Superset[size];
        }
    };

    public int getSId() {
        return sId;
    }

    public int getEId1() {
        return eId1;
    }

    public int getEId2() {
        return eId2;
    }

    public int getSets() {
        return sets;
    }

    public int getReps1() {
        return reps1;
    }

    public int getReps2() {
        return reps2;
    }

    public @Nullable Integer getBreakInSeconds() {
        return breakInSeconds;
    }

    public @Nullable Integer getRpe1() {
        return rpe1;
    }

    public @Nullable Integer getRpe2() {
        return rpe2;
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

    public Superset(int sId, int eId1, int eId2, int sets, int reps1, int reps2, @Nullable Integer breakInSeconds, @Nullable Integer rpe1, @Nullable Integer rpe2, int posInRoutine, int rId) {
        this.sId = sId;
        this.eId1 = eId1;
        this.eId2 = eId2;
        this.sets = sets;
        this.reps1 = reps1;
        this.reps2 = reps2;
        this.breakInSeconds = breakInSeconds;
        this.rpe1 = rpe1;
        this.rpe2 = rpe2;
        this.posInRoutine = posInRoutine;
        this.rId = rId;
    }
}
