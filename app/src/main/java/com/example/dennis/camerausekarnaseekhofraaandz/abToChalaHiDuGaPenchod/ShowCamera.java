package com.example.dennis.camerausekarnaseekhofraaandz.abToChalaHiDuGaPenchod;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

/**
 * Created by Dennis.
 */
public class ShowCamera extends SurfaceView implements SurfaceHolder.Callback {

Camera camera;
SurfaceHolder holder;

public ShowCamera(Context context, Camera camera) {
    super(context);
    this.camera = camera;
    holder = getHolder();
    holder.addCallback(this);

}

//region SurfaceHolder.Callback
@Override
public void surfaceCreated(SurfaceHolder holder) {
    //Setting up some mCamera Parameters

    Camera.Parameters params = camera.getParameters();

    //Setting up mCamera size
//    List<Camera.Size> supportedPictureSizes = params.getSupportedPictureSizes();
//    Camera.Size cameraSize = null;
//
//    for (Camera.Size size : supportedPictureSizes) {
//        cameraSize = size;
//    }

    //Change the orientation of the mCamera..
    if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
        params.set("orientation", "portrait");
        params.setRotation(90);
        camera.setDisplayOrientation(90);
    } else {
        params.set("orientation", "landscape");
        params.setRotation(0);
        camera.setDisplayOrientation(0);
    }
//    if (cameraSize != null)
//        params.setPictureSize(cameraSize.width, cameraSize.height);
    camera.setParameters(params);
    try {
        camera.setPreviewDisplay(holder);
    } catch (IOException e) {
        e.printStackTrace();
    }
    camera.startPreview();
}

@Override
public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    camera.stopPreview();
    camera.release();
}

@Override
public void surfaceDestroyed(SurfaceHolder holder) {

}
//endregion
}
