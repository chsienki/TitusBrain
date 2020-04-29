package com.chsienki.titusbrain;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StartServiceIfPermissionGranted();
    }

    private void StartServiceIfPermissionGranted() {
        if (Settings.canDrawOverlays(this)) {
            Intent service = new Intent(this, BackgroundService.class);
            startService(service);
        }
    }
}
