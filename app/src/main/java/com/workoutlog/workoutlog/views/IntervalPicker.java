package com.workoutlog.workoutlog.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import com.workoutlog.workoutlog.R;
import com.workoutlog.workoutlog.database.AppDatabase;
import com.workoutlog.workoutlog.database.DatabaseInitializer;
import com.workoutlog.workoutlog.database.entities.Routine;
import com.workoutlog.workoutlog.database.entities.Trainingplan;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@SuppressWarnings({"BooleanMethodIsAlwaysInverted", "UnusedReturnValue"})
public class IntervalPicker extends ConstraintLayout {

    private IIntervalClickedListener listener;

    public void setIntervalClickedListener(IIntervalClickedListener listener) {
        this.listener = listener;
    }

    private static final int PAUSE_DAY = 0;

    private final ViewFlipper intervalFrame;
    private final ArrayList<ScrollView> intervals;
    private final ImageButton buttonNext;
    private final ImageButton buttonPrev;
    private final ArrayList<Integer> listInterval1;
    private final ArrayList<Integer> listInterval2;
    private final ArrayList<Integer> listInterval3;
    private final DatabaseInitializer dbInitializer;
    private final AppDatabase database;

    private final float textSizeTop;
    private final float textSizeItems;

    private int currentInterval = 0;

    private final Context context;

    public void refreshButtons() {
        boolean nextEnabled = currentInterval != intervals.size() - 1;
        buttonNext.setEnabled(nextEnabled);
        buttonNext.setClickable(nextEnabled);
        boolean prevEnabled = currentInterval != 0;
        buttonPrev.setEnabled(prevEnabled);
        buttonPrev.setClickable(prevEnabled);
    }

    public boolean setInterval(ArrayList<Integer> interval) {
        boolean isInterval1 = true;
        if(interval.size() == listInterval1.size()) {
            for (int i = 0; i < interval.size(); i++) {
                if(!interval.get(i).equals(listInterval1.get(i))) {
                    isInterval1 = false;
                    break;
                }
            }
        } else {
            isInterval1 = false;
        }
        if(isInterval1) {
            currentInterval = 0;
            return false;
        }
        boolean isInterval2 = true;
        if(interval.size() == listInterval2.size()) {
            for (int i = 0; i < interval.size(); i++) {
                if(!interval.get(i).equals(listInterval2.get(i))) {
                    isInterval2 = false;
                    break;
                }
            }
        } else {
            isInterval2 = false;
        }
        if(isInterval2) {
            currentInterval = 1;
            intervalFrame.showNext();
            return false;
        }
        boolean isInterval3 = true;
        if(interval.size() == listInterval3.size()) {
            for (int i = 0; i < interval.size(); i++) {
                if(!interval.get(i).equals(listInterval3.get(i))) {
                    isInterval3 = false;
                    break;
                }
            }
        } else {
            isInterval3 = false;
        }
        if(isInterval3) {
            currentInterval = 2;
            intervalFrame.showNext();
            intervalFrame.showNext();
            return false;
        }
        return true;
    }

    public ArrayList<Integer> getSelectedInterval() {
        switch (currentInterval) {
            case 0: return listInterval1;
            case 1: return listInterval2;
            case 2: return listInterval3;
            default: return null;
        }
    }

    public IntervalPicker(Context context) {
        this(context, null);
    }

    public IntervalPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IntervalPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;

        LayoutInflater l = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        l.inflate(R.layout.interval_picker, this, true);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IntervalPicker, defStyleAttr, 0);
        textSizeTop = a.getDimension(R.styleable.IntervalPicker_textSizeTopIP, 20);
        textSizeItems = a.getDimension(R.styleable.IntervalPicker_textSizeItemsIP, 16);

        a.recycle();

        Button buttonCustom = findViewById(R.id.button_interval_picker_custom);
        buttonCustom.setOnClickListener(v -> {
            if(listener != null) {
                listener.customInterval();
            }
        });

        buttonNext = findViewById(R.id.button_next_interval_picker);
        buttonNext.setOnClickListener(v -> nextInterval());
        buttonPrev = findViewById(R.id.button_prev_interval_picker);
        buttonPrev.setOnClickListener(v -> prevInterval());

        dbInitializer = DatabaseInitializer.getInstance(context);
        database = AppDatabase.getInstance(context);
        listInterval1 = new ArrayList<>();
        listInterval2 = new ArrayList<>();
        listInterval3 = new ArrayList<>();
        intervals = new ArrayList<>();

        intervalFrame = findViewById(R.id.interval_picker_interval);
        intervalFrame.setOnClickListener(v -> {
            if(listener != null) {
                switch (currentInterval) {
                    case 0:
                        listener.intervalClicked(listInterval1);
                        break;
                    case 1:
                        listener.intervalClicked(listInterval2);
                        break;
                    case 2:
                        listener.intervalClicked(listInterval3);
                        break;
                }
            }
        });

        refreshButtons();
    }

    private void createInterval1(List<Routine> routines) throws ExecutionException, InterruptedException {
        for(int i = 0; i < routines.size(); i++) {
            listInterval1.add(routines.get(i).getRId());
            listInterval1.add(PAUSE_DAY);
        }
        writeInterval(listInterval1);
    }

    private void createInterval2(List<Routine> routines) throws ExecutionException, InterruptedException {
        int size = routines.size();
        if(size < 2) {
            return;
        }

        boolean pauseOnEnd = false;
        if(size % 2 != 0 && size > 4) {
            pauseOnEnd = true;
        }
        for(int i = 0; i < routines.size(); i++) {
            listInterval2.add(routines.get(i).getRId());
            if((i + 1) % 2 == 0 || (pauseOnEnd && (i+1) == size)) {
                listInterval2.add(PAUSE_DAY);
            }
        }
        writeInterval(listInterval2);
    }

    private void createInterval3(List<Routine> routines) throws ExecutionException, InterruptedException {
        int size = routines.size();
        if(size < 3) {
            return;
        }
        for(int i = 0; i < routines.size(); i++) {
            listInterval3.add(routines.get(i).getRId());
        }
        listInterval3.add(PAUSE_DAY);
        writeInterval(listInterval3);
    }

    private TextView createItem(Context context, String routine) {
        TextView tv = new TextView(context);
        tv.setText(routine);
        tv.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        tv.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeItems);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        return tv;
    }

    private TextView createTop(Context context, String routine) {
        TextView tv = new TextView(context);
        tv.setText(routine);
        tv.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        tv.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeTop);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        return tv;
    }

    public void setTrainingplan(Trainingplan trp) throws ExecutionException, InterruptedException {
        List<Routine> routines = dbInitializer.getRoutinesByTpId(database.routineDao(), trp.getTpId());
        createInterval1(routines);
        createInterval2(routines);
        createInterval3(routines);
    }

    private void writeInterval(ArrayList<Integer> interval) throws ExecutionException, InterruptedException {
        ScrollView scrollView = new ScrollView(context);
        scrollView.setHorizontalScrollBarEnabled(false);
        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.WRAP_CONTENT, ScrollView.LayoutParams.MATCH_PARENT));
        LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        layout.setOrientation(LinearLayout.VERTICAL);

        int count = intervals.size() + 1;
        String top = String.format(context.getString(R.string.interval_num), String.valueOf(count));
        layout.addView(createTop(context, top));
        View divider = new View(context);
        divider.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
        divider.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        layout.addView(divider);

        for(int i = 0; i < interval.size(); i++) {
            if(interval.get(i) == PAUSE_DAY)
                layout.addView(createItem(context, context.getString(R.string.pause)));
            else {
                String routine = dbInitializer.getRoutineById(database.routineDao(), interval.get(i)).getRName();
                layout.addView(createItem(context, routine));
            }
        }
        scrollView.addView(layout);
        intervals.add(scrollView);
        intervalFrame.addView(intervals.get(intervals.size() - 1));
    }

    private void nextInterval() {
        currentInterval++;
        intervalFrame.setInAnimation(context, R.anim.slide_in_right);
        intervalFrame.setOutAnimation(context, R.anim.slide_out_left);
        intervalFrame.showNext();
        boolean enabled = currentInterval != intervals.size() - 1;
        buttonNext.setEnabled(enabled);
        buttonPrev.setEnabled(true);
        buttonNext.setClickable(enabled);
        buttonPrev.setClickable(true);
    }

    private void prevInterval() {
        currentInterval--;
        intervalFrame.setInAnimation(context, R.anim.slide_in_left);
        intervalFrame.setOutAnimation(context, R.anim.slide_out_right);
        intervalFrame.showPrevious();
        boolean enabled = currentInterval != 0;
        buttonPrev.setEnabled(enabled);
        buttonNext.setEnabled(true);
        buttonPrev.setClickable(enabled);
        buttonNext.setClickable(true);
    }

    public interface IIntervalClickedListener {
        void intervalClicked(@NonNull ArrayList<Integer> interval);
        void customInterval();
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.childrenStates = new SparseArray();
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).saveHierarchyState(ss.childrenStates);
        }
        return ss;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).restoreHierarchyState(ss.childrenStates);
        }
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    static class SavedState extends BaseSavedState {
        SparseArray childrenStates;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in, ClassLoader classLoader) {
            super(in);
            childrenStates = in.readSparseArray(classLoader);
        }

        @SuppressWarnings({"unchecked"})
        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeSparseArray(childrenStates);
        }

        public static final ClassLoaderCreator<SavedState> CREATOR
                = new ClassLoaderCreator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source, ClassLoader loader) {
                return new SavedState(source, loader);
            }

            @Override
            public SavedState createFromParcel(Parcel source) {
                return createFromParcel(source, null);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
