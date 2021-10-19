package com.edward.edwardapplication;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.edward.edwardapplication.databinding.ActivityCustomViewBinding;
import com.edward.edwardapplication.view.MyStatusCheckBox;

public class CustomViewActivity extends AppCompatActivity {

    private ActivityCustomViewBinding binding;
    private MyStatusCheckBox bluetoothSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_custom_view);
        initView();
        IntentFilter mIntentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                handleStateChanged(state);
            }
        };
        registerReceiver(mReceiver, mIntentFilter);

        unregisterReceiver(mReceiver);

    }

    private void handleStateChanged(int state) {
        switch (state) {
            case BluetoothAdapter.STATE_TURNING_ON:
            case BluetoothAdapter.STATE_TURNING_OFF:
                bluetoothSwitch.setEnabled(false);
                break;
            case BluetoothAdapter.STATE_ON:
                bluetoothSwitch.setEnabled(true);
                bluetoothSwitch.setChecked(true);
                break;
            case BluetoothAdapter.STATE_OFF:
                bluetoothSwitch.setEnabled(true);
                bluetoothSwitch.setChecked(false);
                break;
            default:
                bluetoothSwitch.setEnabled(true);
                bluetoothSwitch.setChecked(false);
        }
    }

    private void initView() {
        bluetoothSwitch = binding.checkbox;
        bluetoothSwitch.setChecked(getBluetoothAdapter().isEnabled());
        bluetoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                if (check) {
                    getBluetoothAdapter().enable();
                } else {
                    getBluetoothAdapter().disable();
                }
            }
        });
    }

    private BluetoothAdapter getBluetoothAdapter() {
        return BluetoothAdapter.getDefaultAdapter();
    }
}