package com.atta.mobilecomputingassingment1;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class FirstFragment extends Fragment {
    private static final String TAG = "MainActivity";
    private Button takePictureButton;
    private TextureView textureView;
    private TextView tViewHeart;
    private Button measureRespButton;
    private TextView tViewResp;


    int heartRate=0;
    int respiratoryRate=0;
    private static final int REQUEST_CAMERA_PERMISSION_CODE = 8080;
    private boolean mFlashSupported;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    TextureView.SurfaceTextureListener surfaceTextureListener=Utils.getSurfaceTextureListener();
    CameraUtils cameraUtils=null;
    SensorAcc sensorAcc;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        Context context=getContext();



        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&ActivityCompat.checkSelfPermission(getActivity() , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE}, REQUEST_CAMERA_PERMISSION_CODE);

        }




            textureView = (TextureView) view.findViewById(R.id.texture);
            tViewHeart = (TextView) view.findViewById(R.id.textView_heart_rate);
            tViewResp = (TextView) view.findViewById(R.id.textView_resp_rate);
            assert textureView != null;
            textureView.setSurfaceTextureListener(surfaceTextureListener);
            takePictureButton = (Button) view.findViewById(R.id.btn_takepicture);
            measureRespButton = (Button) view.findViewById(R.id.btn_resp);
            assert takePictureButton != null;
            try {
                cameraUtils = new CameraUtils(getActivity(), textureView, mBackgroundThread, mBackgroundHandler, tViewHeart);
            }
            catch (IllegalAccessError e){
                cameraUtils=CameraUtils.getCameraUtils();
            }

            sensorAcc = new SensorAcc(getActivity(), tViewResp);

            takePictureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AlertDialog.Builder(getActivity())
                            .setTitle("Heart Rate")
                            .setMessage("Place your finger on the Camera and Flash and press OK ")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                    cameraUtils.openCamera();


                                    // Continue with delete operation
                                }
                            })


                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();





                }
            });


            measureRespButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AlertDialog.Builder(getActivity())
                            .setTitle("Respiratory Rate")
                            .setMessage("Place your Phone on the chest and press OK ")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    sensorAcc.register();


                                    // Continue with delete operation
                                }
                            })


                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();




                }
            });







        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle=new Bundle();
                bundle.putInt("heart",cameraUtils.getHeartRate());
                bundle.putInt("repiratory",sensorAcc.getRespiratoryRate());

                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        //    wakeLock.acquire();
        Log.e(TAG, "onResume");



        if (!textureView.isAvailable()) {
            surfaceTextureListener=Utils.getSurfaceTextureListener();
            textureView.setSurfaceTextureListener(surfaceTextureListener);
            //cameraUtils.openCamera();
            Log.e(TAG,"*************************2*****");}
//        } else {
//            textureView.setSurfaceTextureListener(surfaceTextureListener);
//        }
    }

    @Override
    public void onPause() {
//        sensorAcc.unregester();

        Log.e(TAG, "onPause");
        if(sensorAcc!=null && sensorAcc.getsensorManager()!=null){

        sensorAcc.unregester();
        }
        if(CameraUtils.getCameraUtils()!=null)
        CameraUtils.getCameraUtils().closeCamera();
      //  stopBackgroundThread();
        super.onPause();
    }
}