package com.example.simpleteacher.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class analysisTrainingDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "analysisTraining.db";
    public static final String TABLE_TRAINING = "training_s";
    public static final String ID = "ID";
    public static final String USERID = "USERID";
    public static final String WORD_TOTAL = "WORD_TOTAL";
    public static final String WORD_ERROR = "WORD_ERROR";
    public static final String WORD_ER = "WORD_ER";
    public static final String WORD_CAT_TOTAL = "WORD_CAT_TOTAL";
    public static final String WORD_CAT_ERROR = "WORD_CAT_ERROR";
    public static final String WORD_CAT_ER = "WORD_CAT_ER";
    public static final String NUM_A_ERASE = "NUM_A_ERASE";
    public static final String NUM_ONE_ERASE = "NUM_ONE_ERASE";
    public static final String NUM_EAR = "NUM_EAR";
    public static final String NUM_KEY = "NUM_KEY";
    public static final String DATE = "LAST_UPDATE";

    private static final String TAG = "Main.userDBHelper";
    private static final int MAX_SESSION_BLOCK = 4;

    public analysisTrainingDBHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (int i = 0; i < MAX_SESSION_BLOCK; i++) {
            createTable(db, i);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i = 0; i < MAX_SESSION_BLOCK; i++) {
            createTable(db, i);
        }
    }

    private void createTable(SQLiteDatabase db, int sessionBlock) {
        String tableName = TABLE_TRAINING + sessionBlock;
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "USERID TEXT," +
                "WORD_TOTAL INTEGER," +
                "WORD_ERROR INTEGER," +
                "WORD_ER DOUBLE," +
                "WORD_CAT_TOTAL INTEGER," +
                "WORD_CAT_ERROR INTEGER," +
                "WORD_CAT_ER DOUBLE," +
                "NUM_A_ERASE INTEGER," +
                "NUM_ONE_ERASE INTEGER," +
                "NUM_EAR INTEGER," +
                "NUM_KEY INTEGER," +
                "LAST_UPDATE DATETIME);");
    }

    public boolean replaceData(SQLiteDatabase db, int id, String user, double[] data, int sessionBlock) {
        Log.d(TAG, "replaceData");
        String tableName = TABLE_TRAINING + sessionBlock;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String update = formatter.format(Calendar.getInstance().getTimeInMillis());
        long res = -1;
        if (data.length == 8) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, id);
            contentValues.put(USERID, user);
            contentValues.put(WORD_TOTAL, data[0]);
            contentValues.put(WORD_ERROR, data[1]);
            contentValues.put(WORD_ER, getRateD3(data[1], data[0]));
            contentValues.put(WORD_CAT_TOTAL, data[2]);
            contentValues.put(WORD_CAT_ERROR, data[3]);
            contentValues.put(WORD_CAT_ER, getRateD3(data[3], data[2]));
            contentValues.put(NUM_A_ERASE, data[4]);
            contentValues.put(NUM_ONE_ERASE, data[5]);
            contentValues.put(NUM_EAR, data[6]);
            contentValues.put(NUM_KEY, data[7]);
            contentValues.put(DATE, update);
            res = db.replace(tableName,
                    null, contentValues);
        }
        if (res == -1) {
            return false;
        } else {
            return true;
        }
    }

    private String getRateD3(double numerator, double denominator) {
        double rate = numerator / denominator;
        return roundD3(rate);
    }

    public List<String[]> getDataList(SQLiteDatabase db, int sessionBlock) {
        Log.d(TAG, "getDataList");
        String tableName = TABLE_TRAINING + sessionBlock;

        long res = -1;
        List<String[]> dataList = new ArrayList<String[]>();
        String[] columnName = new String[] {
                "ID",
                "USERID",
                "WORD_TOTAL",
                "WORD_ERROR",
                "WORD_ER",
                "WORD_CAT_TOTAL",
                "WORD_CAT_ERROR",
                "WORD_CAT_ER",
                "NUM_A_ERASE",
                "NUM_ONE_ERASE",
                "NUM_EAR",
                "NUM_KEY",
                "LAST_UPDATE"
        };
        dataList.add(columnName);
        Cursor c = db.rawQuery("SELECT * FROM "
                + tableName, null);
        c.moveToFirst();
        do {
            String[] data = new String[13];
            for (int i = 0; i < 13; i++) {
                data[i] = c.getString(i);
            }
            dataList.add(data);
        } while (c.moveToNext());
        c.close();

        return dataList;
    }

    public List<Double[]> getDataListDouble(SQLiteDatabase db, int sessionBlock) {
        Log.d(TAG, "getDataList");
        String tableName = TABLE_TRAINING + sessionBlock;

        long res = -1;
        List<Double[]> dataList = new ArrayList<Double[]>();
        Cursor c = db.rawQuery("SELECT * FROM "
                + tableName, null);
        c.moveToFirst();
        do {
            Double[] data = new Double[17];
            for (int i = 0; i < 17; i++) {
                data[i] = c.getDouble(i);
            }
            dataList.add(data);
        } while (c.moveToNext());
        c.close();

        return dataList;
    }

    private String roundD3(double datum) {
        String result = String.valueOf(Math.round(datum * 1000) / (double) 1000);
        return result;
    }
}
