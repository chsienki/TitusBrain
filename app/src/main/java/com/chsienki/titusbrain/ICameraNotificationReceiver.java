package com.chsienki.titusbrain;

public interface ICameraNotificationReceiver {

    void OnStarted();
    void OnFrameAvailable(CameraFrame frame);
    void OnNoMoreFrames();
    void OnDisconnected();
}
