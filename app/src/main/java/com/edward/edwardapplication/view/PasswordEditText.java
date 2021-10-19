package com.edward.edwardapplication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

import com.edward.edwardapplication.R;

import java.util.ArrayList;
import java.util.List;

public class PasswordEditText extends AppCompatEditText {
    protected Paint textPaint, strokePaint;
    protected List<RectF> rectFS;
    private Context mContent;
    private String mText;
    private int strokeWidth, spzceX, spzceY;
    private float textSize;
    private int textLength;
    private int textColor, strokeColor;
    private int CircleRadius;
    private boolean isPassword;
    private OnTextChangeListener OnTextChangeListener;

    public PasswordEditText(Context context) {
        super(context);
        mContent = context;
        setAttrs(null);
        init();
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContent = context;
        setAttrs(attrs);
        init();
    }

    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContent = context;
        setAttrs(attrs);
        init();
    }

    private void setAttrs(AttributeSet attrs) {
        TypedArray t = mContent.obtainStyledAttributes(attrs, R.styleable.PasswordEditText);
        if (t != null) {
            textLength = t.getInt(R.styleable.PasswordEditText_textLength, 4);
            spzceX = t.getDimensionPixelSize(R.styleable.PasswordEditText_space, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()));
            spzceY = t.getDimensionPixelSize(R.styleable.PasswordEditText_space, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()));
            strokeWidth = t.getDimensionPixelSize(R.styleable.PasswordEditText_strokeWidth, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()));
            strokeColor = t.getDimensionPixelSize(R.styleable.PasswordEditText_strokeColor, 0xFF000000);
            CircleRadius = t.getDimensionPixelSize(R.styleable.PasswordEditText_circle, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, getResources().getDisplayMetrics()));
            textColor = t.getColor(R.styleable.PasswordEditText_textColor, 0xFF000000);
            textSize = t.getDimensionPixelSize(R.styleable.PasswordEditText_textSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
            isPassword = t.getBoolean(R.styleable.PasswordEditText_isPassword, true);
            t.recycle();
        }
    }

    private void init() {
        //hide origin content
        super.setTextColor(0X00FFFFFF);
        super.setTextSize(TypedValue.COMPLEX_UNIT_SP, 0);

        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        initPaint();
        rectFS = new ArrayList<>();
        mText = "";

        this.setBackgroundDrawable(null);
        setLongClickable(false);
        setTextIsSelectable(false);
        setCursorVisible(false);

    }

    protected void initPaint() {
        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);

        strokePaint = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(strokeWidth);
        strokePaint.setColor(strokeColor);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (mText == null) {
            return;
        }

        if (text.toString().length() <= textLength) {
            mText = text.toString();
        } else {
            setText(mText);
            setSelection(getText().toString().length());
            setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        }
        if (OnTextChangeListener != null) {
            OnTextChangeListener.onTextChange(mText);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        switch (heightMode) {
            case View.MeasureSpec.EXACTLY:
                heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
                break;
            case View.MeasureSpec.AT_MOST:
                heightSize = widthSize / textLength;
                break;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int Wide = Math.min(getMeasuredHeight(), getMeasuredWidth() / textLength);
        drawStroke(canvas, Wide);
        drawText(canvas);
    }

    protected void drawStroke(Canvas canvas, int wide) {
        for (int i = 0; i < textLength; i++) {
            RectF rect = new RectF(i * wide + spzceX, spzceY, i * wide + wide - spzceX, wide - spzceY);
            canvas.drawCircle(rect.centerX(), rect.centerY(), CircleRadius, strokePaint);
            rectFS.add(rect);
        }
    }

    protected void drawText(Canvas canvas) {
        for (int i = 0; i < mText.length(); i++) {
            if (isPassword) {
                canvas.drawCircle(rectFS.get(i).centerX(), rectFS.get(i).centerY(), CircleRadius, textPaint);
            } else {
                canvas.drawText(mText.substring(i, i + 1), rectFS.get(i).centerX(), rectFS.get(i).centerY(), textPaint);
            }
        }
    }

    private int dp2px(float dpValue) {
        float scale = mContent.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public interface OnTextChangeListener {
        void onTextChange(String text);
    }

    public void setOnTextChangeListener(OnTextChangeListener OnTextChangeListener) {
        this.OnTextChangeListener = OnTextChangeListener;
    }

    public void clearText() {
        setText("");
        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
    }

    public void setSpace(int space) {
        spzceX = space;
        spzceY = space;
    }

    public void setTextLength(int textLength) {
        this.textLength = textLength;
    }

    public int getTextLength() {
        return this.textLength;
    }

    public void setStrokeWidth(int width) {
        strokeWidth = width;
    }

    public void setCircleRadius(int Circle) {
        this.CircleRadius = Circle;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    public int getSpzceX() {
        return spzceX;
    }

    public int getSpzceY() {
        return spzceY;
    }

    public int getCircleRadius() {
        return CircleRadius;
    }

    @Override
    public void setTextSize(float size) {
        this.textSize = size;
    }

    @Override
    public void setTextSize(int unit, float size) {
        textSize = TypedValue.applyDimension(unit, size, getResources().getDisplayMetrics());
    }

    @Override
    public float getTextSize() {
        return textSize;
    }

    @Override
    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public boolean isPassword() {
        return isPassword;
    }

    public void setPassword(boolean password) {
        isPassword = password;
    }

    public void addChar(String data) {
        int index = getSelectionStart();
        getText().insert(index, data);
    }

    public void deleteChar() {
        int index = getSelectionStart();
        if (index <= 0) {
            return;
        }
        getText().delete(index - 1, index);
    }

}
