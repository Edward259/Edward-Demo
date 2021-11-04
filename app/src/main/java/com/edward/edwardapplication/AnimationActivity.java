package com.edward.edwardapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.os.Bundle;

import com.edward.edwardapplication.databinding.ActivityAnimationBinding;

/**
 * https://www.jianshu.com/p/14b69ad4aaea
 * */

public class AnimationActivity extends AppCompatActivity {

    private ActivityAnimationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_animation);
    }
}