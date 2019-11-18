package com.workoutlog.workoutlog.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TableRow;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.workoutlog.workoutlog.R;

public class CalenderItem extends androidx.appcompat.widget.AppCompatTextView {

    private int day;
    private int month;
    private int year;
    private IItemClicked listener = null;

    private static final int[] STATE_CHOSEN = {R.attr.state_chosen};
    private static final int[] STATE_IN_MONTH = {R.attr.state_in_month};

    private boolean mIsChosen = false;
    private boolean mIsInMonth = true;

    public void setChosen(boolean isChosen) {
        mIsChosen = isChosen;
        refreshDrawableState();
        refreshTextColor();
    }

    public void setInMonth(boolean inMonth) {
        mIsInMonth = inMonth;
        refreshDrawableState();
        refreshTextColor();
    }

    public void setOnItemClickedListener(IItemClicked listener) {
        this.listener = listener;
    }

    public CalenderItem(Context context) {
        this(context, null);
    }

    public CalenderItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalenderItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setBackgroundResource(R.drawable.background_calender_item);
        if(attrs != null) {
            for (int i = 0; i < attrs.getAttributeCount(); i++) {
                switch (attrs.getAttributeName(i)) {
                    case "state_chosen": mIsChosen = attrs.getAttributeBooleanValue(i, false); break;
                    case "state_in_month": mIsInMonth = attrs.getAttributeBooleanValue(i, true); break;
                }
            }
        }
        refreshDrawableState();
        setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        setOnClickListener(v -> {
            if(listener != null) {
                listener.itemClicked(day, month, year);
            }
        });
        setClickable(true);
        setGravity(Gravity.CENTER);
        setPadding(0, 4, 0, 4);
        setTextColor(ContextCompat.getColor(context, R.color.colorSecondary));
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 2);
        if(mIsChosen) {
            mergeDrawableStates(drawableState, STATE_CHOSEN);
        }
        if(mIsInMonth) {
            mergeDrawableStates(drawableState, STATE_IN_MONTH);
        }
        return drawableState;
    }

    public void setDate(int day, int month, int year) {
        setText(String.format("%s\n", day));
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public void setDateInLandscape(int day, int month, int year) {
        String d = (day < 10 ? "0" : "") + day;
        String m = (month < 10 ? "0" : "") + month;
        setText(String.format("%s.%s\n", d, m));
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public void setRoutine(@Nullable String routine) {
        if(routine == null) return;
        if(routine.length() > 7) {
            routine = routine.substring(0, 4) + "...";
        }
        setText(String.format("%s%s", getText(), routine));
    }

    public void setRoutineInLandscape(@Nullable String routine) {
        if(routine == null) return;
        if(routine.length() > 12) {
            routine = routine.substring(0, 9) + "...";
        }
        setText(String.format("%s%s", getText(), routine));
    }

    public interface IItemClicked {
        void itemClicked(int day, int month, int year);
    }

    private void refreshTextColor() {
        if(!mIsInMonth) {
            setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryAlpha50));
        } else if(mIsChosen) {
            setTextColor(ContextCompat.getColor(getContext(), R.color.colorSecondary));
        } else {
            setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        }
    }
}
