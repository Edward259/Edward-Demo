package com.edward.edwardapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.edward.edwardapplication.databinding.ActivitySystemSettingBinding;

public class SystemSettingActivity extends AppCompatActivity {
    enum SettingFunction {
        SETTINGS,
        UNKNOWN_INSTALL_PERMISSION,
        APPLICATION_SETTINGS,
    }

    private ActivitySystemSettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_system_setting);
        initView();
    }

    private void initView() {

        for (SettingFunction settingFunction : SettingFunction.values()) {
            Button button = new Button(this);
            button.setText(settingFunction.name());
            button.setOnClickListener(v -> {
                switch (settingFunction) {
                    case UNKNOWN_INSTALL_PERMISSION:
                        toInstallPermissionSettingIntent();
                        break;
                    case SETTINGS:
                        startActivity(new Intent(Settings.ACTION_SETTINGS));
                        break;
                    case APPLICATION_SETTINGS:
                        startActivity(new Intent(Settings.ACTION_APPLICATION_SETTINGS));
                        break;
                }
            });
            binding.container.addView(button);
        }
    }

    private void toInstallPermissionSettingIntent() {//打开安装未知来源的设置界面
//        Uri packageURI = Uri.parse("package:com.strongene.ssestudent");
        Uri packageURI = Uri.parse("package:" + getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        startActivity(intent);
    }
}