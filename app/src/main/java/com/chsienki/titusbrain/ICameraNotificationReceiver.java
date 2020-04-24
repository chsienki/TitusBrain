package com.chsienki.titusbrain;

public interface ICameraNotificationReceiver {

    void OnFrameAvailable(CameraFrame frame);
    void OnNoMoreFrames();
    void OnDisconnected();
}
