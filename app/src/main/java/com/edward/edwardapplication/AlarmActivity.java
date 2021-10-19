package com.edward.edwardapplication;

import android.app.AlarmManager;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.edward.edwardapplication.databinding.ActivityAlarmBinding;
import com.edward.edwardapplication.util.AlarmUtil;

import java.util.Calendar;

//see {
// https://blog.csdn.net/vicluo/article/details/8484939
// https://www.jianshu.com/p/b88b1f353d4e?utm_campaign=maleskine&utm_content=note&utm_medium=seo_notes&utm_source=recommendation
// }

public class AlarmActivity extends AppCompatActivity {

    private ActivityAlarmBinding binding;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_alarm);
        initCalendar();
        initView();
        int[] ints = {1, 2, 4};
        int[] ints2 = {16, 32, 64};
        for (int anInt : ints) {
            for (int i : ints2) {
                Log.e("edward--", "onCreate: " + "value1:" + anInt + "  value2:" + i
                        + "  |:" + (anInt | i) + "   &:" + (anInt & i) + "  & 0x007:" + ((anInt | i) & (0x001 | 0x002 | 0x004))
                        + (anInt & i) + "  & boot:" + ((anInt | i) & (0x010 | 0x020 | 0x040)));
            }
        }
    }

    private void initView() {
        binding.timePick.setIs24HourView(true);
        binding.timePick.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                Log.e("edward", "onTimeChanged: " + hour + ":" + minute);
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
            }
        });
        binding.alarm.setOnClickListener(view -> AlarmUtil.alarm(AlarmActivity.this, calendar));
        binding.cancelAlarm.setOnClickListener(view -> AlarmUtil.cancelAlarm(AlarmActivity.this));
    }

    private Calendar initCalendar() {
        calendar = AlarmUtil.defaultCalendar();
        return calendar;
    }
}