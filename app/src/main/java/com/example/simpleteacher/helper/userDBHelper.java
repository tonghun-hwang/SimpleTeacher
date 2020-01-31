package com.example.simpleteacher.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.util.Date;

public class userDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "userInfo.db";
    public static final String TABLE_NAME = "user_table";
    public static final String COL_0 = "ID";
    public static final String COL_1 = "USERID";
    public static final String COL_2 = "USERPW";
    public static final String COL_3 = "NAME";
    public static final String COL_4 = "GRADE";
    public static final String COL_5 = "TEACHER";
    public static final String COL_6 = "SCHOOL";
    public static final String COL_7 = "CATEGORY";
    public static final String DATE = "LAST_UPDATE";
    //static final int DATABASE_VERSION = 2;
    private static final String TAG = "Main.userDBHelper";

    public userDBHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }
/*
    public userDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
*/

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "USERID TEXT," +
                "USERPW TEXT," +
                "NAME TEXT," +
                "GRADE TEXT," +
                "TEACHER TEXT," +
                "SCHOOL TEXT," +
                "CATEGORY INTEGER," +
                "LAST_UPDATE DATETIME);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean updateData(SQLiteDatabase db, ContentValues values, int itemId) {
        String where = String.valueOf(itemId + 1);
        Log.d(TAG, "updateData");
        Log.d(TAG, "updateData: " + values.toString() + " " + where);
        Date date = new Date();
        values.put(DATE, DateFormat.getDateTimeInstance().format(date));
        db.update(TABLE_NAME, values, "ID = ?", new String[] {where});
        return true;
    }

    public boolean replaceData(SQLiteDatabase db, String[] data) {
        Log.d(TAG, "replaceData");

        long res = -1;
        if (data.length == 9) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_0, data[0]);
            contentValues.put(COL_1, data[1]);
            contentValues.put(COL_2, data[2]);
            contentValues.put(COL_3, data[3]);
            contentValues.put(COL_4, data[4]);
            contentValues.put(COL_5, data[5]);
            contentValues.put(COL_6, data[6]);
            contentValues.put(COL_7, Integer.valueOf(data[7]));
            contentValues.put(DATE, data[8]);
            res = db.replace(TABLE_NAME,
                    null, contentValues);
        }
        if (res == -1) {
            return false;
        } else {
            return true;
        }
    }
}