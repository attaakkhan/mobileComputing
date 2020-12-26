package com.atta.mobilecomputingassingment1;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.PowerManager;
import android.util.SparseIntArray;
import android.view.TextureView;
import android.view.View;

import android.widget.Button;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;

import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button takePictureButton;
    private TextureView textureView;
    private TextView tViewHeart;
    private Button measureRespButton;
    private TextView tViewResp;



    private static final int REQUEST_CAMERA_PERMISSION_CODE = 8080;
    private boolean mFlashSupported;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    TextureView.SurfaceTextureListener surfaceTextureListener=Utils.getSurfaceTextureListener();
    CameraUtils cameraUtils=null;
    SensorAcc sensorAcc;
    int heratRate=0;

    public int getHeratRate() {
        return heratRate;
    }

    public void setHeratRate(int heratRate) {
        this.heratRate = heratRate;
    }

    public int getRespiratoryRate() {
        return RespiratoryRate;
    }

    public void setRespiratoryRate(int respiratoryRate) {
        RespiratoryRate = respiratoryRate;
    }
    int REQUEST_PERMISSIONS_REQUEST_CODE=34;
    int RespiratoryRate=0;


//    private void requestPermissions() {
//        boolean shouldProvideRationale =
//                ActivityCompat.shouldShowRequestPermissionRationale(this,
//                        Manifest.permission.ACCESS_FINE_LOCATION);
//
//        // Provide an additional rationale to the user. This would happen if the user denied the
//        // request previously, but didn't check the "Don't ask again" checkbox.
//        if (shouldProvideRationale) {
//            Log.i(TAG, "Displaying permission rationale to provide additional context.");
//        } else {
//            Log.i(TAG, "Requesting permission");
//            // Request permission. It's possible this can be auto answered if device policy
//            // sets the permission in a given state or the user denied the permission
//            // previously and checked "Never ask again".
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION},
//                    REQUEST_PERMISSIONS_REQUEST_CODE);
//        }
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//        requestPermissions();
//        }

//        File dbFile = getDatabasePath("khan");
//        Log.i("AAAAA",dbFile.getAbsolutePath());
        //takePicture();
        //   return;
        /*textureView = (TextureView) findViewById(R.id.texture);
        tViewHeart = (TextView) findViewById(R.id.textView_heart_rate);
        tViewResp = (TextView) findViewById(R.id.textView_resp_rate);
        assert textureView != null;
        textureView.setSurfaceTextureListener(surfaceTextureListener);
        takePictureButton = (Button) findViewById(R.id.btn_takepicture);
        measureRespButton = (Button) findViewById(R.id.btn_resp);
        assert takePictureButton != null;
        cameraUtils=new CameraUtils(this,textureView,mBackgroundThread,mBackgroundHandler,tViewHeart);*/





        /*sensorAcc=new SensorAcc(this,tViewResp);

        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION_CODE);
                    return;
                }

                cameraUtils.openCamera();

            }
        });


        measureRespButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                sensorAcc.register();

            }
        });
*/





    }






    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());

    }

    protected void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        ;
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    PowerManager.WakeLock wakeLock;









    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(MainActivity.this, "Permissions Denied", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG,"Permission granted&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");

            } else {
                // Permission denied.
//             /   setButtonsState(false);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //    wakeLock.acquire();
        Log.e(TAG, "onResume");
        startBackgroundThread();


//        if (textureView.isAvailable()) {
//            cameraUtils.openCamera();
//        } else {
//            textureView.setSurfaceTextureListener(surfaceTextureListener);
//        }
    }

    @Override
    protected void onPause() {
//        sensorAcc.unregester();

        Log.e(TAG, "onPause");
      //  CameraUtils.getCameraUtils().closeCamera();
        stopBackgroundThread();
        super.onPause();
    }






}