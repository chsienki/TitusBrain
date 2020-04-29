package com.chsienki.titusbrain;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class ConnectionManager implements ICameraNotificationReceiver {

    private final IConnectionEventReceiver eventReceiver;

    private final Context context;

    private final CameraController cameraController;

    private final OverlayController overlayController;

    private final SharedPreferences preferences;

    ConnectionManager(IConnectionEventReceiver eventReceiver, Context context){
        this.eventReceiver = eventReceiver;
        this.context = context;

        //create an overlay controller, which we'll use to render frames
        overlayController = new OverlayController(context);

        // next create a camera controller and see if we can get a camera out of it
        cameraController  = new CameraController(context, this);

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private void close() {
        //TODO: any other clean up work we need here?
        overlayController.Hide();
        if(this.eventReceiver != null){
            this.eventReceiver.OnConnectionClosed();
        }

    }

    @Override
    public void OnFrameAvailable(CameraFrame frame) {
        this.overlayController.DisplayFrame(frame);
    }

    @Override
    public void OnNoMoreFrames() {
        this.overlayController.Hide();
    }

    @Override
    public void OnDisconnected() {
        close();
    }

    @Override
    public void OnStarted(){

        if(preferences == null){
            return;
        }

        if(preferences.getBoolean("startAndroidAuto", false)){
            Intent androidAuto = context.getPackageManager().getLaunchIntentForPackage("com.google.android.projection.gearhead");
            context.startActivity(androidAuto);
        }
    }
}
