package com.edward.edwardapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.edward.edwardapplication.databinding.ActivitySerialBinding;

public class SerialActivity extends AppCompatActivity {
/*
    Disallow fetch serial after android Q for third party app,
    need system permission "android.permission.READ_PRIVILEGED_PHONE_STATE"
    see <https://www.jianshu.com/p/e8b6cafa91d5>
*/

    private static final int REQUEST_READ_PHONE_STATE = 101;
    private ActivitySerialBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_serial);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Toast.makeText(this,"android Q后，非系统应用无法获取序列号", Toast.LENGTH_LONG).show();
            finish();
        }
        requestPermissions();
    }

    private void requestPermissions() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            updateSerial();
        }
    }

    private void updateSerial() {
        String serial = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                serial = Build.getSerial();
            } else {
                serial = Build.SERIAL;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        binding.text.setText(serial);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    updateSerial();
                }
                break;
        }
    }
}