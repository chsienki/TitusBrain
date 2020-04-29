package com.chsienki.titusbrain;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class UsbLaunchActivity extends Activity {

    @Override
    public void onResume() {
        super.onResume();

        Log.i("Launcher", "Starting bg service");

        // make sure the background service is running
        Intent service = new Intent(this, BackgroundService.class);
        startService(service);

        // we're done here
        finish();
    }
}
