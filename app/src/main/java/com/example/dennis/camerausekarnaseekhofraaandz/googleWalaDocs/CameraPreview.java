package com.example.dennis.camerausekarnaseekhofraaandz.googleWalaDocs;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dennis.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, Camera.AutoFocusCallback {

private final String TAG = getClass().getSimpleName();
private CameraKaHiInterface cameraKaHiInterface;
private SurfaceHolder mHolder;
private Camera mCamera;
public static boolean metringAreaSupported = false;
Context mContext;
private DrawingView drawingView;
private boolean drawingViewSet;


public CameraPreview(Context context, Camera camera, CameraKaHiInterface cameraKaHiInterface, DrawingView drawingView) {
    super(context);
    mContext = context;
    mCamera = camera;
    setDrawingView(drawingView);
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

    Camera.Parameters parameters = mCamera.getParameters();
    if (parameters.getMaxNumMeteringAreas() > 0) {
        metringAreaSupported = true;
    }


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


public void touched(MotionEvent event) {

    if (event.getAction() == MotionEvent.ACTION_DOWN) {
        float x = event.getX();
        float y = event.getY();

        Rect touchRect = new Rect(
                (int) (x - 100),
                (int) (y - 100),
                (int) (x + 100),
                (int) (y + 100));


        final Rect targetFocusRect = new Rect(
                touchRect.left * 2000 / this.getWidth() - 1000,
                touchRect.top * 2000 / this.getHeight() - 1000,
                touchRect.right * 2000 / this.getWidth() - 1000,
                touchRect.bottom * 2000 / this.getHeight() - 1000);

        doTouchFocus(targetFocusRect);
        if (drawingViewSet) {
            drawingView.setHaveTouch(true, touchRect);
            drawingView.invalidate();

            // Remove the square indicator after 1000 msec
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    drawingView.setHaveTouch(false, new Rect(0, 0, 0, 0));
                    drawingView.invalidate();
                }
            }, 1000);
        }

    }


}

private void doTouchFocus(Rect tfocusRect) {
    try {
        List<Camera.Area> focusList = new ArrayList<Camera.Area>();
        Camera.Area focusArea = new Camera.Area(tfocusRect, 1000);
        focusList.add(focusArea);

        Camera.Parameters param = mCamera.getParameters();
        param.setFocusAreas(focusList);
        param.setMeteringAreas(focusList);
        mCamera.setParameters(param);

        mCamera.autoFocus(this);
    } catch (Exception e) {
        e.printStackTrace();
        Log.i(TAG, "Unable to autofocus");
    }
}

public void setDrawingView(DrawingView dView) {
    drawingView = dView;
    drawingViewSet = true;
}

@Override
public void onAutoFocus(boolean success, Camera camera) {
    if (success)
        mCamera.cancelAutoFocus();
}


/*private void focusOnTouch(MotionEvent event) {
    if (mCamera != null) {
        mCamera.cancelAutoFocus();

        Rect focusRect = calculateTapArea(event.getX(), event.getY(), 1f);
        Rect meteringRect = calculateTapArea(event.getX(), event.getY(), 1.5f);

        Camera.Parameters params = mCamera.getParameters();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        params.setFocusAreas(Lists.newArrayList(new Camera.Area(focusRect, 1000)));
        if (CameraPreview.metringAreaSupported)
            params.setMeteringAreas(Lists.newArrayList(new Camera.Area(focusRect, 1000)));
        mCamera.setParameters(params);
        mCamera.autoFocus(this);
    }
}*/

/**
 * Convert touch position x:y to {@link Camera.Area} position -1000:-1000 to 1000:1000.
 */
/*private Rect calculateTapArea(float x, float y, float coefficient) {
    int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();

    int left = clamp((int) x - areaSize / 2, 0, getSurfaceView().getWidth() - areaSize);
    int top = clamp((int) y - areaSize / 2, 0, getSurfaceView().getHeight() - areaSize);

    RectF rectF = new RectF(left, top, left + areaSize, top + areaSize);
    matrix.mapRect(rectF);

    return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
}

private int clamp(int x, int min, int max) {
    if (x > max) {
        return max;
    }
    if (x < min) {
        return min;
    }
    return x;
}*/
}
