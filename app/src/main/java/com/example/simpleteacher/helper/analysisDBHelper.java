package com.example.simpleteacher.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.DataFormatException;

public class analysisDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "analysis.db";
    public static final String TABLE_DIAGNOSTIC = "Diagnostic";
    public static final String TABLE_TRAINING = "training";
    public static final String ID = "ID";
    public static final String USERID = "USERID";
    public static final String WORD_TOTAL = "WORD_TOTAL";
    public static final String WORD_ERROR = "WORD_ERROR";
    public static final String WORD_ER = "WORD_ER";
    public static final String CAT3_TOTAL = "CAT3_TOTAL";
    public static final String CAT3_ER = "CAT3_ER";
    public static final String CAT4_TOTAL = "CAT4_TOTAL";
    public static final String CAT4_ER = "CAT4_ER";
    public static final String CAT5_TOTAL = "CAT5_TOTAL";
    public static final String CAT5_ER = "CAT5_ER";
    public static final String CAT6_TOTAL = "CAT6_TOTAL";
    public static final String CAT6_ER = "CAT6_ER";
    public static final String CAT7_TOTAL = "CAT7_TOTAL";
    public static final String CAT7_ER = "CAT7_ER";
    public static final String NUM_A_ERASE = "NUM_A_ERASE";
    public static final String NUM_ONE_ERASE = "NUM_ONE_ERASE";
    public static final String NUM_EAR = "NUM_EAR";
    public static final String NUM_KEY = "NUM_KEY";
    public static final String DATE = "LAST_UPDATE";

    private static final String TAG = "Main.userDBHelper";

    public analysisDBHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIAGNOSTIC);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_DIAGNOSTIC +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "USERID TEXT," +
                "WORD_TOTAL INTEGER," +
                "WORD_ERROR INTEGER," +
                "WORD_ER DOUBLE," +
                "NUM_A_ERASE INTEGER," +
                "NUM_ONE_ERASE INTEGER," +
                "NUM_EAR INTEGER," +
                "NUM_KEY INTEGER," +
                "CAT3_TOTAL INTEGER," +
                "CAT3_ER DOUBLE," +
                "CAT4_TOTAL INTEGER," +
                "CAT4_ER DOUBLE," +
                "CAT5_TOTAL INTEGER," +
                "CAT5_ER DOUBLE," +
                "CAT6_TOTAL INTEGER," +
                "CAT6_ER DOUBLE," +
                "CAT7_TOTAL INTEGER," +
                "CAT7_ER DOUBLE," +
                "LAST_UPDATE DATETIME);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIAGNOSTIC);
        onCreate(db);
    }

    public boolean updateData(SQLiteDatabase db, ContentValues values, int itemId) {
        String where = String.valueOf(itemId + 1);
        Log.d(TAG, "updateData: " + values.toString() + " " + where);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String update = formatter.format(Calendar.getInstance().getTimeInMillis());
        values.put(DATE, update);

        db.update(TABLE_DIAGNOSTIC, values, "ID = ?", new String[] {where});
        return true;
    }

    public boolean replaceData(SQLiteDatabase db, int id, String user, double[] data, String lastDate) {
        Log.d(TAG, "replaceData");

        long res = -1;
        if (data.length == 17) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, id);
            contentValues.put(USERID, user);
            contentValues.put(WORD_TOTAL, data[0]);
            contentValues.put(WORD_ERROR, data[1]);
            contentValues.put(WORD_ER, roundD3(data[2]));
            contentValues.put(NUM_A_ERASE, data[3]);
            contentValues.put(NUM_ONE_ERASE, data[4]);
            contentValues.put(NUM_EAR, data[5]);
            contentValues.put(NUM_KEY, data[6]);
            contentValues.put(CAT3_TOTAL, data[7]);
            contentValues.put(CAT3_ER, roundD3(data[8]));
            contentValues.put(CAT4_TOTAL, data[9]);
            contentValues.put(CAT4_ER, roundD3(data[10]));
            contentValues.put(CAT5_TOTAL, data[11]);
            contentValues.put(CAT5_ER, roundD3(data[12]));
            contentValues.put(CAT6_TOTAL, data[13]);
            contentValues.put(CAT6_ER, roundD3(data[14]));
            contentValues.put(CAT7_TOTAL, data[15]);
            contentValues.put(CAT7_ER, roundD3(data[16]));
            contentValues.put(DATE, lastDate);
            res = db.replace(TABLE_DIAGNOSTIC,
                    null, contentValues);
        }
        if (res == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<String[]> getDataList(SQLiteDatabase db) {
        Log.d(TAG, "getDataList");

        long res = -1;
        List<String[]> dataList = new ArrayList<String[]>();
        String[] columnName = new String[] {
                "ID",
                "USERID",
                "WORD_TOTAL",
                "WORD_ERROR",
                "WORD_ER",
                "NUM_A_ERASE",
                "NUM_ONE_ERASE",
                "NUM_EAR",
                "NUM_KEY",
                "CAT3_TOTAL",
                "CAT3_ER",
                "CAT4_TOTAL",
                "CAT4_ER",
                "CAT5_TOTAL",
                "CAT5_ER",
                "CAT6_TOTAL",
                "CAT6_ER",
                "CAT7_TOTAL",
                "CAT7_ER",
                "LAST_UPDATE"
        };
        dataList.add(columnName);
        Cursor c = db.rawQuery("SELECT * FROM "
                + TABLE_DIAGNOSTIC, null);
        c.moveToFirst();
        do {
            String[] data = new String[20];
            for (int i = 0; i < 20; i++) {
                data[i] = c.getString(i);
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

    private double getRate(int datum, int datum1) {
        double res = -1;
        if (datum1 != 0) {
            res = datum / (double) datum1;
        }
        return res;
    }
}
