package com.atta.mobilecomputingassingment1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.camera2.CameraDevice;
import android.location.Location;
import android.media.Image;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.TextureView;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.util.Date;

public class Utils {


   static String TAG=Utils.class.getName();



    public static final String KEY_REQUESTING_LOCATION_UPDATES = "requesting_locaction_updates";

    /**
     * Returns true if requesting location updates, otherwise returns false.
     *
     * @param context The {@link Context}.
     */
    public static boolean requestingLocationUpdates(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false);
    }




//    public static void scheduleJob(Context context) {
//        ComponentName serviceComponent = new ComponentName(context, TestJobService.class);
//        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
//        builder.setMinimumLatency(1 * 1000); // wait at least
//        builder.setOverrideDeadline(3 * 1000); // maximum delay
//        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
//        //builder.setRequiresDeviceIdle(true); // device should be idle
//        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
//        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
//        jobScheduler.schedule(builder.build());
//    }

    /**
     * Stores the location updates state in SharedPreferences.
     * @param requestingLocationUpdates The location updates state.
     */
    public static void setRequestingLocationUpdates(Context context, boolean requestingLocationUpdates) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
                .apply();
    }

    /**
     * Returns the {@code location} object as a human readable string.
     * @param location  The {@link Location}.
     */
    public static String getLocationText(Location location) {
        return location == null ? "Unknown location" :
                "(" + location.getLatitude() + ", " + location.getLongitude() + ")";
    }

//    public static String getLocationTitle(Context context) {
//        return context.getString(R.string.location_updated,
//                DateFormat.getDateTimeInstance().format(new Date()));
//    }


    public static TextureView.SurfaceTextureListener getSurfaceTextureListener(){


        TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                Log.i(TAG, "onSurfaceTextureAvailable");
               // openCamera();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            }
        };

        return surfaceTextureListener;

    }





    public static Bitmap getBitmapFromBytes(byte[] bytes,int w,int h){


        ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
        YuvImage yuvImage = new YuvImage(bytes, ImageFormat.NV21, w, h, null);
        yuvImage.compressToJpeg(new Rect(0, 0, w, h), 100, streamOut);
        byte[] imageBytes = streamOut.toByteArray();
        Bitmap bp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return bp;
    }


    public static Bitmap rotateBitmap(Bitmap bp){


        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        bp = Bitmap.createBitmap(bp, 0, 0,
                bp.getWidth(), bp.getHeight(),
                matrix, true);
        return bp;
    }

    public static byte[] convertYUV420888ToNV21(Image imgYUV420) {

        byte[] data;
        ByteBuffer buffer_1 = imgYUV420.getPlanes()[0].getBuffer();
        ByteBuffer buffer_2 = imgYUV420.getPlanes()[2].getBuffer();
        int buffer0_size = buffer_1.remaining();
        int buffer2_size = buffer_2.remaining();
        data = new byte[buffer0_size + buffer2_size];
        buffer_1.get(data, 0, buffer0_size);
        buffer_2.get(data, buffer0_size, buffer2_size);
        return data;
    }


    public static int getRedAverage(Bitmap image,int w,int h) {

        if (image == null) return 0;


        int width = w;
        int height =h;

        int jj = 0, ii = 0, summ = 0;
        int pixel1 = image.getPixel(ii, jj);
        int red1 = Color.red(pixel1);
        //    Log.e("KKKKKKKeyyyyeee",Integer.toString(width));
        int frames = 0;
        while (ii < width) {
            jj = 0;
            while (jj < height) {

                int pixel = image.getPixel(ii, jj);
                int red = Color.red(pixel);

              //  Log.e("RRRRRR",Integer.toString(red));
                if (red > 150) {



                    frames++;

                    summ = summ + red;
                }


                ++jj;
            }

            ++ii;
        }


     //   final int frameSize = width * height;
        if(frames>0)
        return summ / frames;
        else return 0;
    }
}
