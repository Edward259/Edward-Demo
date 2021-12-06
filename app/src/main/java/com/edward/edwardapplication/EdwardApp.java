package com.edward.edwardapplication;

import android.app.Application;

import com.edward.edwardapplication.service.ReaderInfoServiceHelper;

/**
 * Created by Edward.
 * Date: 2021/10/29
 * Time: 16:28
 * Desc:
 */
public class EdwardApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ReaderInfoServiceHelper.init(this);
    }
}
