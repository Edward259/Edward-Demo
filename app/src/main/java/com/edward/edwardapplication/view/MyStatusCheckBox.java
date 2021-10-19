package com.edward.edwardapplication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.edward.edwardapplication.R;


public class MyStatusCheckBox extends AppCompatCheckBox {

    private int iconWidth;
    private int iconHeight;
    private int iconDrawable;

    public MyStatusCheckBox(Context context) {
        super(context);
    }

    public MyStatusCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MyStatusCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IWGStatusCheckBox);
        iconWidth = typedArray.getDimensionPixelSize(R.styleable.IWGStatusCheckBox_iconWidth, -1);
        iconHeight = typedArray.getDimensionPixelSize(R.styleable.IWGStatusCheckBox_iconHeight, -1);
        iconDrawable = typedArray.getDimensionPixelSize(R.styleable.IWGStatusCheckBox_iconDrawable, R.drawable.iwg_custom_switch);
    }

    private android.graphics.drawable.Drawable getDrawableWithBound(Context context) {
        android.graphics.drawable.Drawable drawable = context.getDrawable(iconDrawable);
        adjustDrawableBound(drawable);
        return drawable;
    }

    private void adjustDrawableBound(android.graphics.drawable.Drawable drawable) {
        int drawableWith = drawable.getIntrinsicWidth();
        int drawableHeight = drawable.getIntrinsicHeight();
        if (drawableWith * drawableHeight == 0) {
            return;
        }
        Rect newBounds;
        if (iconHeight <= 0 && iconHeight <= 0) {
            newBounds = new Rect(0, 0, drawableWith, drawableHeight);
        } else if (iconHeight > 0 && iconWidth > 0) {
            newBounds = new Rect(0, 0, iconWidth, iconHeight);
        } else {
            float aspectRatio = (float) drawableHeight / drawableWith;
            iconWidth = iconWidth <= 0 ? (int) (iconHeight / aspectRatio) : iconWidth;
            iconHeight = iconHeight <= 0 ? (int) (iconWidth * aspectRatio) : iconHeight;
            newBounds = new Rect(0, 0, iconWidth, iconHeight);
        }
        drawable.setBounds(newBounds);
    }

    private void init(Context context, AttributeSet attrs) {
        initAttr(context, attrs);
        setButtonDrawable(null);
        setBackground(null);
        setCompoundDrawables(getDrawableWithBound(context), null, null, null);
    }
}