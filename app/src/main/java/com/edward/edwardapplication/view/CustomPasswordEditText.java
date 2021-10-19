package com.edward.edwardapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * Created by Edward.
 * Date: 2020/2/28
 * Time: 14:24
 * Desc:
 */
public class CustomPasswordEditText extends PasswordEditText {

    public CustomPasswordEditText(Context context) {
        super(context);
    }

    public CustomPasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomPasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void drawStroke(Canvas canvas, int wide) {
        for (int i = 0; i < getTextLength(); i++) {
            RectF rect = new RectF(i * wide + getSpzceX(), getSpzceY(), i * wide + wide - getSpzceX(), wide - getSpzceY());
            canvas.drawLine(rect.left, rect.bottom, rect.right, rect.bottom, strokePaint);
            rectFS.add(rect);
        }
    }

    @Override
    protected void drawText(Canvas canvas) {
        for (int i = 0; i < getText().length(); i++) {
            if (isPassword()) {
                canvas.drawText(i == getText().length() - 1 && i < getTextLength() - 1 ? getText().subSequence(i, i + 1).toString() : "*", rectFS.get(i).centerX(), rectFS.get(i).centerY() + getTextSize() / 2, textPaint);
            } else {
                canvas.drawText(getText().subSequence(i, i + 1).toString(), rectFS.get(i).centerX(), rectFS.get(i).centerY(), textPaint);

            }
        }
    }
}
