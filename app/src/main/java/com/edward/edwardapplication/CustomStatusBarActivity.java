package com.edward.edwardapplication;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.edward.edwardapplication.databinding.ActivityCustomStatusBarBinding;
import com.edward.edwardapplication.util.StatusBarUtil;

/*
    https://blog.csdn.net/guolin_blog/article/details/51763825

*/
public class CustomStatusBarActivity extends AppCompatActivity {

    private boolean isDark;
    private ActivityCustomStatusBarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_custom_status_bar);
        initView();
    }

    private void initView() {
        binding.switchColor.setOnClickListener(view -> switchStatusBarColor());
        binding.switchImmersion.setOnClickListener(view -> StatusBarUtil.setImmersionStatus(this));
        binding.switchActionBar.setOnClickListener(view -> StatusBarUtil.switchActionBar(this));
    }

    private void switchStatusBarColor() {
        StatusBarUtil.setStatusBarMode(this, isDark, isDark ? R.color.white : R.color.black);
        isDark = !isDark;
    }
}