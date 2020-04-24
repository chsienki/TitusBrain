package com.chsienki.titusbrain;

import android.content.Context;

public class ConnectionManager implements ICameraNotificationReceiver {

    private final IConnectionEventReceiver eventReceiver;

    private final Context context;

    private final CameraController cameraController;

    private final OverlayController overlayController;

    ConnectionManager(IConnectionEventReceiver eventReceiver, Context context){
        this.eventReceiver = eventReceiver;
        this.context = context;

        //create an overlay controller, which we'll use to render frames
        overlayController = new OverlayController(context);

        // next create a camera controller and see if we can get a camera out of it
        cameraController  = new CameraController(context, this);

        // TODO: we'll handle the bluetooth etc here, too
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
}
