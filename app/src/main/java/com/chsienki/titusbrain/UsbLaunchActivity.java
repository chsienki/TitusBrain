package com.chsienki.titusbrain;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class UsbLaunchActivity extends Activity {

    @Override
    public void onResume() {
        super.onResume();

       // android.os.Debug.waitForDebugger();
        Log.i("Launcher", "Starting bg service");
        // make sure the background service is running
        Intent service = new Intent(this, BackgroundService.class);
        startService(service);

        // hmmmmm
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        // we're done here
        finish();
    }
}
