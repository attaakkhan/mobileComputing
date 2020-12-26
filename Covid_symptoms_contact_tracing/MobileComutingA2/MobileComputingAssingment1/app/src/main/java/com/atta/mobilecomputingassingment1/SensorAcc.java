package com.atta.mobilecomputingassingment1;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.text.BoringLayout;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.SENSOR_SERVICE;

public class SensorAcc implements SensorEventListener {

    public int getRespiratoryRate() {
        return respiratoryRate;
    }

    public int respiratoryRate=0;
    static String TAG = SensorAcc.class.getName();
    private SensorManager sensorManager;
    private boolean color = false;
    private TextView textview;
    private long lastUpdate;
    int singletonConunt = 0;

    Context context;

    public SensorAcc(Context context, TextView textView) {
        if (singletonConunt > 1)
            throw new IllegalAccessError();


        this.context = context;
        this.textview = textView;
        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);


    }

    public SensorManager getsensorManager() {
        return sensorManager;
    }

    ;


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            measureRespiratoryRate(sensorEvent);
        }
    }

    public Boolean isNear(Float a, Float b, long limit, Boolean high) {

        a = Math.abs(a);
        b = Math.abs(b);
        Float diff = a - b;
        diff = Math.abs(diff);

        if (Math.abs(a - b) <= limit) {
            Log.e(TAG, "isnear a-b " + Math.abs(a - b) + " limit " + limit);

            return true;


        }
        if (high == null) {

            return false;


        }
        Log.e(TAG, "isnear a-b " + a + "   b " + b + "  high " + high.toString());
        //Low Z Value9.435912 high z9.477084

        if (high == true) {
            if (a > 0f) {
                Log.e(TAG, "highfftrue" + (a > b));
                if (a > b) {
                    return true;

                }
            } else if (a < 0f) {
                Log.e(TAG, "highfftrue-" + (a < b));
                if (a < b)
                    return true;

            }
            return false;
        } else if (high == false) {

            Log.e(TAG, "isnearlow");
            if (a > 0f) {
                Log.e(TAG, "highff" + (a < b));
                if (a < b)
                    return true;
            } else if (a < 0f) {
                Log.e(TAG, "highff-" + (a > b));
                if (a > b)
                    return true;


            }
            return false;
        }


        return false;


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    long startTime = -1;
    int beats = 0;
    Float lastZ = null;
    Float highZ = null;
    Float lowZ = null;
    Float lowZSUm = null;
    Float highZSUm = null;
    int lowZSUmCounts = 0;
    Float lowZconst = 0.3f;


    Boolean lowHit = false;
    Boolean highHit = false;

    Float ll = 0f;
    Float hh = 0f;
    int llc = 0;
    int hhc = 0;
    List<Float> l = new ArrayList<Float>();
    float midZ = 0;
    int midZCounts = 0;
    boolean chk = false;

    private void measureRespiratoryRate(SensorEvent event) {

        float[] values = event.values;
        // Movement
        float z = values[2];
        l.add(z);
        if (l.size() > 10) {

            float sum = 0;
            for (float i : l) {
                sum = sum + i;

            }
            z = sum / l.size();
            l.clear();

        } else {

            return;
        }
      /*  Log.i(TAG,  "                high z" + z);

        if(null==null)*/
        //   return;
        if (lastZ == null) {
            lastZ = z;
            long actualTime = event.timestamp;
            lastUpdate = actualTime;
            startTime = System.currentTimeMillis();
            return;

        }

        if (lowZ == null) {
            if (lowZSUm == null) {

                lowZSUm = z + 100;
            }
            if (highZSUm == null) {

                highZSUm = z - 100;
            }
            midZ = (midZ * midZCounts + z) / (midZCounts + 1);
            midZCounts = midZCounts + 1;
            Log.i(TAG, "                mid z" + midZ);

            if (z < midZ + 0.02f) {
                lowZSUm = z;
                ll = ll + z;
                llc++;
            }
            if (z > midZ - 0.02f) {
                highZSUm = z;
                hh = hh + z;
                hhc++;

            }

            lowZSUmCounts++;
            if (lowZSUmCounts == 10) {
                lowZ = ll / llc;
                highZ = hh / hhc;

                midZCounts = 0;

                Log.i(TAG, "Low Z Value" + lowZ + " high z" + highZ);


            }
            Log.i(TAG, "                high z" + z);

            return;


        }


        long actualTime = event.timestamp;

        long tt = actualTime - lastUpdate;
        // Log.e(TAG,"new "+tt);
        /*if (actualTime - lastUpdate < 2 * 66743000) {
            return;
        }*/
        lastUpdate = actualTime;

        if (lowHit == Boolean.FALSE && highHit == Boolean.FALSE) {
            Log.e(TAG, "lowHit!!");
            if (isNear(z, lowZ, (long) (Math.abs(highZ - lowZ / 2.2f)), false)) {

                Log.e(TAG, "lowHit");

                lowHit = Boolean.TRUE;


            }


        } else if (lowHit == Boolean.TRUE && highHit == Boolean.FALSE) {
            Log.e(TAG, "highHit!!");
            if (isNear(z, highZ, (long) (Math.abs(highZ - lowZ) / 2.2f), true)) {
                Log.e(TAG, "highHit");

                highHit = Boolean.TRUE;


            }
        } else if (lowHit == Boolean.TRUE && highHit == Boolean.TRUE) {
            Log.e(TAG, "highHitAgain!!");
            if (isNear(z, highZ, (long) (Math.abs(highZ - lowZ) / 2.2f), true)) {
                Log.e(TAG, "highHitAgain");
                highHit = Boolean.FALSE;
                beats++;


            }
        }


        float x = values[0];
        float y = values[1];


        long endTime = System.currentTimeMillis();
        double totalTimeInSecs = (endTime - startTime) / 1000d;

        // Log.e(TAG, "new Data");
//        Log.e(TAG, "x  " + x + "  y " + y + "   z  " + z);

        // if (totalTimeInSecs > 10)
        textview.setText("Calculating...Time Spent:" + (int) (totalTimeInSecs - 2) + " secs" + " Breaths:" + Integer.toString(beats));
        if (totalTimeInSecs > 45 + 3) {
            ((MainActivity)context).setRespiratoryRate((int) (beats *60/ 45)) ;
            textview.setText(Integer.toString((int) (beats * 60 / 45) ));
            unregester();
        }
        //unregester();


    }

    public void register() {
        respiratoryRate=0;


        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }


    public void unregester() {
        sensorManager.unregisterListener(this);
    }

}
