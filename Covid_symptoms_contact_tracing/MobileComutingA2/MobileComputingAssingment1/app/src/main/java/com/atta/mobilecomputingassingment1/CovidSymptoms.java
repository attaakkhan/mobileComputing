package com.atta.mobilecomputingassingment1;

import android.util.Log;

import java.sql.Timestamp;

public class CovidSymptoms {
    public static final String TABLE_NAME = "CovidSymptoms";

    public static final String COLUMN_HEART_RATE = "heart_rate";
    public static final String COLUMN_RESPIRATORY_RATE = "respiratory_rate";
    public static final String COLUMN_ITEM_1 = "nausea";
    public static final String COLUMN_ITEM_2= "headach";
    public static final String COLUMN_ITEM_3 = "diarhea";
    public static final String COLUMN_ITEM_4 = "soarthroat";
    public static final String COLUMN_ITEM_5 = "fever";
    public static final String COLUMN_ITEM_6 = "muscleache";
    public static final String COLUMN_ITEM_7 = "losssmelltaste";
    public static final String COLUMN_ITEM_8 = "cough";
    public static final String COLUMN_ITEM_9 = "shortnessbreath";
    public static final String COLUMN_ITEM_10 = "tired";
    public static final String COLUMN_ITEM_time = "timestamp";
    public static final String COLUMN_ITEM_log = "longitude";
    public static final String COLUMN_ITEM_lat = "latitude";




    public void setHeartRate(float heartRate) {
        this.heartRate = heartRate;
    }

    public void setRespiratoryRate(float respiratoryRate) {
        this.respiratoryRate = respiratoryRate;
    }

    private float heartRate;
    private float respiratoryRate;

    private float nausea;

    public float getNausea() {
        return nausea;
    }

    public void setNausea(float nausea) {
        this.nausea = nausea;
    }

    public float getHeadAch() {
        return headAch;
    }

    public void setHeadAch(float headAch) {
        this.headAch = headAch;
    }

    public float getDiarhea() {
        return diarhea;
    }

    public void setDiarhea(float diarhea) {
        this.diarhea = diarhea;
    }

    public float getSoarThroat() {
        return soarThroat;
    }

    public void setSoarThroat(float soarThroat) {
        this.soarThroat = soarThroat;
    }

    public float getMuscleAche() {
        return muscleAche;
    }

    public void setMuscleAche(float muscleAche) {
        this.muscleAche = muscleAche;
    }

    public float getLossSmellTaste() {
        return lossSmellTaste;
    }

    public void setLossSmellTaste(float lossSmellTaste) {
        this.lossSmellTaste = lossSmellTaste;
    }

    public float getCough() {
        return cough;
    }

    public void setCough(float cough) {
        this.cough = cough;
    }

    public float getShortnessBreath() {
        return shortnessBreath;
    }

    public void setShortnessBreath(float shortnessBreath) {
        this.shortnessBreath = shortnessBreath;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public float getTired() {
        return tired;
    }

    public void setTired(float tired) {
        this.tired = tired;
    }

    public float getFever() {
        return fever;
    }

    public void setFever(float fever) {
        this.fever = fever;
    }

    private float headAch;
    private float diarhea;
    private float soarThroat;
    private float muscleAche;
    private float lossSmellTaste;
    private float cough;
    private float shortnessBreath;
    private float tired;
    private float fever;
    private Double longitude;
    private Double latitude;


    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    Long timestamp;

   //  nausea, headAch, diarhea,soarThroat,muscleAche,lossSmellTaste,cough,shortnessBreath,tired;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    +COLUMN_ITEM_time+" TIMESTAMP DEFAULT CURRENT_TIMESTAMP  PRIMARY KEY,"
                    + COLUMN_HEART_RATE + " INTEGER ,"
                    + COLUMN_RESPIRATORY_RATE + " INTEGER,"
                    + COLUMN_ITEM_1 + " INTEGER,"
                      + COLUMN_ITEM_2 + " INTEGER,"
            + COLUMN_ITEM_3 + " INTEGER,"
            + COLUMN_ITEM_4 + " INTEGER,"
            + COLUMN_ITEM_5 + " INTEGER,"
            + COLUMN_ITEM_6 + " INTEGER,"
            + COLUMN_ITEM_7 + " INTEGER,"
            + COLUMN_ITEM_8 + " INTEGER,"
            + COLUMN_ITEM_9 + " INTEGER,"
                    + COLUMN_ITEM_10 + " INTEGER,"
                    + COLUMN_ITEM_lat + " REAL,"
                    + COLUMN_ITEM_log + " REAL"
  + ")";

    public CovidSymptoms() {
    }
    public CovidSymptoms(float heartRate, float respiratoryRate,float nausea, float headAch,float diarhea,float soarThroat, float fever, float muscleAche,float lossSmellTaste,float cough,float shortnessBreath,float tired, Double latitude,Double longitude) {
        this.heartRate = heartRate;
        this.respiratoryRate = respiratoryRate;
        this.nausea = nausea;
        this.headAch = headAch;
        this.diarhea = diarhea;
        this.soarThroat = soarThroat;
        this.muscleAche = muscleAche;
        this.lossSmellTaste = lossSmellTaste;
        this.cough = cough;
        this.shortnessBreath = shortnessBreath;
        this.tired = tired;
        this.fever=fever;
        this.longitude = longitude;
        this.latitude=latitude;

    }


    public float getHeartRate() {
        return heartRate;
    }

    public float getRespiratoryRate() {
        return respiratoryRate;
    }

}