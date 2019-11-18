package com.workoutlog.workoutlog.application;

import android.app.Activity;
import androidx.multidex.MultiDexApplication;

@SuppressWarnings("UnusedReturnValue")
public class WorkoutLog extends MultiDexApplication {

    private Activity mCurrentActivity = null;
    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }
    public void setCurrentActivity(Activity currentActivity) {
        mCurrentActivity = currentActivity;
    }
}
