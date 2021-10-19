package com.edward.edwardapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.PowerManager;
import androidx.appcompat.app.AppCompatActivity;

import com.edward.edwardapplication.databinding.ActivityWakeLockBinding;

public class WakeLockActivity extends AppCompatActivity {

    public static final String ONYX_DOWNLOAD = "onyx_Download";
    private PowerManager.WakeLock wakeLock;
    private ActivityWakeLockBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wake_lock);
        initWakeLock();
        initView();
    }

    @SuppressLint("InvalidWakeLockTag")
    private void initWakeLock() {
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, ONYX_DOWNLOAD);
    }

    private void initView() {
        binding.acquire.setOnClickListener(view -> wakeLock.acquire());
        binding.release.setOnClickListener(view -> wakeLock.release());
    }

}