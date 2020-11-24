package com.example.simpleteacher.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.simpleteacher.ItemActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class userCategorySessionDBHelper extends SQLiteOpenHelper {
    public static final String RESULT_DIAGNOSTIC = "result_diagnostic";
    public static final String RESULT_TRAINING = "result_training";
    public static final String RESULT_TRAIN_DETAIL = "result_train_detail";
    public static final String RESULT_TRAIN_MAIN = "result_train_main";

    public static final String ID = "ID";
    public static final String CORRECT_WORD = "CORRECT_WORD";
    public static final String WRITTEN_WORD = "WRITTEN_WORD";
    public static final String CATEGORY_TOTAL = "CATEGORY_TOTAL";
    public static final String CATEGORY_ERROR = "CATEGORY_ERROR";
    public static final String UNCATEGORIZED = "UNCATEGORIZED";
    public static final String MULTI_BIT = "MULTI_BIT";
    public static final String CATEGORY_ERROR_RAW = "CATEGORY_ERROR_RAW";
    public static final String DATE = "LAST_UPDATE";

    public static final String SESSION = "SESSION";
    public static final String CATEGORY_MAIN = "CATEGORY_MAIN";
    public static final String CATEGORY_SUB = "CATEGORY_SUB";
    public static final String NUM_ERROR = "NUM_ERROR";
    public static final String NUM_TOTAL = "NUM_TOTAL";
    public static final String ERROR_RATE = "ERROR_RATE";

    public static final String WORD = "WORD";
    public static final String CATEGORY = "CATEGORY";
    public static final String DIFFICULT = "DIFFICULT";

    private static final String TAG = "Main.userResultDBHelper";
    private ItemActivity mParent;

    public userCategorySessionDBHelper(Context context, String dbName, int version) {
        super(context, dbName, null, version);
        mParent = (ItemActivity) context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createWordListTable(db, RESULT_TRAINING);
        createCategoryResultTable(db, RESULT_TRAIN_DETAIL);
        createCategoryResultTable(db, RESULT_TRAIN_MAIN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RESULT_TRAINING);
        onCreate(db);
    }

    public void createWordListTable(SQLiteDatabase db, String tableName) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "CORRECT_WORD TEXT," +
                "WRITTEN_WORD TEXT," +
                "CATEGORY_TOTAL INTEGER," +
                "CATEGORY_ERROR INTEGER," +
                "UNCATEGORIZED INTEGER," +
                "MULTI_BIT INTEGER," +
                "CATEGORY_ERROR_RAW TEXT," +
                "LAST_UPDATE DATETIME);");
    }

    public void createCategoryResultTable(SQLiteDatabase db, String tableName) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "SESSION INTEGER," +
                "CATEGORY_MAIN INTEGER," +
                "CATEGORY_SUB INTEGER," +
                "NUM_ERROR INTEGER," +
                "NUM_TOTAL INTEGER," +
                "ERROR_RATE REAL," +
                "LAST_UPDATE DATETIME);");
    }

    // TODO: erase id
    public boolean replaceData(SQLiteDatabase db, String[] data, int id) {
        Log.d(TAG, "replaceData" );

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String update = formatter.format(Calendar.getInstance().getTimeInMillis());
        long res = -1;
        if (data.length > 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, id + 1);
            contentValues.put(CORRECT_WORD, data[0]);
            contentValues.put(WRITTEN_WORD, data[1]);
            contentValues.put(CATEGORY_TOTAL, Integer.valueOf(data[2]));
            contentValues.put(CATEGORY_ERROR, Integer.valueOf(data[3]));
            contentValues.put(UNCATEGORIZED, Integer.valueOf(data[4]));
            contentValues.put(MULTI_BIT, Integer.valueOf(data[5]));
            contentValues.put(CATEGORY_ERROR_RAW, data[6]);
            contentValues.put(DATE, update);
            res = db.replace(RESULT_TRAINING,
                    null, contentValues);
        }
        if (res == -1) {
            return false;
        } else {
            return true;
        }
    }

    // TODO: erase id
    public boolean replaceSummaryData(SQLiteDatabase db, String tableName, int[] num, int id) {
        Log.d(TAG, "replaceSummaryData");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String update = formatter.format(Calendar.getInstance().getTimeInMillis());
        double errorRate = (double) num[3] / num[4];

        Log.d(TAG, "errorRate: " + errorRate);
        long res = -1;
        if (num.length > 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, id + 1);
            contentValues.put(SESSION, num[0]);
            contentValues.put(CATEGORY_MAIN, num[1]);
            contentValues.put(CATEGORY_SUB, num[2]);
            contentValues.put(NUM_ERROR, num[3]);
            contentValues.put(NUM_TOTAL, num[4]);
            contentValues.put(ERROR_RATE, errorRate);
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

    public int deleteRowById(SQLiteDatabase db, String tableName, int id) {
        Log.d(TAG, "deleteRowById(): ");

        int res = 0;
        if (id > 0) {
            res = db.delete(tableName, "ID = ?", new String[] {String.valueOf(id)});
        }
        return res;
    }
}