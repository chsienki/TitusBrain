package com.chsienki.titusbrain;

import com.arksine.libusbtv.UsbTvFrame;

import java.nio.ByteBuffer;

// Wrapper around a UsbTvFrame
public class CameraFrame {

    private final UsbTvFrame tvFrame;

    public CameraFrame(UsbTvFrame tvFrame){
        this.tvFrame = tvFrame;
    }

    public int GetWidth(){
        return tvFrame.getWidth();
    }

    public int GetHeight(){
        return tvFrame.getHeight();
    }

    public ByteBuffer GetBuffer(){
        return tvFrame.getFrameBuf();
    }

}
