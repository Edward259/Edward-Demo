package com.edward.edwardapplication.util;

import android.util.Log;

/**
 * Created by Edward.
 * Date: 2021/10/18
 * Time: 14:41
 * Desc:
 */
public class PrintUtil {
    public void printStack() {
        StringBuilder stringBuilder = new StringBuilder();
        for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
            stringBuilder.append(stackTraceElement.toString()).append("\n");
        }
        Log.e("edward-tag", "printStack: " + stringBuilder.toString());
    }
}
