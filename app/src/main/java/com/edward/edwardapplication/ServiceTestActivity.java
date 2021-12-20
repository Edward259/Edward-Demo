package com.edward.edwardapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.edward.edwardapplication.databinding.ActivityServiceTestBinding;
import com.edward.edwardapplication.service.ReaderInfoServiceHelper;
import com.onyx.android.sdk.OnyxSdk;
import com.onyx.android.sdk.padmu.common.ContentException;
import com.onyx.android.sdk.padmu.model.FirmwareInfo;
import com.onyx.android.sdk.padmu.service.Callback;

public class ServiceTestActivity extends AppCompatActivity {

    private ActivityServiceTestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_service_test);
        initView();
    }

    private void initView() {
        binding.cloudFirmwareCheck.setOnClickListener(view -> {
            OnyxSdk.cloudFirmwareCheck(new Callback<FirmwareInfo>() {
                @Override
                public void onCompleted(FirmwareInfo firmware) {
                    if (firmware.isHasNewFirmware()) {
                        Log.e("edward", "onNext: " + firmware.getChangeLog() + firmware.getUrl());
                    }

                }

                @Override
                public void onError(ContentException exception) {
                    Log.e("edward", "cloudFirmwareCheck onError: " + exception.getMessage());

                }
            });
        });
        binding.fetchVendorId.setOnClickListener(view -> {
            String vendorId = ReaderInfoServiceHelper.getInstance().test11();
            Toast.makeText(ServiceTestActivity.this, vendorId, Toast.LENGTH_LONG).show();
        });
        binding.supportPdfAutoDecrypt.setOnClickListener(view -> {
            boolean support = ReaderInfoServiceHelper.getInstance().supportTdhDecryption();
            Toast.makeText(ServiceTestActivity.this, "Support Pdf auto decrypt: " + support, Toast.LENGTH_LONG).show();
        });
        binding.test.setOnClickListener(view -> {
            String test = ReaderInfoServiceHelper.getInstance().test();
            Toast.makeText(ServiceTestActivity.this, test, Toast.LENGTH_LONG).show();
        });
    }
}