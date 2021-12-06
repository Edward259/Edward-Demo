package com.edward.edwardapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.edward.edwardapplication.databinding.ActivityFuntionTestBinding;

public class FunctionTestActivity extends AppCompatActivity {

    private ActivityFuntionTestBinding binding;

    enum FUNCTION {
        TEST_1,
        TEST_2,
        TEST_3,
        TEST_4,
        TEST_5
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_funtion_test);
        initView();
    }

    private void initView() {
        for (FUNCTION function : FUNCTION.values()) {
            Button button = new Button(this);
            button.setText(function.name());
            button.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NewApi")
                @Override
                public void onClick(View view) {
                    switch (function) {
                        case TEST_1:
                            break;
                        case TEST_2:
                            break;
                        case TEST_3:
                            break;
                        case TEST_4:
                            break;
                        case TEST_5:
                            break;
                    }
                }
            });
            binding.container.addView(button);
        }
    }
}