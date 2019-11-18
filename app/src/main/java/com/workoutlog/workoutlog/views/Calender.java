package com.workoutlog.workoutlog.views;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import com.workoutlog.workoutlog.R;
import com.workoutlog.workoutlog.database.entities.Routine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@SuppressWarnings("UnusedReturnValue")
public class Calender extends ConstraintLayout {

    private final TextView tvMonth;
    private final TableLayout calender;

    private int year;
    private int month;
    private final Calendar c;

    private int selectedDay = -1;
    private int selectedMonth = -1;
    private int selectedYear = -1;
    private List<Integer> interval;
    private List<Routine> routines;

    private final Context context;

    private final boolean isEditable;
    private final boolean isInPortrait;

    public void setInterval(List<Integer> interval, List<Routine> routines) {
        this.interval = interval;
        this.routines = routines;
    }

    public void setStart(int day, int month, int year) {
        selectedDay = day;
        selectedMonth = month;
        selectedYear = year;
        makeCalender();
    }

    @SuppressWarnings("UnusedReturnValue")
    public int getSelectedDay() {
        return selectedDay;
    }
    public int getSelectedMonth() {
        return selectedMonth;
    }
    public int getSelectedYear() {
        return selectedYear;
    }

    public Calender(Context context) {
        this(context, null);
    }

    public Calender(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Calender(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;

        LayoutInflater l = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        l.inflate(R.layout.calender, this, true);

        setClickable(false);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Calender, defStyleAttr, 0);

        isEditable = a.getBoolean(R.styleable.Calender_isEditable, true);

        a.recycle();

        isInPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        tvMonth = findViewById(R.id.calender_text_view_month);
        ImageButton buttonNext = findViewById(R.id.calender_button_next_month);
        buttonNext.setOnClickListener(v -> nextMonth());
        ImageButton buttonPrev = findViewById(R.id.calender_button_prev_month);
        buttonPrev.setOnClickListener(v -> prevMonth());
        calender = findViewById(R.id.calender_table_layout);
        ScrollView scrollView = findViewById(R.id.scroll_view_calender_table_layout);

        if(!isEditable) {
            ConstraintSet set = new ConstraintSet();
            set.clear(R.id.calender);
            if(isInPortrait)
                set.connect(R.id.calender_table_layout, ConstraintSet.TOP, R.id.calender, ConstraintSet.TOP);
            else {
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            }
            if(tvMonth != null)
                tvMonth.setVisibility(View.GONE);
            buttonNext.setVisibility(View.GONE);
            buttonPrev.setVisibility(View.GONE);
        }


        c = Calendar.getInstance();
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);
        makeCalender();
    }

    private void nextMonth() {
        if(month == 11) {
            month = 0;
            year++;
        } else
            month++;
        makeCalender();
    }
    private void prevMonth() {
        if(month == 0) {
            month = 11;
            year--;
        } else
            month--;
        makeCalender();
    }
    private void makeCalender() {
        ArrayList<TableRow> rows = new ArrayList<>();
        if(tvMonth != null)
            tvMonth.setText(String.format("%s %s", getMonth(month), year));
        calender.removeAllViews();
        calender.addView(createCalenderHeader());
        int dayOfWeek = getFirstDayOfCurrentMonth() - 2;
        if(dayOfWeek == -1) dayOfWeek = 6;
        int daysOfMonth = getDaysOfMonth(month, year);
        int daysPrevMonth = month > 0 ? getDaysOfMonth(month - 1, year) : getDaysOfMonth(11, year - 1);
        int prevMonth = month > 0 ? month - 1 : 11;
        int prevYear = month > 0 ? year : year - 1;
        TableRow tr1 = new TableRow(context);
        tr1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr1.setShowDividers(TableRow.SHOW_DIVIDER_MIDDLE);
        tr1.setDividerDrawable(ContextCompat.getDrawable(context, R.drawable.table_row_divider));
        for (int day = 1; day <= dayOfWeek; day++) {
            tr1.addView(createCalenderItemPrev(daysPrevMonth + day - dayOfWeek, prevMonth, prevYear));
        }

        for (int day = 1; day <= daysOfMonth; day++) {
            if((dayOfWeek + day) <= 7) {
                tr1.addView(createCalenderItem(day, month, year));
                if(dayOfWeek + day == 7)
                    calender.addView(tr1);
                continue;
            }
            if (rows.size() == 0 ||rows.get(rows.size() - 1).getChildCount() == 7) {
                rows.add(new TableRow(context));

                rows.get(rows.size() - 1).setShowDividers(TableRow.SHOW_DIVIDER_MIDDLE);
                rows.get(rows.size() - 1).setDividerDrawable(ContextCompat.getDrawable(context, R.drawable.table_row_divider));
                rows.get(rows.size() - 1).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            }
            rows.get(rows.size() - 1).addView(createCalenderItem(day, month, year));
        }
        c.set(year, month, daysOfMonth);
        int lastDayOfMonth = c.get(Calendar.DAY_OF_WEEK);
        if(lastDayOfMonth != 1) {
            int nextMonth = month < 11 ? month + 1 : 0;
            int nextYear = month < 11 ? year : year + 1;
            for (int i = lastDayOfMonth; i <= 7; i++) {
                rows.get(rows.size() - 1).addView(createCalenderItemPrev(i - lastDayOfMonth + 1, nextMonth, nextYear));
            }
        }

        for(int i = 0; i < rows.size(); i++) {
            calender.addView(rows.get(i));
        }
    }

    private CalenderItem createCalenderItem(int day, int month, int year) {
        CalenderItem item = new CalenderItem(context);
        if(isInPortrait)
            item.setDate(day, month, year);
        else
            item.setDateInLandscape(day, month, year);
        item.setInMonth(true);
        item.setOnItemClickedListener((day1, month1, year1) -> {
            selectedDay = day1;
            selectedMonth = month1;
            selectedYear = year1;
            makeCalender();
        });
        if(selectedDay != -1 && (year >= selectedYear && month >= selectedMonth && day >= selectedDay ||
                month > selectedMonth && year >= selectedYear || year > selectedYear) &&
                isNotPause(day, month, year)) {
            item.setChosen(true);
            if(isInPortrait)
                item.setRoutine(getRoutine(day, month, year));
            else
                item.setRoutineInLandscape(getRoutine(day, month, year));
        }
        item.setClickable(isEditable);
        return item;
    }

    private CalenderItem createCalenderItemPrev(int day, int month, int year) {
        CalenderItem item = new CalenderItem(context);
        if(isInPortrait)
            item.setDate(day, month, year);
        else
            item.setDateInLandscape(day, month, year);
        item.setInMonth(false);
        if(selectedDay != -1 && (year >= selectedYear && month >= selectedMonth && day >= selectedDay ||
                month > selectedMonth && year >= selectedYear || year > selectedYear) &&
                isNotPause(day, month, year)) {
            item.setChosen(true);
            if(isInPortrait)
                item.setRoutine(getRoutine(day, month, year));
            else
                item.setRoutineInLandscape(getRoutine(day, month, year));
        }

        return item;
    }

    private int getFirstDayOfCurrentMonth() {
        c.set(year, month, 1);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    private int getDaysOfMonth(int month, int year) {
        c.set(year, month, 1);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private String getMonth(int month) {
        switch (month) {
            case 0: return context.getString(R.string.january);
            case 1: return context.getString(R.string.february);
            case 2: return context.getString(R.string.march);
            case 3: return context.getString(R.string.april);
            case 4: return context.getString(R.string.may);
            case 5: return context.getString(R.string.june);
            case 6: return context.getString(R.string.july);
            case 7: return context.getString(R.string.august);
            case 8: return context.getString(R.string.september);
            case 9: return context.getString(R.string.october);
            case 10: return context.getString(R.string.november);
            case 11: return context.getString(R.string.december);
            default: return null;
        }
    }

    private TableRow createCalenderHeader() {
        TableRow header = new TableRow(context);
        header.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        TextView tvMon = new TextView(context);
        tvMon.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        tvMon.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1)
            tvMon.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        else
            tvMon.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView tvTue = new TextView(context);
        tvTue.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        tvTue.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1)
            tvTue.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        else
            tvTue.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView tvWed = new TextView(context);
        tvWed.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        tvWed.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1)
            tvWed.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        else
            tvWed.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView tvThu = new TextView(context);
        tvThu.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        tvThu.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1)
            tvThu.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        else
            tvThu.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView tvFri = new TextView(context);
        tvFri.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        tvFri.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1)
            tvFri.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        else
            tvFri.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView tvSat = new TextView(context);
        tvSat.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        tvSat.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1)
            tvSat.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        else
            tvSat.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView tvSon = new TextView(context);
        tvSon.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        tvSon.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1)
            tvSon.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        else
            tvSon.setGravity(Gravity.CENTER_HORIZONTAL);
        tvMon.setText(R.string.monday);
        tvTue.setText(R.string.tuesday);
        tvWed.setText(R.string.wednesday);
        tvThu.setText(R.string.Thursday);
        tvFri.setText(R.string.Friday);
        tvSat.setText(R.string.Saturday);
        tvSon.setText(R.string.Sunday);
        header.addView(tvMon);
        header.addView(tvTue);
        header.addView(tvWed);
        header.addView(tvThu);
        header.addView(tvFri);
        header.addView(tvSat);
        header.addView(tvSon);
        return header;
    }

    private String getRoutine(int day, int month, int year) {
        if(routines == null || interval == null) return null;
        c.set(year, month, day, 0, 0, 0);
        long timeNow = c.getTimeInMillis();
        c.set(selectedYear, selectedMonth, selectedDay, 0, 0, 0);
        long timeStart = c.getTimeInMillis();
        long dif = timeNow - timeStart;
        int days = Math.round((float) dif / (float) (1000*60*60*24));
        int dayInInterval = days % interval.size();
        int routine = interval.get(dayInInterval);
        if(routine == 0) {
            return context.getString(R.string.pause);
        } else {
            for(int i = 0; i < routines.size(); i++) {
                if(routine == routines.get(i).getRId()) {
                    return routines.get(i).getRName();
                }
            }
        }
        return null;
    }

    private boolean isNotPause(int day, int month, int year) {
        c.set(year, month, day, 0, 0, 0);
        long timeNow = c.getTimeInMillis();
        c.set(selectedYear, selectedMonth, selectedDay, 0, 0, 0);
        long timeStart = c.getTimeInMillis();
        long dif = timeNow - timeStart;
        int days = Math.round((float) dif / (float) (1000*60*60*24));
        int dayInInterval = days % interval.size();
        int routine = interval.get(dayInInterval);
        return routine != 0;
    }
}