package com.atta.mobilecomputingassingment1;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecondFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private static final String[] paths = {"Nausea", "Headach", "diarhea", "Soar Throat", "Fever", "Muscle Ache", "Loss of smell or Taste", "cough", "Shortness of Breath", "Tired"};
    Map<String, Float> map = new HashMap<>();

    String currentItem = paths[0];
    int respiratoryRate=0;
    int heartRate=0;



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

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


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
                    CovidSymptoms sumptoms = new CovidSymptoms(((MainActivity)getActivity()).getHeratRate(), ((MainActivity)getActivity()).getRespiratoryRate(),map.get(paths[0]), map.get(paths[1]), map.get(paths[2]), map.get(paths[3]), map.get(paths[4]), map.get(paths[5]), map.get(paths[6]), map.get(paths[7]), map.get(paths[8]), map.get(paths[9]));
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


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }
}