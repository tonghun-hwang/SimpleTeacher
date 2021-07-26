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

public class teacherDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "teacherID_lb.db";
    public static final String TABLE_NAME = "teacherInfo";
    public static final String ID = "ID";
    public static final String TeacherID = "TEACHER_ID";
    public static final String TeacherPW = "TEACHER_PW";
    public static final String Students = "STUDENTS";
    public static final String DATE = "LAST_UPDATE";

    private static final String TAG = "Main.teacherDBHelper";

    public teacherDBHelper (Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "TEACHER_ID TEXT," +
                "TEACHER_PW TEXT," +
                "STUDENTS TEXT," +
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

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String update = formatter.format(Calendar.getInstance().getTimeInMillis());
        long res = -1;
        if (data.length == 3) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TeacherID, data[0]);
            contentValues.put(TeacherPW, data[1]);
            contentValues.put(Students, data[2]);
            contentValues.put(DATE, update);
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
