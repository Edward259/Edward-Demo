package com.edward.edwardapplication;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ADBActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_d_b);
        findViewById(R.id.open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setADBEnabled(true);
            }
        });
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setADBEnabled(false);
            }
        });
    }

    public void setADBEnabled(Boolean enabled) {
        final String ADB_KEY = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 ?
                Settings.Global.ADB_ENABLED : Settings.Secure.ADB_ENABLED;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                Settings.Global.putInt(getContentResolver(), ADB_KEY, enabled.compareTo(false));
            } else {
                Settings.Secure.putInt(getContentResolver(), ADB_KEY, enabled.compareTo(false));
            }
        } catch (Exception e) {
        }
    }

}