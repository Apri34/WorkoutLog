package com.workoutlog.workoutlog.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.workoutlog.workoutlog.R;

import java.util.ArrayList;

public class PageIdenticator extends LinearLayout {

    public void selectPage(int page) {
        selectDot(page);
    }

    public void nextPage() {
        selectDot(selectedDot + 1);
    }

    public void prevPage() {
        selectDot(selectedDot - 1);
    }

    private final ArrayList<View> dots;
    private int selectedDot;

    public PageIdenticator(Context context) {
        this(context, null);
    }

    public PageIdenticator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageIdenticator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.page_identicator, this, true);

        dots = new ArrayList<>();
        dots.add(findViewById(R.id.page_identicator_dot1));
        dots.add(findViewById(R.id.page_identicator_dot2));
        dots.add(findViewById(R.id.page_identicator_dot3));
        dots.add(findViewById(R.id.page_identicator_dot4));

        setOrientation(LinearLayout.HORIZONTAL);

        setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));

        selectDot(0);
    }

    private void selectDot(int dot) {
        if(dot < 4 && dot >= 0) {
            for (int i = 0; i < 4; i++) {
                dots.get(i).setSelected(i == dot);
            }
            selectedDot = dot;
        }
    }
}
