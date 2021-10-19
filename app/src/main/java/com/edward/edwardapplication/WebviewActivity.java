package com.edward.edwardapplication;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.onyx.android.sdk.api.device.epd.EpdController;

public class WebviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        WebView webView = findViewById(R.id.webview);
        Switch aSwitch = findViewById(R.id.bt);
        webView.loadUrl("https://www.jianshu.com/p/41b98118decc");
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                EpdController.setWebViewContrastOptimize(webView, b);
                aSwitch.setText((b ? "打开" : "关闭") + "优化");
            }
        });
    }
}