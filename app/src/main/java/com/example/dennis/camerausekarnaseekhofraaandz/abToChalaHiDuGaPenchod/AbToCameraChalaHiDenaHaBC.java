package com.example.dennis.camerausekarnaseekhofraaandz.abToChalaHiDuGaPenchod;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.dennis.camerausekarnaseekhofraaandz.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AbToCameraChalaHiDenaHaBC extends AppCompatActivity {

private final String TAG = this.getClass().getSimpleName();
@BindView(R.id.frameLayout)
FrameLayout frameLayout;

Camera mCamera;
ShowCamera showCamera;

Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.e(TAG, "File does not exist");
            return;
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(pictureFile);
            fileOutputStream.write(data);
            fileOutputStream.close();

            camera.startPreview();      //??
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
};

private File getOutputMediaFile() {
    String state = Environment.getExternalStorageState();
    if (!state.equals(Environment.MEDIA_MOUNTED)) {
        Log.e(TAG, "Media not mounted");
        return null;
    }
    File folderGui = new File(Environment.getExternalStorageDirectory() + File.separator + "GUI");

    if (!folderGui.exists())
        folderGui.mkdirs();

    File outputFile = new File(folderGui, "temp.jpg");
    return outputFile;

}

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ab_to_camera_chala_hi_dena_ha_bc);
    ButterKnife.bind(this);
    showCamera = new ShowCamera(this, getCamera());
    frameLayout.addView(showCamera);
}

private Camera getCamera() {
    Camera c = null;
    if (hasCameraHardware()) {
        try {
            c = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Camera Unavailable", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
    return c;
}

private boolean hasCameraHardware() {
    return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
}

@OnClick(R.id.captureImage)
public void captureImage() {
    if (mCamera != null) {
        mCamera.takePicture(null, null, mPictureCallback);
    }
}
}
