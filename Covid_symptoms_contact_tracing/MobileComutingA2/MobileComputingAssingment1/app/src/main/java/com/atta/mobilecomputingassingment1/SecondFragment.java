package com.atta.mobilecomputingassingment1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SecondFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private static final String[] paths = {"Nausea", "Headach", "diarhea", "Soar Throat", "Fever", "Muscle Ache", "Loss of smell or Taste", "cough", "Shortness of Breath", "Tired"};
    Map<String, Float> map = new HashMap<>();

    String currentItem = paths[0];
    int respiratoryRate = 0;
    int heartRate = 0;


    public LocationRequest mLocationRequest;


    /**
     * Provides access to the Fused Location Provider API.
     */
    public FusedLocationProviderClient mFusedLocationClient;

    public Location mLocation;
    Location pLocation;


    int minDistanceLocationServices = 50;
    int timeLocationServices = 500;
    /**
     *
     * Callback for changes in location.
     */
    public LocationCallback mLocationCallback;

    public void stopGPS() {

        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }

        pLocation = null;
        Utils.setRequestingLocationUpdates(getActivity(), false);

        Log.e("GPS*****", "STOPed");

    }




    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
//

        mLocationRequest.setInterval(timeLocationServices);
        mLocationRequest.setFastestInterval(timeLocationServices / 2);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }




    public void startGPS() {

//        SharedPreferences prefs = PreferenceManager
//                .getDefaultSharedPreferences(LocationUpdatesService.this);


        if (mLocationCallback == null) {
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    onNewLocation(locationResult.getLastLocation());
                }
            };
        }
        if (mFusedLocationClient == null)
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        createLocationRequest();
        // requestLocationUpdates();


        mFusedLocationClient.requestLocationUpdates(mLocationRequest
                , mLocationCallback,
                Looper.myLooper());

            //getLastLocation();

            Log.e("GPS*****", "STart");


    }


    public void onNewLocation(Location location) {

    if(location==null)
        return;


        mLocation=location;
        String db_text="Latitude: "+  location.getLatitude()+"\n"+"Longitude: "+location.getLongitude();
        textView_gps.setText(db_text);
        Log.e("***LocationService", "Got new Location Latitude:" + location.getLatitude() + "   Longitude:" + location.getLongitude());
        stopGPS();}



        @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }


    void do1() {
        for (String str : paths)
            // Log.e(TAG,str);
            map.put(str, 0f);


    }


    RatingBar ratingBar;
    TextView textView_db;
    TextView textView_gps;






    public class TalkToServer extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            /*
             *    do things before doInBackground() code runs
             *    such as preparing and showing a Dialog or ProgressBar
             */
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            /*
             *    updating data
             *    such a Dialog or ProgressBar
             */

        }
        String str;
        @Override
        protected Void doInBackground(String... params) {

            try {
                URL u = new URL(params[0]);
                URLConnection conn = u.openConnection();
                conn.setReadTimeout(999999999);
                conn.setConnectTimeout(999999999);

                int contentLength = conn.getContentLength();


                DataInputStream stream = new DataInputStream(conn.getInputStream());

                byte[] buffer = new byte[contentLength];
                stream.readFully(buffer);
                stream.close();


                String str = new String(buffer, StandardCharsets.UTF_8);
                Log.i(TAG,"****************"+str);
                this.str=str;



//            DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
//            fos.write(buffer);
//            fos.flush();
//            fos.close();
            } catch(FileNotFoundException e) {

            } catch (IOException e) {

            }
            //do your work here
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            textView_db.setText("Matrix File Returned from the server:\n"+str);
            /*
             *    do something with data here
             *    display it or send to mainactivity
             *    close any dialogs/ProgressBars/etc...
             */
        }
    }


    private  void downloadFile(String url) {
        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            conn.setReadTimeout(1000000000);
            conn.setConnectTimeout(100000000);

            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();


            String str = new String(buffer, StandardCharsets.UTF_8);
                Log.i(TAG,"****************"+str);
            textView_db.setText("Matrix File Returned from the server:\n"+str);


//            DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
//            fos.write(buffer);
//            fos.flush();
//            fos.close();
        } catch(FileNotFoundException e) {
            return; // swallow a 404
        } catch (IOException e) {
            return; // swallow a 404
        }
    }
    private int mYear, mMonth, mDay, mHour, mMinute;
    String date;


public void     showdate(){


        // Get Current Date
        final Calendar c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                       Log.i(TAG,dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                       String m="";
                       String y="";
                       String d="";

                       if((monthOfYear + 1)<10)
                           m="0"+monthOfYear;
                       else
                           m=""+monthOfYear;
                        if((dayOfMonth)<10)
                            d="0"+dayOfMonth;
                        else
                            d=""+dayOfMonth;
                        y=year+"";

                       date=y+m+d;
                        Log.i(TAG,"*****************"+date);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        for (String str : paths) {
            Log.e(TAG, str);
            map.put(str, 0f);
        }

        spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(SecondFragment.this);
        ratingBar = view.findViewById(R.id.rating);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {


                map.remove(currentItem);
                map.put(currentItem, rating);

            }
        });
        textView_db= view.findViewById(R.id.text_view_db);
        textView_gps= view.findViewById(R.id.textview_gps);


        Log.i(TAG,"88888888888888888888888888888888888888888888888888888888888");

        view.findViewById(R.id.btn_db).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBManager dbManager=new DBManager();
                try {
                    for (String i : paths) {
                        Log.e(TAG, "asas" + map.get(i) + "    " + paths.length + "  " + i);}
                    dbManager.open(getActivity());
                    List<CovidSymptoms> l=dbManager.readAllItems();
                    int counts=0;
                    counts=dbManager.getCount();
                    CovidSymptoms cS=l.get(counts-1);

                    String db_text="DataBase Name:"+DBHelper.DATABASE_NAME+"\n";
                    db_text=db_text+"Table Name:"+CovidSymptoms.TABLE_NAME+"\n";
                    db_text=db_text+"Number Of records:"+dbManager.getCount()+"\n";
                    db_text=db_text+"Last Inserted ROW values are:"+"\n";
                    db_text=db_text+"HeartRate : "+ cS.getHeartRate()+"\n";
                    db_text=db_text+"RespiratoryRate : "+ cS.getRespiratoryRate()+"\n";
                    db_text=db_text+"Fever : "+ cS.getFever()+"\n";
                    db_text=db_text+"Cough : "+ cS.getCough()+"\n";
                    db_text=db_text+"Diarhea : "+ cS.getDiarhea()+"\n";
                    db_text=db_text+"HeadAch : "+ cS.getHeadAch()+"\n";
                    db_text=db_text+"LossSmellTaste : "+ cS.getLossSmellTaste()+"\n";
                    db_text=db_text+"MuscleAche : "+ cS.getMuscleAche()+"\n";
                    db_text=db_text+"Nausea : "+ cS.getNausea()+"\n";
                    db_text=db_text+"ShortnessBreath : "+ cS.getShortnessBreath()+"\n";
                    db_text=db_text+"SoarThroat : "+ cS.getSoarThroat()+"\n";
                    db_text=db_text+"Tired : "+ cS.getTired()+"\n";
                    db_text=db_text+"Latitude : "+ cS.getLatitude()+"\n";
                    db_text=db_text+"Longitude : "+ cS.getLongitude()+"\n";







                    textView_db.setText(db_text);
                    //  Log.e(TAG,"******assfasf**********************************"+((CovidSymptoms)l.get(dbManager.getCount()-1)).getTimestamp());

                } catch (SQLException e) {
                    e.printStackTrace();
                }




            }
        });

        view.findViewById(R.id.button_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBManager dbManager=new DBManager();
                try {
                    for (String i : paths) {
                        Log.e(TAG, "asas" + map.get(i) + "    " + paths.length + "  " + i);}
                    dbManager.open(getActivity());
                    CovidSymptoms sumptoms = new CovidSymptoms(((MainActivity)getActivity()).getHeratRate(), ((MainActivity)getActivity()).getRespiratoryRate(),map.get(paths[0]), map.get(paths[1]), map.get(paths[2]), map.get(paths[3]), map.get(paths[4]), map.get(paths[5]), map.get(paths[6]), map.get(paths[7]), map.get(paths[8]), map.get(paths[9]),mLocation.getLatitude(),mLocation.getLongitude());
                            dbManager.insert(sumptoms);
                    Log.e(TAG,"****************************************"+dbManager.getCount());

                    List<CovidSymptoms> l=dbManager.readAllItems();
                  //  Log.e(TAG,"******assfasf**********************************"+((CovidSymptoms)l.get(dbManager.getCount()-1)).getTimestamp());

                } catch (SQLException e) {
                    e.printStackTrace();
                }

//                NavHostFragment.findNavController(SecondFragment.this)
//                        .navigate(R.id.action_SecondFragment_to_FirstFragment);


            }
        });
        view.findViewById(R.id.button_upload_server).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File dbFile = getActivity().getDatabasePath("khan");
;
               uploadFile(dbFile);


            }
        });


        view.findViewById(R.id.button_gps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           startGPS();


            }
        });
        final EditText mEdit   = (EditText)view.findViewById(R.id.textview_sub);

        view.findViewById(R.id.button_contact_graph).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id=Integer.valueOf(mEdit.getText().toString());

                String SERVER_URL="http://192.168.0.73:8080/contactGraph"+"?date="+date+"000000"+"&id="+id;
               // downloadFile(SERVER_URL);TalkToServer
                TalkToServer t=new TalkToServer();
                t.execute(SERVER_URL);

            }
        });


        view.findViewById(R.id.button_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showdate();


            }
        });


    }

    static String TAG = SecondFragment.class.getName();


    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        for (String i : paths) {
            Log.e(TAG, "asas" + map.get(i) + "    " + paths.length + "  " + i);


        }

        switch (position) {
            case 0:
                currentItem = paths[0];
                ratingBar.setRating(map.get(currentItem));
                Log.e(TAG, "Case 0");
                break;
            case 1:
                currentItem = paths[1];
                ratingBar.setRating(map.get(currentItem));
                Log.e(TAG, "Case 1");
                break;
            case 2:
                currentItem = paths[2];
                ratingBar.setRating(map.get(currentItem));
                Log.e(TAG, "Case 2");
                break;
            case 3:
                currentItem = paths[3];
                ratingBar.setRating(map.get(currentItem));
                Log.e(TAG, "Case 3");
                break;
            case 4:
                currentItem = paths[4];
                ratingBar.setRating(map.get(currentItem));
                Log.e(TAG, "Case 4");
                break;
            case 5:
                currentItem = paths[5];
                ratingBar.setRating(map.get(currentItem));
                Log.e(TAG, "Case 5");
                break;
            case 6:
                currentItem = paths[6];
                ratingBar.setRating(map.get(currentItem));
                Log.e(TAG, "Case 6");
                break;
            case 7:
                currentItem = paths[7];
                ratingBar.setRating(map.get(currentItem));
                Log.e(TAG, "Case 7");
                break;

            case 8:
                currentItem = paths[8];
                ratingBar.setRating(map.get(currentItem));
                Log.e(TAG, "Case 8");
                break;
            case 9:
                currentItem = paths[9];
                ratingBar.setRating(map.get(currentItem));
                Log.e(TAG, "Case 9");
                break;


        }
    }





    public int uploadFile(final File file) {

        int serverResponseCode = 0;
        String SERVER_URL="http://192.168.0.73:8080/api/upload/";

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(file.getAbsolutePath());


        String[] parts = file.getAbsolutePath().split("/");
        final String fileName = parts[parts.length - 1];

        if (!selectedFile.isFile()) {


            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
            return 0;
        } else {
            try {
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(SERVER_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");

                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file", fileName);

               // connection.setRequestProperty("filedata", fileName);

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""
                        + fileName + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0) {
                    Log.i("aaaaa","aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                final String serverResponseMessage = connection.getResponseMessage();

                Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if (serverResponseCode == 200) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView_db.setText("Database File Uploaded successfully:");

                        }
                    });
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "File Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
            };
            return serverResponseCode;
        }

    }







//



//    public int uploadFile(File file) {
//        String urlString = "http://192.168.0.73:8080/api/upload/";
//
//
//        String fileName = file.getName();
//
//        HttpURLConnection conn = null;
//        DataOutputStream dos = null;
//        String lineEnd = "\r\n";
//        String twoHyphens = "--";
//        String boundary = "*****";
//        int bytesRead, bytesAvailable, bufferSize;
//        byte[] buffer;
//        int maxBufferSize = 1 * 1024 * 1024;
//        File sourceFile = file;
//
//
//            try {
//
//                // open a URL connection to the Servlet
//                FileInputStream fileInputStream = new FileInputStream(sourceFile);
//                URL url = new URL(urlString);
//
//                // Open a HTTP  connection to  the URL
//                conn = (HttpURLConnection) url.openConnection();
//                conn.setDoInput(true); // Allow Inputs
//                conn.setDoOutput(true); // Allow Outputs
//                conn.setUseCaches(false); // Don't use a Cached Copy
//                conn.setRequestMethod("POST");
//                conn.setRequestProperty("Connection", "Keep-Alive");
//                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
//                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
//                conn.setRequestProperty("uploaded_file", fileName);
//
//                dos = new DataOutputStream(conn.getOutputStream());
//
//                dos.writeBytes(twoHyphens + boundary + lineEnd);
//                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename="
//                        + fileName + "" + lineEnd);
//
//                   dos.writeBytes(lineEnd);
//
//                   // create a buffer of  maximum size
//                   bytesAvailable = fileInputStream.available();
//
//                   bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                   buffer = new byte[bufferSize];
//
//                   // read file and write it into form...
//                   bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//
//                   while (bytesRead > 0) {
//
//                     dos.write(buffer, 0, bufferSize);
//                     bytesAvailable = fileInputStream.available();
//                     bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                     bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//
//                    }
//
//                   // send multipart form data necesssary after file data...
//                   dos.writeBytes(lineEnd);
//                   dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//
//                   // Responses from the server (code and message)
//                   int serverResponseCode = conn.getResponseCode();
//                   String serverResponseMessage = conn.getResponseMessage();
//
//                   Log.i("uploadFile", "HTTP Response is : "
//                           + serverResponseMessage + ": " + serverResponseCode);
//
//                   if(serverResponseCode == 200){
//
//
//                   }
//
//                   //close the streams //
//                   fileInputStream.close();
//                   dos.flush();
//                   dos.close();
//
//              } catch (MalformedURLException ex) {
//
//
//                  Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
//              } catch (Exception e) {
//
//
//                  Log.e(TAG, "Exception : "
//                                                   + e.getMessage(), e);
//              }
//
//              return 0;
//
//           } // End else b
//
//
//
//




//    private void doFileUpload(File exsistingFileName){
//        HttpURLConnection conn = null;
//        DataOutputStream dos = null;
//        DataInputStream inStream = null;
//
//        //String exsistingFileName = "/sdcard/six.3gp";
//        // Is this the place are you doing something wrong.
//
//        String lineEnd = "\r\n";
//        String twoHyphens = "--";
//        String boundary =  "*****";
//        int bytesRead, bytesAvailable, bufferSize;
//        byte[] buffer;
//        int maxBufferSize = 1*1024*1024;
//        String urlString = "http://192.168.0.73:8080/api/upload/";
//        try
//        {
//            Log.e("MediaPlayer","Inside second Method");
//            FileInputStream fileInputStream = new FileInputStream(exsistingFileName );
//            URL url = new URL(urlString);
//            conn = (HttpURLConnection) url.openConnection();
//            conn.setDoInput(true);
//            // Allow Outputs
//            conn.setDoOutput(true);
//            // Don't use a cached copy.
//            conn.setUseCaches(false);
//            // Use a post method.
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Connection", "Keep-Alive");
//            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
//            dos = new DataOutputStream( conn.getOutputStream() );
//            dos.writeBytes(twoHyphens + boundary + lineEnd);
//            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + exsistingFileName.getName() +"\"" + lineEnd);
//            dos.writeBytes(lineEnd);
//            Log.e("MediaPlayer","Headers are written");
//            bytesAvailable = fileInputStream.available();
//            bufferSize = Math.min(bytesAvailable, maxBufferSize);
//            buffer = new byte[bufferSize];
//            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//            while (bytesRead > 0)
//            {
//                dos.write(buffer, 0, bufferSize);
//                bytesAvailable = fileInputStream.available();
//                bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//            }
//            dos.writeBytes(lineEnd);
//            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String inputLine;
//            String LogString = "";
//
//            while ((inputLine = in.readLine()) != null)  {
//                LogString= LogString + inputLine;
//            }
//
//            Log.i(Utils.TAG, LogString);
//            // close streams
//            fileInputStream.close();
//            dos.flush();
//            dos.close();
//        }
//        catch (MalformedURLException ex)
//        {
//            Log.e("MediaPlayer", "error: " + ex.getMessage(), ex);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }  catch (ProtocolException e) {
//            e.printStackTrace();
//        }  catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        //------------------ read the SERVER RESPONSE
//        try {
//            inStream = new DataInputStream ( conn.getInputStream() );
//            String str;
//            while (( str = inStream.readLine()) != null)
//            {
//                Log.e("MediaPlayer","Server Response"+str);
//            }
//        /*while((str = inStream.readLine()) !=null ){
//
//        }*/
//            inStream.close();
//        }
//        catch (IOException ioex){
//            Log.e("MediaPlayer", "error: " + ioex.getMessage(), ioex);
//        }
//    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }
}