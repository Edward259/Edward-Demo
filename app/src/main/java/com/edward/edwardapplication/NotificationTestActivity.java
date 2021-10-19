package com.edward.edwardapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.databinding.DataBindingUtil;

import com.edward.edwardapplication.databinding.ActivityNotificationTestBinding;

//see { https://www.jianshu.com/p/6aec3656e274 }

public class NotificationTestActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_NORMAL = 1;
    private String channelId = "edward_music";
    private ActivityNotificationTestBinding binding;
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification_test);
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        initView();
    }

    private void initView() {
        binding.notify.setOnClickListener(view -> notifyMessage());
        binding.clear.setOnClickListener(view -> notificationManager.cancelAll());
    }

    private void notifyMessage() {
        //Android8.0以上的适配
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,"音乐消息",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager =(NotificationManager)getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        //设置通知点击后跳转到详情界面
        PendingIntent pendingIntent =  PendingIntent.getActivity(this, REQUEST_CODE_NORMAL,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);
        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("我是标题")
                .setContentText("我是内容")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .build();
        //调用管理器的notify方法
        notificationManager.notify(1,notification);
    }
}