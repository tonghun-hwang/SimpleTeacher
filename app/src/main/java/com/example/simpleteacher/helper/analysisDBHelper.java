package com.example.simpleteacher.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.DataFormatException;

public class analysisDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "analysis.db";
    public static final String TABLE_DIAGNOSTIC = "Diagnostic";
    public static final String TABLE_TRAINING = "training";
    public static final String ID = "ID";
    public static final String USERID = "USERID";
    public static final String TOTAL_WORDS = "TOTAL_WORDS";
    public static final String TOTAL_ERROR = "TOTAL_ERROR";
    public static final String TOTAL_ERROR_RATE = "TOTAL_ERROR_RATE";
    public static final String CAT_WORDS = "CAT_WORDS";
    public static final String CAT_ERROR = "CAT_ERROR";
    public static final String CAT_ERROR_RATE = "CAT_ERROR_RATE";
    public static final String FIRST_ERROR = "FIRST_ERROR";
    public static final String FIRST_ERROR_RATE = "CAT_ERROR_RATE";
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
                "TOTAL_WORDS INTEGER," +
                "TOTAL_ERROR INTEGER," +
                "TOTAL_ERROR_RATE DOUBLE," +
                "CAT_WORDS INTEGER," +
                "CAT_ERROR INTEGER," +
                "CAT_ERROR_RATE DOUBLE," +
                "FIRST_ERROR INTEGER," +
                "FIRST_ERROR_RATE DOUBLE," +
                "NUM_A_ERASE INTEGER," +
                "NUM_ONE_ERASE INTEGER," +
                "NUM_EAR INTEGER," +
                "NUM_KEY INTEGER," +
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

    public boolean replaceData(SQLiteDatabase db, String[] data) {
        Log.d(TAG, "replaceData");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String update = formatter.format(Calendar.getInstance().getTimeInMillis());
        long res = -1;
        if (data.length == 9) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(USERID, data[0]);
            contentValues.put(TOTAL_WORDS, data[1]);
            contentValues.put(TOTAL_ERROR, data[2]);
            contentValues.put(TOTAL_ERROR_RATE, data[3]);
            contentValues.put(CAT_WORDS, data[4]);
            contentValues.put(CAT_ERROR, data[5]);
            contentValues.put(CAT_ERROR_RATE, data[6]);
            contentValues.put(FIRST_ERROR, data[7]);
            contentValues.put(FIRST_ERROR_RATE, data[8]);
            contentValues.put(NUM_A_ERASE, data[9]);
            contentValues.put(NUM_ONE_ERASE, data[10]);
            contentValues.put(NUM_EAR, data[11]);
            contentValues.put(NUM_KEY, data[12]);
            contentValues.put(DATE, update);
            res = db.replace(TABLE_DIAGNOSTIC,
                    null, contentValues);
        }
        if (res == -1) {
            return false;
        } else {
            return true;
        }
    }
}
