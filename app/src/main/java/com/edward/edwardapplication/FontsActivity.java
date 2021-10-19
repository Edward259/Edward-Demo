package com.edward.edwardapplication;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.edward.edwardapplication.databinding.ActivityFontsBinding;

import java.io.IOException;

public class FontsActivity extends AppCompatActivity {

    private ActivityFontsBinding binding;
    private String[] fonts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fonts);
        initData();
        initView();
        Log.e("edward", " FontsActivity onCreate: "  );
    }

    private void initView() {
        if (fonts.length <= 0) {
            Log.e("edward", "initView: no fonts in assets");
            return;
        }
        Button system = new Button(this);
        system.setText("system");
        system.setOnClickListener(view -> binding.text.setTypeface(Typeface.DEFAULT));
        binding.container.addView(system);

        for (String font : fonts) {
            Button button = new Button(this);
            button.setText(font.substring(0, font.lastIndexOf(".")));
            button.setOnClickListener(view -> binding.text.setTypeface(getFromAsset("fonts/" + font)));
            binding.container.addView(button);
        }
    }

    private void initData() {
        fonts = getFonts();
    }

    private String[] getFonts() {
        String[] fonts = new String[]{};
        try {
            fonts = getResources().getAssets().list("fonts");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fonts;
    }

    private Typeface getFromAsset(String s) {
        Typeface fromAsset = null;
        try {
            fromAsset = Typeface.createFromAsset(getAssets(), s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fromAsset == null) {
            return Typeface.DEFAULT;
        }
        return fromAsset;
    }
}