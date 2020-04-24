package com.chsienki.titusbrain;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class UsbBroadcastReceiver extends BroadcastReceiver {

    //TODO: this isn't used, right?

    private static final String TAG = "UsbBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        StringBuilder sb = new StringBuilder();
        sb.append("Action: " + intent.getAction() + "\n");
        sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
        String log = sb.toString();
        Log.d(TAG, log);
    }
}
