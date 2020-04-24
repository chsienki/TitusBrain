package com.chsienki.titusbrain;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.arksine.libusbtv.DeviceParams;
import com.arksine.libusbtv.IUsbTvDriver;
import com.arksine.libusbtv.UsbTv;
import com.arksine.libusbtv.UsbTvFrame;

import java.util.ArrayList;

public class CameraController implements UsbTv.DriverCallbacks, UsbTv.onFrameReceivedListener {

    private static final String TAG = "CameraController";

    private final Context context;

    private final ICameraNotificationReceiver receiver;

    private final Object CAM_LOCK = new Object();

    private IUsbTvDriver tvDriver = null;

    private int framesToNoMore = 0;

    private static int detectionThreshold = 5;

    public CameraController(Context context, ICameraNotificationReceiver receiver){

        this.context = context;
        this.receiver = receiver;

        StartCamera();
    }

    private void StartCamera() {

        if (tvDriver != null && tvDriver.isOpen()) {
            if (tvDriver.isStreaming()) {
                Log.i(TAG, "Device Already Open and Streaming");
            } else {
                Log.i(TAG, "Device Already Open, not Streaming");
            }
            return;
        }

        ArrayList<UsbDevice> devList = UsbTv.enumerateUsbtvDevices(context);
        UsbDevice device = null;
        if (!devList.isEmpty()) {
            device = devList.get(0); // assume it's device zero as we have the filter
        }

        if (device == null) {
            Log.i(TAG, "Device not found");
            NotifyClosed();
            return;
        }

        // set up the parameters to open with
        DeviceParams usbTvParams = new DeviceParams.Builder()
                .setUsbDevice(device)
                .setDriverCallbacks(this)
                .setInput(UsbTv.InputSelection.COMPOSITE)
                .setScanType(UsbTv.ScanType.INTERLEAVED)
                .setTvNorm(UsbTv.TvNorm.NTSC)
                .build();

        // figure out permissions?
        UsbTv.registerUsbReceiver(context);

        // open the device (will call onOpen when done)
        UsbTv.open(context, usbTvParams);
    }

    @Override
    public void onOpen(IUsbTvDriver driver, boolean status) {
        Log.i(TAG, "Device opened.");
        Log.i(TAG, String.format("UsbTv Open Status: %b", status));
        synchronized (CAM_LOCK) {
            tvDriver = driver;
            if (tvDriver != null) {

                // request frame notifications
                tvDriver.setOnFrameReceivedListener(this);

                // start streaming
                if (!tvDriver.isStreaming()) {
                    tvDriver.startStreaming();
                }
            }
        }
    }

    @Override
    public void onClose() {
        Log.i(TAG, "Device closed.");
        NotifyClosed();
    }

    @Override
    public void onError() {
        Log.i(TAG, "Device error.");
        NotifyClosed();
    }

    private void NotifyClosed(){
        // tv driver handles already being closed etc. and does the right thing
        if (tvDriver != null){
           tvDriver.close();
           tvDriver = null;
        }
        UsbTv.unregisterUsbReceiver(context);
        receiver.OnDisconnected();
    }

    @Override
    public void onFrameReceived(UsbTvFrame frame) {

        // If we detect a backup camera notify the receiver
        if (DetectedBackupCamera(frame)) {
            //TODO: is the perf hit of copying going to kill the app?
            final CameraFrame cf = new CameraFrame(frame.copyOfFrame());
            receiver.OnFrameAvailable(cf);

            framesToNoMore = detectionThreshold;
        } else if(framesToNoMore > 0 && --framesToNoMore == 0){
            // if we haven't detected for 5 consecutive frames, issue no more frames
            receiver.OnNoMoreFrames();
        }

        frame.returnFrame();
    }

    private boolean DetectedBackupCamera(UsbTvFrame frame) {

        // this is the 'magic' of looking for the backup camera.
        // we know that the camera adds an overlay in a static position
        // so we can just sample a pixel in that position and see if it matches.

        // Pixel (128, 404) is part of the red overlay. We can just check it's V
        // component, and that seems to give us a pretty accurate hit rate.

        // Driver gives back a stream of YUV bytes in 4:2:2 format
        // That is every four bytes is (y1, u, y2, v) where y1 and y2 are the
        // luminance for pixel 1 and 2, and the u and v are shared between them.
        // each pixel takes up 2 bytes, but you have to read 4 which gets the value of both

        // 582019 is calculated thus =>
        //      pixel (128, 404) =>
        //      720   *      2      * 404  +      2      * 128  + 3
        //      width * bytes/pixel *  Y   + bytes/pixel *  X   + 4th component of (y1, u, y2, v)
        //
        //                                                                                 (0,  1,  2, 3)
        byte v = frame.getFrameBuf().get(582019);

        // When connected, V will be equal to 254 (-2 in signed)
        // which is due to it being ~total red
        //return v == -2;
        return true;
    }
}
