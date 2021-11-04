package com.edward.edwardapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.edward.edwardapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public static final String MODE_KEY = "mode_key";
    private ActivityMainBinding binding;

    public enum FUNCTION {
        SETTINGS(SystemSettingActivity.class),
        FONT_TEST(FontsActivity.class),
        SERIAL_TEST(SerialActivity.class),
        SIGNATURE_TEST(SignatureActivity.class),
        WAKE_LOCK_TEST(WakeLockActivity.class),
        WIFI_DIRECT_TEST(WifiDirectActivity.class),
        WEB_VIEW_TEST(WebviewActivity.class),
        CUSTOM_VIEW_TEST(CustomViewActivity.class),
        NOTIFICATION_TEST(NotificationTestActivity.class),
        ALARM_TEST(AlarmActivity.class),
        ADJUST_STATUS_BAR(CustomStatusBarActivity.class),
        SERVICE_TEST(ServiceTestActivity.class),
        RXJAVA_TEST(RxjavaTestActivity.class),
        PERMISSION_TEST(PermissionRequestActivity.class),
        ANIMATION_TEST(AnimationActivity.class),
        ADB_TEST(ADBActivity.class);

        private Class targetActivity;

        FUNCTION(Class targetActivity) {
            this.targetActivity = targetActivity;
        }

        public Class getTargetActivity() {
            return targetActivity;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void initView() {
        for (FUNCTION value : FUNCTION.values()) {
            Button button = new Button(this);
            button.setText(value.name());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    go(value.getTargetActivity());
                }
            });
            binding.container.addView(button);
        }
    }

    private void go(Class targetActivity) {
        startActivity(new Intent(MainActivity.this, targetActivity));
    }
}