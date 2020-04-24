package com.chsienki.titusbrain;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.chsienki.titusbrain.databinding.OverlayBinding;

public class BackgroundService extends Service implements IConnectionEventReceiver {

    ConnectionManager manager = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // TODO: should this be in on create?
        //       we could have a 'tryStart' or something on the mgr. That way
        //       we'll always only have one instance?

        // TODO: should we run this on another thread?
        if (manager == null){
            Log.i("BGService", "Setting up new connection manager");
            manager = new ConnectionManager(this, this);
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        manager = null; //TODO: do we need to 'dispose' this instance?
        Log.i("BGService", "Background service stopped");
    }

    @Override
    public void OnConnectionClosed() {
        Log.d("BGService", "Connection closed. Stopping service.");
        stopSelf();
    }
}