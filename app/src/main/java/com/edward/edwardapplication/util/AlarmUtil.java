package com.edward.edwardapplication.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.edward.edwardapplication.MainActivity;

import java.util.Calendar;
import java.util.TimeZone;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Edward.
 * Date: 2021/9/17
 * Time: 17:58
 * Desc:
 */
public class AlarmUtil {
    public static void alarm(Context context, Calendar calendar) {
        long systemTime = System.currentTimeMillis();
        long selectTime = calendar.getTimeInMillis();

        //  如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if (System.currentTimeMillis() > selectTime) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
        }
        long time = selectTime - systemTime;
        Log.e("edward", "alarm: " + time);

        // api 19后无法保证精确
        // am.setRepeating(AlarmManager.RTC_WAKEUP, selectTime, INTERVAL_MILLIS, getPendingIntent());

        //重复任务需要收到任务后再次调用；关机后生效需要接受开机广播再次启用
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.setExact(AlarmManager.RTC_WAKEUP, selectTime, getPendingIntent(context));
    }

    public static void cancelAlarm(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(getPendingIntent(context));
    }


    public static Calendar defaultCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 1);
//        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    private static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent();
        intent.setAction(Constant.ALARM_ACTION);
        intent.setComponent(new ComponentName(context.getPackageName(), Constant.ALARM_BROADCAST_CLASS_NAME));
        return PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
    }
}
