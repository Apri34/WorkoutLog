package com.workoutlog.workoutlog.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.workoutlog.workoutlog.LayoutWrapContentUpdater;
import com.workoutlog.workoutlog.R;

public class CustomEditText extends ConstraintLayout {

    private static final int HINT_MARGIN_SIDE = 5;
    private static final int DEF_HINT_SIZE = 6;
    private static final int DEF_HINT_SIZE_FOCUSED = 4;
    private static final int DEF_TEXT_SIZE = 6;
    private static final int ANIMATION_DURATION = 150;

    private final EditText textField;
    private final TextView hint;
    private final ConstraintLayout layout;
    private final TextView errorMessage;
    private final float hintSize;
    private final float hintSizeFocused;
    private final int hintColorFocused;
    private final int hintColor;
    private final int hintColorError;

    private Error error = Error.NULL;

    public EditText getTextField() {
        return textField;
    }

    public void showErrorMessage(Error error) {
        if(error == Error.NULL) {
            hideErrorMessage();
            return;
        }

        textField.setActivated(true);
        this.error = error;
        switch (error) {
            case ERROR_FIELD_EMPTY: {
                errorMessage.setText(R.string.error_message_field_empty);
                break;
            }
            case ERROR_WRONG_EMAIL: {
                errorMessage.setText(R.string.error_message_wrong_email);
                break;
            }
            case ERROR_WRONG_PASSWORD: {
                errorMessage.setText(R.string.error_message_wrong_password);
                break;
            }
            case ERROR_EMAIL_ALREADY_USED: {
                errorMessage.setText(R.string.error_message_email_already_used);
                break;
            }
            case ERROR_EMAIL_NOT_EXISTS: {
                errorMessage.setText(R.string.error_message_email_not_exists);
                break;
            }
            case ERROR_CONFIRM_PASSWORD_WRONG: {
                errorMessage.setText(R.string.error_message_confirm_password_wrong);
                break;
            }
            case ERROR_PASSWORD_REQUIREMENTS_NOT_MET: {
                errorMessage.setText(R.string.error_message_password_requirements_not_met);
                break;
            }
            case EXERCISE_ALREADY_EXISTS: {
                errorMessage.setText(R.string.this_exercise_already_exists);
                break;
            }
            case NO_ERROR_MESSAGE: {
                errorMessage.setText("");
                break;
            }
        }
        hint.setTextColor(hintColorError);
    }

    public void hideErrorMessage() {
        textField.setActivated(false);
        errorMessage.setText("");
        error = Error.NULL;
        if(textField.hasFocus()) {
            hint.setTextColor(hintColorFocused);
        } else {
            hint.setTextColor(hintColor);
        }
    }

    public Error getError() {
        return error;
    }

    public void setText(String text) {
        textField.setText(text);
        ConstraintSet set = new ConstraintSet();
        set.clone(layout);
        set.clear(R.id.custom_edit_text_hint, ConstraintSet.LEFT);
        set.connect(R.id.custom_edit_text_hint, ConstraintSet.RIGHT, R.id.custom_edit_text_constraint_layout, ConstraintSet.RIGHT, dpToPx(HINT_MARGIN_SIDE));
        set.applyTo(layout);
        hint.setTextSize(hintSizeFocused);
    }

    public CustomEditText(Context context) {
        this(context, null);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.custom_edit_text, this, true);
        setClickable(false);
        textField = findViewById(R.id.custom_edit_text_text_field);
        textField.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    focusHint();
                } else {
                    unfocusHint();
                }
            }
        });
        hint = findViewById(R.id.custom_edit_text_hint);
        hint.setClickable(false);
        layout = findViewById(R.id.custom_edit_text_constraint_layout);
        errorMessage = findViewById(R.id.custom_edit_text_error_message);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText, defStyleAttr, 0);
        hintSize = a.getDimension(R.styleable.CustomEditText_hintSize, dpToPx(DEF_HINT_SIZE));
        hint.setTextSize(hintSize);
        hintSizeFocused = a.getDimension(R.styleable.CustomEditText_hintSizeFocused, dpToPx(DEF_HINT_SIZE_FOCUSED));
        if(a.hasValue(R.styleable.CustomEditText_hint)) {
            hint.setText(a.getText(R.styleable.CustomEditText_hint));
        }
        hintColorFocused = a.getColor(R.styleable.CustomEditText_hintColorFocused, getResources().getColor(R.color.colorPrimary));
        hintColor = a.getColor(R.styleable.CustomEditText_hintColor, getResources().getColor(R.color.def_hint_color));
        if(hint.hasFocus()) {
            hint.setTextColor(hintColorFocused);
        } else {
            hint.setTextColor(hintColor);
        }
        hintColorError = a.getColor(R.styleable.CustomEditText_hintColorError, getResources().getColor(R.color.hint_color_error));
        textField.setTextColor(a.getColor(R.styleable.CustomEditText_textColor, getResources().getColor(android.R.color.black)));
        if (a.hasValue(R.styleable.CustomEditText_customBackground)) {
            Drawable background = a.getDrawable(R.styleable.CustomEditText_customBackground);
            textField.setBackground(background);
        }
        if(a.getBoolean(R.styleable.CustomEditText_isPassword, false)) {
            textField.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            textField.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        textField.setTextSize(a.getDimension(R.styleable.CustomEditText_textSize, dpToPx(DEF_TEXT_SIZE)));

        a.recycle();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
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

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).restoreHierarchyState(ss.childrenStates);
        }

        if(!textField.getText().toString().isEmpty()) {
            hint.setTextSize(hintSizeFocused);
            ConstraintSet set = new ConstraintSet();
            set.clone(layout);
            set.clear(R.id.custom_edit_text_hint, ConstraintSet.LEFT);
            set.connect(R.id.custom_edit_text_hint, ConstraintSet.RIGHT, R.id.custom_edit_text_constraint_layout, ConstraintSet.RIGHT, dpToPx(HINT_MARGIN_SIDE));
            set.applyTo(layout);
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

    private void focusHint() {
        hint.setTextSize(hintSizeFocused);
        LayoutWrapContentUpdater.wrapContentAgain(this);
        if(!textField.isActivated()) {
            hint.setTextColor(hintColorFocused);
        }
        if(textField.getText().toString().isEmpty())
            moveHintToRight();
        if(textField.getPaddingRight() == 0) {
            textField.setPadding(textField.getPaddingLeft(), textField.getPaddingTop(), hint.getWidth() + dpToPx(HINT_MARGIN_SIDE), textField.getPaddingBottom());
        }
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(textField, InputMethodManager.SHOW_IMPLICIT);
    }

    private void unfocusHint() {
        if(!textField.isActivated()) {
            hint.setTextColor(hintColor);
        }
        if(textField.getText().toString().isEmpty()) {
            moveHintToLeft();
            hint.setTextSize(hintSize);
            LayoutWrapContentUpdater.wrapContentAgain(this);
        }
    }

    private void moveHintToRight() {
        int horizontalDistance = layout.getWidth() - hint.getWidth() - dpToPx(HINT_MARGIN_SIDE * 2);
        TranslateAnimation anim = new TranslateAnimation(0, horizontalDistance, 0, 0);
        anim.setDuration(ANIMATION_DURATION);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                if(textField.hasFocus()) {
                    ConstraintSet set = new ConstraintSet();
                    set.clone(layout);
                    set.clear(R.id.custom_edit_text_hint, ConstraintSet.LEFT);
                    set.connect(R.id.custom_edit_text_hint, ConstraintSet.RIGHT, R.id.custom_edit_text_constraint_layout, ConstraintSet.RIGHT, dpToPx(HINT_MARGIN_SIDE));
                    set.applyTo(layout);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        hint.startAnimation(anim);
    }

    private void moveHintToLeft() {
        ConstraintSet set = new ConstraintSet();
        set.clone(layout);
        set.clear(R.id.custom_edit_text_hint, ConstraintSet.RIGHT);
        set.connect(R.id.custom_edit_text_hint, ConstraintSet.LEFT, R.id.custom_edit_text_constraint_layout, ConstraintSet.LEFT, dpToPx(HINT_MARGIN_SIDE));
        set.applyTo(layout);
    }

    private int dpToPx(int dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
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

        @SuppressWarnings({"rawtypes", "unchecked"})
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

    public enum Error {
        //EditText is empty
        ERROR_FIELD_EMPTY,

        //Register form: Passwords don't match
        ERROR_CONFIRM_PASSWORD_WRONG,

        //Register form: Password requirements not met
        ERROR_PASSWORD_REQUIREMENTS_NOT_MET,

        //Register form: Email does not exist (absolutely not)
        ERROR_EMAIL_NOT_EXISTS,

        //Register form: Email is already signed up
        ERROR_EMAIL_ALREADY_USED,

        //Login form: Password is wrong
        ERROR_WRONG_PASSWORD,

        //Login form: Email is not signed up
        ERROR_WRONG_EMAIL,

        //Exercise Dialog
        EXERCISE_ALREADY_EXISTS,

        NO_ERROR_MESSAGE,

        //Just as return value
        NULL
    }
}