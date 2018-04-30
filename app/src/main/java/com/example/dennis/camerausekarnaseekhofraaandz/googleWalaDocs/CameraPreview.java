package com.example.dennis.camerausekarnaseekhofraaandz.googleWalaDocs;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Created by Dennis.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

private final String TAG = getClass().getSimpleName();
private CameraKaHiInterface cameraKaHiInterface;
private SurfaceHolder mHolder;
private Camera mCamera;

public CameraPreview(Context context, Camera camera, CameraKaHiInterface cameraKaHiInterface) {
    super(context);
    mCamera = camera;
    this.cameraKaHiInterface = cameraKaHiInterface;

    //Install a SurfaceHolder.Callback so we get notified when the
    // underlying surface is created and destoryed

    mHolder = getHolder();      //method from SurfaceView.class
    mHolder.addCallback(this);

    //depricated setting, but required on Android versions prior to 3.0
    mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
}

@Override
public void surfaceCreated(SurfaceHolder holder) {
    // The Surface has been created, now tell the camera where to draw the preview
    try {
        mCamera.setPreviewDisplay(holder);
        mCamera.startPreview();
    } catch (IOException e) {
        Log.e(TAG, "Error setting camera preview " + e.getMessage());
        e.printStackTrace();
    }
}

@Override
public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    // If your preview can change or rotate, take care of those events here.
    //Make sure to STOP the preview before resizing or reformatting it.

    //If tou also want to set size for your camera preview, set it here.
    //set values taken from Camera.Parameters.getSupportedPreviewSizes()
    if (mHolder.getSurface() != null) {
        //preview surface does not exist
        return;
    }

    //stop the preview from making the changes
    try {
        mCamera.stopPreview();
    } catch (Exception e) {
        // ignore: tried to stop a non-existent preview
    }

    //set preview size and make any resize,rotate or
    //reformatting changes here
    try {
        mCamera.setPreviewDisplay(mHolder);
        mCamera.startPreview();
    } catch (Exception e) {
        Log.e(TAG, "Error starting camera preview " + e.getMessage());
    }
}

@Override
public void surfaceDestroyed(SurfaceHolder holder) {
    cameraKaHiInterface.releaseTheCamera();
}
}
