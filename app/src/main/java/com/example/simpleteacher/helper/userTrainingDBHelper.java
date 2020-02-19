package com.example.simpleteacher.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.simpleteacher.ItemActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class userTrainingDBHelper extends SQLiteOpenHelper {
    public static final String ID = "ID";
    public static final String USERID = "USERID";
    public static final String SESSION_NO = "SESSION_NO";
    public static final String CORRECT_WORD = "CORRECT_WORD";
    public static final String DATE = "LAST_UPDATE";

    public static final String EVENT = "EVENT";
    public static final String LETTER_INDEX = "LETTER_INDEX";
    public static final String REPEAT_COUNT = "REPEAT_COUNT";
    public static final String PARAM1 = "PARAM1";
    public static final String PARAM2 = "PARAM2";

    private static final String TAG = "Main.readTrainingURL";
    private ItemActivity mParent;
    private int mSessionNo;
    private String mTableName;

    public userTrainingDBHelper(Context context, String dbName, String userID, int version) {
        super(context, dbName, null, version);
        mParent = (ItemActivity) context;
        mTableName = userID;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createEventTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + mTableName);
        onCreate(db);
    }

    public void createEventTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + mTableName +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "SESSION_NO INTEGER," +
                "CORRECT_WORD TEXT," +
                "EVENT TEXT," +
                "LETTER_INDEX INTEGER," +
                "REPEAT_COUNT INTEGER," +
                "PARAM1 TEXT," +
                "PARAM2 BLOB," +
                "LAST_UPDATE DATETIME);");
    }

    public void createNewTable(SQLiteDatabase db) {
        Cursor c = db.rawQuery("SELECT name FROM "
                + "sqlite_sequence", null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            do {
                db.execSQL("DROP TABLE IF EXISTS "
                        + c.getString(c.getColumnIndex("name")));
            } while (c.moveToNext());
        }

        db.execSQL("CREATE TABLE IF NOT EXISTS " + mTableName +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "SESSION_NO INTEGER," +
                "CORRECT_WORD TEXT," +
                "EVENT TEXT," +
                "LETTER_INDEX INTEGER," +
                "REPEAT_COUNT INTEGER," +
                "PARAM1 TEXT," +
                "PARAM2 BLOB," +
                "LAST_UPDATE DATETIME);");
    }

    public boolean insertEventData(SQLiteDatabase db, String[] parm, int[] num) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String update = formatter.format(Calendar.getInstance().getTimeInMillis());
        long res = -1;
        if (parm != null && num != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(SESSION_NO, num[2]);
            contentValues.put(CORRECT_WORD, parm[0]);
            contentValues.put(EVENT, parm[1]);
            contentValues.put(LETTER_INDEX, num[0]);
            contentValues.put(REPEAT_COUNT, num[1]);
            contentValues.put(PARAM1, parm[2]);
            contentValues.put(PARAM2, parm[3]);
            contentValues.put(DATE, update);
            res = db.insert(mTableName,
                    null, contentValues);
        }
        if (res == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertEventData(SQLiteDatabase db, Cursor c) {
        long res = -1;
        if (c != null && c.getCount() > 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(SESSION_NO, c.getInt(1));
            contentValues.put(CORRECT_WORD, c.getString(2));
            contentValues.put(EVENT, c.getString(3));
            contentValues.put(LETTER_INDEX, c.getInt(4));
            contentValues.put(REPEAT_COUNT, c.getInt(5));
            contentValues.put(PARAM1, c.getString(6));
            contentValues.put(PARAM2, c.getBlob(7));
            contentValues.put(DATE, c.getString(8));
            res = db.insert(mTableName,
                    null, contentValues);
        }
        if (res == -1) {
            return false;
        } else {
            return true;
        }
    }
}
