package com.atta.mobilecomputingassingment1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.Arrays;

public class CameraUtils {

    public int getHeartRate() {
        return heartRate;
    }

    private int heartRate=0;


    private static final String TAG = CameraUtils.class.getName();

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    protected static final int BackCamera = CameraCharacteristics.LENS_FACING_BACK;

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    int TIME_HERAT_MEASURE=45;

    private String cameraId;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSessions;

    protected CaptureRequest.Builder captureRequestBuilder;
    private Size camImageDementions;
    private ImageReader imageReader;
    private File file;
    private static final int REQUEST_CAMERA_PERMISSION_CODE = 8080;
    private boolean mFlashSupported;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    private TextureView textureView;
    Context context;
    public static int countSingleton=0;

    public static enum RED {
        HIGHRED, LOWRED
    }
    TextView tViewHeart;
    static CameraUtils cameraUtils;

   public static CameraUtils getCameraUtils(){
       return cameraUtils;
   }
    public CameraUtils(Context context, TextureView textureView, HandlerThread mBackgroundThread, Handler mBackgroundHandler, TextView tViewHeart){
        countSingleton++;
        if(countSingleton>1){


            throw new IllegalAccessError();
        }
        cameraUtils=this;

        this.context=context;
        this.textureView=textureView;
        this.mBackgroundHandler=mBackgroundHandler;
        this.mBackgroundThread=mBackgroundThread;
        this.tViewHeart=tViewHeart;
    }

    private static RED currentRED = RED.HIGHRED;

    public static RED getCurrentRED() {
        return currentRED;
    }

    int redAvgArraySize = 5;
    private final int[] redAvgArray = new int[redAvgArraySize];
    int redAvgArrayIndex = 0;
    int beats = 0;

    private static long startTime = -1;

    private ImageReader.OnImageAvailableListener onImageAvailableListener=getOnImageAvailableListener();
    final CameraDevice.StateCallback CameraCallback=getCameraCallback();















    private  CameraDevice.StateCallback getCameraCallback(){
     final CameraDevice.StateCallback CameraCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            //This is called when the camera is open
            Log.e(TAG, "onOpened");
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            cameraDevice.close();
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };
    return CameraCallback;}


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



    protected void createCameraPreview() {


        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
            Log.e("******************",""+camImageDementions);
            texture.setDefaultBufferSize(camImageDementions.getWidth(), camImageDementions.getHeight());
            Surface surface = new Surface(texture);

            //create capture req
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_TORCH);
            captureRequestBuilder.addTarget(surface);
            captureRequestBuilder.addTarget(imageReader.getSurface());


            cameraDevice.createCaptureSession(Arrays.asList(surface, imageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {

                    if (null == cameraDevice) {
                        return;
                    }

                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();

                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(context, "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }


    }

    public String getCam(CameraManager manager) {
        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                int cOrientation = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (cOrientation == BackCamera) {
                    return cameraId;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private CaptureRequest createCaptureReq() {
        try {
            CaptureRequest.Builder builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            builder.addTarget(imageReader.getSurface());
            return builder.build();
        } catch (CameraAccessException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }
    int width;
    int height;
    public void openCamera() {
        heartRate=0;
        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        Log.e(TAG, "is camera open");
        try {
            cameraId = getCam(manager);
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            camImageDementions = map.getOutputSizes(SurfaceTexture.class)[0];


            Size[] jpegSizes = null;
            if (characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.YUV_420_888);
            }
            width = 160;
            height = 120;
            // if (jpegSizes != null && 0 < jpegSizes.length) {
            //   width = jpegSizes[0].getWidth();
            //  height = jpegSizes[0].getHeight();
            //}
            imageReader = ImageReader.newInstance(width, height, ImageFormat.YUV_420_888, 20); //fps * 10 min
            imageReader.setOnImageAvailableListener(onImageAvailableListener, null);
            manager.openCamera(cameraId, CameraCallback, null);


            Log.e(TAG, " Camera is open now :" + cameraId);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    protected void updatePreview() {


        if (null == cameraDevice) {
            Log.e(TAG, "updatePreview error, return");
            return;
        }
        // captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        // captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

        try {

            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, null);
        } catch (CameraAccessException e) {
            Log.e(TAG, "Failed to start camera preview because it couldn't access camera", e);
        } catch (IllegalStateException e) {
            Log.e(TAG, "Failed to start camera preview.", e);
        }


    }

    void closeCamera() {
        cameraUtils=null;
        countSingleton--;
        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (null != imageReader) {
            imageReader.close();
            imageReader = null;
        }
    }




     private  ImageReader.OnImageAvailableListener getOnImageAvailableListener(){

    ImageReader.OnImageAvailableListener onImageAvailableListener    = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            Image img = reader.acquireLatestImage();
            if (img != null) {
                if(startTime==-1)
                    startTime=System.currentTimeMillis();


                Bitmap bp = Utils.getBitmapFromBytes(Utils.convertYUV420888ToNV21(img),width,height);
                // MainActivity.this.imageView.setImageBitmap(bp);

                reader.discardFreeBuffers();
                // bp=Utils.rotateBitmap(bp);
              //  Log.i("&&&&&&&&&", Integer.toString(bp.getHeight()));
                // createCameraPreview();
                int redAvg= Utils.getRedAverage(bp,width,height);


                int avgRedArray = 0;
                int avgRedcount = 0;
                for (int i = 0; i < redAvgArray.length; i++) {
                    if (redAvgArray[i] > 0) {
                        avgRedArray += redAvgArray[i];
                        avgRedcount++;
                    }
                }

                int allAverage = (avgRedcount > 0) ? (avgRedArray / avgRedcount) : 0;
                RED newRED = currentRED;
                if (redAvg < allAverage) {
                    newRED = RED.HIGHRED;
                    if (newRED != currentRED) {
                        beats++;
                        // Log.d(TAG, "BEAT!! beats="+beats);
                    }
                } else if (redAvg > allAverage) {
                    newRED = RED.LOWRED;

                }

                if(newRED!=currentRED)
                    currentRED=newRED;

                if (redAvgArrayIndex == redAvgArraySize) redAvgArrayIndex = 0;
                redAvgArray[redAvgArrayIndex] = redAvg;
                redAvgArrayIndex++;

                // Transitioned from one state to another to the same
                long endTime = System.currentTimeMillis();
                double totalTimeInSecs = (endTime - startTime) / 1000d;
                if (totalTimeInSecs >= TIME_HERAT_MEASURE){
                    double bps = (beats / totalTimeInSecs);
                    int dpm = (int) (bps * 60d);
                    startTime = -1;//System.currentTimeMillis();
                    beats = 0;
                    if (dpm >30 && dpm < 1000) {

                        Log.e("&lkl&&&",Integer.toString(dpm));
                        startTime = System.currentTimeMillis();
                        ((MainActivity)context).setHeratRate(dpm);
                        tViewHeart.setText(Integer.toString(dpm));
                        beats = 0;

                        closeCamera();
                    }}


                img.close();
                //  reader.close();
            }
        }
    };
    return onImageAvailableListener;}


}
