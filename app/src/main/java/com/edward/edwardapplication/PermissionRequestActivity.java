package com.edward.edwardapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.edward.edwardapplication.databinding.ActivityPermissionRequestBinding;

import java.util.Set;

public class PermissionRequestActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private static String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA
    };

    private ActivityPermissionRequestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_permission_request);
        initView();
        verifyPermissions(PERMISSIONS);
    }

    private void initView() {
        binding.audioRecorder.setOnClickListener(view -> verifyPermissions(PERMISSIONS));
    }


    public void verifyPermissions(String[] permissions) {
        boolean hasPermission = true;
        for (String permission : permissions) {
            boolean granted = ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
            hasPermission &= granted;
            Log.e("edward", "verifyPermissions: " + permission + "   granted:" + granted + hasPermission);
        }
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
        }
    }
}