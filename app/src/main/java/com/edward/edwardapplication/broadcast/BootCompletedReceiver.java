package com.edward.edwardapplication.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.edward.edwardapplication.util.AlarmUtil;

/**
 * Created by Edward.
 * Date: 2021/9/17
 * Time: 17:52
 * Desc:
 */
public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmUtil.alarm(context, AlarmUtil.defaultCalendar());
        Log.e("edward", "onReceive: AlarmBroadcastReceiver"  );
        Toast.makeText(context,"AlarmBroadcastReceiver", Toast.LENGTH_LONG).show();
    }
}
