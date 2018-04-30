package com.example.dennis.camerausekarnaseekhofraaandz.googleWalaDocs;

import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.dennis.camerausekarnaseekhofraaandz.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoogleKiApp extends AppCompatActivity implements CameraKaHiInterface {
@BindView(R.id.frameLayout)
FrameLayout frameLayout;

Camera mCamera;
CameraPreview mPreview;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_google_ki_app);
    ButterKnife.bind(this);
    mCamera = getCameraInstance();

    if (mCamera != null) {
        mPreview = new CameraPreview(this, mCamera, this);
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
}
