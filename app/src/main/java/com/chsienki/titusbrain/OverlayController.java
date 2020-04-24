package com.chsienki.titusbrain;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.PixelFormat;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.chsienki.titusbrain.databinding.OverlayBinding;

public class OverlayController implements View.OnClickListener {

    private Context context;

    private OverlayBinding binding;

    private WindowManager windowManager;

    private Boolean isVisible = false;

    private  OGLRenderer renderer;

    private WindowManager.LayoutParams visible;

    private  WindowManager.LayoutParams hidden;

    private Handler uiHandler;

    public OverlayController(Context context){
        this.context = context;
        this.binding = OverlayBinding.inflate(LayoutInflater.from(context));
        this.binding.fab.setOnClickListener(this);
        this.windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.uiHandler = new Handler(Looper.getMainLooper());

        visible = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        hidden = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        hidden.gravity = visible.gravity = Gravity.LEFT | Gravity.BOTTOM;
    }

    private void ShowOverlay()
    {
        this.windowManager.addView(this.binding.getRoot(), visible);
        this.isVisible = true;
    }

    private void InitializerRenderer(int width, int height){
        if(this.renderer == null) {
            this.renderer = new OGLRenderer(context);
            this.binding.cameraView.getHolder().setFixedSize(width, height);
            this.binding.cameraView.setEGLContextClientVersion(2);
            this.binding.cameraView.setRenderer(this.renderer);
        }
    }

    public void DisplayFrame(final CameraFrame frame){
         uiHandler.post(new Runnable() {
             public void run() {
                 if (!isVisible) {
                     InitializerRenderer(frame.GetWidth(), frame.GetHeight());
                     ShowOverlay();
                 }
                 renderer.setFrame(frame);
             }
         });
    }

    public void Hide() {
        if(this.isVisible) {
            uiHandler.post(new Runnable() {
                public void run() {
                    windowManager.removeView(binding.getRoot());
                }
            });
        }
        this.isVisible = false;
    }

    @Override
    public void onClick(View v) {
        if(this.binding.cameraView.getVisibility() == View.VISIBLE){
            this.binding.cameraView.setVisibility(View.GONE);
            this.windowManager.updateViewLayout(this.binding.getRoot(), hidden);
            this.binding.fab.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_videocam_white_24dp, context.getTheme()));
        }else {
            this.binding.cameraView.setVisibility(View.VISIBLE);
            this.windowManager.updateViewLayout(this.binding.getRoot(), visible);
            this.binding.fab.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_videocam_off_white_24dp, context.getTheme()));
        }
    }
}
