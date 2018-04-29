package com.example.dennis.camerausekarnaseekhofraaandz;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity {

private static final int REQUEST_IMAGE_CAPTURE = 1;

@BindView(R.id.imageView)
ImageView imageView;

@BindView(R.id.clickPicture)
Button btn;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
}

@OnClick(R.id.openCamera)
public void launchCameraActivity() {
    if (isCameraAvaialble())
        startActivity(new Intent(this, CameraActivity.class));
    else
        Toast.makeText(this, "The device does not have a camera in it", Toast.LENGTH_SHORT).show();
}

/**
 * Check if device has a camera
 */
private boolean isCameraAvaialble() {
    if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
        return true;
    return false;
}

@OnClick(R.id.clickPicture)
public void clickPictureAndDisplayItInImageView() {
    /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);*/

    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    //Ensure that there's a camera activity that can handle the intent
    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
        //Create the file where photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        if (photoFile != null) {
            /*Uri photoUri = FileProvider.getUriForFile(this,
                    getPackageName() + ".fileprovider",
                    photoFile);*/
            String authorities = getPackageName() + ".fileprovider";
//            String authorities = BuildConfig.APPLICATION_ID+".fileprovider";
            Uri photoUri = FileProvider.getUriForFile(this,
                    authorities,
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }


    }
}

/**
 * Getting the thumbnail
 */
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    //Don't compare data to null as it will always come as null bcz we are providing file URI, so load with the imageFilePath
    //which we have already obtained before opening the camera intent
    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//        Bundle extras = data.getExtras();
//        Bitmap imageThumbnail = (Bitmap) extras.get("data");
//        imageView.setImageBitmap(imageThumbnail);

        File file = new File(mCurrentPhotoPath);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);
        bitmap = Bitmap.createScaledBitmap(bitmap, imageView.getWidth(), imageView.getHeight(), true);
        imageView.setImageBitmap(bitmap);


    }
}

/**
 * use {@link Environment#getExternalStoragePublicDirectory(String)} with
 * {@link Environment#DIRECTORY_PICTURES} argument if you want to share this picture among all the other apps.
 * Otherwise use {@link #getExternalFilesDir(String)}
 */
String mCurrentPhotoPath;

private File createImageFile() throws IOException {
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    String imageFileName = "JPEG_" + timeStamp + "_";

//    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//    File storageDir = new File("/storage/emulated/0/Android/data/com.example.dennis.camerausekarnaseekhofraaandz/lib");
    File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    if (!storageDir.exists()) {
        storageDir.mkdirs();
    }
    File tempImage = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg",  /* suffix */
            storageDir    /* directory */
    );

    // Save a file: path for use with ACTION_VIEW intents
    mCurrentPhotoPath = tempImage.getAbsolutePath();
    Log.e("PATH", mCurrentPhotoPath);
    return tempImage;
}

/**
 * Note, this will not work if you have used
 * {@link #getExternalFilesDir(String)}
 */
private void addPhotoToGallery() {
    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
    File f = new File(mCurrentPhotoPath);
    Uri contentUri = Uri.fromFile(f);
    mediaScanIntent.setData(contentUri);
    this.sendBroadcast(mediaScanIntent);
}

}
