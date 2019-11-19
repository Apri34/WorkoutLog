package com.workoutlog.workoutlog.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import com.workoutlog.workoutlog.LayoutWrapContentUpdater;
import com.workoutlog.workoutlog.R;
import com.workoutlog.workoutlog.database.AppDatabase;
import com.workoutlog.workoutlog.database.DatabaseInitializer;
import com.workoutlog.workoutlog.database.entities.Routine;
import com.workoutlog.workoutlog.database.entities.Trainingplan;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("UnusedReturnValue")
public class CustomIntervalCreator extends ConstraintLayout {

    private final LinearLayout customInterval;
    private final LinearLayout buttonBar;
    private final Button buttonCreateInterval;
    private final Button buttonRemove;
    private final Button buttonPause;
    private ArrayList<Integer> interval;
    private List<Routine> routines;

    private IIntervalCreatedListener listener;

    private final DatabaseInitializer dbInitializer;
    private final AppDatabase database;

    private final Context context;
    private final float textSizeItems;
    private final int buttonHeight;

    private boolean isCustomIntervalFinished = false;
    public boolean isCustomIntervalFinished() {
        return isCustomIntervalFinished;
    }

    public void removeCustomInterval() {
        interval.clear();
        customInterval.removeAllViews();
    }
    public void setInterval(ArrayList<Integer> interval) {
        this.interval = interval;
        for(int i = 0; i < interval.size(); i++) {
            if(interval.get(i) == 0) {
                customInterval.addView(createItem(context.getString(R.string.pause)));
            } else {
                for (int j = 0; j < routines.size(); j++) {
                    if(routines.get(j).getRId() == interval.get(i)) {
                        customInterval.addView(createItem(routines.get(j).getRName()));
                    }
                }
            }
        }
        refreshIntervalFinished();
        refreshButtons();
    }

    public ArrayList<Integer> getInterval() {
        return interval;
    }

    public CustomIntervalCreator(Context context) {
        this(context, null);
    }

    public CustomIntervalCreator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomIntervalCreator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater l = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        l.inflate(R.layout.custom_interval_creator, this, true);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomIntervalCreator, defStyleAttr, 0);

        textSizeItems = a.getDimension(R.styleable.CustomIntervalCreator_textSizeItemsCIC, 16);
        buttonHeight = a.getDimensionPixelSize(R.styleable.CustomIntervalCreator_buttonHeight, 100);

        a.recycle();

        customInterval = findViewById(R.id.custom_interval);
        buttonCreateInterval = findViewById(R.id.button_interval_created);
        buttonCreateInterval.setOnClickListener(v -> {
            if(listener != null)
                listener.intervalCreated(interval);
        });
        buttonRemove = findViewById(R.id.button_interval_remove_item);
        buttonRemove.setOnClickListener(v -> removeItem());
        buttonBar = findViewById(R.id.button_bar_custom_interval_creator);
        buttonPause = findViewById(R.id.button_interval_pause);
        buttonPause.setOnClickListener(v -> {
            addItem(-1);
            refreshButtons();
        });

        interval = new ArrayList<>();
        dbInitializer = DatabaseInitializer.getInstance(context);
        database = AppDatabase.getInstance(context);
        getViewTreeObserver().addOnGlobalLayoutListener(this::resizeWidgets);
    }

    public void setTrainingplan(Trainingplan trp) throws ExecutionException, InterruptedException {
        routines = dbInitializer.getRoutinesByTpId(database.routineDao(), trp.getTpId());
        for(int i = 0; i < routines.size(); i++) {
            buttonBar.addView(createButton(routines.get(i).getRName()));
        }
        refreshIntervalFinished();
        refreshButtons();
    }

    public interface IIntervalCreatedListener {
        void intervalCreated(@NonNull ArrayList<Integer> interval);
    }

    public void setIntervalCreatedListener(IIntervalCreatedListener listener) {
        this.listener = listener;
    }

    private void addItem(int which) {
        if(which == -1) {
            customInterval.addView(createItem(context.getString(R.string.pause)));
            interval.add(0);
        } else {
            customInterval.addView(createItem(routines.get(which).getRName()));
            interval.add(routines.get(which).getRId());
        }
    }

    private TextView createItem(String routine) {
        if(routine.length() > 20) {
            routine = routine.substring(0, 17) + "...";
        }
        TextView tv = new TextView(context);
        tv.setText(routine);
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        tv.setTextColor(typedValue.data);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeItems);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return tv;
    }

    private Button createButton(String routine) {
        if(routine.length() > 15) {
            routine = routine.substring(0, 12) + "...";
        }
        Button btn = new Button(context);
        btn.setText(routine);
        btn.setTextColor(ContextCompat.getColor(context, R.color.white));
        btn.setBackground(ContextCompat.getDrawable(context, R.drawable.background_button));
        btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, buttonHeight));
        btn.setTag(buttonBar.getChildCount());
        btn.setOnClickListener(v -> {
            int index = (int) v.getTag();
            addItem(index);
            refreshIntervalFinished();
            refreshButtons();
        });
        btn.setHeight(getResources().getDimensionPixelSize(R.dimen.button_height_custom_interval_creator));
        int padding = getResources().getDimensionPixelSize(R.dimen.button_padding_custom_interval_creator);
        btn.setPadding(padding, padding, padding, padding);
        btn.setMinHeight(0);
        btn.setMinWidth(0);

        return btn;
    }

    private void removeItem() {
        if(interval.size() > 0) {
            interval.remove(interval.size() - 1);
            customInterval.removeViewAt(customInterval.getChildCount() - 1);
        }
        refreshIntervalFinished();
        refreshButtons();
    }

    private void refreshIntervalFinished() {
        for(int i = 0; i < routines.size(); i++) {
            if(!interval.contains(routines.get(i).getRId())) {
                isCustomIntervalFinished = false;
                return;
            }
        }
        isCustomIntervalFinished = true;
    }

    private void refreshButtons() {
        buttonRemove.setEnabled(interval.size() > 0);
        buttonRemove.setClickable(interval.size() > 0);
        buttonCreateInterval.setEnabled(isCustomIntervalFinished);
        buttonCreateInterval.setClickable(isCustomIntervalFinished);
    }

    private void resizeWidgets() {
        LayoutWrapContentUpdater.wrapContentAgain(buttonBar);
        int width = 0;
        for(int i = 0; i < buttonBar.getChildCount(); i++) {
            if(buttonBar.getChildAt(i).getWidth() > width) {
                width = buttonBar.getChildAt(i).getWidth();
            }
        }
        if(buttonRemove.getWidth() > width)
            width = buttonRemove.getWidth();
        if(buttonPause.getWidth() > width)
            width = buttonPause.getWidth();
        for(int i = 0; i < buttonBar.getChildCount(); i++) {
            Button btn = (Button) buttonBar.getChildAt(i);
            btn.setWidth(width);
        }
        buttonRemove.setWidth(width);
        buttonPause.setWidth(width);
        LayoutWrapContentUpdater.wrapContentAgain(buttonBar);
    }
}