package com.example.dennis.camerausekarnaseekhofraaandz.googleWalaDocs;

import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.dennis.camerausekarnaseekhofraaandz.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoogleKiApp extends AppCompatActivity implements CameraKaHiInterface {
@BindView(R.id.frameLayout)
FrameLayout frameLayout;

@BindView(R.id.drawingView)
DrawingView drawingView;

Camera mCamera;
CameraPreview mPreview;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_google_ki_app);
    ButterKnife.bind(this);
    mCamera = getCameraInstance();


    if (mCamera != null) {
        mPreview = new CameraPreview(this, mCamera, this, drawingView);
        Camera.Parameters cameraParam = mCamera.getParameters();
        cameraParam.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
        mCamera.setParameters(cameraParam);
        setCameraDisplayOrientation(mCamera);
        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mPreview.touched(event);
                return false;
            }
        });


        frameLayout.addView(mPreview);

    }
}

public static Camera getCameraInstance() {
    Camera c = null;

    try {
        c = Camera.open();  //Attempt to get camera instance
    } catch (Exception e) {
        //Camera is not avaialbe (in use or does not exist)
        e.printStackTrace();
    }
    return c;
}

@Override
public void releaseTheCamera() {
    Toast.makeText(this, "Releasing the camera", Toast.LENGTH_SHORT).show();
    if (mCamera != null)
        mCamera.release();
    mCamera = null;
}

public void setCameraDisplayOrientation(Camera camera) {
    Camera.CameraInfo info = new Camera.CameraInfo();
    Camera.getCameraInfo(0, info);
    ;
    int rotation = getWindowManager().getDefaultDisplay().getRotation();

    int degrees = 0;

    switch (rotation) {
        case Surface.ROTATION_0:
            degrees = 0;
            break;
        case Surface.ROTATION_90:
            degrees = 90;
            break;
        case Surface.ROTATION_180:
            degrees = 180;
            break;
        case Surface.ROTATION_270:
            degrees = 270;
            break;
    }

    int result;
    if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
        result = (info.orientation + degrees) % 360;
    } else {
        result = (info.orientation - degrees + 360) % 360;
    }
    camera.setDisplayOrientation(result);
}


}
